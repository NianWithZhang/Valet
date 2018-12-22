using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Valet_Backend.Model.Suit;

namespace Valet_Backend.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class SuitController : ControllerBase
    {
		[HttpPost]
		public object add(IFormFile pic,string name,int wardrobe_id,int[] clothes)
		{
			return new { ans = SuitManager.add(pic,name, wardrobe_id, clothes) };
		}

		[HttpDelete]
		public object delete(int id)
		{
			return new { ans = SuitManager.delete(id) };
		}

		[HttpGet]
		public object getClothes(int id)
		{
			return new { ans = SuitManager.getClothes(id) };
		}

		[HttpPut]
		public object wear(int id)
		{
			return new { ans = SuitManager.wear(id) };
		}

		[HttpPut]
		public object wear(int[] clothes_ids)
		{
			return new { ans = SuitManager.wear(clothes_ids) };
		}
    }
}