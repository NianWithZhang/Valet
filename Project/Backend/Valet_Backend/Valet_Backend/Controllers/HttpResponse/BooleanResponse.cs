using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Controllers.HttpResponse
{
	/// <summary>
	/// Http服务返回的Boolean信息格式
	/// </summary>
	public class BooleanResponse
	{
		/// <summary>
		/// 结果布尔值
		/// </summary>
		public bool ans;

		/// <summary>
		/// 初始化成员变量
		/// </summary>
		/// <param name="_ans"></param>
		public BooleanResponse(bool _ans) { ans = _ans; }
	}
}
