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
		public const string ConnectionString = "server=localhost;uid=root;pwd=1234;database=ValetDB;charset=utf8";

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

		/// <summary>
		/// 前端获取地理位置失败时的默认位置城市
		/// </summary>
		public const string defaultWeatherCity = "上海市";
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

		/// <summary>
		/// 淘宝获取图片搜索结果时的Cookie
		/// </summary>
		public const string taobaoGetSearchAnswerCookie = "miid=78576571323667569; t=6f340eb674a23e1f1fd723e18124551d; cna=TNYzFCaEjDsCAbSgJpWeCsNW; thw=cn; hng=CN%7Czh-CN%7CCNY%7C156; tg=0; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; UM_distinctid=16623b5c6e81066-05a9eefefbc599-8383268-240000-16623b5c6e9696; enc=7N7hDtElajEtpbapUP%2B08KbRZo6tyLnHIMjjg5nvKb5Gg0vmmknJjbsmaKq8qCnrYH%2BcZP9MkMvJ1bCO%2B8kDqQ%3D%3D; _uab_collina=154324623556526562670981; cookie2=1cb0223843ed09c997058921d406c8a4; _tb_token_=6687e3b5e3db; alitrackid=www.taobao.com; v=0; unb=2550369571; sg=%E8%8B%9219; _l_g_=Ug%3D%3D; skt=1f7d9b0cac2e8d2e; cookie1=AiGZaF8DhgXX0dwGl24mfXIZiY99kfd3LcCp%2F4mgCY4%3D; csg=9551cb7d; uc3=vt3=F8dByRMKNpo2x82rb7Y%3D&id2=UU23A4BgYNpufQ%3D%3D&nk2=hDYdPGVqN0I%3D&lg2=UtASsssmOIJ0bQ%3D%3D; existShop=MTU0NjMyNTk3Nw%3D%3D; tracknick=%5Cu7F31%5Cu7EFB%5Cu834F%5Cu82D2; lgc=%5Cu7F31%5Cu7EFB%5Cu834F%5Cu82D2; _cc_=Vq8l%2BKCLiw%3D%3D; dnk=%5Cu7F31%5Cu7EFB%5Cu834F%5Cu82D2; _nk_=%5Cu7F31%5Cu7EFB%5Cu834F%5Cu82D2; cookie17=UU23A4BgYNpufQ%3D%3D; mt=ci=59_1; lastalitrackid=www.taobao.com; uc1=cookie16=URm48syIJ1yk0MX2J7mAAEhTuw%3D%3D&cookie21=WqG3DMC9Fb5mPLIQo9kR&cookie15=WqG3DMC9VAQiUQ%3D%3D&existShop=false&pas=0&cookie14=UoTYMDi%2FE0S0Hw%3D%3D&tag=10&lng=zh_CN; isg=BLq60YOksh71lDl37AxlkWiXC-Acwz9BuApXfsSzZs0Yt1rxrPuOVYDFAwPOPLbd; l=aBZrAuWGys1uomBm9Ma5Vlq4MxrxygBPz6Bq1MazBTqGdP8v7RXy1jno-Vw69_qC559y_K-iI";

		/// <summary>
		/// 淘宝图片搜索上传图片时的Cookie
		/// </summary>
		public const string taobaoPostImgSearchCookie = "miid=78576571323667569; t=6f340eb674a23e1f1fd723e18124551d; cna=TNYzFCaEjDsCAbSgJpWeCsNW; thw=cn; hng=CN%7Czh-CN%7CCNY%7C156; tg=0; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; UM_distinctid=16623b5c6e81066-05a9eefefbc599-8383268-240000-16623b5c6e9696; enc=7N7hDtElajEtpbapUP%2B08KbRZo6tyLnHIMjjg5nvKb5Gg0vmmknJjbsmaKq8qCnrYH%2BcZP9MkMvJ1bCO%2B8kDqQ%3D%3D; _uab_collina=154324623556526562670981; cookie2=1cb0223843ed09c997058921d406c8a4; _tb_token_=6687e3b5e3db; alitrackid=www.taobao.com; lastalitrackid=www.taobao.com; x5sec=7b227061696c6974616f3b32223a22333435323133623631656235303433343036333532396461666535346436353443505737724f4546454c5077774d75687862665550686f4d4d6a6b784e44557a4e54637a4e7a737a227d; v=0; unb=2914535737; uc1=cookie16=WqG3DMC9UpAPBHGz5QBErFxlCA%3D%3D&cookie21=W5iHLLyFe3xm&cookie15=UtASsssmOIJ0bQ%3D%3D&existShop=false&pas=0&cookie14=UoTYMDi%2FHU%2FsJA%3D%3D&tag=8&lng=zh_CN; sg=%E5%BF%8371; _l_g_=Ug%3D%3D; skt=1a86f732e4a1c358; cookie1=BxpUZnwgGeuhn9OV3U22zquF8QFEauwX%2FM%2FSBhGrxF4%3D; csg=f8d9abb1; uc3=vt3=F8dByRMKNYhRgqzACgU%3D&id2=UUGjNjFpXcdZuQ%3D%3D&nk2=svVEfhbXTYM%3D&lg2=U%2BGCWk%2F75gdr5Q%3D%3D; existShop=MTU0NjMyOTYxNA%3D%3D; tracknick=%5Cu6F3E%5Cu84DD%5Cu4E4B%5Cu5FC3; lgc=%5Cu6F3E%5Cu84DD%5Cu4E4B%5Cu5FC3; _cc_=U%2BGCWk%2F7og%3D%3D; dnk=%5Cu6F3E%5Cu84DD%5Cu4E4B%5Cu5FC3; _nk_=%5Cu6F3E%5Cu84DD%5Cu4E4B%5Cu5FC3; cookie17=UUGjNjFpXcdZuQ%3D%3D; mt=ci=0_0&np=; isg=BFlZdBC-YeO5hDry85G2HE-CaEXzTkycPz-Uu3sO1QD_gnkUwzZdaMeQgAZRIeXQ; l=aBZrAuWGys1uoVbXoManDlu_uxrxygBPzkUy1MazBTqGdP8v7RXy1jno-Vw69_qC559y_K-iI";
		
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
