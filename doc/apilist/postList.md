#### 获取文章列表

#### url:postm


##### 请求方法
---
GET

##### 调用样例
---
```
postm
```

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
|digest     |string  |???  |
|thumb     |string  |???  |
|from_url     |string  |源地址  |
|origin_id     |int  |???  |
|create_t     |date  |创建时间  |


