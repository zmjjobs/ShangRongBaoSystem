package com.zmj.srb.core.controller.api;


import com.zmj.srb.base.util.JwtUtils;
import com.zmj.srb.common.constant.RedisConstant;
import com.zmj.srb.common.result.R;
import com.zmj.srb.common.result.ResponseEnum;
import com.zmj.srb.common.util.Assert;
import com.zmj.srb.common.util.RegexValidateUtils;
import com.zmj.srb.core.pojo.vo.LoginVO;
import com.zmj.srb.core.pojo.vo.RegisterVO;
import com.zmj.srb.core.pojo.vo.UserInfoVO;
import com.zmj.srb.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author zhumengjun
 * @since 2023-11-07
 */
@RestController
@Api(tags = "会员接口")
@RequestMapping("/api/core/userInfo")
@Slf4j
@CrossOrigin
public class UserInfoController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation("会员注册")
    @PostMapping("/register")
    public R register(@RequestBody RegisterVO registerVO) {
        //校验验证码是否正确
        String key = RedisConstant.RedisKey.SMS_CODE_PREFIX.getCode() + registerVO.getMobile();
        String codeGen = stringRedisTemplate.opsForValue().get(key);
        log.info("codeGen={}",codeGen);
        Assert.equals(registerVO.getCode(),codeGen, ResponseEnum.CODE_ERROR);
        Assert.notEmpty(registerVO.getMobile(),ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notEmpty(registerVO.getPassword(),ResponseEnum.PASSWORD_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkCellphone(registerVO.getMobile()),ResponseEnum.MOBILE_ERROR);

        //注册
        userInfoService.register(registerVO);

        return R.ok().message("注册成功");
    }

    @ApiOperation("会员登录")
    @PostMapping("/login")
    public R register(@RequestBody LoginVO loginVO, HttpServletRequest request) {
        Assert.notEmpty(loginVO.getMobile(),ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notEmpty(loginVO.getPassword(),ResponseEnum.PASSWORD_NULL_ERROR);

        String ip = request.getRemoteAddr();
        loginVO.setIp(ip);
        UserInfoVO userInfoVO = userInfoService.login(loginVO);
        return R.ok().data("userInfo",userInfoVO);
    }

    @ApiOperation("校验Token")
    @GetMapping("/checkToken")
    public R checkToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        boolean isOK = JwtUtils.checkToken(token);
        if (isOK) {
            return R.ok();
        }
        return R.setR(ResponseEnum.LOGIN_MOBILE_ERROR);
    }
}

