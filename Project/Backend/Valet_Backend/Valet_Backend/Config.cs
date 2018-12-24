using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Valet_Backend.Model.Suit;

namespace Valet_Backend
{
	/// <summary>
	/// 参数设置
	/// </summary>
	public class Config
	{
		/// <summary>
		/// 数据库连接字符串
		/// </summary>
		public const string ConnectionString = "server=kousakareina.cn;uid=root;pwd=1234;database=ValetDB;charset=utf8";

		/// <summary>
		/// 图片存储路径
		/// </summary>
		public static string PicSaveDir => Directory.GetCurrentDirectory() + "\\wwwroot\\";

		/// <summary>
		/// 衣物评价描述列表
		/// </summary>
		public static Dictionary<suitEvaluation, string> evaluationStrings = new Dictionary<suitEvaluation, string>()
		{
			{ suitEvaluation.TooThin,"只穿这点会冻感冒的"},
			{ suitEvaluation.Thin,"只穿这些会冷的"},
			{ suitEvaluation.LittleThin,"穿这些稍微有点点薄"},
			{ suitEvaluation.JustOK,"穿着些差不多"},
			{ suitEvaluation.LittleThick,"穿这些稍微有点点厚"},
			{ suitEvaluation.Thick,"穿这么多会热的"},
			{ suitEvaluation.TooThick,"穿这么多会热出毛病的"}
		};

		#region API url&key

		#region BaiduMap
		/// <summary>
		/// 通过百度地图获取逆地理编码接口URL
		/// </summary>
		public const string iGeoCodeUrl = "http://api.map.baidu.com/geocoder/v2/";

		/// <summary>
		/// 百度地图API KEY
		/// </summary>
		public const string baiduMapAk = "uNrkc1iislhkWLNcEHGS64ycQumvnvoX";
		#endregion

		#region Taobao
		/// <summary>
		/// 淘宝图片搜索API
		/// </summary>
		public const string taobaoPicApiUrl = "https://s.taobao.com/image";

		/// <summary>
		/// 淘宝搜索结果地址
		/// </summary>
		public const string taobaoSearchUrl = "https://s.taobao.com/search";

		/// <summary>
		/// 淘宝宝贝链接基地址
		/// </summary>
		public const string taobaoItemUrl = "https://item.taobao.com/item.htm/";
		#endregion

		#region Juhe
		/// <summary>
		/// 天气查询API
		/// </summary>
		public const string weatherApiUrl = "http://v.juhe.cn/weather/index";

		/// <summary>
		/// 聚合数据API KEY
		/// </summary>
		public const string juheKey = "ac3acab2283aa7080a37c17cd722b5d5";
		#endregion

		#endregion


		/// <summary>
		/// 私有构造函数 防止被实例化
		/// </summary>
		private Config() { }
	}
}
