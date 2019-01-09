using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Valet_Backend.Controllers.HttpResponse;
using Valet_Backend.Model;
using Valet_Backend.Model.User;
using Valet_Backend.Model.Wardrobe;

namespace Valet_Backend.Controllers
{
	/// <summary>
	/// 衣橱模块Http服务接口
	/// </summary>
	[Route("api/[controller]")]
	[ApiController]
	public class WardrobeController : ControllerBase
	{
		#region HttpGet 查询

		/// <summary>
		/// 获取指定用户所有的所有衣橱列表
		/// </summary>
		/// <param name="id">用户ID</param>
		/// <returns>获取的衣橱列表</returns>
		[HttpGet]
		public WardrobeResponseList get(string id)
		{
			return WardrobeManager.getByUser(id);
		}

		#endregion
		
		#region HttpPost 添加

		/// <summary>
		/// 新建衣橱
		/// </summary>
		/// <param name="user_id">用户ID</param>
		/// <param name="name">新衣橱名称</param>
		/// <returns>新建结果 是否成功新建衣橱</returns>
		[HttpPost]
		public BooleanResponse addWardrobe(string user_id, string name)
		{
			return new BooleanResponse(WardrobeManager.add(user_id, name));
		}

		#endregion

		#region HttpDelete

		/// <summary>
		/// 删除衣橱
		/// </summary>
		/// <param name="id">需要删除的衣橱ID</param>
		/// <returns>删除结果 是否成功删除衣橱</returns>
		[HttpDelete]
		public BooleanResponse deleteWardrobe(int id)
		{
			return new BooleanResponse(WardrobeManager.delete(id));
		}

		/// <summary>
		/// 批量删除衣橱
		/// </summary>
		/// <param name="ids">需要删除的衣橱ID列表</param>
		/// <returns>删除结果 是否成功删除衣橱</returns>
		[Route("many")]
		[HttpDelete]
		public BooleanResponse deleteWardrobes(int[] ids)
		{
			return new BooleanResponse(WardrobeManager.delete(ids));
		}

		#endregion

		#region HttpPut 修改

		/// <summary>
		/// 重命名衣橱
		/// </summary>
		/// <param name="id">需要重命名的衣橱ID</param>
		/// <param name="name">衣橱的新名称</param>
		/// <returns>重命名结果 是否成功重命名</returns>
		[HttpPut]
		public BooleanResponse renameWardrobe(int id, string name)
		{
			return new BooleanResponse(WardrobeManager.rename(id, name));
		}

		#endregion
		
	}
}