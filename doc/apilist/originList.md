#### API名称
---
```
站点列表
```

#### 描述
---
```
这个接口，主要实现，对源站点的，查询

相关表，bd_origin，user_origin

1. 返回某个 user_id 订阅的站点列表
2. 返回某个 origin_category_id 下的站点列表

输入参数：user_id 和 origin_category_id 

1. 只提供 user_id 参数
则是 user_origin inner join bd_origin

2. 只提供 origin_category_id 参数
则是 直接返回 bd_origin ，
返回参数的 user_id 为空

3. 同时提供 user_id 和 origin_category_id
则是：bd_origin left join user_origin
也就是，要知道，当前 origin_category_id 对应的 origin_id 是否被用户订阅


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
|user_id     |string  |  用户编号<br/>（可选）<br/>如果提供，则从表 user_origin 串表 bd_origin 取用户订阅的站点列表  |
|origin_category_id     |string  | 源分类编号<br/>（可选） |

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
|user_id     |string  |  用户编号<br/>（可选）<br/>如果有值返回，则说明，此 origin_id 已经被用户订阅|
|origin_id     |string  |  源站点编号|
|bd_origin.name     |string  | 源站点名称|

