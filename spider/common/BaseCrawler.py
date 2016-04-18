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

from bs4 import BeautifulSoup

class Throttle:
	Count = 3000
	def __init__(self, delay):
		self.delay = delay
		self.domains = {}
		
	def wait(self, url):
		domain = urlparse.urlparse(url).netloc
		last_accessed = self.domains.get(domain)
		if self.delay > 0 and last_accessed is not None:
			sleep_secs = self.delay - (datetime.datetime.now() - last_accessed).seconds
			if sleep_secs > 0:
				time.sleep(sleep_secs)
		self.domains[domain] = datetime.datetime.now()
		
def DownLoad(seedUrl, urlPattern):
	urlList = [seedUrl]
	seenUrlList = set(urlList)
	count = 0
	while urlList:
		url = urlList.pop()
		#throttle.wait(url)
		html = urllib2.urlopen(url).read()
		
		tree = lxml.html.fromstring(html)
		fixedHtml = tree.cssselect('div#relatedshow')[0]
		fixedHtml = lxml.html.tostring(fixedHtml, pretty_print=True)
		
		# objSoup = BeautifulSoup(html,'html.parser')
		# fixedHtml = objSoup.find("div", id="news_details")
		# fixedHtml = fixedHtml.prettify('gb2312')
		
		HandleHtml(url, fixedHtml)
		for link in GetLinks(fixedHtml):
			if re.match(urlPattern, link):
				if link not in seenUrlList:
					seenUrlList.add(link)
					count = count + 1
					if count <= Throttle.Count:
						urlList.append(link)

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

seedUrl = 'http://b2b.nbdeli.com/Goods/ItemDetail_100043999_40.htm'
urlPattern = 'http://b2b.nbdeli.com/Goods/ItemDetail'
throttle = Throttle(0)
DownLoad(seedUrl,urlPattern)

#from pymongo import MongoClient
#client = MongoClient('localhost', 27017)
#db = client.cache
#db.webpage.insert({'url': url, 'html': html})
#print db.webpage.find_one()
#db.webpage.update({'_id': url}, {'$set': {'html': '123'}},upsert=True)
#print db.webpage.find_one({'_id': url})









