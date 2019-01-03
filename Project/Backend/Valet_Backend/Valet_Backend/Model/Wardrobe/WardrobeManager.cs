using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Valet_Backend.Controllers;
using Valet_Backend.Model.Clothes;
using Valet_Backend.Model.Suit;
using Valet_Backend.Model.User;

namespace Valet_Backend.Model.Wardrobe
{
	/// <summary>
	/// 衣橱模块的控制类 实现该模块接口
	/// </summary>
	public class WardrobeManager : DbContext
	{
		#region 添加

		/// <summary>
		/// 添加新衣橱
		/// </summary>
		/// <param name="userID">用户ID</param>
		/// <param name="wardrobeName">衣橱名称</param>
		/// <returns>添加结果 是否成功添加</returns>
		public static bool add(string userID, string wardrobeName)
		{
			if (UserManager.exist(userID) || wardrobeDb.IsAny(x => x.userID == userID && x.name == wardrobeName))
				return false;

			return wardrobeDb.Insert(new Wardrobe(userID, wardrobeName));
		}

		#endregion

		#region 查询

		/// <summary>
		/// 查找指定衣橱是否存在
		/// </summary>
		/// <param name="wardrobeID"></param>
		/// <returns></returns>
		public static bool exist(int wardrobeID)
		{
			return wardrobeDb.GetById(wardrobeID) != null;
		}

		/// <summary>
		/// 获取衣橱所属的用户ID
		/// </summary>
		/// <param name="wardrobeID">衣橱ID</param>
		/// <returns>衣橱所属的用户ID 未找到则返回null</returns>
		public static string user(int wardrobeID)
		{
			Wardrobe wardrobe = wardrobeDb.GetById(wardrobeID);

			//确认衣橱是否存在
			if (wardrobe == null)
#if DEBUG
				throw new Exception();
#else
			return null;
#endif

			return wardrobe.userID;
		}

		#endregion

		#region 修改

		/// <summary>
		/// 重命名衣橱
		/// </summary>
		/// <param name="wardrobeID">衣橱ID</param>
		/// <param name="newName">新名称</param>
		/// <returns>重命名结果 是否成功重命名衣橱</returns>
		public static bool rename(int wardrobeID, string newName)
		{
			Wardrobe wardrobe = wardrobeDb.GetById(wardrobeID);

			//确保衣橱存在
			if (wardrobe == null)
				return false;

			wardrobe.name = newName;

			//执行更新操作并返回更新结果
			return wardrobeDb.Update(wardrobe);
		}

		#endregion
		
		#region 删除

		/// <summary>
		/// 删除指定衣橱
		/// </summary>
		/// <param name="wardrobeID">需要删除的衣橱ID</param>
		/// <returns>删除结果 是否成功删除</returns>
		public static bool delete(int wardrobeID)
		{
			ClothesManager.deleteByWardrobe(wardrobeID);

			return wardrobeDb.DeleteById(wardrobeID);
		}

		/// <summary>
		/// 批量删除衣橱
		/// </summary>
		/// <param name="wardrobeIDs">需要删除的衣橱ID列表</param>
		/// <returns>删除结果 是否成功删除衣橱</returns>
		public static bool delete(int[] wardrobeIDs)
		{
			bool ans = true;

			foreach (int wardrobeID in wardrobeIDs)
				ans &= delete(wardrobeID);

			return ans;
		}

		#endregion
		
		#region 穿着衣物

		/// <summary>
		/// 穿着衣橱中衣物 更新衣橱的最后使用时间信息
		/// </summary>
		/// <param name="wardrobeID">衣橱ID</param>
		/// <returns>操作结果 是否成功更新内容</returns>
		public static bool wear(int wardrobeID)
		{
			Wardrobe wardrobe = wardrobeDb.GetById(wardrobeID);

			//确保找到衣橱
			if (wardrobe == null)
#if DEBUG
				throw new Exception();
#else
			return false;
#endif

			wardrobe.wear();

			//更新信息并返回更新结果
			return wardrobeDb.Update(wardrobe);
		}

		#endregion
	}
}
