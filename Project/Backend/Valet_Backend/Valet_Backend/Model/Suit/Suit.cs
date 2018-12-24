using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model.Suit
{
	/// <summary>
	/// 一套穿衣搭配
	/// </summary>
	[SugarTable("SuitTable")]
	public class Suit
	{
		/// <summary>
		/// 穿搭ID
		/// </summary>
		public int id { get; set; }

		/// <summary>
		/// 所属衣橱ID
		/// </summary>
		[SugarColumn(ColumnName = "wardrobe_id")]
		public int wardrobeID { get; set; }

		/// <summary>
		/// 穿搭名称
		/// </summary>
		public string name { get; set; }

		/// <summary>
		/// 穿搭适宜温度
		/// </summary>
		[SugarColumn(ColumnName = "warmth_degree")]
		public double warmthDegree { get; set; }

		/// <summary>
		/// 穿着次数
		/// </summary>
		[SugarColumn(ColumnName = "wearing_frequency")]
		public int wearingFrequency { get; set; }

		/// <summary>
		/// 最后穿着时间
		/// </summary>
		[SugarColumn(ColumnName = "last_wearing_time")]
		public DateTime lastWearingTime { get; set; }

		/// <summary>
		/// 穿搭图片保存路径
		/// </summary>
		[SugarColumn(IsIgnore = true)]
		public string picPath => Config.PicSaveDir + "SuitPics\\" + id.ToString() + ".jpg";

		/// <summary>
		/// 默认构造函数
		/// </summary>
		public Suit() { }

		/// <summary>
		/// 初始化成员变量
		/// </summary>
		/// <param name="_name"></param>
		/// <param name="_wardobeID"></param>
		/// <param name="_warmthDegree"></param>
		public Suit(string _name,int _wardobeID,double _warmthDegree)
		{
			name = _name;
			wardrobeID = _wardobeID;

			warmthDegree = _warmthDegree;
			wearingFrequency = 0;
			lastWearingTime = DateTime.MinValue;
		}

		/// <summary>
		/// 穿着穿搭 将更新穿搭的最后穿着时间以及穿着次数
		/// </summary>
		public void wear()
		{
			lastWearingTime = DateTime.Now;
			wearingFrequency++;
		}
	}
}
