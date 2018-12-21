using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model
{
	[SugarTable("SuitTable")]
	public class Suit
	{
		public int id { get; set; }

		[SugarColumn(ColumnName = "wardrobe_id")]
		public int wardrobeID { get; set; }

		public string name { get; set; }

		[SugarColumn(ColumnName = "warmth_degree")]
		public double warmthDegree { get; set; }

		[SugarColumn(ColumnName = "wearing_frequency")]
		public int wearingFrequency { get; set; }

		public Suit()
		{
		}

		public Suit(int _id,string _name,double _warmthDegree)
		{
			id = _id;
			name = _name;
			warmthDegree = _warmthDegree;

			wearingFrequency = 0;
		}
	}
}
