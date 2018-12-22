using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;

namespace Valet_Backend
{
	public class HttpRequest
	{
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
	}
}
