using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using SqlSugar;
using Valet_Backend.Model;
using Valet_Backend.Model.User;

namespace Valet_Backend.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	public class UserController : ControllerBase
	{
		[HttpGet]
		public object checkUser(string id, string password)
		{
			return new { ans = UserManager.check(id,password) };
		}

		[HttpGet]
		public TaobaoItem getRecommend(string id)
		{
			return UserManager.getRecommend(id);
		}
		
		[HttpPost]
		public object addUser(string id, string password)
		{
			return new {ans = UserManager.create(id,password)};
		}
		
		[HttpGet]
		public object getWardrobes(string id)
		{
			return new { ans = UserManager.getWardrobes(id) };
		}
	}
}