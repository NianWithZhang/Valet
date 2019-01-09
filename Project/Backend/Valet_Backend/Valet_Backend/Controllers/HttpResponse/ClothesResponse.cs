using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Valet_Backend.Model.Clothes;

namespace Valet_Backend.Controllers.HttpResponse
{
	/// <summary>
	/// 返回的衣物简要信息格式
	/// </summary>
	public class ClothesResponse
	{
		/// <summary>
		/// 衣物ID
		/// </summary>
		public int id;

		/// <summary>
		/// 衣物名称
		/// </summary>
		public string name;

		/// <summary>
		/// 衣物类型
		/// </summary>
		public ClothesType type;

		/// <summary>
		/// 初始化成员变量
		/// </summary>
		/// <param name="_id"></param>
		/// <param name="_name"></param>
		public ClothesResponse(int _id, string _name, ClothesType _type)
		{
			id = _id;
			name = _name;
			type = _type;
		}

		/// <summary>
		/// 按照指定衣物获取对应返回信息
		/// </summary>
		/// <param name="clothes">指定的衣物</param>
		public ClothesResponse(Clothes clothes)
		{
			id = clothes.id;
			name = clothes.name;
			type = clothes.type;
		}
	}
	/// <summary>
	/// 返回衣物列表的信息格式
	/// </summary>
	public class ClothesResponseList
	{
		/// <summary>
		/// 衣物信息列表
		/// </summary>
		public ClothesResponse[] clothes;

		/// <summary>
		/// 默认构造函数 生成空列表
		/// </summary>
		public ClothesResponseList()
		{
			clothes = new ClothesResponse[0];
		}

		/// <summary>
		/// 初始化成员变量
		/// </summary>
		/// <param name="_clothes"></param>
		public ClothesResponseList(ClothesResponse[] _clothes)
		{
			clothes = _clothes;
		}
	}

	/// <summary>
	/// 返回衣物详细信息的格式
	/// </summary>
	public class ClothesInfoResponse
	{
		/// <summary>
		/// 衣物ID
		/// </summary>
		public int id;

		/// <summary>
		/// 衣物名称
		/// </summary>
		public string name;

		/// <summary>
		/// 衣物类型
		/// </summary>
		public ClothesType type;

		/// <summary>
		/// 衣物厚度
		/// </summary>
		public int thickness;

		/// <summary>
		/// 衣物距今的未穿着天数
		/// </summary>
		public int lastWearingTime;

		/// <summary>
		/// 衣物的穿着次数
		/// </summary>
		public int wearingFrequency;

		public ClothesInfoResponse(Clothes clothes)
		{
			id = clothes.id;
			name = clothes.name;
			type = clothes.type;
			thickness = clothes.thickness;
			lastWearingTime = (int)(DateTime.Now - clothes.lastWearingTime).TotalDays;
			wearingFrequency = clothes.wearingFrequency;
		}
	}
}
