using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model
{
	public class DbContext
	{
		protected static SqlSugarClient db => new SqlSugarClient(new ConnectionConfig()
		{
			ConnectionString = Config.ConnectionString,
			DbType = DbType.MySql,
			InitKeyType = InitKeyType.SystemTable //初始化主键和自增列信息到ORM的方式
		});

		protected static SimpleClient<User.User> userDb => new SimpleClient<User.User>(db);
		protected static SimpleClient<Wardrobe.Wardrobe> wardrobeDb => new SimpleClient<Wardrobe.Wardrobe>(db);
		protected static SimpleClient<Clothes.Clothes> clothesDb => new SimpleClient<Clothes.Clothes>(db);
		protected static SimpleClient<Suit.Suit> suitDb => new SimpleClient<Suit.Suit>(db);
		protected static SimpleClient<Suit.Clothes_Suit> clothes_suitDb => new SimpleClient<Suit.Clothes_Suit>(db);

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
