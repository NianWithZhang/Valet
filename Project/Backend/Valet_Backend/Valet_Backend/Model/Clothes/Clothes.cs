using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model.Clothes
{
	/// <summary>
	/// 衣物类型枚举列表
	/// </summary>
	public enum ClothesType
	{
		Hat = 0,
		Coat = 1,
		Shirt = 2,
		Bottom = 3,
		Sock = 4,
		Shoe = 5
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

		[SugarColumn(ColumnName = "last_wearing_time")]
		public DateTime lastWearingTime { get; set; }

		[SugarColumn(ColumnName = "wearing_frequency")]
		public int wearingFrequency { get; set; }

		[SugarColumn(IsIgnore = true)]
		public string picPath => Config.PicSaveDir + "ClothesPics\\" + id.ToString() + ".jpg";

		public Clothes() { }

		public Clothes(int _wardrobeID, string _name, ClothesType _type, int _thickness)
		{
			wardrobeID = _wardrobeID;
			name = _name;
			type = _type;
			color = 0;
			thickness = _thickness;
			lastWearingTime = DateTime.MinValue;
			wearingFrequency = 0;
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

		public void wear()
		{
			lastWearingTime = DateTime.Now;
			wearingFrequency++;
		}

		public double warmthDegree()
		{
			switch (type)
			{
				case ClothesType.Hat:
					return 0.5 + thickness * 0.2;
				case ClothesType.Shirt:
					return 1.2 + thickness * 0.8;
				case ClothesType.Coat:
					return 2 + thickness * 1;
				case ClothesType.Bottom:
					return 1.5 + thickness * 0.6;
				case ClothesType.Sock:
					return 0.6 + thickness * 0.2;
				case ClothesType.Shoe:
					return 0.5 + thickness * 0.2;
			}

#if DEBUG
			throw new Exception();
#endif
			return 0;
		}
	}
}
