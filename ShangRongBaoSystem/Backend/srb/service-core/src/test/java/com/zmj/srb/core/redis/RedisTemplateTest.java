package com.zmj.srb.core.redis;

import com.zmj.srb.core.mapper.DictMapper;
import com.zmj.srb.core.pojo.entity.Dict;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTemplateTest {
    @Resource
    private RedisTemplate<String,Dict> redisTemplate;

    @Resource
    private DictMapper dictMapper;

    @Test
    public void saveDict(){
        Dict dict = dictMapper.selectById(1);
        redisTemplate.opsForValue().set("dict",dict,5, TimeUnit.MINUTES);

    }

    @Test
    public void getDict(){

        Dict dict = redisTemplate.opsForValue().get("dict");
        System.out.println(dict);
    }

}
