using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model
{
	[SugarTable("UserTable")]
	public class User
	{
		public string id { get; set; }

		public string password { get; set; }

		public string recommendItemUrl { get; set; }
		public string recommendItemPicUrl { get; set; }

		public User()
		{
			id = "hello";
			password = "hello";
			recommendItemUrl = "";
			recommendItemPicUrl = "";
		}

		public User(string _id, string _password)
		{
			id = _id;
			password = _password;

			recommendItemUrl = "";
			recommendItemPicUrl = "";
		}
		
		//public void resetRecomend()
		//{
		//	reco
		//}
		//public void setRecommend(string itemUrl,string picUrl)
		//{
		//	recommendedItemUrl = itemUrl;
		//	recommendedItemPicUrl = picUrl;
		//}
	}
}
