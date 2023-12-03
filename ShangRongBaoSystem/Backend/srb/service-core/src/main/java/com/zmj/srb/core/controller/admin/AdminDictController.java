package com.zmj.srb.core.controller.admin;


import com.alibaba.excel.EasyExcel;
import com.zmj.srb.common.exception.entity.BusinessException;
import com.zmj.srb.common.result.R;
import com.zmj.srb.common.result.ResponseEnum;
import com.zmj.srb.common.util.Assert;
import com.zmj.srb.common.util.MimeTypeEnum;
import com.zmj.srb.core.pojo.dto.ExcelDictDTO;
import com.zmj.srb.core.pojo.entity.Dict;
import com.zmj.srb.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author zhumengjun
 * @since 2023-11-07
 */
@Api(tags = "数据字典管理")
@RestController
@CrossOrigin
@RequestMapping("/admin/core/dict")
@Slf4j
public class AdminDictController {

    @Resource
    private DictService dictService;

    @ApiOperation("Excel数据的批量导入")
    @PostMapping("/batch_import")
    public R batchImport(
            @ApiParam(value = "Excel数据字典文件",required = true)
            @RequestParam("file") MultipartFile file,
            @ApiParam(value = "操作类型 1.追加记录不许重复 2.表被清空后再插入",required = true)
            @RequestParam("operationType") Integer operationType){
        try {
            dictService.importData(file,operationType);
            return R.ok().message("数据字典批量导入成功！");
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                throw new BusinessException(e.getMessage(),ResponseEnum.UPLOAD_ERROR.getCode());
            }
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR,e);
        }
    }

    /**
     * 文件下载（失败了会返回一个有部分数据的Excel）
     * <p> 注意: 使用swagger 会导致各种问题，请直接用浏览器或者用postman验证
     * 1. 创建excel对应的实体对象 参照{@link ExcelDictDTO}
     * <p>
     * 2. 设置返回的 参数
     * <p>
     * 3. 直接写，这里注意，finish的时候会自动关闭OutputStream,当然你外面再关闭流问题不大
     */
    @ApiOperation("导出Excel")
    @GetMapping("/download")
    public void download(HttpServletResponse response,
                         @ApiParam(value = "Excel类型[.xlsx][.xls][.csv]",required = true)
                         @RequestParam(value = "excelType") String excelType) throws IOException {
        log.info("要导出的excel扩展名为{}",excelType);
        MimeTypeEnum mimeTypeEnum = MimeTypeEnum.getByExtension(excelType);
        Assert.notNull(mimeTypeEnum,ResponseEnum.INPUT_PARAM_ERROR);
        response.setContentType(mimeTypeEnum.getMimeType());
        log.info("要导出的excel的ContentType为{}",response.getContentType());
        response.setCharacterEncoding("utf-8");
        //默认文件名称 这里URLEncoder.encode可以防止中文乱码
        String defaultFileName = URLEncoder.encode("字典表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + defaultFileName + excelType);
        List<Dict> dictList = dictService.list();
        Assert.notEmpty(dictList,ResponseEnum.QUERY_NO_DATA);
        List<ExcelDictDTO> excelDictList = new ArrayList<>(dictList.size());
        dictList.forEach(dict -> {
            ExcelDictDTO excelDict = new ExcelDictDTO();
            BeanUtils.copyProperties(dict,excelDict);
            excelDictList.add(excelDict);
        });
        EasyExcel.write(response.getOutputStream(), ExcelDictDTO.class).sheet("字典表").doWrite(excelDictList);
    }


    //树形数据的两种加载方案
    //方案一:非延迟加载需要后端返回的数据结构中包含嵌套数据，并且嵌套数据放在children属性中
    //方案二:延迟加载不需要后端返回数据中包含嵌套数据，并且要定义布尔属性hasChildren，表示当前节点是否包含子数据
    // 如果hasChildren为true，就表示当前节点包含子数据
    // 如果hasChildren为false，就表示当前节点不包含子数据
    // 如果当前节点包含子数据，那么点击当前节点的时候，就需要通过load方法加载子数据
    @ApiOperation("根据父ID获取子节点列表")
    @GetMapping("/listByParentId/{parentId}")
    public R listByParentId(
            @ApiParam(value = "父节点ID",required = true)
            @PathVariable("parentId") Long parentId
    ){
        List<Dict> dictList = dictService.listByParentId(parentId);
        return R.ok().data("dictList",dictList);
    }
}

