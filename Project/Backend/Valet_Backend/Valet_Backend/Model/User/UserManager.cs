using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model.User
{
	public class UserManager : DbContext
	{
		/// <summary>
		/// 查找某用户id是否存在
		/// </summary>
		/// <param name="id"></param>
		/// <returns></returns>
		public static bool exist(string id)
		{
			return userDb.GetById(id) != null;
		}

		public static bool check(string id, string password)
		{
			if (id == null)
				return false;

			User user = userDb.GetById(id);

			return user != null && user.password == password;
		}

		public static bool create(string id,string password)
		{
			if (exist(id))
				return false;

			return userDb.Insert(new User(id, password));
		}

		public static IEnumerable<KeyValuePair<int,string>> getWardrobes(string id)
		{
			return wardrobeDb.GetList(x=>x.userID == id).Select(x=>new KeyValuePair<int,string>(x.id,x.name));
		}

		public static bool setRecommend(string userID,TaobaoItem item)
		{
			User user = userDb.GetById(userID);

			if (user == null)
#if DEBUG
				throw new Exception();
#else
			return false;
#endif

			user.recommendItemUrl = item.itemUrl;
			user.recommendItemPicUrl = item.picUrl;

			return userDb.Update(user);
		}

		public static TaobaoItem getRecommend(string userID)
		{
			User user = userDb.GetById(userID);

			if (user == null)
#if DEBUG
				throw new Exception();
#else
			return new KeyValuePair<string, string>(null,null);
#endif
			TaobaoItem item = user.recommednItem;

			userDb.Update(user);

			return item;
		}

		public static IEnumerable<string> similarUsers(string id)
		{
			User user = userDb.GetById(id);

			if (user == null)
				return null;

			//TODO 现在返回的是所有用户
			return userDb.GetList().Select(x=>x.id);
		}
	}
}
