package com.zmj.srb.core.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zmj.srb.core.mapper.DictMapper;
import com.zmj.srb.core.pojo.entity.Dict;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class WrapperTests {

    @Resource
    private DictMapper dictMapper;



    /**
     * 查询所有用户的用户名和年龄
     */
    @Test
    public void test5(){

        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(20000L);
        String pIdColumnName = "parent_id";
        //queryWrapper.select("parent_id as key,count(*) ");
        queryWrapper.select(pIdColumnName+",count(*)");
        queryWrapper.in(pIdColumnName,ids);
        queryWrapper.eq("is_deleted",0);
        queryWrapper.groupBy(pIdColumnName);

        //select语句通常会和selectMaps一起出现
        List<Map<String, Object>> mapList = dictMapper.selectMaps(queryWrapper);
        Log log = LogFactory.getLog(this.getClass());
        Map<String, Object> objectMap = SqlHelper.getObject(log, mapList);


        for(int i = 0;i < mapList.size();i++) {
            System.out.println("list.index="+i);
            Map<String, Object> map = mapList.get(i);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println(entry.getKey()+"="+ entry.getValue());
            }
        }
        System.out.println("---------------------------------------------------");
        Map<Long,Long> resultMap = new HashMap<>();
        mapList.forEach(map->{
            Long key = null;
            Long value = -1L;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (pIdColumnName.equals(entry.getKey())) {
                    key = (Long) entry.getValue();
                } else {
                    value = (Long) entry.getValue();
                }
                resultMap.put(key,value);
            }
        });

        for (Map.Entry<Long, Long> entry : resultMap.entrySet()) {
            System.out.println(entry.getKey()+"="+entry.getValue());
        }
    }

}
