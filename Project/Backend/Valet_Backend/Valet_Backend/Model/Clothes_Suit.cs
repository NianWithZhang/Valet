using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model
{
	[SugarTable("ClothesTable_SuitTable")]
	public class Clothes_Suit
	{
		[SugarColumn(ColumnName = "clothes_id")]
		public int clothesID { get; set; }

		[SugarColumn(ColumnName = "suit_id")]
		public int suitID { get; set; }

		public Clothes_Suit()
		{
		}

		public Clothes_Suit(int _clothes_id,int _suit_id)
		{
			clothesID = _clothes_id;
			suitID = _suit_id;
		}
	}
}
