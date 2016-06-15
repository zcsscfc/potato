#### API名称
---
```
用户登录
```

#### 描述
---
```

```

#### url
---
```
http://host:port/user/login
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
    "log_name":"vic",
    "password":"123456"
}
```

##### 输入参数
---
|字段     |类型     |备注
|---------|:------:|:-------|
|log_name     |string  |  登录名|
|password     |string  | 密码 base64加密|

##### 返回结果
---
```
*** JSON示例 ***
{
  "meta": {
    "success": true,
    "message": "ok"
  },
  "data": {
    "user_id": "66",
    "token": "63C3E8FC45B9468AB8E66DD0AE53AF3E",
    "log_name": "zcsscfc5",
    "wechat": "MTcyMDA2NjM4",
    "nick_name": "victor",
    "mobile": "MTg1MjEzNTc2Mjk=",
    "photo": "/aaa.jpg"
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







