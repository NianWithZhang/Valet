using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using SqlSugar;
using Valet_Backend.Model;

namespace Valet_Backend.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	public class UserController : ControllerBase
	{
		// GET api/user
		[HttpGet]
		public object checkUser(string id, string password)
		{
			return new { ans = UserManager.checkPassword(id,password) };
		}

		// POST api/user
		[HttpPost]
		public object addUser(string id, string password)
		{
			return new {ans = UserManager.register(id,password)};
		}

		[HttpGet]
		public IEnumerable<KeyValuePair<int,string>> getWardrobes(string id)
		{
			return UserManager.getWardrobes(id);
		}
	}
}