package com.zmj.srb.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zmj.srb.core.pojo.entity.Dict;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author zhumengjun
 * @since 2023-11-07
 */
public interface DictService extends IService<Dict> {
    void importData(MultipartFile file,Integer operationType) throws IOException;

    List<Dict> listByParentId(Long parentId);
}
