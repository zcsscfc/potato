import urllib2
import urlparse
import re
import os
import datetime
import time
import lxml.html

import sys
reload(sys)
sys.setdefaultencoding('utf-8')
sys.path.append("../common")
sys.path.append("../utility")

from bs4 import BeautifulSoup

from Throttle import *

def DownLoad(seedUrl, urlPattern):
	urlList = [seedUrl]
	seenUrlList = set(urlList)
	count = 0
	while urlList:
		url = urlList.pop()
		#throttle.wait(url)
		html = urllib2.urlopen(url).read()
		
		tree = lxml.html.fromstring(html)
		fixedHtml = tree.cssselect('div#art_left')[0]
		fixedHtml = lxml.html.tostring(fixedHtml, pretty_print=True)
		
		# objSoup = BeautifulSoup(html,'html.parser')
		# fixedHtml = objSoup.find("div", id="news_details")
		# fixedHtml = fixedHtml.prettify('gb2312')
		
		# HandleHtml(url, fixedHtml)
		HandleHtml2MySql(url, fixedHtml)
		for link in GetLinks(fixedHtml):
			if re.match(urlPattern, link):
				if link not in seenUrlList:
					seenUrlList.add(link)
					count = count + 1
					if count <= Throttle.Count:
						a = 1
						#urlList.append(link)

def GetLinks(html):
	webpage_regex = re.compile('<a[^>]+href=["\'](.*?)["\']', re.IGNORECASE)
	return webpage_regex.findall(html)
	
def HandleHtml(url, html):
	filename = GenerateFileName(url)
	folder = os.path.dirname(filename)
	if not os.path.exists(folder):
		os.makedirs(folder)
	with open(filename, 'wb') as fp:
		fp.write(html)
		
def HandleHtml2MySql(url, html)
	import sys
	sys.path.append("../service")
	post_id = Common.GetId('post_id')
	
	

#INSERT INTO `potato`.`post_m` (`post_id`, `title`, `digest`, `thumb`, `origin_id`, `create_t`)
# VALUES ('1', '1', '1', '1', '1', '2016-05-12 00:00:00');
	
	
	from DbHelper import *
	
	db = DbHelper()
	result = db.ExecNoQuery("update post_m set origin_id=300 where title=1")
	print result
	
def GenerateFileName(url):
	components = urlparse.urlsplit(url)
	path = components.path
	if not path:
		path = '/index.html'
	elif path.endswith('/'):
		path += 'index.html'
	filename = components.netloc + path + components.query
	filename = re.sub('[^/0-9a-zA-Z\-.,;_ ]', '_', filename)
	return '/'.join(segment[:250] for segment in filename.split('/'))+'.html'

#seedUrl = 'http://b2b.nbdeli.com/Goods/ItemDetail_100043999_40.htm'
#urlPattern = 'http://b2b.nbdeli.com/Goods/ItemDetail'
seedUrl = 'http://www.yz88.cn/Article/7423438.shtml'
urlPattern = 'http://www.yz88.cn/Article/'
throttle = Throttle(0)
DownLoad(seedUrl,urlPattern)








