using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Valet_Backend.Model;
using Valet_Backend.Model.Clothes;

namespace Valet_Backend.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ClothesController : ControllerBase
    {
		[HttpGet]
		public Clothes getClothesInfo(int id)
		{
			return ClothesManager.getInfo(id);
		}

		[HttpPost]
		public object addClothes(IFormFile pic,int wardrobe_id,string name,ClothesType type,int thickness)
		{
			return new { ans = ClothesManager.add(pic, wardrobe_id, name, type, thickness) };
		}

		[HttpPut]
		public object changeClothesWardrobe(int clothes_id, int wardrobe_id)
		{
			return new { ans = ClothesManager.changeWardrobe(clothes_id, wardrobe_id) };
		}
		[HttpPut]
		public object changeClothesWardrobe(int[] clothes_ids,int wardrobe_id)
		{
			return new { ans = ClothesManager.changeWardrobe(clothes_ids,wardrobe_id) };
		}

		[HttpPut]
		public object modifyClothesPic(IFormFile pic,int id)
		{
			return new { ans = ClothesManager.savePic(pic, id) };
		}

		[HttpPut]
		public object modifyClothes(IFormFile pic,int id,string name,ClothesType type,int thickness)
		{
			return new { ans = ClothesManager.modify(pic, id, name, type, thickness) };
		}

		[HttpDelete]
		public object deleteClothes(int id)
		{
			return new { ans = ClothesManager.delete(id) };
		}
    }
}