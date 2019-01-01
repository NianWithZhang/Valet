using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Valet_Backend.Controllers;
using Valet_Backend.Model.Suit;
using Valet_Backend.Model.User;
using Valet_Backend.Model.Wardrobe;

namespace Valet_Backend.Model.Clothes
{
	/// <summary>
	/// 衣物模块控制类 实现该模块相关接口
	/// </summary>
	public class ClothesManager : DbContext
	{
		#region 查询

		/// <summary>
		/// 获取指定编号的衣物
		/// </summary>
		/// <param name="clothesID">衣物ID</param>
		/// <returns>编号对应的衣物 未找到的返回null</returns>
		public static Clothes get(int clothesID)
		{
			Clothes clothes = clothesDb.GetById(clothesID);

			//确保衣物存在
			if (clothes == null)
				return null;

			return clothes;
		}

		/// <summary>
		/// 获取指定衣橱的所有衣物列表
		/// </summary>
		/// <param name="wardrobeID">衣橱ID</param>
		/// <returns>对应衣橱的所有衣橱列表</returns>
		public static ClothesResponseList getByWardrobe(int wardrobeID)
		{
			//找不到衣橱则返回空列表
			if (!WardrobeManager.exist(wardrobeID))
#if DEBUG
				throw new Exception();
#else
			return new ClothesResponseList(new ClothesResponse[0]);
#endif

			return new ClothesResponseList(clothesDb.GetList(x => x.wardrobeID == wardrobeID).OrderByDescending(x => x.lastWearingTime).Select(x => new ClothesResponse(x.id, x.name, x.type)).ToArray());
		}

		#endregion

		#region 添加

		/// <summary>
		/// 添加新衣物
		/// </summary>
		/// <param name="clothesPic">新衣物照片</param>
		/// <param name="wardrobeID">衣物所属衣橱ID</param>
		/// <param name="name">新衣物名称</param>
		/// <param name="type">新衣物类型</param>
		/// <param name="thickness">新衣物厚度指数</param>
		/// <returns></returns>
		public static bool add(IFormFile clothesPic, int wardrobeID, string name, ClothesType type, int thickness)
		{
			//确保衣橱存在
			if (!WardrobeManager.exist(wardrobeID))
				return false;

			Clothes clothes = new Clothes(wardrobeID, name, type, thickness);

			clothes.id = clothesDb.InsertReturnIdentity(clothes);

			savePic(clothesPic, clothes);

			TaobaoItem taobaoItem = TaobaoApi.getTaobaoItem(clothes.picPath);

			//处理购衣推荐
			if (taobaoItem != null)
			{
				Thread setCommendThread = new Thread(new ParameterizedThreadStart(UserManager.setRecommend_thread));
				setCommendThread.Start(new SetRecommendInfo(UserManager.similarUsers(WardrobeManager.user(wardrobeID)), taobaoItem));
				//已改为多线程执行
				//UserManager.setRecommend(userID,taobaoItem);
			}

			return true;
		}
		/// <summary>
		/// 保存衣物照片
		/// </summary>
		/// <param name="clothesPicFile">衣物照片</param>
		/// <param name="clothes">需要保存照片的衣物</param>
		/// <returns>保存结果 是否保存成功</returns>
		public static bool savePic(IFormFile clothesPicFile, Clothes clothes)
		{
			//确认衣物不为空
			if (clothes == null)
				return false;

			//确认衣物照片不为空
			if (clothesPicFile != null)
			{
				string fileDir = Config.PicSaveDir;

				//确认保存路径存在
				if (!Directory.Exists(fileDir))
				{
					Directory.CreateDirectory(fileDir);
				}

				string picPath = clothes.picPath;

				FileStream fs = System.IO.File.Create(picPath);

				//保存照片
				clothesPicFile.CopyTo(fs);
				fs.Flush();
				fs.Close();

				//根据保存的照片获取衣物主题色
				double color = getPicMainColor(picPath);
				clothes.color = color;
				clothesDb.Update(clothes);

				return true;
			}
			else
				return false;
		}
		/// <summary>
		/// 根据衣物保存的照片获取其主要颜色
		/// </summary>
		/// <param name="filePath">衣物照片路径</param>
		/// <returns>主要颜色的Hue值</returns>
		public static double getPicMainColor(string filePath)
		{
			//读取照片
			Image img = Image.FromFile(filePath);

			Bitmap bitmap = new Bitmap(img);

			//获取中心点像素值
			return bitmap.GetPixel(bitmap.Width / 2, bitmap.Height / 2).GetHue();
		}

		#endregion

		#region 修改

		/// <summary>
		/// 修改衣物属性
		/// </summary>
		/// <param name="clothesPic">衣物新照片</param>
		/// <param name="clothesID">需要编辑的衣物ID</param>
		/// <param name="clothesName">衣物新名称</param>
		/// <param name="type">衣物新类型</param>
		/// <param name="thickness">衣物新厚度</param>
		/// <returns>修改结果 是否修改成功</returns>
		public static bool modify(IFormFile clothesPic, int clothesID, string clothesName, ClothesType type, int thickness)
		{
			Clothes clothes = clothesDb.GetById(clothesID);

			//确认衣物存在
			if (clothes == null)
				return false;

			//保存衣物照片
			if (clothesPic != null)
				savePic(clothesPic, clothes);

			//修改衣物信息
			clothes.modify(clothesName, type, thickness);

			//执行更新数据库操作并保存更新结果
			bool ans = clothesDb.Update(clothes);

			//更新其相关穿搭的保暖指数
			SuitManager.regenerateWarmthDegreeByClothes(clothesID);

			return ans;
		}

		/// <summary>
		/// 衣物更换衣橱
		/// </summary>
		/// <param name="clothesID">衣物ID</param>
		/// <param name="targetWardrobeID">需要切换至的衣橱ID</param>
		/// <returns>切换结果 是否成功切换衣橱</returns>
		public static bool changeWardrobe(int clothesID, int targetWardrobeID)
		{
			Clothes clothes = clothesDb.GetById(clothesID);

			//确保衣橱和衣物存在
			if (clothes == null || !WardrobeManager.exist(targetWardrobeID))
				return false;

			//更换衣橱
			clothes.wardrobeID = targetWardrobeID;

			SuitManager.deleteByClothes(clothesID);

			return clothesDb.Update(clothes);
		}
		/// <summary>
		/// 衣物批量更换衣橱
		/// </summary>
		/// <param name="clothesIDs">需要更换衣橱的衣物ID列表</param>
		/// <param name="targetWardrobeID">要切换至的目标衣橱ID</param>
		/// <returns>更换衣橱结果 是否成功更换衣橱</returns>
		public static bool changeWardrobe(int[] clothesIDs, int targetWardrobeID)
		{
			bool allChanged = true;

			foreach (var clothesID in clothesIDs)
				allChanged &= changeWardrobe(clothesIDs, targetWardrobeID);

			return allChanged;
		}

		#endregion

		#region 删除

		/// <summary>
		/// 删除单件衣物
		/// </summary>
		/// <param name="clothesID">需要删除的衣物ID</param>
		/// <returns>删除结果 是否成功删除</returns>
		public static bool delete(int clothesID)
		{
			Clothes clothes = clothesDb.GetById(clothesID);

			if (clothes == null)
				return false;

			deleteClothesPic(clothes);

			SuitManager.deleteByClothes(clothesID);

			return clothesDb.Delete(clothes);
		}
		/// <summary>
		/// 批量删除衣物
		/// </summary>
		/// <param name="clothesIDs">待删除的衣物ID列表</param>
		/// <returns>删除结果 是否全部删除成功</returns>
		public static bool delete(int[] clothesIDs)
		{
			bool ans = true;

			foreach (var clothesID in clothesIDs)
				ans &= delete(clothesID);

			return ans;
		}
		/// <summary>
		/// 删除衣物照片
		/// </summary>
		/// <param name="clothes">需要删除照片的衣物</param>
		/// <returns>删除结果 是否照片删除成功</returns>
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

		#endregion

		#region 穿着衣物

		/// <summary>
		/// 穿着衣物
		/// </summary>
		/// <param name="clothesID">被穿着的衣物ID</param>
		/// <returns>操作结果 是否成功更新相关信息</returns>
		public static bool wear(int clothesID)
		{
			Clothes clothes = clothesDb.GetById(clothesID);

			//确认衣物存在
			if (clothes == null)
#if DEBUG
				throw new Exception();
#else
			return false;
#endif

			//更新衣橱被使用信息
			WardrobeManager.wear(clothes.wardrobeID);

			//更新衣物被穿着信息
			clothes.wear();

			return clothesDb.Update(clothes);
		}
		/// <summary>
		/// 批量穿着衣物
		/// </summary>
		/// <param name="clothesIDs">被穿着的衣物ID列表</param>
		/// <returns>操作结果 是否成功更新相关信息</returns>
		public static bool wear(IEnumerable<int> clothesIDs)
		{
			bool allUpdated = true;

			foreach (int clothesID in clothesIDs)
				allUpdated &= wear(clothesID);

			return allUpdated;
		}

		#endregion

		#region 衣物保暖评价

		/// <summary>
		/// 计算一套穿搭的保暖指数信息
		/// </summary>
		/// <param name="clothesIDs">一套穿搭中的衣物ID列表</param>
		/// <returns>Key标识是否找到全部衣物 Value表示该穿搭的最适宜温度</returns>
		public static KeyValuePair<bool, double> calculateWarmthDegree(int[] clothesIDs)
		{
			bool allFound = true;

			double ans = 0;

			foreach (var clothesID in clothesIDs)
			{
				Clothes clothes = clothesDb.GetById(clothesID);

				if (clothes == null)
				{
#if DEBUG
					throw new Exception();
#else
					allFound = false;
					continue;
#endif
				}

				//累积每件衣物的保暖指数
				ans += clothes.warmthDegree();
			}

			ans = 38 - ans;

			return new KeyValuePair<bool, double>(allFound, ans);
		}

		#endregion
	}
}
