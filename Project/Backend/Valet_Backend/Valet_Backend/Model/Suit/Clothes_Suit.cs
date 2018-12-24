using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model.Suit
{
	/// <summary>
	/// 衣物与穿搭套装对应关系
	/// </summary>
	[SugarTable("ClothesTable_SuitTable")]
	public class Clothes_Suit
	{
		/// <summary>
		/// 衣物ID
		/// </summary>
		[SugarColumn(ColumnName = "clothes_id")]
		public int clothesID { get; set; }

		/// <summary>
		/// 穿搭ID
		/// </summary>
		[SugarColumn(ColumnName = "suit_id")]
		public int suitID { get; set; }

		/// <summary>
		/// 默认构造函数
		/// </summary>
		public Clothes_Suit() { }

		/// <summary>
		/// 初始化成员变量
		/// </summary>
		/// <param name="_clothes_id">衣物ID</param>
		/// <param name="_suit_id">穿搭ID</param>
		public Clothes_Suit(int _clothes_id, int _suit_id)
		{
			clothesID = _clothes_id;
			suitID = _suit_id;
		}
	}
}
