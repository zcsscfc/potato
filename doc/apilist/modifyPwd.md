#### API名称
---
```
密码修改
```

#### 描述
---
```
必须将token传到服务端
token放到请求头里:

X-Token:76774348C8D0483C8A665FDFD20B03C8

```

#### url
---
```
http://host:port/user/modifyPassword
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
    "old_password":"MTIzNDU2",
    "new_password":"MDY1OTQ1"
}
```

##### 输入参数
---
|字段     |类型     |备注
|---------|:------:|:-------|
|old_password     |string  |原密码 base64加密 |
|new_password     |string  |新密码 base64加密 |


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


