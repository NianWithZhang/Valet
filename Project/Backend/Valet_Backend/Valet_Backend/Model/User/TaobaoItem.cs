using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model.User
{
	public class TaobaoItem
	{
		//淘宝宝贝连接
		public string itemUrl;

		//淘宝宝贝图片地址
		public string picUrl;

		public TaobaoItem() { }

		public TaobaoItem(string _itemUrl, string _picUrl)
		{
			itemUrl = _itemUrl;
			picUrl = _picUrl;
		}
	}
}
