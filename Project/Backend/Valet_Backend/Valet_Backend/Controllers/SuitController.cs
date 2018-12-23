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

    [Route("api/[controller]")]
    [ApiController]
    public class SuitController : ControllerBase
    {
		[HttpPost]
		public object add(IFormFile pic,string name,int wardrobe_id,int[] clothes)
		{
			return new { ans = SuitManager.add(pic,name, wardrobe_id, clothes) };
		}

		[HttpDelete]
		public object delete(int id)
		{
			return new { ans = SuitManager.delete(id) };
		}

		//[HttpGet]
		//public WeatherInfo weather(double latitude,double longitude)
		//{
		//	return WeatherApi.getLocationWeather(latitude,longitude);
		//}

		[HttpGet]
		public ClothesResponseList getClothes(int id)
		{
			return SuitManager.getClothes(id);
		}

		[HttpGet]
		public SuitResponseList getByWardrobe(int wardrobe_id,int temperature)
		{
			return SuitManager.getByWardrobe(wardrobe_id, temperature);
		}

		[HttpGet]
		public EvaluationResponse getWarmth(int[] clothes,double temperature)
		{
			EvaluationResponse ans = SuitManager.evaluate(clothes,temperature);
			return ans;
		}

		[HttpGet]
		public SuitResponseList getAdvices(int wardrobe_id,double latitude,double longitude)
		{
			return SuitManager.advise(wardrobe_id,latitude,longitude);
		}

		[HttpPut]
		public BooleanResponse wear(int id)
		{
			return new BooleanResponse(SuitManager.wear(id));
		}

		[HttpPut]
		public BooleanResponse wear(int[] clothes_ids)
		{
			return new BooleanResponse(SuitManager.wear(clothes_ids));
		}
    }
}