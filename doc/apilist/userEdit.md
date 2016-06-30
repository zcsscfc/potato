#### API名称
---
```
用户编辑
```

#### 描述
---
```
更新用户信息时必须将token传到服务端
token放到请求头里:

X-Token:76774348C8D0483C8A665FDFD20B03C8

```

#### url
---
```
http://host:port/user/edit
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
    "photo":"/aaa.jpg",
    "wechat":"MTcyMDA2NjM4",
    "mobile":"MTg1MjEzNTc2Mjk=",
    "nick_name":"vic"
}
```

##### 输入参数
---
|字段     |类型     |备注
|---------|:------:|:-------|
|wechat     |string  |微信号 base64加密 |
|nick_name     |string  |用户昵称，注册和编辑，都不允许为空 |
|mobile     |string  |手机号码 base64加密  |
|photo     |string  |头像地址  |


##### 返回结果
---
*** JSON示例 ***
```
{
  "meta": {
    "success": true,
    "message": "ok"
  },
  "data": null
}
```

#### 说明
---


