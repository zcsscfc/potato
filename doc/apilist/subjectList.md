#### API名称
---
```
主题列表
```

#### 描述
---
```
这个接口，主要实现，对主题的，查询

相关表，bd_subject，user_subject

1. 返回某个 user_id 订阅的主题列表
2. 返回某个 subject_category_id 下的主题列表

输入参数：user_id 和 subject_category_id 

1. 只提供 user_id 参数
则是 user_subject inner join bd_subject

2. 只提供 subject_category_id 参数
则是 直接返回 bd_subject ，
返回参数的 user_id 为空

3. 同时提供 user_id 和 subject_category_id
则是：bd_subject left join bd_subject
也就是，要知道，当前 subject_category_id 对应的 subject_id 是否被用户订阅

```

#### url
---
```
http://host:port/???????????????????
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
？？？？？？？？？？
}
```

##### 输入参数
---
|字段     |类型     |备注
|---------|:------:|:-------|
|user_id     |string  |  用户编号<br/>（可选）<br/>如果提供，则从表 user_subject 串表 bd_subject 取用户订阅的主题列表  |
|subject_category_id     |string  | 源分类编号<br/>（可选） |

##### 返回结果
---
*** JSON示例 ***
```
{
  ？？？？？？？
}
```

#### 说明
---
|字段     |类型     |备注
|---------|:------:|:-------|
|user_id     |string  |  用户编号<br/>（可选）<br/>如果有值返回，则说明，此 subject_id 已经被用户订阅|
|subject_id     |string  |  源站点编号|
|bd_subject.name     |string  | 源站点名称|


