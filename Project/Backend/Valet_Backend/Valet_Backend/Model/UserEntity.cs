using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model
{
	[SugarTable("UserTable")]
	public class UserEntity
	{
		public string id { get; set; }

		public string password { get; set; }

		public string recommendedItemUrl { get; set; }
		public string recommendedItemPicUrl { get; set; }

		public UserEntity()
		{
			id = "hello";
			password = "hello";
			recommendedItemUrl = "";
			recommendedItemPicUrl = "";
		}

		public UserEntity(string _id, string _password)
		{
			id = _id;
			password = _password;

			recommendedItemUrl = "";
			recommendedItemPicUrl = "";
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
