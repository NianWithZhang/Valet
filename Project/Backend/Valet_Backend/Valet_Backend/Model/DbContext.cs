using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model
{
	/// <summary>
	/// 各模块控制类Manager的基类 初始化数据库相关操作
	/// </summary>
	public class DbContext
	{
		protected static SqlSugarClient db => new SqlSugarClient(new ConnectionConfig()
		{
			ConnectionString = Config.ConnectionString,
			DbType = DbType.MySql,
			InitKeyType = InitKeyType.SystemTable, //初始化主键和自增列信息到ORM的方式
			IsAutoCloseConnection = true
		});

		protected static SimpleClient<User.User> userDb => new SimpleClient<User.User>(db);
		protected static SimpleClient<Wardrobe.Wardrobe> wardrobeDb => new SimpleClient<Wardrobe.Wardrobe>(db);
		protected static SimpleClient<Clothes.Clothes> clothesDb => new SimpleClient<Clothes.Clothes>(db);
		protected static SimpleClient<Suit.Suit> suitDb => new SimpleClient<Suit.Suit>(db);
		protected static SimpleClient<Suit.Clothes_Suit> clothes_suitDb => new SimpleClient<Suit.Clothes_Suit>(db);

		////私有默认构造函数 防止被实例化
		//private DbContext() { }

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
