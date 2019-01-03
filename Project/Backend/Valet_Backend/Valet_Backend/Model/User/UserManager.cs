using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Valet_Backend.Controllers;

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

		/// <summary>
		/// 获取指定用户的所有衣橱列表
		/// </summary>
		/// <param name="id">用户ID</param>
		/// <returns>用户的衣橱列表</returns>
		public static WardrobeResponseList getWardrobes(string id)
		{
			return new WardrobeResponseList(wardrobeDb.GetList(x=>x.userID == id).OrderByDescending(x=>x.lastUsedTime).Select(x=>new WardrobeResponse(x.id,x.name)).ToArray());
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
			
			//清除该用户推荐宝贝信息
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

			if (user == null)
				return null;

			//TODO 现在返回的是所有用户
			return userDb.GetList();
		}

		#endregion
	}
}
