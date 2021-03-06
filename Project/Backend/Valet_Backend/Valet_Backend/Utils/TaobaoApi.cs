﻿using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text.RegularExpressions;
using System.Threading;
using System.Threading.Tasks;
using Valet_Backend.Model.User;

namespace Valet_Backend.Model.Clothes
{
	public class TaobaoApi
	{
		/// <summary>
		/// 向淘宝识图API发送HttpPOST请求
		/// </summary>
		/// <param name="url"></param>
		/// <param name="picPath"></param>
		/// <returns></returns>
		public static string uploadTaobaoApi(string picPath)
		{
			for (int i = 20; !File.Exists(picPath) && i > 0; i--)
				Thread.Sleep(100);

			using (HttpClient client = new HttpClient())
			{
				using (var httpContent = new MultipartFormDataContent())
				{
					var fileContent = new ByteArrayContent(File.ReadAllBytes(picPath));
					fileContent.Headers.ContentType = new MediaTypeHeaderValue("image/jpeg");
					httpContent.Add(fileContent, "imgfile", "need_a_name_here");

					httpContent.Headers.Add("Cookie", Config.taobaoPostImgSearchCookie);

					//var result = client.PostAsync(Config.taobaoApiUrl, httpContent).Result;
					//string json = result.Content.ReadAsStringAsync().Result;

					JObject result = (JObject)JsonConvert.DeserializeObject(client.PostAsync(Config.taobaoPicApiUrl, httpContent).Result.Content.ReadAsStringAsync().Result);//post请求

					try
					{
						if (result["status"].ToString() != "1")
#if DEBUG
							throw new Exception();
#else
					return "";
#endif
					}
					catch (Exception e)
					{
#if DEBUG
						throw e;
#else
					return "";
#endif
					}


					return result["name"].ToString();
				}
			}
		}

		/// <summary>
		/// 通过淘宝搜图API搜索图片并返回最高匹配度商品
		/// </summary>
		/// <param name="picPath">图片路径</param>
		/// <returns></returns>
		public static TaobaoItem getTaobaoItem(string picPath)
		{
			string tfsid = uploadTaobaoApi(picPath);

			if (tfsid == "")
				return null;

			Dictionary<string, string> parameters = new Dictionary<string, string>();
			parameters.Add("app", "imgsearch");
			parameters.Add("tfsid", tfsid);

			Dictionary<string, string> headers = new Dictionary<string, string>();
			headers.Add("Cookie", Config.taobaoGetSearchAnswerCookie);

			string docStr = HttpUtils.HttpGet(Config.taobaoSearchUrl, parameters, headers: headers);

			return new TaobaoItem(Config.taobaoItemUrl + "?id=" + Regex.Match(docStr, "(?<=nid\\\":\\\").*?(?=\\\",)").Value, Regex.Match(docStr, "(?<=pic_url\\\":\\\").*?(?=\\\",)").Value);
		}
	}
}
