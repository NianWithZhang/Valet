using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Valet_Backend.Controllers.HttpResponse;
using Valet_Backend.Model;
using Valet_Backend.Model.Clothes;

namespace Valet_Backend.Controllers
{
	/// <summary>
	/// 衣物相关Http服务接口
	/// </summary>
    [Route("api/[controller]")]
    [ApiController]
    public class ClothesController : ControllerBase
    {
		#region HttpGet 查询

		/// <summary>
		/// 通过编号获取衣物信息
		/// </summary>
		/// <param name="id">衣物编号</param>
		/// <returns>衣物信息</returns>
		[HttpGet]
		public ClothesInfoResponse getClothesInfo(int id)
		{
			return ClothesManager.getInfo(id);
		}

		/// <summary>
		/// 通过衣橱编号获取衣物列表
		/// </summary>
		/// <param name="id">衣橱编号</param>
		/// <returns>衣物列表</returns>
		[Route("wardrobe")]
		[HttpGet]
		public ClothesResponseList getByWardrobe(int id)
		{
			return ClothesManager.getByWardrobe(id);
		}

		#endregion

		#region HttpPost 添加

		/// <summary>
		/// 添加衣物
		/// </summary>
		/// <param name="pic">衣物照片</param>
		/// <param name="wardrobe_id">衣物所属衣橱ID</param>
		/// <param name="name">衣物名称</param>
		/// <param name="type">衣物类型</param>
		/// <param name="thickness">衣物厚度指数</param>
		/// <returns>添加结果 是否成功添加</returns>
		[HttpPost]
		public BooleanResponse addClothes(IFormFile pic,int wardrobe_id,string name,ClothesType type,int thickness)
		{
			return new BooleanResponse(ClothesManager.add(pic, wardrobe_id, name, type, thickness));
		}

		#endregion

		#region HttpDelete 删除

		/// <summary>
		/// 删除单件衣物
		/// </summary>
		/// <param name="id">需要删除的衣物ID</param>
		/// <returns>删除结果 是否成功删除衣物</returns>
		[HttpDelete]
		public BooleanResponse deleteClothes(int id)
		{
			return new BooleanResponse(ClothesManager.delete(id));
		}
		/// <summary>
		/// 批量删除衣物
		/// </summary>
		/// <param name="ids">需要删除的衣物ID列表</param>
		/// <returns>删除结果 是否成功删除所有衣物</returns>
		[Route("many")]
		[HttpDelete]
		public BooleanResponse deleteClothes(int[] ids)
		{
			return new BooleanResponse(ClothesManager.delete(ids));
		}

		#endregion

		#region HttpPut 修改

		/// <summary>
		/// 衣物变更衣橱
		/// </summary>
		/// <param name="clothes_id">需要变更衣橱的衣物ID</param>
		/// <param name="wardrobe_id">衣橱ID</param>
		/// <returns>变更结果 是否变更成功</returns>
		[HttpPut]
		public BooleanResponse changeClothesWardrobe(int clothes_id, int wardrobe_id)
		{
			return new BooleanResponse(ClothesManager.changeWardrobe(clothes_id, wardrobe_id));
		}
		/// <summary>
		/// 衣物批量变更衣橱
		/// </summary>
		/// <param name="clothes_ids">需要变更衣橱的衣物ID列表</param>
		/// <param name="wardrobe_id">衣橱ID</param>
		/// <returns>变更结果 是否全部变更成功</returns>
		[Route("many")]
		[HttpPut]
		public BooleanResponse changeClothesWardrobe(int[] clothes_ids, int wardrobe_id)
		{
			return new BooleanResponse(ClothesManager.changeWardrobe(clothes_ids, wardrobe_id));
		}

		/// <summary>
		/// 更改衣物信息
		/// </summary>
		/// <param name="pic">修改后衣物照片</param>
		/// <param name="id">衣物ID</param>
		/// <param name="name">修改后衣物名称</param>
		/// <param name="type">修改后衣物类型</param>
		/// <param name="thickness">修改后衣物厚度</param>
		/// <returns>变更结果 是否修改成功</returns>
		[Route("modify")]
		[HttpPut]
		public BooleanResponse modifyClothes(IFormFile pic, int id, string name, ClothesType type, int thickness)
		{
			return new BooleanResponse(ClothesManager.modify(pic, id, name, type, thickness));
		}

		#endregion
	}
}