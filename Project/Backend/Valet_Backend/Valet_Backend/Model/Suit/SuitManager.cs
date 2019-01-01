using Microsoft.AspNetCore.Http;
using SqlSugar;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Valet_Backend.Controllers;
using Valet_Backend.Model.Clothes;
using Valet_Backend.Model.Wardrobe;

namespace Valet_Backend.Model.Suit
{
	/// <summary>
	/// 穿搭评价枚举变量 分别标识穿搭过薄至过厚
	/// </summary>
	public enum suitEvaluation
	{
		TooThin,
		Thin,
		LittleThin,
		JustOK,
		LittleThick,
		Thick,
		TooThick
	}

	/// <summary>
	/// 穿搭模块控制类 实现该模块接口
	/// </summary>
	public class SuitManager : DbContext
	{
		#region 查询
		/// <summary>
		/// 查找指定穿搭是否存在
		/// </summary>
		/// <param name="suitID">需要查找的穿搭ID</param>
		/// <returns>查询结果</returns>
		public static bool exist(int suitID)
		{
			return suitDb.GetById(suitID) != null;
		}

		/// <summary>
		/// 按照衣橱以及温度获取包含保暖评价的穿搭信息列表
		/// </summary>
		/// <param name="wardrobeID">衣橱ID</param>
		/// <param name="temperature">当前温度</param>
		/// <returns>获取到的穿搭信息列表</returns>
		public static SuitResponseList getByWardrobe(int wardrobeID, double temperature)
		{
			//保证衣橱存在
			if (!WardrobeManager.exist(wardrobeID))
				return new SuitResponseList(new WeatherInfo(), new SuitResponse[0]);

			return new SuitResponseList(new WeatherInfo(), suitDb.GetList(x => x.wardrobeID == wardrobeID).OrderByDescending(x => x.lastWearingTime).Select(x => new SuitResponse(x.id, x.name, evaluate(x.warmthDegree, temperature))).ToArray());
		}

		/// <summary>
		/// 获取指定穿搭包含的所有衣物列表
		/// </summary>
		/// <param name="suitID">穿搭ID</param>
		/// <returns>其包含的衣物信息列表</returns>
		public static ClothesResponseList getClothes(int suitID)
		{
			//确保穿搭存在
			if (suitDb.GetById(suitID) == null)
				return new ClothesResponseList();

			return new ClothesResponseList(clothes_suitDb.GetList(x => x.suitID == suitID).Select(x => new ClothesResponse(ClothesManager.get(x.clothesID))).ToArray());
		}
		#endregion

		#region 添加
		/// <summary>
		/// 添加穿搭
		/// </summary>
		/// <param name="suitPic">穿搭图片</param>
		/// <param name="name">穿搭名称</param>
		/// <param name="wardobeID">穿搭所属衣橱ID</param>
		/// <param name="clothesIDs">穿搭包含衣物ID列表</param>
		/// <returns>操作结果 是否成功添加</returns>
		public static bool add(IFormFile suitPic, string name, int wardobeID, int[] clothesIDs)
		{
			Suit suit = new Suit(name, wardobeID, ClothesManager.calculateWarmthDegree(clothesIDs).Value);

			suit.id = suitDb.InsertReturnIdentity(suit);

			List<Clothes_Suit> list = new List<Clothes_Suit>();

			foreach (int clothesID in clothesIDs)
				list.Add(new Clothes_Suit(clothesID, suit.id));

			//插入数据库并返回操作结果
			return clothes_suitDb.InsertRange(list.ToArray());
		}
		/// <summary>
		/// 存储穿搭图片
		/// </summary>
		/// <param name="suitPicFile">穿搭图片</param>
		/// <param name="suit">需要存储图片的穿搭</param>
		/// <returns>操作结果 是否成功存储</returns>
		public static bool savePic(IFormFile suitPicFile, Suit suit)
		{
			//确保穿搭存在
			if (suit == null)
				return false;

			//确保图片文件存在
			if (suitPicFile != null)
			{
				//获取设置的图片存储路径
				string fileDir = Config.PicSaveDir;

				//保证存储路径存在
				if (!Directory.Exists(fileDir))
					Directory.CreateDirectory(fileDir);

				//获取穿搭图片存储路径
				string picPath = suit.picPath;

				FileStream fs = System.IO.File.Create(picPath);

				//进行存储操作
				suitPicFile.CopyTo(fs);
				fs.Flush();
				fs.Close();

				return true;
			}
			else
				return false;
		}
		#endregion

		#region 删除
		/// <summary>
		/// 删除指定穿搭
		/// </summary>
		/// <param name="suitID">需要删除的穿搭ID</param>
		/// <returns>删除结果 是否成功删除</returns>
		public static bool delete(int suitID)
		{
			Suit suit = suitDb.GetById(suitID);

			//确保穿搭存在
			if (suit == null)
#if DEBUG
				throw new Exception();
#else
			return true;
#endif

			if (!suitDb.Delete(suit))
				return false;

			deleteSuitPic(suit);

			return clothes_suitDb.Delete(x => x.suitID == suit.id);
		}
		/// <summary>
		/// 删除指定穿搭的图片
		/// </summary>
		/// <param name="suit">需要删除图片的穿搭</param>
		/// <returns>删除结果 是否成功删除图片</returns>
		public static bool deleteSuitPic(Suit suit)
		{
			try
			{
				string filePath = suit.picPath;

				//确保图片存在
				if (File.Exists(filePath))
					File.Delete(filePath);

				return true;
			}
			catch (Exception e)
			{
				Console.WriteLine(e);
				return false;
			}
		}

		/// <summary>
		/// 按照衣物删除其相关的穿搭
		/// </summary>
		/// <param name="clothesID">被删除的衣物ID</param>
		/// <returns>删除结果 是否成功删除所有相关的穿搭</returns>
		public static bool deleteByClothes(int clothesID)
		{
			//找到所有与指定衣物相关的穿搭
			List<Clothes_Suit> clothes_suitList = clothes_suitDb.GetList(x => x.clothesID == clothesID);

			foreach (var clothes_suit in clothes_suitList)
				clothes_suitDb.Delete(x => x.suitID == clothes_suit.suitID);

			return suitDb.DeleteById(clothes_suitList.Select(x => x.suitID).ToArray());
		}
		#endregion

		#region 修改
		/// <summary>
		/// 依据有修改的衣物编号重新生成相关穿搭的适宜温度
		/// </summary>
		/// <param name="clothesID">有变更的衣物ID</param>
		public static void regenerateWarmthDegreeByClothes(int clothesID)
		{
			//找到衣物相关的所有穿搭
			foreach (int suitID in clothes_suitDb.GetList(x => x.clothesID == clothesID).Select(x => x.suitID))
			{
				Suit suit = suitDb.GetById(suitID);

				//确保穿搭存在
				if (suit != null)
				{
					suit.warmthDegree = ClothesManager.calculateWarmthDegree(getClothes(suitID).clothes.Select(x => x.id).ToArray()).Value;
					suitDb.Update(suit);
				}
#if DEBUG
				else
					throw new Exception();
#endif
			}

		}
		#endregion

		#region 穿着衣物
		/// <summary>
		/// 穿着指定穿搭
		/// </summary>
		/// <param name="suitID">穿搭ID</param>
		/// <returns>操作结果 是否成功对指定穿搭完成穿着操作</returns>
		public static bool wear(int suitID)
		{
			//找到指定穿搭
			Suit suit = suitDb.GetById(suitID);

			//确保穿搭存在
			if (suit == null)
				return false;

			//进行穿着操作
			suit.wear();

			//更新信息并返回更新结果
			return suitDb.Update(suit) && ClothesManager.wear(clothes_suitDb.GetList(x => x.suitID == suitID).Select(x => x.clothesID));
		}
		/// <summary>
		/// 穿着一套衣物混搭
		/// </summary>
		/// <param name="clothesIDs">混搭包含的衣物编号列表</param>
		/// <returns>操作结果</returns>
		public static bool wear(int[] clothesIDs)
		{
			return ClothesManager.wear(clothesIDs);
		}
		#endregion
		
		#region 穿搭建议
		/// <summary>
		/// 根据位置以及衣橱 进行穿搭建议
		/// </summary>
		/// <param name="wardrobeID">衣橱ID</param>
		/// <param name="latidude">纬度</param>
		/// <param name="longitude">经度</param>
		/// <returns>生成的穿搭建议</returns>
		public static SuitResponseList advise(int wardrobeID, double latidude, double longitude)
		{
			//获取位置对应的天气状态
			WeatherInfo weather = WeatherApi.getLocationWeather(latidude, longitude);

			//生成适宜的穿搭列表
			List<SuitResponse> suitResponses = searchSuitableSuits(wardrobeID, weather.temperature);

			//生成穿搭建议并返回
			return new SuitResponseList(weather, suitResponses.ToArray());
		}
		/// <summary>
		/// 根据温度以及衣橱获取最适宜的部分穿搭
		/// </summary>
		/// <param name="wardrobeID">衣橱编号</param>
		/// <param name="temperature">当前温度</param>
		/// <returns>最适宜的一部分穿搭的列表</returns>
		public static List<SuitResponse> searchSuitableSuits(int wardrobeID, double temperature)
		{
			//确保衣橱存在
			if (!WardrobeManager.exist(wardrobeID))
				return new List<SuitResponse>();

			return suitDb.GetList(x => x.wardrobeID == wardrobeID && (x.warmthDegree - temperature) < 10&&(temperature-x.warmthDegree)<10).OrderBy(x => Math.Abs(x.warmthDegree - temperature)).Take(5).Select(x => new SuitResponse(x.id, x.name, evaluate(x.warmthDegree, temperature))).ToList();
		}
		#endregion
		
		#region 保暖评价
		/// <summary>
		/// 对一套混搭的保暖程度进行评价
		/// </summary>
		/// <param name="clothes">混搭中的所有衣物</param>
		/// <param name="temperature">当前温度</param>
		/// <returns>评价结果</returns>
		public static EvaluationResponse evaluate(int[] clothes, double temperature)
		{
			KeyValuePair<bool, double> ans = ClothesManager.calculateWarmthDegree(clothes);

			return new EvaluationResponse(ans.Key, evaluate(ans.Value, temperature));
		}
		/// <summary>
		/// 根据混搭温度指标以及当前温度生成评价描述
		/// </summary>
		/// <param name="warmthDegree"></param>
		/// <param name="temperature"></param>
		/// <returns></returns>
		public static string evaluate(double warmthDegree, double temperature)
		{
			//超过35℃的天气效果都差不多
			temperature = Math.Min(temperature, 35);
			//低于-10℃的天气效果都差不多
			temperature = Math.Max(temperature, -10);

			double warmthMinusTemperature = warmthDegree - temperature;

			if (warmthMinusTemperature < -15)
				return Config.evaluationStrings[suitEvaluation.TooThick];
			else if (warmthMinusTemperature < -10)
				return Config.evaluationStrings[suitEvaluation.Thick];
			else if (warmthMinusTemperature < -5)
				return Config.evaluationStrings[suitEvaluation.LittleThick];
			else if (warmthMinusTemperature > 15)
				return Config.evaluationStrings[suitEvaluation.TooThin];
			else if (warmthMinusTemperature > 10)
				return Config.evaluationStrings[suitEvaluation.Thin];
			else if (warmthMinusTemperature > 5)
				return Config.evaluationStrings[suitEvaluation.LittleThin];

			return Config.evaluationStrings[suitEvaluation.JustOK];
		}
		#endregion

		//DELETE!!!!!!!!!!!!!!
		//		/// <summary>
		//		/// 对一套穿搭的保暖程度进行评价
		//		/// </summary>
		//		/// <param name="suitID"></param>
		//		/// <param name="temperature"></param>
		//		/// <returns></returns>
		//		public static EvaluationResponse evalueate(int suitID,double temperature)
		//		{
		//			Suit suit = suitDb.GetById(suitID);

		//			if (suit == null)
		//#if DEBUG
		//				throw new Exception();
		//#else
		//			return new KeyValuePair<bool,string>(false,"");
		//#endif

		//			return new EvaluationResponse(true,generateEvaluation(suit.warmthDegree-temperature));
		//		}
	}
}
