using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;

namespace Valet_Backend.Model
{
	public class NotDistributedFunctions
	{
		#region Weather
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

			JObject result = (JObject)JsonConvert.DeserializeObject(HttpGet(Config.iGeoCodeUrl, parameters));

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

			JObject result = (JObject)JsonConvert.DeserializeObject(HttpGet(Config.weatherApiUrl, parameters));

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
		#endregion

		public static string testTaobao()
		{
			return HttpPost_Taobao(Config.taobaoApiUrl, "C:\\Users\\黏黏\\Desktop\\arale.jpg");
		}

		/// <summary>
		/// 将参数附加到url上
		/// </summary>
		/// <param name="url"></param>
		/// <param name="parameters"></param>
		/// <returns></returns>
		public static string setParamToUrl(string url, Dictionary<string, string> parameters)
		{
			if (parameters != null && parameters.Any())
			{
				url += "?";
				foreach (var i in parameters)
					url += i.Key + "=" + i.Value + "&";

				url = url.Remove(url.Count() - 1);
			}

			return url;
		}

		/// <summary>
		/// 发起GET同步请求
		/// </summary>
		/// <param name="url"></param>
		/// <param name="headers"></param>
		/// <param name="contentType"></param>
		/// <returns></returns>
		public static string HttpGet(string url, Dictionary<string, string> parameters = null, string contentType = null, Dictionary<string, string> headers = null)
		{
			using (HttpClient client = new HttpClient())
			{
				url = setParamToUrl(url, parameters);

				if (contentType != null)
					client.DefaultRequestHeaders.Add("ContentType", contentType);
				if (headers != null)
				{
					foreach (var header in headers)
						client.DefaultRequestHeaders.Add(header.Key, header.Value);
				}
				HttpResponseMessage response = client.GetAsync(url).Result;
				return response.Content.ReadAsStringAsync().Result;
			}
		}

		/// <summary>
		/// 向淘宝识图API发送HttpPOST请求
		/// </summary>
		/// <param name="url"></param>
		/// <param name="filePath"></param>
		/// <returns></returns>
		public static string HttpPost_Taobao(string url, string filePath)
		{
			using (HttpClient client = new HttpClient())
			{
				using (var httpContent = new MultipartFormDataContent())
				{
					var fileContent = new ByteArrayContent(File.ReadAllBytes(filePath));
					fileContent.Headers.ContentType = new MediaTypeHeaderValue("image/jpeg");
					httpContent.Add(fileContent, "imgfile", "need_a_name_here");

					var result = client.PostAsync(url, httpContent).Result;//post请求
					return result.Content.ReadAsStringAsync().Result;//将响应结果显示在文本框内
				}
			}
		}


		//Httppost 尝试
		///// <summary>  
		///// 获取文件集合对应的ByteArrayContent集合  
		///// </summary>  
		///// <param name="files"></param>  
		///// <returns></returns>  
		//private static List<ByteArrayContent> GetFileByteArrayContent(List<string> files)
		//{
		//	List<ByteArrayContent> list = new List<ByteArrayContent>();
		//	foreach (var file in files)
		//	{
		//		var fileContent = new ByteArrayContent(File.ReadAllBytes(file));
		//		fileContent.Headers.ContentDisposition = new ContentDispositionHeaderValue("attachment")
		//		{
		//			FileName = Path.GetFileName(file)
		//		};
		//		list.Add(fileContent);
		//	}
		//	return list;
		//}
		///// <summary>  
		///// 获取键值集合对应的ByteArrayContent集合  
		///// </summary>  
		///// <param name="collection"></param>  
		///// <returns></returns>  
		//private static List<ByteArrayContent> GetFormDataByteArrayContent(NameValueCollection collection)
		//{
		//	List<ByteArrayContent> list = new List<ByteArrayContent>();
		//	foreach (var key in collection.AllKeys)
		//	{
		//		var dataContent = new ByteArrayContent(Encoding.UTF8.GetBytes(collection[key]));
		//		dataContent.Headers.ContentDisposition = new ContentDispositionHeaderValue("attachment")
		//		{
		//			Name = key
		//		};
		//		list.Add(dataContent);
		//	}
		//	return list;
		//}

		//	public static string HttpPostWithData(string url)
		//	{
		//		using (HttpClient client = new HttpClient())
		//		{
		//			//client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("text/" + cmbResponseContentType.Text.ToLower()));//设定要响应的数据格式  
		//			using (var content = new MultipartFormDataContent("----WebKitFormBoundary0KghpkBYOw88mgw6"))//表明是通过multipart/form-data的方式上传数据  
		//			{
		//				//NameValueCollection formData = new NameValueCollection();
		//				//formData.Add("file", "imgfile");
		//				//var formDatas = GetFormDataByteArrayContent(formData);//获取键值集合对应的ByteArrayContent集合  
		//				//List<string> filePaths = new List<string>();
		//				//filePaths.Add("C:\\Users\\酒酿圆子蛋花汤\\Desktop\\632ADF55EE3B005A3F88D1368AB15F47.png");
		//				//var files = GetFileByteArrayContent(filePaths);//获取文件集合对应的ByteArrayContent集合  
		//				//Action<List<ByteArrayContent>> act = (dataContents) =>
		//				//{//声明一个委托，该委托的作用就是将ByteArrayContent集合加入到MultipartFormDataContent中  
		//				//	foreach (var byteArrayContent in dataContents)
		//				//	{
		//				//		content.Add(byteArrayContent);
		//				//	}
		//				//};
		//				//act(formDatas);//执行act  
		//				//act(files);//执行act  
		//				var fileContent = new ByteArrayContent(File.ReadAllBytes("C:\\Users\\黏黏\\Desktop\\arale.jpg"));
		//				fileContent.Headers.ContentType = new MediaTypeHeaderValue("image/jpeg");
		//				content.Add(fileContent, "imgfile", "need_a_name_here");

		//				var result = client.PostAsync(url, content).Result;//post请求  
		//				return result.Content.ReadAsStringAsync().Result;//将响应结果显示在文本框内  
		//			}
		//		}
		//	}
	}
}
