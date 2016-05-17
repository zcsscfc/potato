#### API名称
---
```
用户注册编辑
```

#### 描述
---
```
相关的表
sys_user 用户表

用户注册编辑接口，支持以下功能：
1.实现注册功能，insert sys_user
2.实现编辑功能，update sys_user

主要就是，通过判断请求的参数，是否提供 user_id 参数：

（1）如果提供user_id参数，则是update更新模式
此时，先验证数据库sys_user表里对应user_id 的 password 和请求提交的 password_old 是不是相同，相同则允许编辑更新，不相同则返回类似，“没有权限修改”

（2）如果提交的user_id为空，则是 insert 注册模式

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
/user/edit
```

##### 输入参数
---
|字段     |类型     |备注
|---------|:------:|:-------|
|user_id   | string  |（1）注册，此参数为空，服务端自己通过函数 select f_getid('user_id') 产生新的 user_id<br/>（2）编辑，此参数不能为空，服务端根据此参数，更新 sys_user 数据  |
|log_name     |string  |登录账号，注册和编辑，都不允许为空  |
|wechat     |string  |微信号，注册和编辑，都允许为空 |
|password_old     |string  |旧登录密码，注册和编辑，都不允许为空  |
|password     |string  |登录密码，注册和编辑，都不允许为空 |
|nick_name     |string  |用户昵称，注册和编辑，都不允许为空 |
|mobile     |string  |手机号码，注册和编辑，都允许为空  |

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
      "user_id": 200106050110417,
      "log_name": "victor",
      "wechat": "",
      "nick_name": "victor",
      "mobile": "",
      "create_t": "2016-05-10 23:56:15"
    }
}
```

#### 说明
---
|字段     |类型     |备注
|---------|:------:|-------:|
|user_id     |int  |用户编号   |
|log_name     |string  |登录账号  |
|wechat     |string  |微信号，此栏位备用  |
|nick_name     |string  |用户昵称  |
|mobile     |string  |手机号码，此栏位备用 |
|create_t     |date  |创建时间  |


