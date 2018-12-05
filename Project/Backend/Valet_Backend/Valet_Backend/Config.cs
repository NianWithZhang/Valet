using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend
{
	public class Config
	{
		public const string ConnectionString = "server=kousakareina.cn;uid=root;pwd=1234;database=ValetDB;charset=utf8";

		#region API url&key

		#region BaiduMap
		//通过百度地图获取逆地理编码接口URL
		public const string iGeoCodeUrl = "http://api.map.baidu.com/geocoder/v2/";
		//百度地图API KEY
		public const string baiduMapAk = "uNrkc1iislhkWLNcEHGS64ycQumvnvoX";
		#endregion

		#region Taobao
		//淘宝图片搜索API
		public const string taobaoApiUrl = "https://s.taobao.com/image";
		#endregion

		#region Juhe
		//天气查询API
		public const string weatherApiUrl = "http://v.juhe.cn/weather/index";
		//聚合数据API KEY
		public const string juheKey = "ac3acab2283aa7080a37c17cd722b5d5";
		#endregion

		#endregion
	}
}
