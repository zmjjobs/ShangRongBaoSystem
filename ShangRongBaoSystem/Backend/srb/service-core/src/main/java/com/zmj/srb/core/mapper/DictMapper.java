package com.zmj.srb.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmj.srb.core.pojo.dto.ExcelDictDTO;
import com.zmj.srb.core.pojo.entity.Dict;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author zhumengjun
 * @since 2023-11-07
 */
public interface DictMapper extends BaseMapper<Dict> {

    void insertBatch(List<ExcelDictDTO> list);
    void deleteAllPhysical();
    //Map<String,Integer> selectByIds(List<Long> ids);
}
