using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Valet_Backend.Model.Clothes;

namespace Valet_Backend.Model.Suit
{
	public class SuitManager : DbContext
	{
		public static bool add(IFormFile suitPic, string name, int wardobeID, int[] clothesIDs)
		{
			Suit suit = new Suit(name, wardobeID,ClothesManager.calculateWarmthDegree(clothesIDs));

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

		public static List<KeyValuePair<int, string>> getByWardrobe(int wardrobeID)
		{
			return suitDb.GetList(x => x.wardrobeID == wardrobeID).OrderByDescending(x => x.lastWearingTime).Select(x => new KeyValuePair<int, string>(x.id, x.name)).ToList();
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

		public static List<KeyValuePair<int, string>> getClothes(int suitID)
		{
			List<KeyValuePair<int, string>> ans = new List<KeyValuePair<int, string>>();

			if (suitDb.GetById(suitID) == null)
				return ans;

			return clothes_suitDb.GetList(x => x.suitID == suitID).Select(x => new KeyValuePair<int, string>(x.clothesID, ClothesManager.getInfo(x.clothesID).name)).ToList();
		}
		
	}
}
