using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model.Suit
{
	/// <summary>
	/// 天气信息类
	/// </summary>
	public class WeatherInfo
	{
		//温度
		public double temperature = 16;

		//温度（包含'℃'的字符串）
		public string tempStr = "";

		//风力风向信息
		public string wind = "";

		//天气描述
		public string weather = "";

		//穿衣建议描述
		public string dressingAdvice = "";
	}
}
