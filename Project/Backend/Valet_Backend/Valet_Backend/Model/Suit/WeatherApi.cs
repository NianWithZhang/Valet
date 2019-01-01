using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model.Suit
{
	/// <summary>
	/// 天气相关接口
	/// </summary>
	public class WeatherApi
	{
		/// <summary>
		/// 获取对应坐标位置的城市名称 用于天气查询
		/// </summary>
		/// <param name="latitude">纬度</param>
		/// <param name="longitude">经度</param>
		/// <returns>获取到的城市名称</returns>
		public static string getLocationCityName(double latitude, double longitude)
		{
			//设置Http查询参数
			Dictionary<string, string> parameters = new Dictionary<string, string>();

			parameters.Add("location", latitude.ToString() + "," + longitude.ToString());
			parameters.Add("output", "json");
			parameters.Add("ak", Config.baiduMapAk);

			//执行Http请求并获取结果
			JObject result = (JObject)JsonConvert.DeserializeObject(HttpRequest.HttpGet(Config.iGeoCodeUrl, parameters));

			//如果查找失败则默认为上海
			int tempInt = 0;
			if (!int.TryParse(result["status"].ToString(), out tempInt) || tempInt != 0)
#if DEBUG
				throw new Exception();
#else
			return "上海市";
#endif

			string ans = result["result"]["addressComponent"]["city"].ToString();
			//string ans = ((JObject)JsonConvert.DeserializeObject(((JObject)JsonConvert.DeserializeObject(result["result"].ToString()))["addressComponent"].ToString()))["city"].ToString();

			if (ans.Count() == 0)
#if DEBUG
				throw new Exception();
#else
			ans = "上海市"
#endif

			Console.WriteLine("查询城市结果 - " + ans);

			return ans;
		}
		/// <summary>
		/// 获取指定城市的天气信息
		/// </summary>
		/// <param name="cityName">城市名称</param>
		/// <returns>指定城市的天气信息</returns>
		public static WeatherInfo getCityWeather(string cityName)
		{
			//设定Http查询参数
			Dictionary<string, string> parameters = new Dictionary<string, string>();

			parameters.Add("cityname", cityName);
			parameters.Add("key", Config.juheKey);

			//进行Http查询并获取结果
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
		/// <param name="latitude">纬度</param>
		/// <param name="longitude">经度</param>
		/// <returns>获取到的天气信息</returns>
		public static WeatherInfo getLocationWeather(double latitude, double longitude)
		{
			if (latitude < 0 || longitude < 0)
				return getCityWeather(Config.defaultWeatherCity);

			return getCityWeather(getLocationCityName(latitude, longitude));
		}
	}
}
