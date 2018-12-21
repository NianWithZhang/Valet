using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model
{
	public class WardrobeManager:DbContext
	{
		public static bool add(string userID,string wardrobeName)
		{
			User user = userDb.GetById(userID);

			if (user == null||wardrobeDb.IsAny(x=>x.userID==userID&&x.name == wardrobeName))
				return false;

			return wardrobeDb.Insert(new Wardrobe(userID, wardrobeName));
		}

		public static bool delete(int wardrobeID)
		{
			return wardrobeDb.DeleteById(wardrobeID);
		}

		public static IEnumerable<KeyValuePair<int,string>> getClothes(int wardrobeID)
		{
			List<KeyValuePair<int, string>> ans = new List<KeyValuePair<int, string>>();

			Wardrobe wardrobe = wardrobeDb.GetById(wardrobeID);

			if (wardrobe == null)
				return ans;

			return clothesDb.GetList(x=>x.wardrobeID==wardrobeID).Select(x=>new KeyValuePair<int,string>(x.id,x.name));
		}

		public static bool rename(int wardrobeID,string newName)
		{
			Wardrobe wardrobe = wardrobeDb.GetById(wardrobeID);

			if (wardrobe == null)
				return false;

			wardrobe.name = newName;

			return wardrobeDb.Update(wardrobe);
		}
	}
}
