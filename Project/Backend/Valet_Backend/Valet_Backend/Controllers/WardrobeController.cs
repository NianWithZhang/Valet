using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Valet_Backend.Model;

namespace Valet_Backend.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class WardrobeController : ControllerBase
    {
		[HttpGet]
		public IEnumerable<KeyValuePair<int, string>> getClothes(int id)
		{
			return WardrobeManager.getClothes(id);
		}

		[HttpPost]
		public bool addWardrobe(string user_id,string name)
		{
			return WardrobeManager.add(user_id,name);
		}

		[HttpPut]
		public bool renameWardrobe(int id,string name)
		{
			return WardrobeManager.rename(id,name);
		}

		[HttpDelete]
		public bool deleteWardrobe(int id)
		{
			return WardrobeManager.delete(id);
		}
		
    }
}