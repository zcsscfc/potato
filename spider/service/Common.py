
import sys
sys.path.append("../utility")

from DbHelper import *

class Common():

	@staticmethod
	def GetId(id_type):
		db = DbHelper()
		result = db.ExecQuery("select f_getid('" + id_type + "')")
		return result[0][0]




























