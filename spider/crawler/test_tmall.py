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
	count = 0
	while url_list:
		url = url_list.pop()
		
		PROXY = '10.43.146.29:8080'
		headers = {
			'Accept': 'text/html, application/xhtml+xml, */*',
			'User-Agent': 'Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko',
			'Accept-Encoding': 'gzip, deflate',
			'DNT': '1',
			'Connection': 'Keep-Alive',
			'Cookie': 'cna=JWzNDxyeEUMCAXTimov8+66/; l=Alpa9qYBcVLQU4nde1rhm2Yw6g58nt5l; t=aaf34248d05832bc2b0224588ecb6c42; _tb_token_=VknDYrghLIXD; cookie2=47a5b832857e164c4ee6cea3378d6317; pnm_cku822=109UW5TcyMNYQwiAiwVQX1EeUR5RH5Cd0xiNGI%3D%7CUm5OcktwSXZPc0lyTHNLfyk%3D%7CU2xMHDJxPk82UjVOI1h2VngRd1snQSJEI107F2gFfgRlAmRKakQYeR9zFGoQPmg%2B%7CVGhXd1llXGdeYVhkXmVbZFxoX2JAdUx1S3VOc0x0QXtPek96Q207%7CVWldfS0TMws2Dy8TLw8hHz8DJnBacBsiW29BF0E%3D%7CVmhIGCAYOAUlGSceKgo0DjUAIBwiGSICOAM2FioULxQ0DjEEUgQ%3D%7CV25Tbk5zU2xMcEl1VWtTaUlwJg%3D%3D; cq=ccp%3D1'
		}
		proxy_handler = urllib2.ProxyHandler({"https":PROXY, "http":PROXY})
		opener = urllib2.build_opener(proxy_handler, urllib2.HTTPHandler)
		urllib2.install_opener(opener)
		html = urllib2.urlopen(url).read()
		tree = lxml.html.fromstring(html)
		
		fixedHtml = lxml.html.tostring(fixedHtml, pretty_print=True,encoding="utf-8")
		tree = tree.cssselect('ul#J_UlThumb')[0]
		image_list = tree.cssselect('img')
		
		count = 0
		for image in image_list:
			count = count + 1
			src = image.get('src')
			name = 'H:\\tmall_img_down_test\\' + str(count) + src[src.rfind('.'):]
			src = 'http:' + src[0:src.rfind('_')]
			conn = urllib2.urlopen(src)
			f = open(name,'wb')
			f.write(conn.read())
			f.close()
			print(name + ' Saved!')
		fixedHtml = ''

		fixedHtml = BeautifulSoup(fixedHtml,'html5lib')
		fixedHtml = fixedHtml.prettify('utf-8').replace('\n',' ')
		
		#HandleHtml(url, fixedHtml)
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
	print 'HandleHtml'
	filename = GenerateFileName(url)
	folder = os.path.dirname(filename)
	if not os.path.exists(folder):
		os.makedirs(folder)
	with open(filename, 'wb') as fp:
		fp.write(html)

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

origin_name = ''
url_seed = 'https://detail.tmall.com/item.htm?spm=a1z10.5-b.w4011-11611893624.94.s5fTwi&id=13371072438&rn=c135f614d00170bbf60fd1a112bd5174&abbucket=15'
url_pattern = 'no need pattern'

throttle = Throttle(0)
DownLoad(url_seed, url_pattern, origin_name)








