package com.zmj.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmj.srb.base.util.JwtUtils;
import com.zmj.srb.common.result.ResponseEnum;
import com.zmj.srb.common.util.Assert;
import com.zmj.srb.common.util.MD5;
import com.zmj.srb.core.constant.UserInfoStatusEnum;
import com.zmj.srb.core.mapper.UserAccountMapper;
import com.zmj.srb.core.mapper.UserInfoMapper;
import com.zmj.srb.core.mapper.UserLoginRecordMapper;
import com.zmj.srb.core.pojo.entity.UserAccount;
import com.zmj.srb.core.pojo.entity.UserInfo;
import com.zmj.srb.core.pojo.entity.UserLoginRecord;
import com.zmj.srb.core.pojo.vo.LoginVO;
import com.zmj.srb.core.pojo.vo.RegisterVO;
import com.zmj.srb.core.pojo.vo.UserInfoVO;
import com.zmj.srb.core.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 * @author zhumengjun
 * @since 2023-11-07
 */
@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private UserAccountMapper userAccountMapper;

    @Resource
    private UserLoginRecordMapper userLoginRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterVO registerVO) {
        log.info("registerVO",registerVO);

        //判断用户手机号是否被注册
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("mobile",registerVO.getMobile());
        Integer count = baseMapper.selectCount(userInfoQueryWrapper);
        Assert.isTrue(count == 0, ResponseEnum.MOBILE_EXIST_ERROR);
        //user_info
        UserInfo userInfo = new UserInfo();
        userInfo.setMobile(registerVO.getMobile());
        userInfo.setNickName(registerVO.getMobile());
        userInfo.setName(registerVO.getMobile());
        userInfo.setUserType(registerVO.getUserType());
        userInfo.setPassword(MD5.encrypt(registerVO.getPassword()));
        userInfo.setStatus(UserInfoStatusEnum.NORMAL.getCode());
        userInfo.setHeadImg("file:///E:/%E4%B8%AA%E4%BA%BA%E8%B5%84%E6%96%99/u.jpeg");
        baseMapper.insert(userInfo);//insert后会赋值ID给userInfo

        //useer_accout
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(userInfo.getId());
        userAccountMapper.insert(userAccount);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfoVO login(LoginVO loginVO) {
        //用户是否存在
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",loginVO.getMobile()).eq("user_type", loginVO.getUserType());
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        Assert.notNull(userInfo,ResponseEnum.LOGIN_MOBILE_ERROR);
        //密码是否正确
        Assert.equals(userInfo.getPassword(),MD5.encrypt(loginVO.getPassword()),ResponseEnum.LOGIN_PASSWORD_ERROR);
        //用户是否锁定
        Assert.equals(userInfo.getStatus(),UserInfoStatusEnum.NORMAL.getCode(),ResponseEnum.LOGIN_LOKED_ERROR);

        //记录登录日志
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(userInfo.getId());
        userLoginRecord.setIp(loginVO.getIp());
        userLoginRecordMapper.insert(userLoginRecord);

        //生成Token
        String token = JwtUtils.createToken(userInfo.getId(), userInfo.getName());
        //组装UserInfoVO对象
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfo,userInfoVO);
        userInfoVO.setToken(token);
        return userInfoVO;
    }
}
