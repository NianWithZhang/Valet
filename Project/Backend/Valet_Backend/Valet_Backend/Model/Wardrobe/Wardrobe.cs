using SqlSugar;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Valet_Backend.Model.Wardrobe
{
	[SugarTable("WardrobeTable")]
	public class Wardrobe
	{
		public int id { get; set; }

		[SugarColumn(ColumnName = "user_id")]
		public string userID { get; set; }

		public string name { get; set; }

		[SugarColumn(ColumnName = "last_used_time")]
		public DateTime lastUsedTime { get; set; }

		public Wardrobe(){}

		public Wardrobe(string _user_id, string _name)
		{
			userID = _user_id;
			name = _name;
			lastUsedTime = DateTime.MinValue;
		}

		public void wear()
		{
			lastUsedTime = DateTime.Now;
		}
	}
}
