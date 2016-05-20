#### API名称
---
```
获取文章列表
```

#### 描述
---
```
从以下表，综合返回数据
post_m 文章主表
post_subject 文章与主题对应表，一对多

获取文章列表接口，支持以下功能：
1.按 order_by 输入参数排序
2.按 num 输入参数返回数据总行数
3.可选，按 subject_id 返回特定主题数据
4.可选，按 origin_id 返回特定站点数据
5.不用分页，采用类似下拉刷新，上拉加载历史数据的方式
详见新增加的输入参数，first_post_id, first_create_t, last_post_id, last_create_t
```

#### url
---
```
http://host:port/postm
```

##### 请求方法
---
```
POST
```

##### 调用样例
---
```
postm
```

##### 输入参数
---
|字段     |类型     |备注
|---------|:------:|:-------|
|order_by   | string  |目前固定按 create_t 排序，但仍旧作为输入参数，主要是考虑，后续是否有其他需求  |
|num     |string  |返回的记录行数  |
|subject_id     |string  |文章对应的主题id，比如，农业，水产，种植等。可以为空，如果提供，则只返回对应主题的文章。注意，通过 post_subject join post_m |
|origin_id     |string  |文章对应的站点id，比如，中国农业技术网，中国养殖网等。可以为空，如果提供，则只返回对应站点的文章。  |
|first_post_id     |string  |第一行 post_id  |
|first_create_t     |string  |第一行 create_t  |
|last_post_id     |string  |最后一行 post_id  |
|last_create_t     |string  |最后一行 create_t <br/><br/> 对上面2组参数做下用途介绍：<br/><br/>必须只提供 first 或 last 其中1组 参数，提供 first 代表下拉刷新，提供 last 代表上拉加载历史。<br/><br/> （1）提供 first 时，按照 order_by 排序，排在 first_create_t 和 first_post_id <font color='red'>之上</font>的所有记录数量。大于 num 数量时，取 top num ； 小于等于 num 数量，直接返回 num 行 <br/><br/> （2）提供 last 时，按照 order_by 排序，排在 last_create_t 和 last_post_id <font color='red'>之下</font>的所有记录数量。大于 num 数量时，取 top num ; 小于等于 num 数量，直接返回 num 行|

##### 返回结果
---
*** JSON示例 ***
```
  {
  "meta": {
    "success": true,
    "message": "ok"
  },
  "data": [
    {
      "post_id": 200106050110417,
      "title": "养殖栏如何在规模养猪业发挥作用",
      "digest": "",
      "thumb": "",
      "from_url": "http://www.inong.net/jishu/show-9582.html",
      "origin_id": 1,
      "create_t": "2016-05-10 23:56:15"
    },
    {
      "post_id": 200106050110008,
      "title": "畜舍的防潮管理",
      "digest": "",
      "thumb": "",
      "from_url": "http://www.zhuwang.cc/zhuchangjs/201605/264654.html",
      "origin_id": 1,
      "create_t": "2016-05-10 23:33:06"
    }  
  ]
}
```

#### 说明
---
|字段     |类型     |备注
|---------|:------:|-------:|
|post_id     |int  |文章id   |
|title     |string  |文章标题  |
|digest     |string  |文章摘要，此栏位备用  |
|thumb     |string  |文章缩略图  |
|from_url     |string  |源地址  |
|origin_id     |int  |站点编号  |
|origin_name     |string  |站点名称，通过 origin_id 从 bd_origin 表取出name讯息  |
|create_t     |date  |创建时间  |


