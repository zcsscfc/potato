
import pymysql

class DbHelper():

	def SayHi(self, name): # test function
		return 'hi ' + name

	def ConnDb(self): # innser function, no need call outer
		conn = pymysql.connect(host='ec2-52-193-201-108.ap-northeast-1.compute.amazonaws.com',user='root',passwd='123',db='potato',charset='utf8',autocommit='true')
		cur = conn.cursor()
		return (conn,cur)
	
	def ExecQuery(self, sql): # return row[0][0]
		conn = None
		cur = None
		try:
			conn,cur = self.ConnDb()
			cur.execute(sql)
			result = cur.fetchall()
			return result
		finally:
			if cur:
				cur.close()
			if conn:
				conn.close()
	
	def ExecNoQuery(self, sql): # if update return affected rows, if insert return 1
		conn = None
		cur = None
		try:
			conn,cur = self.ConnDb()
			result = cur.execute(sql)
			return result
		finally:
			if cur:
				cur.close()
			if conn:
				conn.close()
















