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
    "log_name":"zcsscfc6",
    "wechat":"MTcyMDA2NjM4",
    "password":"MTIzNDU2",
    "nick_name":"victor",
    "mobile":"MTg1MjEzNTc2Mjk="
}
```

##### 输入参数
---
|字段     |类型     |备注
|---------|:------:|:-------|
|log_name     |string  |  登录名|
|wechat     |string  | 微信号 base64加密|
|password     |string  | 密码 base64加密|
|nick_name     |string  |外号 |
|mobile     |string  |手机号码 base64加密|

##### 返回结果
---
*** JSON示例 ***
```
{
  "meta": {
    "success": true,
    "message": "ok"
  },
  "data": {
    "user_id": "68",
    "token": "6D0BCB668A0A403080DB12A8DEB0E7EE",
    "log_name": "zcsscfc6",
    "wechat": "MTcyMDA2NjM4",
    "nick_name": "zcsscfc6",
    "mobile": "MTg1MjEzNTc2Mjk=",
    "photo": null
  }
}
```

#### 说明
---
|字段     |类型     |备注
|---------|:------:|:-------|
|user_id     |string  |  UID|
|token     |string  | token|
|log_name     |string  | 登录名|
|wechat     |string  | 微信 base64加密|
|nick_name     |string  | 外号|
|mobile     |string  | 手机 base64加密|
|photo     |string  | 头像地址|


