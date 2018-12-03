using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model
{
	public class UserManager : DbContext
	{
		public bool checkUserPassword(string id, string password)
		{
			UserEntity user = userDb.GetById(id);

			return user != null && user.password == password;
		}

		public bool registerNewUser(string id,string password)
		{
			if (userDb.GetById(id) == null)
				return false;

			return userDb.Insert(new UserEntity(id, password));
		}
	}
}
