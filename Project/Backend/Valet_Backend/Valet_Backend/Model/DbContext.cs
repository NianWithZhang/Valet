using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model
{
	public class DbContext
	{

		public SqlSugarClient db;
		public SimpleClient<UserEntity> userDb { get { return new SimpleClient<UserEntity>(db); } }
		public SimpleClient<WardrobeEntity> wardrobeDb { get { return new SimpleClient<WardrobeEntity>(db); } }

		public DbContext()
		{
			 db = new SqlSugarClient(new ConnectionConfig()
			 {
				 ConnectionString = Config.ConnectionString,
				 DbType = DbType.MySql,
				 InitKeyType = InitKeyType.SystemTable //初始化主键和自增列信息到ORM的方式
			 });
		}

	}
}
