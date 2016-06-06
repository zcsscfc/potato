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
*** JSON示例 ***
{
  "meta": {
    "success": true,
    "message": "ok"
  },
  "data": {
    "user_id": "25",
    "token": "E95D588EB2A3481CAA5E2FD6360E7E55"
  }
}
```

#### 说明
---
|字段     |类型     |备注
|---------|:------:|:-------|
|user_id     |string  |  UID|
|token     |string  | token|

