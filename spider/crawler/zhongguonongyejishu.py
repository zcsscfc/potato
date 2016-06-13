#-*-coding:utf-8-*- 
import json
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
	count = 1
	while url_list:
		url = url_list.pop()
		if Common.HasDownLoad(url): # has download, go to next
			continue
		html = urllib2.urlopen(url).read()
		tree = lxml.html.fromstring(html)
		post_id = Common.GetId('post_id')
		thumb = ''

		try:
			print 'hi begin'
			fixedHtml = tree.cssselect('div#news_details')[0]
			
			imageTag = fixedHtml.cssselect('img')[0]
			imageTagSrc = imageTag.get('src')
			print imageTagSrc
			conn = urllib2.urlopen(imageTagSrc)
			
			boundary = '----------%s' % hex(int(time.time() * 1000))
			data = []
			
			data.append('--%s' % boundary)
			
			data.append('Content-Disposition: form-data; name="%s"\r\n' % 'path')
			data.append('post/' + str(post_id) + '/thumb')
			
			data.append('--%s' % boundary)
			
			data.append('Content-Disposition: form-data; name="%s"; filename="2.jpg"' % 'fileUpload')
			data.append('Content-Type: %s\r\n' % 'image/jpg')
			data.append(conn.read())
			
			data.append('--%s--\r\n' % boundary) # 结尾标记
			
			file_upload_url = 'http://ec2-52-193-201-108.ap-northeast-1.compute.amazonaws.com/file/upload'
			http_body='\r\n'.join(data)
			
			request = urllib2.Request(file_upload_url,data=http_body)
			request.add_header('Content-Type', 'multipart/form-data; boundary=%s' % boundary)
			
			response = urllib2.urlopen(request, timeout=5)
			qrcont = response.read()
			json_qrcont = json.loads(qrcont)
			thumb = json_qrcont['data'][0]
			
			print('upload pic finish')

			titleText = fixedHtml.cssselect('center>h1')[0].text.strip()
			fixedHtml = lxml.html.tostring(fixedHtml)
		except Exception as ex:
			print ex
			continue
		
		fixedHtml = BeautifulSoup(fixedHtml,'html5lib')
		fixedHtml = fixedHtml.prettify('utf-8').replace('\n',' ')

		HandleHtml2MySql(url, fixedHtml, titleText, post_id, thumb)
		for link in GetLinks(html): # fixedHtml
			if re.match(url_pattern, link):
				count = count + 1
				if count <= Throttle.Count:
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

def HandleHtml2MySql(url, html, titleText, post_id, thumb):
	create_t = datetime.now()
	
	ins_post_m = PotatoDb.tbl_post_m.insert()
	ins_post_m.execute(post_id=post_id,title=titleText,digest='',thumb=thumb,\
		origin_id='9',create_t=create_t,from_url=url)

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

origin_name = '中国农业技术网'
url_seed = 'http://www.chinanyjs.com/news/18285872.html'
url_pattern = 'http://www.chinanyjs.com/news'

throttle = Throttle(0)
DownLoad(url_seed, url_pattern, origin_name)







