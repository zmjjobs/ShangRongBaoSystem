package com.zmj.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmj.srb.common.exception.entity.BusinessException;
import com.zmj.srb.common.util.MimeTypeEnum;
import com.zmj.srb.core.ExcelOperationTypeEnum;
import com.zmj.srb.core.listener.ExcelDictDTOListener;
import com.zmj.srb.core.mapper.DictMapper;
import com.zmj.srb.core.pojo.dto.ExcelDictDTO;
import com.zmj.srb.core.pojo.entity.Dict;
import com.zmj.srb.core.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author zhumengjun
 * @since 2023-11-07
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    private RedisTemplate<String,List<Dict>> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importData(MultipartFile file,Integer operationType) throws IOException {
        if (ExcelOperationTypeEnum.CLEAN_UP_BEFORE_INSERTING.getCode().equals(operationType)) {
           baseMapper.deleteAllPhysical();  //清空字典表
        }
        ExcelReaderBuilder builder = EasyExcel.read(file.getInputStream(), ExcelDictDTO.class, new ExcelDictDTOListener(baseMapper));
        log.info("上传文件的ContentType为{}",file.getContentType());
        //TODO 这里后面可以改成存入到Redis中
        MimeTypeEnum mimeTypeEnum = MimeTypeEnum.getByMimeType(file.getContentType());
        String extension = mimeTypeEnum.getExtension();//文件扩展名
        log.info("上传文件的扩展名为{}",extension);
        ExcelTypeEnum[] excelTypeEnums = ExcelTypeEnum.values();
        boolean isSupported = false;
        String supportedStr = "上传文件类型目前仅支持这些：";
        for (ExcelTypeEnum excelTypeEnum : excelTypeEnums) {
            if (excelTypeEnum.getValue().equalsIgnoreCase(extension)) {
                builder.excelType(excelTypeEnum);
                isSupported = true;
            }
            supportedStr += "["+excelTypeEnum.getValue()+ "] ";
        }
        if (!isSupported) throw new BusinessException(supportedStr);
        builder.sheet().doRead();
        log.info("Excel导入成功！");
    }

    @Override
    public List<Dict> listByParentId(Long parentId) {
        String redisKey = "srb:core:dictList:" + parentId;
        //首先从Redis中获取
        List<Dict> dictList = redisTemplate.opsForValue().get(redisKey);
        if (dictList != null && dictList.size() > 0) {
            log.info("从Redis获取到数据直接返回结果......");
            return dictList;
        }
        //若从Redis获取不到，从数据库中取
        log.info("从Redis获取不到，从数据库中取数据......");
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",parentId);
        dictList = baseMapper.selectList(queryWrapper);
        if (dictList != null && dictList.size() > 0) {
            List<Long> ids = new ArrayList<>();
            dictList.forEach(dict->{
                ids.add(dict.getId());
            });
            Map<Long, Long> parentIdChildrenCountMap = parentIdChildrenCountMap(ids, "parent_id");
            if (parentIdChildrenCountMap.size() > 0) {
                dictList.forEach(dict->{
                    Long childrenCount = parentIdChildrenCountMap.get(dict.getId());
                    if (childrenCount != null && childrenCount > 0) {
                        dict.setHasChildren(true);
                    }
                });
            }
            //将数据库获取的数据更新到Redis中,保存一个小时
            redisTemplate.opsForValue().set(redisKey,dictList,1, TimeUnit.HOURS);
            log.info("将数据库中取得的数据更新到Redis......");
        }
        return dictList;
    }

    /**
     * groupByParentId统计子ID个数
     * @param ids  Id或ParentId，只统计
     * @param columnName 列名，目前只支持ParentId
     * @return Map<列名, 统计子ID个数>
     */
    public Map<Long, Long> parentIdChildrenCountMap(List<Long> ids, String columnName) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(columnName +",count(*) ");
        queryWrapper.in(columnName, ids);
        queryWrapper.groupBy(columnName);
        List<Map<String, Object>> mapList = baseMapper.selectMaps(queryWrapper);
        Map<Long,Long> resultMap = new HashMap<>();
        if (mapList.size() > 0) {
            mapList.forEach(map->{
                Long key = null;
                Long value = null;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (columnName.equals(entry.getKey())) {
                        key = (Long) entry.getValue();
                    } else {
                        value = (Long) entry.getValue();
                    }
                    resultMap.put(key,value);
                }
            });
        }
        return resultMap;
    }
}
