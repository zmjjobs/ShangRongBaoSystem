package com.zmj.srb.core.service.impl;

import com.zmj.srb.core.pojo.entity.UserInfo;
import com.zmj.srb.core.mapper.UserInfoMapper;
import com.zmj.srb.core.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 * @author zhumengjun
 * @since 2023-11-07
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
