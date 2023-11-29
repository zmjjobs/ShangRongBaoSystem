package com.zmj.srb.core.controller.admin;


import com.zmj.srb.common.result.R;
import com.zmj.srb.common.result.ResponseEnum;
import com.zmj.srb.common.util.Assert;
import com.zmj.srb.core.pojo.entity.IntegralGrade;
import com.zmj.srb.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 积分等级表 前端控制器
 * </p>
 *
 * @author zhumengjun
 * @since 2023-11-07
 */
@Api(tags="积分等级管理")
@CrossOrigin //自动跨域
@RestController
@Slf4j
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {

    @Resource
    private IntegralGradeService integralGradeService;

    @GetMapping("/list")
    @ApiOperation("积分等级列表")
    public R listAll(){
        List<IntegralGrade> list = integralGradeService.list();
        if (list == null || list.size() == 0) {
            return R.setR(ResponseEnum.QUERY_NO_DATA);
        }
        return R.ok().data("list",list).message("获取列表成功");
    }

    @ApiOperation(value = "根据ID删除积分等级",notes = "逻辑删除")
    @DeleteMapping("remove/{id}")
    public R remove(
            @ApiParam(value = "记录ID",example = "100",required = true)
            @PathVariable Integer id) {
        boolean b = integralGradeService.removeById(id);
        if (b) {
            return R.ok().message("删除成功").data("result",true);
        }
        return R.error().message("删除失败").data("result",false);
    }

    @PostMapping("/save")
    @ApiOperation("新增积分等级")
    public R save(
            @ApiParam(value = "积分等级对象",required = true)
            @RequestBody IntegralGrade integralGrade) {
        Assert.notNull(integralGrade.getBorrowAmount(),ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
        boolean b = integralGradeService.save(integralGrade);
        if (b) {
            return R.ok().message("保存成功").data("result",true);
        }
        return R.error().message("保存失败").data("result",false);
    }

    @GetMapping("/get/{id}")
    @ApiOperation("根据ID获取积分等级")
    public R getById(
            @ApiParam(value = "积分等级记录ID",required = true,example = "1")
            @PathVariable Integer id
    ){
        IntegralGrade integralGrade = integralGradeService.getById(id);
        if (integralGrade == null) {
            return R.setR(ResponseEnum.QUERY_NO_DATA);
        }
        return R.ok().data("record",integralGrade);
    }

    @PutMapping("/update")
    @ApiOperation("更新积分等级")
    public R update(
            @ApiParam(value = "积分等级对象",required = true)
            @RequestBody IntegralGrade integralGrade) {
        boolean b = integralGradeService.updateById(integralGrade);
        if (b) {
            return R.ok().message("更新成功").data("result",true);
        }
        return R.error().message("更新失败").data("result",false);
    }
}

