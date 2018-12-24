using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Valet_Backend.Model.Clothes;
using Valet_Backend.Model.Suit;

namespace Valet_Backend.Controllers
{
	/// <summary>
	///  获取穿搭时返回的单个穿搭信息
	/// </summary>
	public class SuitResponse
	{
		public int id;
		public string name;
		public string evaluation;

		public SuitResponse(int _id,string _name,string _evaluation = "")
		{
			id = _id;
			name = _name;
			evaluation = _evaluation;
		}
	}

	/// <summary>
	/// 获取穿搭时返回的穿搭信息列表
	/// 包含穿搭ID列表以及天气信息 若仅为获取穿搭信息则天气信息内容为空
	/// </summary>
	public class SuitResponseList
	{
		WeatherInfo weather;
		SuitResponse[] suits;

		public SuitResponseList(WeatherInfo _weather,SuitResponse[] _suits)
		{
			weather = _weather;
			suits = _suits;
		}
	}

	/// <summary>
	/// 穿搭冷暖评价信息返回格式
	/// </summary>
	public class EvaluationResponse
	{
		//表示评价相关衣物和套装是否都正确找到
		public bool ans;
		public string description;

		public EvaluationResponse(bool _ans,string _description)
		{
			ans = _ans;
			description = _description;
		}
	}

	/// <summary>
	/// 穿搭相关Http服务接口
	/// </summary>
    [Route("api/[controller]")]
    [ApiController]
    public class SuitController : ControllerBase
    {
		#region HttpGet

		/// <summary>
		/// 获取指定穿搭的衣物列表
		/// </summary>
		/// <param name="id">穿搭ID</param>
		/// <returns>穿搭所包含的衣物ID列表</returns>
		[HttpGet]
		public ClothesResponseList getClothes(int id)
		{
			return SuitManager.getClothes(id);
		}

		/// <summary>
		/// 获取指定衣橱中的所有穿搭的ID以及根据温度的评价列表
		/// </summary>
		/// <param name="wardrobe_id">衣橱ID</param>
		/// <param name="temperature">当前温度</param>
		/// <returns>衣橱中的穿搭的ID以及</returns>
		[HttpGet]
		public SuitResponseList getByWardrobe(int wardrobe_id, double temperature)
		{
			return SuitManager.getByWardrobe(wardrobe_id, temperature);
		}

		/// <summary>
		/// 获取一套衣物搭配相关指定温度的保暖程度评价
		/// </summary>
		/// <param name="clothes">一套衣物ID列表</param>
		/// <param name="temperature">当前温度</param>
		/// <returns>评价结果</returns>
		[HttpGet]
		public EvaluationResponse getWarmth(int[] clothes, double temperature)
		{
			EvaluationResponse ans = SuitManager.evaluate(clothes, temperature);
			return ans;
		}

		/// <summary>
		/// 根据指定衣橱以及位置 获取当前位置的天气信息以及穿搭推荐
		/// </summary>
		/// <param name="wardrobe_id">衣橱ID</param>
		/// <param name="latitude">纬度</param>
		/// <param name="longitude">经度</param>
		/// <returns>根据指定的衣橱以及地理位置产生的天气信息以及推荐穿搭列表</returns>
		[HttpGet]
		public SuitResponseList getAdvices(int wardrobe_id, double latitude, double longitude)
		{
			return SuitManager.advise(wardrobe_id, latitude, longitude);
		}

		#endregion

		#region HttpPost

		/// <summary>
		/// 新增穿搭套装
		/// </summary>
		/// <param name="pic">穿搭图片</param>
		/// <param name="name">穿搭名称</param>
		/// <param name="wardrobe_id">所属衣橱ID</param>
		/// <param name="clothes">所包含的所有衣物ID列表</param>
		/// <returns>添加结果 是否成功添加</returns>
		[HttpPost]
		public BooleanResponse add(IFormFile pic,string name,int wardrobe_id,int[] clothes)
		{
			return new BooleanResponse(SuitManager.add(pic,name, wardrobe_id, clothes));
		}

		#endregion

		#region HttpDelete

		/// <summary>
		/// 删除穿搭
		/// </summary>
		/// <param name="id">需要删除的穿搭的ID</param>
		/// <returns>删除结果 是否成功删除</returns>
		[HttpDelete]
		public BooleanResponse delete(int id)
		{
			return new BooleanResponse(SuitManager.delete(id));
		}

		#endregion

		#region HttpPut

		/// <summary>
		/// 选择今日穿某套穿搭
		/// </summary>
		/// <param name="id">将要穿的穿搭ID</param>
		/// <returns>操作结果 是否对套装进行穿着操作</returns>
		[HttpPut]
		public BooleanResponse wear(int id)
		{
			return new BooleanResponse(SuitManager.wear(id));
		}

		/// <summary>
		/// 选择今日穿某套混搭
		/// </summary>
		/// <param name="clothes_ids">混搭中所有衣物ID</param>
		/// <returns>操作结果 是否成功对所有衣物进行穿着操作</returns>
		[HttpPut]
		public BooleanResponse wear(int[] clothes_ids)
		{
			return new BooleanResponse(SuitManager.wear(clothes_ids));
		}

		#endregion

		//DELETE!!!!!!!!!!!!!!
		//不需要直接获取天气信息 需要在启动应用时getAdvice时获取
		//[HttpGet]
		//public WeatherInfo weather(double latitude,double longitude)
		//{
		//	return WeatherApi.getLocationWeather(latitude,longitude);
		//}
	}
}