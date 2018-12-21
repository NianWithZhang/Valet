using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model
{
	public class UserManager : DbContext
	{
		public static bool checkPassword(string id, string password)
		{
			if (id == null)
				return false;

			User user = userDb.GetById(id);

			return user != null && user.password == password;
		}

		public static bool register(string id,string password)
		{
			if (userDb.GetById(id) == null)
				return false;

			return userDb.Insert(new User(id, password));
		}

		public static IEnumerable<KeyValuePair<int,string>> getWardrobes(string id)
		{
			return wardrobeDb.GetList(x=>x.userID == id).Select(x=>new KeyValuePair<int,string>(x.id,x.name));
		}
	}
}
