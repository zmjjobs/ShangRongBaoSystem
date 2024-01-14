package com.zmj.srb.sms.controller.api;

import com.zmj.srb.common.constant.RedisConstant;
import com.zmj.srb.common.result.R;
import com.zmj.srb.common.result.ResponseEnum;
import com.zmj.srb.common.util.Assert;
import com.zmj.srb.common.util.RandomUtils;
import com.zmj.srb.common.util.RegexValidateUtils;
import com.zmj.srb.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/sms")
@Api(tags = "短信管理")
@CrossOrigin //跨域
@Slf4j
public class ApiSmsController {

    @Resource
    private SmsService smsService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation("获取验证码")
    @GetMapping("/send/{mobile}")
    public R send(
            @ApiParam(value = "手机号", required = true)
            @PathVariable String mobile){

        //MOBILE_NULL_ERROR(-202, "手机号不能为空"),
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        //MOBILE_ERROR(-203, "手机号不正确"),
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile), ResponseEnum.MOBILE_ERROR);

        //生成验证码 生成四位的随机数字
        String code = RandomUtils.getFourBitRandom();
        //组装短信模板参数
        Map<String,Object> param = new HashMap<>();
        param.put("code", code);
        //发送短信
        //FIXME 临时注掉，省去每次都真实发送短信
        //smsService.send(mobile, SmsProperties.TEMPLATE_CODE, param);

        //将验证码存入redis
        String key = RedisConstant.RedisKey.SMS_CODE_PREFIX.getCode() + mobile;
        stringRedisTemplate.opsForValue().set(key, code, 5, TimeUnit.HOURS);

        log.info("{}={}",key,code);
        return R.ok().message("短信发送成功");
    }
}