using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Valet_Backend.Controllers;
using Valet_Backend.Model.Suit;
using Valet_Backend.Model.User;

namespace Valet_Backend.Model.Wardrobe
{
	public class WardrobeManager : DbContext
	{
		/// <summary>
		/// 查找指定衣橱是否存在
		/// </summary>
		/// <param name="wardrobeID"></param>
		/// <returns></returns>
		public static bool exist(int wardrobeID)
		{
			return wardrobeDb.GetById(wardrobeID) != null;
		}

		public static bool add(string userID, string wardrobeName)
		{
			if (UserManager.exist(userID) || wardrobeDb.IsAny(x => x.userID == userID && x.name == wardrobeName))
				return false;

			return wardrobeDb.Insert(new Wardrobe(userID, wardrobeName));
		}

		public static bool delete(int wardrobeID)
		{
			return wardrobeDb.DeleteById(wardrobeID);
		}



		public static bool rename(int wardrobeID, string newName)
		{
			Wardrobe wardrobe = wardrobeDb.GetById(wardrobeID);

			if (wardrobe == null)
				return false;

			wardrobe.name = newName;

			return wardrobeDb.Update(wardrobe);
		}
		
		public static string user(int wardrobeID)
		{
			Wardrobe wardrobe = wardrobeDb.GetById(wardrobeID);

			if (wardrobe == null)
#if DEBUG
				throw new Exception();
#else
			return null;
#endif

			return wardrobe.userID;
		}

		public static bool wear(int wardrobeID)
		{
			Wardrobe wardrobe = wardrobeDb.GetById(wardrobeID);

			if (wardrobe == null)
#if DEBUG
				throw new Exception();
#else
			return;
#endif

			wardrobe.wear();

			return wardrobeDb.Update(wardrobe);
		}
	}
}
