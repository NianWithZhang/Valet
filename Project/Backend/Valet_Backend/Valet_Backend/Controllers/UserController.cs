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
			//UserEntity user = db.Queryable<UserEntity>().InSingle(id);
			
			UserManager userManager = new UserManager();

			NotDistributedFunctions.getLocationCityName(38.418651, 114.645415);
			//Console.WriteLine(NotDistributedFunctions.testTaobao());

			return new { ans = userManager.checkUserPassword(id,password) };
		}

		// POST api/user
		[HttpPost]
		public object addUser(string id, string password)
		{
			UserManager userManager = new UserManager();

			return new {ans = userManager.registerNewUser(id,password)};
		}
	}
}