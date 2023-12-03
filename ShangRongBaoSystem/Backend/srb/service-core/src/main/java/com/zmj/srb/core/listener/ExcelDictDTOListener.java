package com.zmj.srb.core.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.zmj.srb.core.mapper.DictMapper;
import com.zmj.srb.core.pojo.dto.ExcelDictDTO;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

/**
 * 注意：ExcelDictDTOListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 */
@Slf4j
@NoArgsConstructor
public class ExcelDictDTOListener implements ReadListener<ExcelDictDTO> {

    /**
     * 开发验证中每隔5条存储数据库，实际生产使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    /**
     * 缓存的数据
     */
    private List<ExcelDictDTO> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private DictMapper dictMapper;

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param dictMapper
     */
    public ExcelDictDTOListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(ExcelDictDTO data, AnalysisContext context) {
        log.info("解析到一条数据:{}", data);
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        if (cachedDataList.size() > 0) {
            log.info("{}条数据，开始存储数据库！", cachedDataList.size());
            dictMapper.insertBatch(cachedDataList);
            log.info("{}条数据，存储数据库成功！", cachedDataList.size());
        }
    }
}