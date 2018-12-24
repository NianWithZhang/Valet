using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model.User
{
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
