

# 参考链接

# http://lxml.de/api/lxml.cssselect-pysrc.html


# 参考，天猫，描述图片

# https://desc.alicdn.com/i2/130/710/13371072438/TB18hcHKXXXXXbzXVXX8qtpFXlX.desc%7Cvar%5Edesc%3Bsign%5E3c583fe1ae72146a31516233853bc4f1%3Blang%5Egbk%3Bt%5E1461227321


# 输入：origin_name，seed_url

# 逻辑：

# 输入是配置在xml里的

# 程序，是一个定时执行的程序，每次遍历 xml 每一个 item

# 首先检查 item 的 origin_name 获取 origin_id

# 如果存在 origin_id ，则说明曾经抓取过，则从 post_m 取出抓取的最新的一个，页面，继续抓取 ...

# 如果不存在 origin_id，则说明1次都还没抓取过，则使用 xml 的 seed_url，去抓取

# ------------

# 可能遇到的问题：origin_id 已经抓去过，但是最新抓取的页面中，已经不包含需要提取的 next_page_url 了

# 这时候，


















