using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model
{
	[SugarTable("WardrobeTable")]
	public class WardrobeEntity
	{
		public string user_id { get; set; }

		public int id { get; set; }

		public string name { get; set; }

		public WardrobeEntity()
		{
			user_id = "defaultUser";
			id = 0;
		}

		public WardrobeEntity(string _user_id,string _name)
		{
			user_id = _user_id;
			name = _name;
		}
	}
}
