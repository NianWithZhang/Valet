using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Controllers.HttpResponse
{
	/// <summary>
	/// 衣橱信息格式
	/// </summary>
	public class WardrobeResponse
	{
		//衣橱ID
		public int id;

		//衣橱名称
		public string name;

		/// <summary>
		/// 初始化成员变量
		/// </summary>
		/// <param name="_id"></param>
		/// <param name="_name"></param>
		public WardrobeResponse(int _id, string _name)
		{
			id = _id;
			name = _name;
		}
	}
	/// <summary>
	/// 衣橱信息列表格式
	/// </summary>
	public class WardrobeResponseList
	{
		/// <summary>
		/// 衣橱信息列表
		/// </summary>
		public WardrobeResponse[] wardrobes;

		/// <summary>
		/// 初始化成员变量
		/// </summary>
		/// <param name="_wardrobes"></param>
		public WardrobeResponseList(WardrobeResponse[] _wardrobes)
		{
			wardrobes = _wardrobes;
		}
	}

}
