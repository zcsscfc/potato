package com.potato.api.service;

import com.potato.api.Param.UserRegParam;
import com.potato.api.entity.UserEntity;
import com.potato.api.framework.jdbc.dao.DataAccessor;
import com.potato.api.framework.util.Base64Util;
import com.potato.api.framework.util.Md5Util;
import com.potato.api.framework.util.StringUtil;
import com.potato.api.model.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by zhangcs on 2016/6/3.
 */
@Service
public class UserService {

    @Autowired
    private DataAccessor dataAccessor;

    public ServiceResult userReg(UserRegParam userRegParam) {
        ServiceResult serviceResult=new ServiceResult();
        if (userRegParam != null) {

            UserEntity existUser = getUserEntityByLogName(userRegParam.getLogName());

            if(existUser!=null){
                serviceResult.setIsSuccess(false);
                serviceResult.setMessage(String.format("登录名：[%s] 已存在!",userRegParam.getLogName()));
                return serviceResult;
            }

            String _mobile="";
            String _wechat="";
            String _password="";

            if(StringUtil.isNotEmpty(userRegParam.getMobile())){
                _mobile= Base64Util.decode(userRegParam.getMobile());
            }
            if(StringUtil.isNotEmpty(userRegParam.getWechat())){
                _wechat=Base64Util.decode(userRegParam.getWechat());
            }
            _password=Base64Util.decode(userRegParam.getPassword());

            UserEntity userEntity = new UserEntity();
            userEntity.setCreateT(new Date());
            userEntity.setLogName(userRegParam.getLogName());
            userEntity.setMobile(_mobile);
            userEntity.setNickName(userRegParam.getNickName());
            userEntity.setPassword(Md5Util.encode(_password));
            userEntity.setWechat(_wechat);

            BigInteger userId=getUserId();
            userEntity.setUserId(userId);

            insertUser(userEntity);
        }
        return serviceResult;
    }


    private UserEntity getUserEntityByLogName(String logName) {
        return dataAccessor.selectOne("select_user_logName", logName);
    }

    private void insertUser(UserEntity userEntity){
        dataAccessor.insert("insert_user",userEntity);
    }

    private BigInteger getUserId(){
        return dataAccessor.selectOne("select_userid");
    }
}
