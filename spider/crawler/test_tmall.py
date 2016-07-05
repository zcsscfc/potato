import pymssql
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

from bs4 import BeautifulSoup

def DownLoad(url_seed):
	origin_name = ''
	url_pattern = 'no need pattern'

	url_list = [url_seed]
	while url_list:
		url = url_list.pop()
		PROXY = '10.43.146.29:8080'
		headers = {
			'Accept': 'text/html, application/xhtml+xml, */*',
			'User-Agent': 'Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko',
			'Content-type':'text/html;charset=utf-8',
			'Connection': 'Keep-Alive',
			'Cookie': 'cna=JWzNDxyeEUMCAXTimov8+66/; l=Alpa9qYBcVLQU4nde1rhm2Yw6g58nt5l; t=aaf34248d05832bc2b0224588ecb6c42; _tb_token_=VknDYrghLIXD; cookie2=47a5b832857e164c4ee6cea3378d6317; pnm_cku822=109UW5TcyMNYQwiAiwVQX1EeUR5RH5Cd0xiNGI%3D%7CUm5OcktwSXZPc0lyTHNLfyk%3D%7CU2xMHDJxPk82UjVOI1h2VngRd1snQSJEI107F2gFfgRlAmRKakQYeR9zFGoQPmg%2B%7CVGhXd1llXGdeYVhkXmVbZFxoX2JAdUx1S3VOc0x0QXtPek96Q207%7CVWldfS0TMws2Dy8TLw8hHz8DJnBacBsiW29BF0E%3D%7CVmhIGCAYOAUlGSceKgo0DjUAIBwiGSICOAM2FioULxQ0DjEEUgQ%3D%7CV25Tbk5zU2xMcEl1VWtTaUlwJg%3D%3D; cq=ccp%3D1'
		}
		proxy_handler = urllib2.ProxyHandler({"https":PROXY, "http":PROXY})
		opener = urllib2.build_opener(proxy_handler, urllib2.HTTPHandler)
		urllib2.install_opener(opener)
		request = urllib2.Request(url,'',headers)
		html = urllib2.urlopen(request).read()
		tree = lxml.html.fromstring(html)
		tree = tree.cssselect('ul#J_UlThumb')[0]
		image_list = tree.cssselect('img')
		count = 0
		for image in image_list:
			count = count + 1
			src = image.get('src')
			name = 'H:\\deli_items\\' + str(count) + src[src.rfind('.'):]
			src = 'http:' + src[0:src.rfind('_')]
			conn = urllib2.urlopen(src)
			f = open(name,'wb')
			f.write(conn.read())
			f.close()
			print(name + ' Saved!')

def GetPageUrlList():
	count = 3
	while count < 4:
		url = 'https://deli.tmall.com/category.htm?spm=a1z10.5-b.w4011-11611893624.424.hEUpPe&pageNo=' + str(count) + '#anchor'
		count = count + 1
		GetPageItemUrlList(url)

def GetPageItemUrlList(url):
	PROXY = '10.43.146.29:8080'
	headers = {
		'Accept': 'text/html, application/xhtml+xml, */*',
		'User-Agent': 'Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko',
		'Content-type':'text/html;charset=utf-8',
		'Connection': 'Keep-Alive',
		'Cookie': 'cna=JWzNDxyeEUMCAXTimov8+66/; l=Alpa9qYBcVLQU4nde1rhm2Yw6g58nt5l; t=aaf34248d05832bc2b0224588ecb6c42; _tb_token_=VknDYrghLIXD; cookie2=47a5b832857e164c4ee6cea3378d6317; pnm_cku822=109UW5TcyMNYQwiAiwVQX1EeUR5RH5Cd0xiNGI%3D%7CUm5OcktwSXZPc0lyTHNLfyk%3D%7CU2xMHDJxPk82UjVOI1h2VngRd1snQSJEI107F2gFfgRlAmRKakQYeR9zFGoQPmg%2B%7CVGhXd1llXGdeYVhkXmVbZFxoX2JAdUx1S3VOc0x0QXtPek96Q207%7CVWldfS0TMws2Dy8TLw8hHz8DJnBacBsiW29BF0E%3D%7CVmhIGCAYOAUlGSceKgo0DjUAIBwiGSICOAM2FioULxQ0DjEEUgQ%3D%7CV25Tbk5zU2xMcEl1VWtTaUlwJg%3D%3D; cq=ccp%3D1'
	}
	proxy_handler = urllib2.ProxyHandler({"https":PROXY, "http":PROXY})
	opener = urllib2.build_opener(proxy_handler, urllib2.HTTPHandler)
	urllib2.install_opener(opener)
	request = urllib2.Request(url,'',headers)
	html = urllib2.urlopen(request).read()
	tree = lxml.html.fromstring(html)
	item_list = tree.cssselect('div.J_TItems > div.item4line1 > dl.item')
	for item in item_list:
		a = item.cssselect('dd.detail > a')[0]
		href = 'https:' + a.get('href')
		name = a.text
		
		print '1'
		conn = pymssql.connect(server='localhost',user="sa",password="123",database="esyldb02",charset='utf8')
		cursor = conn.cursor()
		cursor.execute('SELECT * FROM P2_BDIDS')
		row = cursor.fetchone()
		print row
		conn.close()
		
		#DownLoad(href)
		break
	
GetPageUrlList()
			
#DownLoad()








