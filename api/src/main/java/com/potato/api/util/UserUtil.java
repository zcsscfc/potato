package com.potato.api.util;

import com.potato.api.framework.jdbc.ServiceException;
import com.potato.api.framework.security.TokenManager;
import com.potato.api.framework.security.impl.DefaultTokenManager;
import com.potato.api.framework.util.StringUtil;
import com.potato.api.framework.web.WebContext;

import java.math.BigInteger;

/**
 * Created by zhangcs on 2016/6/23.
 */
public class UserUtil {

    public static BigInteger getUserId() {

        String token = WebContext.getRequest().getHeader("X-Token");
        if(StringUtil.isEmpty(token)){
            throw new ServiceException("未登录！");
        }
        TokenManager tokenManager=getTokenManager();
        BigInteger userId = tokenManager.getTokenValue(token);
        return userId;
    }

    public static TokenManager getTokenManager(){
        return new DefaultTokenManager();
    }
}
