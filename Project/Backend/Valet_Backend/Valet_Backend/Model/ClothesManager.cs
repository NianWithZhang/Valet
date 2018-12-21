using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model
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
			Wardrobe wardrobe = wardrobeDb.GetById(wardrobeID);

			//确保衣橱存在
			if (wardrobe == null)
				return false;

			int clothesID = clothesDb.InsertReturnIdentity(new Clothes(wardrobe.id, name, type,thickness));

			savePic(clothesPic, clothesID);

			//TODO:开始准备推荐内容

			return true;
		}

		public static bool savePic(IFormFile clothesPicFile, int clothesID)
		{
			Clothes clothes = clothesDb.GetById(clothesID);

			if (clothes == null)
				return false;

			if (clothesPicFile != null)
			{
				string fileDir = Config.PicSaveDir;

				if (!Directory.Exists(fileDir))
				{
					Directory.CreateDirectory(fileDir);
				}
				//文件名称
				string projectFileName = clothes.id.ToString() + ".jpg";

				//上传的文件的路径
				string filePath = fileDir + projectFileName;
				FileStream fs = System.IO.File.Create(filePath);

				clothesPicFile.CopyTo(fs);
				fs.Flush();
				fs.Close();

				////TODO:需要？
				//fs = File.OpenRead(filePath);

				//根据图片获取主题色
				double color = getPicMainColor(filePath);
				clothes.color = color;
				clothesDb.Update(clothes);

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

			deleteClothesPic(clothesID);

			//TODO:删除对应的Suit

			clothesDb.Delete(clothes);

			//会自动级联删除？ 
			//clothes_suitDb.Delete(x=>x.clothesID==clothes.id);

			return true;
		}

		public static bool deleteClothesPic(int clothesID)
		{
			try
			{
				string filePath = Config.PicSaveDir + clothesID.ToString() + ".jpg";

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
			Wardrobe wardrobe = wardrobeDb.GetById(targetWardrobeID);

			//确保衣橱和衣物存在
			if (clothes == null || wardrobe == null)
				return false;

			//更换衣橱
			clothes.wardrobeID = targetWardrobeID;

			return clothesDb.Update(clothes);
		}

	}
}
