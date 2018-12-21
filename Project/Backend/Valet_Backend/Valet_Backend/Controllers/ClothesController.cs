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
    public class ClothesController : ControllerBase
    {
		[HttpGet]
		public Clothes getClothesInfo(int id)
		{
			return ClothesManager.getInfo(id);
		}

		[HttpPost]
		public bool addClothes(IFormFile pic,int wardrobe_id,string name,ClothesType type,int thickness)
		{
			return ClothesManager.add(pic,wardrobe_id,name,type,thickness);
		}

		[HttpPut]
		public bool changeClothesWardrobe(int clothes_id, int wardrobe_id)
		{
			return ClothesManager.changeWardrobe(clothes_id, wardrobe_id);
		}

		[HttpPut]
		public bool modifyClothesPic(IFormFile pic,int id)
		{
			return ClothesManager.savePic(pic,id);
		}

		[HttpPut]
		public bool modifyClothes(IFormFile pic,int id,string name,ClothesType type,int thickness)
		{
			return ClothesManager.modify(pic,id,name,type,thickness);
		}

		[HttpDelete]
		public bool deleteClothes(int id)
		{
			return ClothesManager.delete(id);
		}
    }
}