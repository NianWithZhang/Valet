using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model.Wardrobe
{
	/// <summary>
	/// 衣橱
	/// </summary>
	[SugarTable("WardrobeTable")]
	public class Wardrobe
	{
		/// <summary>
		/// 衣橱ID
		/// </summary>
		public int id { get; set; }

		/// <summary>
		/// 衣橱所属于的用户ID
		/// </summary>
		[SugarColumn(ColumnName = "user_id")]
		public string userID { get; set; }

		/// <summary>
		/// 衣橱名称
		/// </summary>
		public string name { get; set; }

		/// <summary>
		/// 衣橱的最后使用时间
		/// </summary>
		[SugarColumn(ColumnName = "last_used_time")]
		public DateTime lastUsedTime { get; set; }

		/// <summary>
		/// 默认构造函数
		/// </summary>
		public Wardrobe(){}

		/// <summary>
		/// 初始化成员变量
		/// </summary>
		/// <param name="_user_id"></param>
		/// <param name="_name"></param>
		public Wardrobe(string _user_id, string _name)
		{
			userID = _user_id;
			name = _name;
			lastUsedTime = DateTime.MinValue;
		}

		/// <summary>
		/// 穿着衣橱中的衣物 更新衣橱的最后使用时间信息
		/// </summary>
		public void wear()
		{
			lastUsedTime = DateTime.Now;
		}
	}
}
