﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Valet_Backend.Model;
using Valet_Backend.Model.Clothes;

namespace Valet_Backend.Controllers
{

	/// <summary>
	/// 返回的衣物简要信息格式
	/// </summary>
	public class ClothesResponse
	{
		public int id;
		public string name;

		public ClothesResponse(int _id,string _name)
		{
			id = _id;
			name = _name;
		}
	}

	/// <summary>
	/// 返回衣物列表的信息格式
	/// </summary>
	public class ClothesResponseList
	{
		public ClothesResponse[] clothes;

		public ClothesResponseList()
		{
			clothes = new ClothesResponse[0];
		}

		public ClothesResponseList(ClothesResponse[] _clothes)
		{
			clothes = _clothes;
		}
	}
	
    [Route("api/[controller]")]
    [ApiController]
    public class ClothesController : ControllerBase
    {
		/// <summary>
		/// 通过编号获取衣物信息
		/// </summary>
		/// <param name="id">衣物编号</param>
		/// <returns>衣物信息</returns>
		[HttpGet]
		public Clothes getClothesInfo(int id)
		{
			return ClothesManager.getInfo(id);
		}

		/// <summary>
		/// 通过衣橱编号获取衣物列表
		/// </summary>
		/// <param name="wardrobe_id">衣橱编号</param>
		/// <returns>衣物列表</returns>
		[HttpGet]
		public ClothesResponseList getByWardrobe(int wardrobe_id)
		{
			return ClothesManager.getByWardrobe(wardrobe_id);
		}

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
		[HttpPut]
		public BooleanResponse changeClothesWardrobe(int[] clothes_ids,int wardrobe_id)
		{
			return new BooleanResponse(ClothesManager.changeWardrobe(clothes_ids,wardrobe_id));
		}

		//不会单独修改衣物照片信息
		///// <summary>
		///// 更改衣物照片
		///// </summary>
		///// <param name="pic">新衣物照片</param>
		///// <param name="id">要变更照片的衣物ID</param>
		///// <returns>变更结果 是否变更成功</returns>
		//[HttpPut]
		//public BooleanResponse modifyClothesPic(IFormFile pic,int id)
		//{
		//	return new BooleanResponse(ClothesManager.savePic(pic, id));
		//}
		/// <summary>
		/// 更改衣物信息
		/// </summary>
		/// <param name="pic">修改后衣物照片</param>
		/// <param name="id">衣物ID</param>
		/// <param name="name">修改后衣物名称</param>
		/// <param name="type">修改后衣物类型</param>
		/// <param name="thickness">修改后衣物厚度</param>
		/// <returns>变更结果 是否修改成功</returns>
		[HttpPut]
		public BooleanResponse modifyClothes(IFormFile pic,int id,string name,ClothesType type,int thickness)
		{
			return new BooleanResponse(ClothesManager.modify(pic, id, name, type, thickness));
		}

		[HttpDelete]
		public BooleanResponse deleteClothes(int id)
		{
			return new BooleanResponse(ClothesManager.delete(id));
		}
    }
}