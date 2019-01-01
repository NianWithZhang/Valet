using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model.User
{
	/// <summary>
	/// 设置推荐宝贝时用于多线程传递信息的结构体
	/// </summary>
	public class SetRecommendInfo
	{
		/// <summary>
		/// 需要设置推荐的用户
		/// </summary>
		public IEnumerable<User> users;

		/// <summary>
		/// 推荐的宝贝信息
		/// </summary>
		public TaobaoItem item;

		/// <summary>
		/// 初始化成员变量
		/// </summary>
		/// <param name="_users"></param>
		/// <param name="_item"></param>
		public SetRecommendInfo(IEnumerable<User> _users,TaobaoItem _item)
		{
			users = _users;
			item = _item;
		}
	}

	/// <summary>
	/// 推荐给用户的淘宝宝贝信息
	/// </summary>
	public class TaobaoItem
	{
		/// <summary>
		/// 淘宝宝贝连接
		/// </summary>
		public string itemUrl;
		
		/// <summary>
		/// 淘宝宝贝图片地址
		/// </summary>
		public string picUrl;

		/// <summary>
		/// 默认构造函数
		/// </summary>
		public TaobaoItem() { }

		/// <summary>
		/// 初始化成员变量
		/// </summary>
		/// <param name="_itemUrl"></param>
		/// <param name="_picUrl"></param>
		public TaobaoItem(string _itemUrl, string _picUrl)
		{
			itemUrl = _itemUrl;
			picUrl = _picUrl;
		}
	}
}
