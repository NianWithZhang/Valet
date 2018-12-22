using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model.Suit
{
	public class WeatherApi
	{
		/// <summary>
		/// 获取对应坐标位置的城市名称 用于天气查询
		/// </summary>
		/// <param name="latitude"></param>
		/// <param name="longitude"></param>
		/// <returns></returns>
		public static string getLocationCityName(double latitude, double longitude)
		{
			Dictionary<string, string> parameters = new Dictionary<string, string>();

			parameters.Add("location", latitude.ToString() + "," + longitude.ToString());
			parameters.Add("output", "json");
			parameters.Add("ak", Config.baiduMapAk);

			JObject result = (JObject)JsonConvert.DeserializeObject(HttpRequest.HttpGet(Config.iGeoCodeUrl, parameters));

			//如果查找失败则默认为北京
			int tempInt = 0;
			if (!int.TryParse(result["status"].ToString(), out tempInt) || tempInt != 0)
#if DEBUG
				throw new Exception();
#else
			return "北京市";
#endif

			string ans = result["result"]["addressComponent"]["city"].ToString();
			//string ans = ((JObject)JsonConvert.DeserializeObject(((JObject)JsonConvert.DeserializeObject(result["result"].ToString()))["addressComponent"].ToString()))["city"].ToString();

			if (ans.Count() == 0)
#if DEBUG
				throw new Exception();
#else
			ans = "北京市"
#endif

			Console.WriteLine("查询城市结果 - " + ans);

			return ans;
		}
		/// <summary>
		/// 获取城市的天气信息
		/// </summary>
		/// <param name="cityName"></param>
		/// <returns></returns>
		public static WeatherInfo getCityWeather(string cityName)
		{
			Dictionary<string, string> parameters = new Dictionary<string, string>();

			parameters.Add("cityname", cityName);
			parameters.Add("key", Config.juheKey);

			JObject result = (JObject)JsonConvert.DeserializeObject(HttpRequest.HttpGet(Config.weatherApiUrl, parameters));

			if (result["resultcode"].ToString() != "200")
#if DEBUG
				throw new Exception();
#else
			return new WeatherInfo();
#endif
			Console.WriteLine("查询天气结果 - " + result["result"]["sk"]["temp"].ToString());

			return new WeatherInfo
			{
				temperature = double.Parse(result["result"]["sk"]["temp"].ToString()),
				tempStr = result["result"]["today"]["temperature"].ToString(),
				wind = result["result"]["today"]["wind"].ToString(),
				weather = result["result"]["today"]["weather"].ToString(),
				dressingAdvice = result["result"]["today"]["dressing_advice"].ToString()
			};
		}
		/// <summary>
		/// 获取坐标位置的天气信息
		/// </summary>
		/// <param name="latitude"></param>
		/// <param name="longitude"></param>
		/// <returns></returns>
		public static WeatherInfo getLocationWeather(double latitude, double longitude)
		{
			return getCityWeather(getLocationCityName(latitude, longitude));
		}
	}
}
