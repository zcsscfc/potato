
from sqlalchemy import *

class PotatoDb():
	hi = 'hi'
	engine = create_engine('mysql+pymysql://root:123@ec2-52-193-201-108.ap-northeast-1.compute.amazonaws.com/potato?charset=utf8',echo=False)
	metadata = MetaData(engine)
	tbl_post_m = Table('post_m',metadata,autoload=True)
	tbl_post_d = Table('post_d',metadata,autoload=True)
	tbl_bd_origin = Table('bd_origin',metadata,autoload=True)
	
	
	
	















