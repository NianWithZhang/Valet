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
		public const string BaiduMapAk = "uNrkc1iislhkWLNcEHGS64ycQumvnvoX";
		#endregion

		#region Taobao
		public const string taobaoApiUrl = "https://s.taobao.com/image";
		#endregion

		#endregion
	}
}
