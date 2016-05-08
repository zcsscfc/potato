import urllib2
import urlparse
import re
import os
from datetime import *
import time
import lxml.html

import sys
reload(sys)
sys.setdefaultencoding('utf-8')
sys.path.append("../common")
sys.path.append("../utility")

sys.path.append("../service")
from Common import Common

sys.path.append("../orm")
from PotatoDb import PotatoDb

from bs4 import BeautifulSoup

from Throttle import *

def DownLoad(url_seed, url_pattern, origin_name):
	url_list = [url_seed]
	seenUrlList = set(url_list) # make sure not crawler same page loop
	count = 0
	while url_list:
		url = url_list.pop()

		#throttle.wait(url)
		html = urllib2.urlopen(url).read()

		tree = lxml.html.fromstring(html)
		fixedHtml = tree.cssselect('div.zxxw')[0]
		titleText = fixedHtml.cssselect('p.zxxw1')[0].text
		fixedHtml = lxml.html.tostring(fixedHtml)
		
		# fixedHtml = lxml.html.tostring(fixedHtml, pretty_print=True,encoding="utf-8")
		
		# fixedHtml = BeautifulSoup(fixedHtml,'html.parser')
		fixedHtml = BeautifulSoup(fixedHtml,'html5lib')
		# fixedHtml = objSoup.find("div", id="news_details")
		fixedHtml = fixedHtml.prettify('utf-8')
		
		#HandleHtml(url, fixedHtml)
		HandleHtml2MySql(url, fixedHtml, titleText)
		for link in GetLinks(html): # fixedHtml
			if re.match(url_pattern, link):
				if link not in seenUrlList:
					seenUrlList.add(link)
					count = count + 1
					if count <= Throttle.Count:
						a = 1
						url_list.append(link)

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

def HandleHtml2MySql(url, html, titleText):
	create_t = datetime.now()
	post_id = Common.GetId('post_id')

	ins_post_m = PotatoDb.tbl_post_m.insert()
	ins_post_m.execute(post_id=post_id,title=titleText,digest='',thumb='',\
		origin_id='1',create_t=create_t,from_url=url)

	ins_post_d = PotatoDb.tbl_post_d.insert()
	ins_post_d.execute(post_id=post_id,detail=html,create_t=create_t)

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

def CheckPageUrl(page_url):
	
	return True
	
#seedUrl = 'http://b2b.nbdeli.com/Goods/ItemDetail_100043999_40.htm'
#url_pattern = 'http://b2b.nbdeli.com/Goods/ItemDetail'
url_seed = 'http://www.zhuwang.cc/zhuchangjs/201605/264653.html'
url_pattern = 'http://www.zhuwang.cc/zhuchangjs/201605'
origin_name = ''
throttle = Throttle(0)
DownLoad(url_seed, url_pattern, origin_name)








