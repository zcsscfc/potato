#### API名称
---
```
用户注册
```

#### 描述
---
```

```

#### url
---
```
http://host:port/user/reg
```

##### 请求方法
---
```
POST
```

##### 调用样例
---
```
{
    "log_name":"vicaa",
    "wechat":"172006638",
    "password":"123456",
    "nick_name":"victor",
    "mobile":"18521222222"
}
```

##### 输入参数
---
|字段     |类型     |备注
|---------|:------:|:-------|
|log_name     |string  |  登录名|
|wechat     |string  | 微信号|
|password     |string  | 密码，base64加密|
|nick_name     |string  |外号 |
|mobile     |string  |手机号码 |

##### 返回结果
---
*** JSON示例 ***
```
 {
  "meta": {
    "success": true,
    "message": "ok"
  }
}
```

#### 说明
---


