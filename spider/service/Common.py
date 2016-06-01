
import sys
sys.path.append("../utility")

from DbHelper import *

class Common():

	@staticmethod
	def GetId(id_type):
		db = DbHelper()
		result = db.ExecQuery("select f_getid('" + id_type + "')")
		return result[0][0]

	@staticmethod
	def HasDownLoad(page_url):
		db = DbHelper()
		result = db.ExecQuery('''select 1 from post_m a
			where a.from_url="''' + page_url + '''"
			limit 0,1''')
		if len(result) > 0:
			return True
		else:
			return False


























