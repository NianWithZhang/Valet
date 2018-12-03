using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace Valet_Backend.Model
{
	public class NotDistributedFunctions
	{
		public static string getCityName(double latitude, double longitude)
		{
			Dictionary<string, string> parameters = new Dictionary<string, string>();

			parameters.Add("location", latitude.ToString() + "," + longitude.ToString());
			parameters.Add("output", "json");
			parameters.Add("ak", Config.BaiduMapAk);

			Console.WriteLine(HttpGet(Config.iGeoCodeUrl,parameters));

			return "";
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
				url = setParamToUrl(url,parameters);

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

		public static string HttpPost(string url, string postData = null, string contentType = null, int timeOut = 30, Dictionary<string, string> headers = null)
		{
			postData = postData ?? "";

			using (HttpClient client = new HttpClient())
			{
				if (headers != null)
				{
					foreach (var header in headers)
						client.DefaultRequestHeaders.Add(header.Key, header.Value);
				}
				using (HttpContent httpContent = new StringContent(postData, Encoding.UTF8))
				{
					if (contentType != null)
						httpContent.Headers.ContentType = new System.Net.Http.Headers.MediaTypeHeaderValue(contentType);

					HttpResponseMessage response = client.PostAsync(url, httpContent).Result;
					return response.Content.ReadAsStringAsync().Result;
				}
			}
		}
	}
}
