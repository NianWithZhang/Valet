using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Valet_Backend.Model;

namespace Valet_Backend.Controllers
{
	[Route("api/[controller]")]
	[ApiController]
	public class TestController : ControllerBase
	{
		//GET api/test
		[HttpGet]
		public void test()
		{

		}

		[HttpPost]
		public object uploadClothesPic(IFormFile file, string userid,string clothesid)
		{
			return new { ans = NotDistributedFunctions.uploadClothesPic(file,userid,clothesid).ToString() };
		}


		///// <summary>
		///// 图片上传  [FromBody]string token
		///// </summary>
		///// <returns></returns>
		//[HttpPost]
		////[Route("api/test")]
		//public Task<Hashtable> ImgUpload()
		//{
		//	// 检查是否是 multipart/form-data 
		//	if (!Request.HasFormContentType)
		//		throw new Exception();

		//	////文件保存目录路径
		//	//const string saveTempPath = "~/UploadFiles/";
		//	//var dirTempPath = HttpContext.Current.Server.MapPath(saveTempPath);

		//	var files = HttpContext.Request.Form.Files;

		//	string upload_path = Directory.GetCurrentDirectory() + "/wwwroot";

		//	// 设置上传目录 
		//	var provider = new MultipartFormDataStreamProvider(upload_path);

		//	var task = Request.Content.ReadAsMultipartAsync(provider).
		//		ContinueWith<Hashtable>(o =>
		//		{
		//			var hash = new Hashtable();
		//			var file = provider.FileData[0];
		//			// 最大文件大小
		//			const int maxSize = 10000000;
		//			// 定义允许上传的文件扩展名 
		//			const string fileTypes = "gif,jpg,jpeg,png,bmp";

		//			// token验证
		//			var token = string.Empty;
		//			try
		//			{
		//				token = provider.FormData["token"].ToString();
		//			}
		//			catch (Exception exception)
		//			{
		//				throw new Exception("未附带token，非法访问!", exception);
		//			}

		//			if (!string.IsNullOrEmpty(token))
		//			{
		//				if (!CacheManager.TokenIsExist(token))
		//				{
		//					throw new UserLoginException("Token已失效，请重新登陆!");
		//				}
		//				if (accountInfoService.Exist_User_IsForzen(AccountHelper.GetUUID(token)))
		//				{
		//					CacheManager.RemoveToken(token);
		//					tempCacheService.Delete_OneTempCaches(new Guid(token));
		//					throw new UserLoginException("此用户已被冻结,请联系管理员（客服）!");
		//				}
		//			}
		//			else
		//			{
		//				throw new Exception("token为空，非法访问!");
		//			}

		//			string orfilename = file.Headers.ContentDisposition.FileName.TrimStart('"').TrimEnd('"');
		//			var fileinfo = new FileInfo(file.LocalFileName);
		//			if (fileinfo.Length <= 0)
		//			{
		//				hash["Code"] = -1;
		//				hash["Message"] = "请选择上传文件。";
		//			}
		//			else if (fileinfo.Length > maxSize)
		//			{
		//				hash["Code"] = -1;
		//				hash["Message"] = "上传文件大小超过限制。";
		//			}
		//			else
		//			{
		//				var fileExt = orfilename.Substring(orfilename.LastIndexOf('.'));

		//				if (String.IsNullOrEmpty(fileExt) ||
		//					Array.IndexOf(fileTypes.Split(','), fileExt.Substring(1).ToLower()) == -1)
		//				{
		//					hash["Code"] = -1;
		//					hash["Message"] = "不支持上传文件类型。";
		//				}
		//				else
		//				{
		//					try
		//					{
		//						AttachmentFileResultDTO attachmentFileResult;
		//						attachmentFileService.UploadAttachmentFile(fileinfo, dirTempPath, fileExt,
		//							Path.GetFileNameWithoutExtension(file.LocalFileName), out attachmentFileResult);

		//						hash["Code"] = 0;
		//						hash["Message"] = "上传成功";
		//						hash["FileId"] = attachmentFileResult.ID;
		//						hash["FileName"] = attachmentFileResult.FileName + attachmentFileResult.FileType;
		//						hash["NetImageUrl"] = attachmentFileResult.FileUrl;
		//					}
		//					catch (Exception exception)
		//					{
		//						throw new Exception("上传失败!", exception);
		//					}
		//				}
		//			}
		//			return hash;
		//		});
		//	return task;
		//}
	}
}