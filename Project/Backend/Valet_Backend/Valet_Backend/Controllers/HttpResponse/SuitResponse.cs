using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Valet_Backend.Model.Suit;

namespace Valet_Backend.Controllers.HttpResponse
{
	/// <summary>
	///  获取穿搭时返回的单个穿搭信息
	/// </summary>
	public class SuitResponse
	{
		public int id;
		public string name;
		public string evaluation;

		public SuitResponse(int _id, string _name, string _evaluation = "")
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
		public WeatherInfo weather;
		public SuitResponse[] suits;

		public SuitResponseList(WeatherInfo _weather, SuitResponse[] _suits)
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

		public EvaluationResponse(bool _ans, string _description)
		{
			ans = _ans;
			description = _description;
		}
	}
}
