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

# this file for download subject from http://nongye.gongchang.com/
# and insert into bd_subject / bd_subject_category

def DownLoad(url_seed, url_pattern, origin_name):
	url_list = [url_seed]
	count = 0
	while url_list:
		url = url_list.pop()
		thumb = ''
			
		# throttle.wait(url)
		#PROXY = '127.0.0.1:8888' # fiddler proxy
		#PROXY = '10.43.146.29:8080'
		headers = {  
			'Accept': 'text/html, application/xhtml+xml, */*',
			'User-Agent': 'Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko',
			'Accept-Encoding': 'gzip, deflate',
			#'Host': 'detail.tmall.com',
			'DNT': '1',
			'Connection': 'Keep-Alive',
			'Cookie': 'cna=JWzNDxyeEUMCAXTimov8+66/; l=Alpa9qYBcVLQU4nde1rhm2Yw6g58nt5l; t=aaf34248d05832bc2b0224588ecb6c42; _tb_token_=VknDYrghLIXD; cookie2=47a5b832857e164c4ee6cea3378d6317; pnm_cku822=109UW5TcyMNYQwiAiwVQX1EeUR5RH5Cd0xiNGI%3D%7CUm5OcktwSXZPc0lyTHNLfyk%3D%7CU2xMHDJxPk82UjVOI1h2VngRd1snQSJEI107F2gFfgRlAmRKakQYeR9zFGoQPmg%2B%7CVGhXd1llXGdeYVhkXmVbZFxoX2JAdUx1S3VOc0x0QXtPek96Q207%7CVWldfS0TMws2Dy8TLw8hHz8DJnBacBsiW29BF0E%3D%7CVmhIGCAYOAUlGSceKgo0DjUAIBwiGSICOAM2FioULxQ0DjEEUgQ%3D%7CV25Tbk5zU2xMcEl1VWtTaUlwJg%3D%3D; cq=ccp%3D1'
		}
		#request = urllib2.Request(url,'',headers)
		#proxy_handler = urllib2.ProxyHandler({"https":PROXY, "http":PROXY})
		#opener = urllib2.build_opener(proxy_handler, urllib2.HTTPHandler)
		#urllib2.install_opener(opener)
		html = urllib2.urlopen(url).read()
		tree = lxml.html.fromstring(html)

		try:
			fixedHtml = tree.cssselect('div#category')[0]
			fixedHtml = fixedHtml.cssselect('div.cate-more div')
			
			for div in fixedHtml:
				subject_category_a = div.cssselect('p a')[0]
				subject_category_name = subject_category_a.text
				subject_category_tb = PotatoDb.tb_bd_subject_category
				subject_category_row = subject_category_tb.select(subject_category_tb.c.name == subject_category_name).execute().first()
				subject_category_id = 0
				if not subject_category_row:
					subject_category_id = Common.GetId('subject_category_id')
					InsertIntoBdSubjectCategory(subject_category_id,subject_category_name)
				else:
					subject_category_id = subject_category_row[0]
				subject_a_list = div.cssselect('ul a')
				for subject_a in subject_a_list:
					subject_name = subject_a.text
					subject_tb = PotatoDb.tb_bd_subject
					subject_row = subject_tb.select(subject_tb.c.name == subject_name).execute().first()
					if not subject_row:
						subject_id = Common.GetId('subject_id')
						InsertIntoBdSubject(subject_id,subject_name,subject_category_id)
				
			print 'ok'
			
			fixedHtml = '' # lxml.html.tostring(fixedHtml)
		except Exception as ex:
			print 'lance_test:' + str(ex)
			continue

		fixedHtml = BeautifulSoup(fixedHtml,'html5lib')
		fixedHtml = fixedHtml.prettify('utf-8').replace('\n',' ')
		
		#HandleHtml(url, fixedHtml)
		#HandleHtml2MySql(url, fixedHtml, titleText, post_id, thumb)

def HandleHtml(url, html):
	print 'HandleHtml'
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
		origin_id='1',create_t=create_t,from_url=url)

	ins_post_d = PotatoDb.tbl_post_d.insert()
	ins_post_d.execute(post_id=post_id,detail=html,create_t=create_t)
	
def InsertIntoBdSubjectCategory(subject_category_id,name):
	ins_bd_subject_category = PotatoDb.tb_bd_subject_category.insert()
	ins_bd_subject_category.execute(subject_category_id=subject_category_id,name=name)
	
def InsertIntoBdSubject(subject_id,name,subject_category_id):
	ins_bd_subject = PotatoDb.tb_bd_subject.insert()
	ins_bd_subject.execute(subject_id=subject_id,name=name,subject_category_id=subject_category_id)
	
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
url_seed = 'http://nongye.gongchang.com/'
url_pattern = 'no-pattern'

throttle = Throttle(0)
DownLoad(url_seed, url_pattern, origin_name)








