package com.zmj.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmj.srb.common.result.ResponseEnum;
import com.zmj.srb.common.util.Assert;
import com.zmj.srb.common.util.MD5;
import com.zmj.srb.core.constant.UserInfoStatusEnum;
import com.zmj.srb.core.mapper.UserAccountMapper;
import com.zmj.srb.core.mapper.UserInfoMapper;
import com.zmj.srb.core.pojo.entity.UserAccount;
import com.zmj.srb.core.pojo.entity.UserInfo;
import com.zmj.srb.core.pojo.vo.RegisterVO;
import com.zmj.srb.core.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
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
}
