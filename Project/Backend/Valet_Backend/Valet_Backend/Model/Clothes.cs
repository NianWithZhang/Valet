using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model
{
	public enum ClothesType
	{
		Hat,
		Coat,
		Shirt,
		Bottom,
		Sock,
		Shoe,
		UnderWear
	}

	[SugarTable("ClothesTable")]
	public class Clothes
	{
		public int id { get; set; }

		[SugarColumn(ColumnName = "wardrobe_id")]
		public int wardrobeID { get; set; }

		public string name { get; set; }

		public double color { get; set; }

		public ClothesType type { get; set; }

		public int thickness { get; set; }

		public Clothes() { }

		public Clothes(int _wardrobeID,string _name,ClothesType _type,int _thickness)
		{
			wardrobeID = _wardrobeID;
			name = _name;
			type = _type;
			color = 0;
			thickness = _thickness;
		}

		public void modify(string _name, ClothesType _type, int _thickness)
		{
			name = _name;
			type = _type;
			thickness = _thickness;
		}

		public void changeWardrobe(int targetWardobeID)
		{
			wardrobeID = targetWardobeID;
		}
	}
}
