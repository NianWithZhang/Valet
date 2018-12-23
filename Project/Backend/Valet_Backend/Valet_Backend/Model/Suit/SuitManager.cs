using Microsoft.AspNetCore.Http;
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

	public class SuitManager : DbContext
	{
		public static bool add(IFormFile suitPic, string name, int wardobeID, int[] clothesIDs)
		{
			Suit suit = new Suit(name, wardobeID, ClothesManager.calculateWarmthDegree(clothesIDs).Value);

			suit.id = suitDb.InsertReturnIdentity(suit);

			List<Clothes_Suit> list = new List<Clothes_Suit>();

			foreach (int clothesID in clothesIDs)
				list.Add(new Clothes_Suit(clothesID, suit.id));

			clothes_suitDb.InsertRange(list.ToArray());

			return true;
		}
		public static bool savePic(IFormFile suitPicFile, Suit suit)
		{
			if (suit == null)
				return false;

			if (suitPicFile != null)
			{
				string fileDir = Config.PicSaveDir;

				if (!Directory.Exists(fileDir))
				{
					Directory.CreateDirectory(fileDir);
				}

				string picPath = suit.picPath;

				FileStream fs = System.IO.File.Create(picPath);

				suitPicFile.CopyTo(fs);
				fs.Flush();
				fs.Close();

				return true;
			}
			else
				return false;
		}

		public static void regenerateWarmthDegreeByClothes(int clothesID)
		{
			foreach (int suitID in clothes_suitDb.GetList(x => x.clothesID == clothesID).Select(x => x.suitID))
			{
				Suit suit = suitDb.GetById(suitID);

				if (suit != null)
				{
					suit.warmthDegree = ClothesManager.calculateWarmthDegree(getClothes(suitID).clothes.Select(x => x.id).ToArray()).Value;
					suitDb.Update(suit);
				}
			}

		}

		public static bool exist(int suitID)
		{
			return suitDb.GetById(suitID) != null;
		}

		public static bool delete(int suitID)
		{
			Suit suit = suitDb.GetById(suitID);

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
		public static bool deleteSuitPic(Suit suit)
		{
			try
			{
				string filePath = suit.picPath;

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

		//？？重复了
		//public static List<KeyValuePair<int, string>> getByWardrobe(int wardrobeID)
		//{
		//	return suitDb.GetList(x => x.wardrobeID == wardrobeID).OrderByDescending(x => x.lastWearingTime).Select(x => new KeyValuePair<int, string>(x.id, x.name)).ToList();
		//}

		public static SuitResponseList getByWardrobe(int wardrobeID, double temperature)
		{
			if (!WardrobeManager.exist(wardrobeID))
				return new SuitResponseList(new WeatherInfo(), new SuitResponse[0]);

			return new SuitResponseList(new WeatherInfo(), suitDb.GetList(x => x.wardrobeID == wardrobeID).OrderByDescending(x => x.lastWearingTime).Select(x => new SuitResponse(x.id, x.name, evaluate(x.warmthDegree, temperature))).ToArray());
		}

		public static bool deleteByClothes(int clothesID)
		{
			List<Clothes_Suit> clothes_suitList = clothes_suitDb.GetList(x => x.clothesID == clothesID);

			foreach (var clothes_suit in clothes_suitList)
				clothes_suitDb.Delete(x => x.suitID == clothes_suit.suitID);

			return suitDb.DeleteById(clothes_suitList.Select(x => x.suitID).ToArray());
		}

		public static bool wear(int suitID)
		{
			Suit suit = suitDb.GetById(suitID);

			if (suit == null)
				return false;

			suit.wear();

			return suitDb.Update(suit) && ClothesManager.wear(clothes_suitDb.GetList(x => x.suitID == suitID).Select(x => x.clothesID));
		}
		public static bool wear(int[] clothesIDs)
		{
			return ClothesManager.wear(clothesIDs);
		}

		public static ClothesResponseList getClothes(int suitID)
		{
			if (suitDb.GetById(suitID) == null)
				return new ClothesResponseList();

			return new ClothesResponseList(clothes_suitDb.GetList(x => x.suitID == suitID).Select(x => new ClothesResponse(x.clothesID, ClothesManager.getInfo(x.clothesID).name)).ToArray());
		}

		public static SuitResponseList advise(int wardrobeID, double latidude, double longitude)
		{
			WeatherInfo weather = WeatherApi.getLocationWeather(latidude, longitude);

			List<SuitResponse> suitResponses = searchSuitableSuits(wardrobeID, weather.temperature);

			return new SuitResponseList(weather, suitResponses.ToArray());
		}

		public static List<SuitResponse> searchSuitableSuits(int wardrobeID, double temperature)
		{
			if (!WardrobeManager.exist(wardrobeID))
				return new List<SuitResponse>();

			return suitDb.GetList(x => x.wardrobeID == wardrobeID && Math.Abs(x.warmthDegree - temperature) < 10).OrderBy(x => Math.Abs(x.warmthDegree - temperature)).Take(3).Select(x => new SuitResponse(x.id, x.name, evaluate(x.warmthDegree, temperature))).ToList();
		}

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
	}
}
