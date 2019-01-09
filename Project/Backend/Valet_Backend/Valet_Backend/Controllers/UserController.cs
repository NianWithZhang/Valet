using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using SqlSugar;
using Valet_Backend.Controllers.HttpResponse;
using Valet_Backend.Model;
using Valet_Backend.Model.User;

namespace Valet_Backend.Controllers
{
	/// <summary>
	/// 用户相关Http服务接口
	/// </summary>
	[Route("api/[controller]")]
	[ApiController]
	public class UserController : ControllerBase
	{
		#region HttpGet 查询

		/// <summary>
		/// 检查用户ID是否已存在
		/// </summary>
		/// <param name="id">待检查的用户ID</param>
		/// <returns>是否存在当前用户ID</returns>
		///
		[Route("exist")]
		[HttpGet]
		public BooleanResponse checkIDExistence(string id)
		{
			return new BooleanResponse(UserManager.exist(id));
		}

		/// <summary>
		/// 检查用户ID和密码
		/// </summary>
		/// <param name="id">用户ID</param>
		/// <param name="password">密码</param>
		/// <returns>检查结果 是否用户存在且密码正确</returns>
		[HttpGet]
		public BooleanResponse checkUser(string id, string password)
		{
			return new BooleanResponse((UserManager.check(id, password)));
		}

		#endregion

		#region HttpPost 添加

		/// <summary>
		/// 添加新用户
		/// </summary>
		/// <param name="id">用户ID</param>
		/// <param name="password">密码</param>
		/// <returns>添加结果 是否有重复用户ID</returns>
		[HttpPost]
		public BooleanResponse addUser(string id, string password)
		{
			return new BooleanResponse(UserManager.add(id,password));
		}

		#endregion

		#region HttpPut 修改

		/// <summary>
		/// 获取并清空用户的待推送推荐宝贝信息
		/// </summary>
		/// <param name="id">用户ID</param>
		/// <returns>淘宝宝贝信息</returns>
		[HttpPut]
		public TaobaoItem getRecommend(string id)
		{
			return UserManager.getRecommend(id);
		}

		#endregion
	}
}