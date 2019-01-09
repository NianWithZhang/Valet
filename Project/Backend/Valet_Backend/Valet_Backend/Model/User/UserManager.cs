using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Valet_Backend.Controllers;
using Valet_Backend.Model.Clothes;
using Valet_Backend.Model.Wardrobe;

namespace Valet_Backend.Model.User
{
	/// <summary>
	/// 用户模块控制类 实现该模块接口
	/// </summary>
	public class UserManager : DbContext
	{
		#region 添加
		/// <summary>
		/// 新建用户
		/// </summary>
		/// <param name="id">新用户ID</param>
		/// <param name="password">新用户密码</param>
		/// <returns>创建结果 是否不存在已有用户且创建成功</returns>
		public static bool add(string id, string password)
		{
			if (exist(id))
				return false;

			return userDb.Insert(new User(id, password));
		}
		#endregion

		#region 查询
		/// <summary>
		/// 查找某用户id是否存在
		/// </summary>
		/// <param name="id">用户ID</param>
		/// <returns>查询结果</returns>
		public static bool exist(string id)
		{
			if (id == null)
				return false;

			return userDb.GetById(id) != null;
		}

		/// <summary>
		/// 查询用户名密码是否匹配
		/// </summary>
		/// <param name="id">用户ID</param>
		/// <param name="password">密码</param>
		/// <returns>查询结果 是否用户ID存在且密码正确</returns>
		public static bool check(string id, string password)
		{
			if (id == null)
				return false;

			User user = userDb.GetById(id);

			return user != null && user.password == password;
		}

		#endregion

		#region 衣物推荐

		/// <summary>
		/// 设置推荐内容 执行时另开单独线程操作
		/// </summary>
		/// <param name="recommendInfo">推荐内容信息</param>
		public static void setRecommend_thread(object recommendInfo)
		{
			IEnumerable<User> users = (recommendInfo as SetRecommendInfo).users;

			if (!users.Any())
				return;

			foreach (User user in users)
				user.recommednItem = (recommendInfo as SetRecommendInfo).item;

			userDb.UpdateRange((recommendInfo as SetRecommendInfo).users.ToArray());
		}

		/// <summary>
		/// 获取指定用户的推荐宝贝信息
		/// </summary>
		/// <param name="userID">用户ID</param>
		/// <returns>其推荐宝贝信息 若无推荐宝贝则返回结果内容为空</returns>
		public static TaobaoItem getRecommend(string userID)
		{
			User user = userDb.GetById(userID);

			if (user == null)
#if DEBUG
				throw new Exception();
#else
				return new TaobaoItem();
#endif
			TaobaoItem item = user.recommednItem;

			userDb.Update(user);

			return item;
		}

		/// <summary>
		/// 查找与指定用户衣物喜好相似的用户
		/// </summary>
		/// <param name="id">源用户ID</param>
		/// <returns>找到的所有相似用户ID列表</returns>
		public static IEnumerable<User> similarUsers(string id)
		{
			User user = userDb.GetById(id);

			if (user == null || WardrobeManager.getByUser(id).wardrobes.Count() == 0)
#if DEBUG
				throw new Exception();
#else
				return new List<User>();
#endif

			//获取源用户衣物列表
			IEnumerable<int> wardrobeIDs = WardrobeManager.getByUser(user.id).wardrobes.ToList().Select(x => x.id);

			IEnumerable<IEnumerable<int>> clothesIDLists = wardrobeIDs.Select(x => ClothesManager.getByWardrobe(x).clothes.Select(y => y.id));

			List<Clothes.Clothes> clothes = new List<Clothes.Clothes>();

			foreach (var clothesIDs in clothesIDLists)
				foreach (var clothesID in clothesIDs)
					clothes.Add(ClothesManager.get(clothesID));

			Dictionary<double, User> similarUsers = new Dictionary<double, User>();

			foreach (User anotherUser in userDb.GetList())
			{
				if (anotherUser.id == user.id)
					continue;

				double similarity = calculateUserSimilarity(anotherUser, clothes);

				if (similarUsers.Count == 0 || similarity > similarUsers.Keys.Min())
				{
					if (similarUsers.Count == 0)
						similarUsers.Add(similarity, anotherUser);

					if (similarUsers.Count >= Config.matchUserNum)
						similarUsers.Remove(similarUsers.Min().Key);

					if (similarUsers.ContainsKey(similarity))
						similarity += 0.000001;

					similarUsers.Add(similarity, anotherUser);
				}
			}

			return similarUsers.Values;
		}

		/// <summary>
		/// 计算从源用户到目标用户的相似度
		/// </summary>
		/// <param name="user">目标用户</param>
		/// <param name="srcClothesList">源用户衣物列表用户</param>
		/// <returns>相似度值 范围为0-1 值越大相似度越高</returns>
		private static double calculateUserSimilarity(User user, List<Clothes.Clothes> srcClothesList)
		{
			//获取匹配目标用户最近常穿的衣橱
			IEnumerable<int> wardrobeIDs = WardrobeManager.getByUser(user.id).wardrobes.ToList().Select(x => x.id);

			IEnumerable<IEnumerable<int>> clothesIDLists = wardrobeIDs.Select(x => ClothesManager.getByWardrobe(x).clothes.Select(y => y.id));

			List<Clothes.Clothes> clothesList = new List<Clothes.Clothes>();

			foreach (var clothesIDs in clothesIDLists)
				foreach (var clothesID in clothesIDs)
					clothesList.Add(ClothesManager.get(clothesID));

			IEnumerable<Clothes.Clothes> mostDressedClothes = clothesList.OrderByDescending(x => x.wearingFrequency).OrderByDescending(x => x.lastWearingTime).Take(Config.matchClothesNum);

			double ans = 0;

			foreach (var clothes in mostDressedClothes)
			{
				double maxSimilarity = double.MinValue;

				foreach (var targetClothes in srcClothesList)
				{
					double temp = ClothesManager.getSimilarity(new KeyValuePair<Clothes.Clothes,Clothes.Clothes>(targetClothes,clothes));

					if(temp>maxSimilarity)
						maxSimilarity = temp;
				}

				if (maxSimilarity > double.MinValue)
					ans += maxSimilarity*Config.clothesTypeCoeffiencient[clothes.type];
			}

			if(mostDressedClothes.Any())
				ans /= mostDressedClothes.Sum(x=>Config.clothesTypeCoeffiencient[x.type]);

			return ans;
		}

		#endregion
	}
}
