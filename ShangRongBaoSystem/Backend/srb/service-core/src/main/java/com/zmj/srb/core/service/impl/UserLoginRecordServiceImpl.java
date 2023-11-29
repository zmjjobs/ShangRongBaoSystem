package com.zmj.srb.core.service.impl;

import com.zmj.srb.core.pojo.entity.UserLoginRecord;
import com.zmj.srb.core.mapper.UserLoginRecordMapper;
import com.zmj.srb.core.service.UserLoginRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户登录记录表 服务实现类
 * </p>
 *
 * @author zhumengjun
 * @since 2023-11-07
 */
@Service
public class UserLoginRecordServiceImpl extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord> implements UserLoginRecordService {

}
