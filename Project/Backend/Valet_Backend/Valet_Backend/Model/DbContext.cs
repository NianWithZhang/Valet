using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model
{
	public class DbContext
	{

		public static SqlSugarClient db
		{
			get=>new SqlSugarClient(new ConnectionConfig()
		{
			ConnectionString = Config.ConnectionString,
				 DbType = DbType.MySql,
				 InitKeyType = InitKeyType.SystemTable //初始化主键和自增列信息到ORM的方式
			 });
		}
		public static SimpleClient<User> userDb { get { return new SimpleClient<User>(db); } }
		public static SimpleClient<Wardrobe> wardrobeDb { get { return new SimpleClient<Wardrobe>(db); } }
		public static SimpleClient<Clothes> clothesDb { get { return new SimpleClient<Clothes>(db); } }
		public static SimpleClient<Suit> suitDb { get { return new SimpleClient<Suit>(db); } }
		public static SimpleClient<Clothes_Suit> clothes_suitDb { get { return new SimpleClient<Clothes_Suit>(db); } }

		//public DbContext()
		//{
		//	 db = new SqlSugarClient(new ConnectionConfig()
		//	 {
		//		 ConnectionString = Config.ConnectionString,
		//		 DbType = DbType.MySql,
		//		 InitKeyType = InitKeyType.SystemTable //初始化主键和自增列信息到ORM的方式
		//	 });
		//}

	}
}
