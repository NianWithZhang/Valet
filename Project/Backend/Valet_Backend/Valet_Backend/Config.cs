using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend
{
	public class Config
	{
		public const string ConnectionString = "server=kousakareina.cn;uid=root;pwd=1234;database=ValetDB;charset=utf8";

		public static string PicSaveDir { get { return Directory.GetCurrentDirectory() + "\\wwwroot\\"; } }

		//public const int thicknessMaximium = 10;
		
		#region API url&key

		#region BaiduMap
		//通过百度地图获取逆地理编码接口URL
		public const string iGeoCodeUrl = "http://api.map.baidu.com/geocoder/v2/";
		//百度地图API KEY
		public const string baiduMapAk = "uNrkc1iislhkWLNcEHGS64ycQumvnvoX";
		#endregion

		#region Taobao
		//淘宝图片搜索API
		public const string taobaoPicApiUrl = "https://s.taobao.com/image";

		//淘宝搜索结果地址
		public const string taobaoSearchUrl = "https://s.taobao.com/search";

		public const string taobaoItemUrl = "https://item.taobao.com/item.htm/";
		#endregion

		#region Juhe
		//天气查询API
		public const string weatherApiUrl = "http://v.juhe.cn/weather/index";
		//聚合数据API KEY
		public const string juheKey = "ac3acab2283aa7080a37c17cd722b5d5";
		#endregion

		#endregion


		//私有构造函数 防止被实例化
		private Config()
		{
		}
	}
}
