using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model.Suit
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

		[SugarColumn(ColumnName = "last_wearing_time")]
		public DateTime lastWearingTime { get; set; }

		[SugarColumn(IsIgnore = true)]
		public string picPath => Config.PicSaveDir + "SuitPics\\" + id.ToString() + ".jpg";

		public Suit() { }

		public Suit(string _name,int _wardobeID,double _warmthDegree)
		{
			name = _name;
			wardrobeID = _wardobeID;

			warmthDegree = _warmthDegree;
			wearingFrequency = 0;
			lastWearingTime = DateTime.MinValue;
		}

		public void wear()
		{
			lastWearingTime = DateTime.Now;
		}
	}
}
