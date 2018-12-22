using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Valet_Backend.Model.Suit;
using Valet_Backend.Model.User;
using Valet_Backend.Model.Wardrobe;

namespace Valet_Backend.Model.Clothes
{
	public class ClothesManager : DbContext
	{
		public static Clothes getInfo(int clothesID)
		{
			Clothes clothes = clothesDb.GetById(clothesID);

			//确保衣物存在
			if (clothes == null)
				return null;

			return clothes;
		}

		public static bool add(IFormFile clothesPic, int wardrobeID, string name, ClothesType type,int thickness)
		{
			//确保衣橱存在
			if (WardrobeManager.exists(wardrobeID))
				return false;

			Clothes clothes = new Clothes(wardrobeID, name, type, thickness);

			clothes.id = clothesDb.InsertReturnIdentity(clothes);

			savePic(clothesPic, clothes);
			
			//处理购衣推荐
			foreach(string userID in UserManager.similarUsers(WardrobeManager.user(wardrobeID)))
			{
				TaobaoItem taobaoItem = TaobaoApi.getTaobaoItem(clothes.picPath);

				UserManager.setRecommend(userID,taobaoItem);
			}

			return true;
		}
		public static bool savePic(IFormFile clothesPicFile, object clothes)
		{
			if (clothes is string)
				clothes = clothesDb.GetById(clothes as string);

			if (clothes == null)
				return false;

			if (clothesPicFile != null)
			{
				string fileDir = Config.PicSaveDir;

				if (!Directory.Exists(fileDir))
				{
					Directory.CreateDirectory(fileDir);
				}

				string picPath = (clothes as Clothes).picPath;

				FileStream fs = System.IO.File.Create(picPath);

				clothesPicFile.CopyTo(fs);
				fs.Flush();
				fs.Close();

				//根据图片获取主题色
				double color = getPicMainColor(picPath);
				(clothes as Clothes).color = color;
				clothesDb.Update(clothes as Clothes);

				return true;
			}
			else
				return false;
		}
		public static double getPicMainColor(string filePath)
		{
			//Image image = new Image<Rgba32>(fs);

			Image img = Image.FromFile(filePath);

			Bitmap bitmap = new Bitmap(img);

			//获取中心点像素值
			return bitmap.GetPixel(bitmap.Width / 2, bitmap.Height / 2).GetHue();
		}

		public static bool modify(IFormFile clothesPic, int clothesID, string clothesName, ClothesType type,int thickness)
		{
			Clothes clothes = clothesDb.GetById(clothesID);

			if (clothes == null)
				return false;

			savePic(clothesPic, clothesID);

			clothes.modify(clothesName,type,thickness);

			return true;
		}

		public static bool delete(int clothesID)
		{
			Clothes clothes = clothesDb.GetById(clothesID);

			if (clothes == null)
				return false;

			deleteClothesPic(clothes);

			SuitManager.deleteByClothes(clothesID);

			clothesDb.Delete(clothes);

			return true;
		}

		public static bool deleteClothesPic(Clothes clothes)
		{
			try
			{
				string filePath = clothes.picPath;

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

		public static bool changeWardrobe(int clothesID, int targetWardrobeID)
		{
			Clothes clothes = clothesDb.GetById(clothesID);

			//确保衣橱和衣物存在
			if (clothes == null || !WardrobeManager.exists(targetWardrobeID))
				return false;

			//更换衣橱
			clothes.wardrobeID = targetWardrobeID;

			SuitManager.deleteByClothes(clothesID);

			return clothesDb.Update(clothes);
		}
		public static bool changeWardrobe(int[] clothesIDs, int targetWardrobeID)
		{
			bool allChanged = true;

			foreach (var clothesID in clothesIDs)
				allChanged &= changeWardrobe(clothesIDs, targetWardrobeID);

			return allChanged;
		}

		public static bool wear(int clothesID)
		{
			Clothes clothes = clothesDb.GetById(clothesID);

			if (clothes == null)
#if DEBUG
				throw new Exception();
#else
			return false;
#endif
			WardrobeManager.wear(clothes.wardrobeID);

			clothes.wear();

			return clothesDb.Update(clothes);
		}
		public static bool wear(IEnumerable<int> clothesIDs)
		{
			bool allUpdated = true;

			foreach (int clothesID in clothesIDs)
				allUpdated &= wear(clothesID);

			return allUpdated;
		}

		public static double calculateWarmthDegree(int[] clothesIDs)
		{
			//TODO

			return 0;
		}
	}
}
