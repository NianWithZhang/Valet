using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model.User
{
	/// <summary>
	/// 单个用户
	/// </summary>
	[SugarTable("UserTable")]
	public class User
	{
		/// <summary>
		/// 用户ID 用户的标识符
		/// </summary>
		public string id { get; set; }

		/// <summary>
		/// 用户密码
		/// </summary>
		public string password { get; set; }

		/// <summary>
		/// 用户的推荐宝贝链接地址
		/// </summary>
		[SugarColumn(ColumnName = "recommend_item_url")]
		public string recommendItemUrl { get; set; }
		
		/// <summary>
		/// 用户的推荐宝贝的图片地址
		/// </summary>
		[SugarColumn(ColumnName = "recommend_item_pic_url")]
		public string recommendItemPicUrl { get; set; }

		/// <summary>
		/// 推荐给该用户的宝贝信息
		/// </summary>
		[SugarColumn(IsIgnore = true)]
		public TaobaoItem recommednItem
		{
			get
			{
				TaobaoItem item = new TaobaoItem(recommendItemUrl, recommendItemPicUrl);

				recommendItemUrl = null;
				recommendItemPicUrl = null;

				return item;
			}

			set
			{
				recommendItemUrl = value.itemUrl;
				recommendItemPicUrl = value.picUrl;
			}
		}

		/// <summary>
		/// 默认构造函数
		/// </summary>
		public User()
		{
			id = "hello";
			password = "hello";
			recommendItemUrl = "";
			recommendItemPicUrl = "";
		}

		/// <summary>
		/// 初始化成员变量
		/// </summary>
		/// <param name="_id"></param>
		/// <param name="_password"></param>
		public User(string _id, string _password)
		{
			id = _id;
			password = _password;

			recommendItemUrl = "";
			recommendItemPicUrl = "";
		}
		
		//DELETE!!!!!!!!!!!!!!!
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
