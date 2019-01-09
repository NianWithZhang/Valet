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

	/// <summary>
	/// 衣物属性类型枚举列表
	/// </summary>
	public enum ClothesAttrType
	{
		Name,
		Color,
		Type,
		Thickness
	}

	[SugarTable("ClothesTable")]
	public class Clothes
	{
		/// <summary>
		/// 衣物ID
		/// </summary>
		public int id { get; set; }

		/// <summary>
		/// 所属的衣橱ID
		/// </summary>
		[SugarColumn(ColumnName = "wardrobe_id")]
		public int wardrobeID { get; set; }

		/// <summary>
		/// 衣物名称
		/// </summary>
		public string name { get; set; }

		/// <summary>
		/// 衣物颜色Hue值
		/// </summary>
		public double color { get; set; }

		/// <summary>
		/// 衣物类型
		/// </summary>
		public ClothesType type { get; set; }

		/// <summary>
		/// 衣物厚度
		/// </summary>
		public int thickness { get; set; }

		/// <summary>
		/// 衣物的最后穿着时间
		/// </summary>
		[SugarColumn(ColumnName = "last_wearing_time")]
		public DateTime lastWearingTime { get; set; }

		/// <summary>
		/// 衣物的穿着次数
		/// </summary>
		[SugarColumn(ColumnName = "wearing_frequency")]
		public int wearingFrequency { get; set; }

		/// <summary>
		/// 衣物图片保存路径
		/// </summary>
		[SugarColumn(IsIgnore = true)]
		public string picPath => Config.ClothesPicSaveDir + id.ToString() + ".jpg";

		/// <summary>
		/// 衣物图片缓存保存路径
		/// </summary>
		[SugarColumn(IsIgnore = true)]
		public string tempPicPath => Config.TempDir + "clothes_" + id.ToString() + ".jpg";

		public Clothes() { }

		/// <summary>
		/// 初始化成员变量
		/// </summary>
		/// <param name="_wardrobeID"></param>
		/// <param name="_name"></param>
		/// <param name="_type"></param>
		/// <param name="_thickness"></param>
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

		/// <summary>
		/// 修改衣物信息
		/// </summary>
		/// <param name="_name">修改后名称</param>
		/// <param name="_type">修改后类型</param>
		/// <param name="_thickness">修改后厚度</param>
		public void modify(string _name, ClothesType _type, int _thickness)
		{
			name = _name;
			type = _type;
			thickness = _thickness;
		}

		/// <summary>
		/// 更换衣橱
		/// </summary>
		/// <param name="targetWardobeID"></param>
		public void changeWardrobe(int targetWardobeID)
		{
			wardrobeID = targetWardobeID;
		}

		/// <summary>
		/// 穿着 更新穿着次数和最后穿着时间
		/// </summary>
		public void wear()
		{
			lastWearingTime = DateTime.Now;
			wearingFrequency++;
		}

		/// <summary>
		/// 获取当前衣物的保暖程度指数
		/// </summary>
		/// <returns></returns>
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
