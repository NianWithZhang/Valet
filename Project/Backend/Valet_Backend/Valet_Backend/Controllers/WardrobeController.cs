using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Valet_Backend.Model;
using Valet_Backend.Model.Wardrobe;

namespace Valet_Backend.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	public class WardrobeController : ControllerBase
	{
		[HttpGet]
		public object getClothes(int id)
		{
			return new { ans = WardrobeManager.getClothes(id) };
		}

		[HttpPost]
		public object addWardrobe(string user_id, string name)
		{
			return new { ans = WardrobeManager.add(user_id, name) };
		}

		[HttpPut]
		public object renameWardrobe(int id, string name)
		{
			return new { ans = WardrobeManager.rename(id, name) };
		}

		[HttpDelete]
		public object deleteWardrobe(int id)
		{
			return new { ans = WardrobeManager.delete(id) };
		}

	}
}