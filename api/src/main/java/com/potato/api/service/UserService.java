package com.potato.api.service;

import com.potato.api.Param.user.LoginParam;
import com.potato.api.Param.user.UserEditParam;
import com.potato.api.Param.user.UserRegParam;
import com.potato.api.entity.UserEntity;
import com.potato.api.framework.jdbc.dao.DataAccessor;
import com.potato.api.framework.security.TokenManager;
import com.potato.api.framework.security.impl.DefaultTokenManager;
import com.potato.api.framework.util.Base64Util;
import com.potato.api.framework.util.Md5Util;
import com.potato.api.framework.util.StringUtil;
import com.potato.api.framework.web.WebContext;
import com.potato.api.model.Message;
import com.potato.api.model.ServiceResult;
import com.potato.api.result.LoginResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by zhangcs on 2016/6/3.
 */
@Service
public class UserService {

    @Autowired
    private DataAccessor dataAccessor;

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    private TokenManager tokenManager = new DefaultTokenManager();

    public ServiceResult userReg(UserRegParam userRegParam) {
        ServiceResult serviceResult = new ServiceResult();
        if (userRegParam != null) {

            UserEntity existUser = getUserEntityByLogName(userRegParam.getLogName());

            if (existUser != null) {
                serviceResult.setIsSuccess(false);
                serviceResult.setMessage(String.format(Message.LOG_NAME_EXIST, userRegParam.getLogName()));
                return serviceResult;
            }

            String _mobile = "";
            String _wechat = "";
            String _password = "";

            if (StringUtil.isNotEmpty(userRegParam.getMobile())) {
                _mobile = Base64Util.decode(userRegParam.getMobile());
            }
            if (StringUtil.isNotEmpty(userRegParam.getWechat())) {
                _wechat = Base64Util.decode(userRegParam.getWechat());
            }
            _password = Base64Util.decode(userRegParam.getPassword());

            UserEntity userEntity = new UserEntity();
            userEntity.setCreateT(new Date());
            userEntity.setLogName(userRegParam.getLogName());
            userEntity.setMobile(_mobile);
            userEntity.setNickName(userRegParam.getLogName());
            userEntity.setPassword(Md5Util.encode(_password));
            userEntity.setWechat(_wechat);

            BigInteger userId = getUserId();
            userEntity.setUserId(userId);

            insertUser(userEntity);

            String token = tokenManager.createToken(userEntity.getUserId().toString());
            LoginResult loginResult = new LoginResult();
            loginResult.setToken(token);
            loginResult.setUserId(userEntity.getUserId().toString());
            if (StringUtil.isNotEmpty(userEntity.getWechat())) {
                loginResult.setWechat(Base64Util.encode(userEntity.getWechat()));
            }
            if (StringUtil.isNotEmpty(userEntity.getMobile())) {
                loginResult.setMobile(Base64Util.encode(userEntity.getMobile()));
            }
            loginResult.setNickName(userEntity.getNickName());
            loginResult.setLogName(userEntity.getLogName());
            loginResult.setPhoto(userEntity.getPhoto());

            serviceResult.setData(loginResult);
        }
        return serviceResult;
    }

    public ServiceResult userLogin(LoginParam loginParam) {
        ServiceResult serviceResult = new ServiceResult();

        if (loginParam != null) {
            UserEntity userEntity = getUserEntityByLogName(loginParam.getLogName());
            if (userEntity != null
                    && userEntity.getLogName().equals(loginParam.getLogName())
                    && userEntity.getPassword().equals(Md5Util.encode(Base64Util.decode(loginParam.getPassword())))) {
                serviceResult.setIsSuccess(true);
                serviceResult.setMessage("");

                String token = tokenManager.createToken(userEntity.getUserId().toString());

                LoginResult loginResult = new LoginResult();
                loginResult.setToken(token);
                loginResult.setUserId(userEntity.getUserId().toString());
                if (StringUtil.isNotEmpty(userEntity.getWechat())) {
                    loginResult.setWechat(Base64Util.encode(userEntity.getWechat()));
                }
                if (StringUtil.isNotEmpty(userEntity.getMobile())) {
                    loginResult.setMobile(Base64Util.encode(userEntity.getMobile()));
                }
                loginResult.setNickName(userEntity.getNickName());
                loginResult.setLogName(userEntity.getLogName());
                loginResult.setPhoto(userEntity.getPhoto());
                serviceResult.setData(loginResult);
                return serviceResult;
            }
        }

        serviceResult.setIsSuccess(false);
        serviceResult.setMessage(Message.LOGIN_ERROR);
        return serviceResult;
    }

    public ServiceResult userEdit(UserEditParam userEditParam) {
        ServiceResult serviceResult = new ServiceResult();

        if (userEditParam != null) {
            UserEntity userEntity = new UserEntity();

            String _mobile = "";
            String _wechat = "";

            if (StringUtil.isNotEmpty(userEditParam.getMobile())) {
                _mobile = Base64Util.decode(userEditParam.getMobile());
            }
            if (StringUtil.isNotEmpty(userEditParam.getWechat())) {
                _wechat = Base64Util.decode(userEditParam.getWechat());
            }

            userEntity.setMobile(_mobile);
            userEntity.setPhoto(userEditParam.getPhoto());
            userEntity.setWechat(_wechat);
            userEntity.setNickName(userEditParam.getNickName());

            String token = WebContext.getRequest().getHeader("X-Token");
            String userId = tokenManager.getTokenValue(token);
            BigInteger uid = new BigInteger(userId);
            userEntity.setUserId(uid);

            updateUser(userEntity);
            serviceResult.setIsSuccess(true);
        }
        return serviceResult;
    }

    private UserEntity getUserEntityByLogName(String logName) {
        return dataAccessor.selectOne("select_user_logName", logName);
    }

    private void insertUser(UserEntity userEntity) {
        dataAccessor.insert("insert_user", userEntity);
    }

    private void updateUser(UserEntity userEntity) {
        dataAccessor.update("update_user", userEntity);
    }

    private BigInteger getUserId() {
        return dataAccessor.selectOne("select_userid");
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }
}
