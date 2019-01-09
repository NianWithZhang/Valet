using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace Valet_Backend.Utils
{
	public class ImageUtil
	{
		#region 压缩图片

		/// <summary>
		/// <summary>
		/// 压缩图片 由外部调用的方法
		/// </summary>
		/// <param name="inputPath">输入路径</param>
		/// <param name="outputPath">输出路径</param>
		/// <param name="multiThread">是否另开线程处理</param>
		/// <param name="deleteInputFile">是否删除输入源图片</param>
		/// <returns></returns>
		public static bool compressImage(string inputPath, string outputPath, bool multiThread = false,bool deleteInputFile = true)
		{
			try
			{
				if (multiThread)
				{
					Thread thread = new Thread(new ParameterizedThreadStart(compressImage_thread));
					thread.Start(new KeyValuePair<KeyValuePair<string,string>,bool>(new KeyValuePair<string,string>(inputPath,outputPath),deleteInputFile));
				}
				else
				{
					compressImage_thread(new KeyValuePair<KeyValuePair<string, string>, bool>(new KeyValuePair<string, string>(inputPath, outputPath), deleteInputFile));
				}

				return true;
			}
			catch
			{
				return false;
			}
		}
		/// <summary>
		/// 压缩图片线程函数
		/// </summary>
		/// <param name="paths">Pair<string,string>形式输入input和output路径 以及最后是否删除输入源图片</param>
		private static void compressImage_thread(object paths)
		{
			compressImage_inner((paths as KeyValuePair<KeyValuePair<string, string>, bool>?).Value.Key.Key, (paths as KeyValuePair<KeyValuePair<string, string>, bool>?).Value.Key.Value);

			if ((paths as KeyValuePair<KeyValuePair<string, string>, bool>?).Value.Value)
				File.Delete((paths as KeyValuePair<KeyValuePair<string, string>, bool>?).Value.Key.Key);
		}
		/// <summary>
		/// 压缩图片的内部递归调用函数
		/// </summary>
		/// <param name="sourcePath">原图片地址</param>
		/// <param name="destPath">压缩后保存图片地址</param>
		/// <param name="isFirstTime">是否是第一次调用</param>
		/// <returns></returns>
		private static void compressImage_inner(string sourcePath, string destPath, bool isFirstTime = true)
		{
			int flag = Config.picCompressQuality, size = 300;

			//如果是第一次调用，原始图像的大小小于要压缩的大小，则直接复制文件，并且返回true
			FileInfo firstFileInfo = new FileInfo(sourcePath);
			if (isFirstTime == true && firstFileInfo.Length < size * 1024)
			{
				firstFileInfo.CopyTo(destPath);
				return;
			}
			Image iSource = Image.FromFile(sourcePath);
			ImageFormat tFormat = iSource.RawFormat;
			int dHeight = iSource.Height / 2;
			int dWidth = iSource.Width / 2;
			int sW = 0, sH = 0;
			//按比例缩放
			Size tem_size = new Size(iSource.Width, iSource.Height);
			if (tem_size.Width > dHeight || tem_size.Width > dWidth)
			{
				if ((tem_size.Width * dHeight) > (tem_size.Width * dWidth))
				{
					sW = dWidth;
					sH = (dWidth * tem_size.Height) / tem_size.Width;
				}
				else
				{
					sH = dHeight;
					sW = (tem_size.Width * dHeight) / tem_size.Height;
				}
			}
			else
			{
				sW = tem_size.Width;
				sH = tem_size.Height;
			}

			Bitmap ob = new Bitmap(dWidth, dHeight);
			Graphics g = Graphics.FromImage(ob);

			//g.Clear(Color.WhiteSmoke);
			g.CompositingQuality = System.Drawing.Drawing2D.CompositingQuality.HighQuality;
			g.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.HighQuality;
			g.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.HighQualityBicubic;

			g.DrawImage(iSource, new Rectangle((dWidth - sW) / 2, (dHeight - sH) / 2, sW, sH), 0, 0, iSource.Width, iSource.Height, GraphicsUnit.Pixel);

			g.Dispose();

			//以下代码为保存图片时，设置压缩质量
			EncoderParameters ep = new EncoderParameters();
			long[] qy = new long[1];
			qy[0] = flag;//设置压缩的比例1-100
			EncoderParameter eParam = new EncoderParameter(System.Drawing.Imaging.Encoder.Quality, qy);
			ep.Param[0] = eParam;

			try
			{
				ImageCodecInfo[] arrayICI = ImageCodecInfo.GetImageEncoders();
				ImageCodecInfo jpegICIinfo = null;
				for (int x = 0; x < arrayICI.Length; x++)
				{
					if (arrayICI[x].FormatDescription.Equals("JPEG"))
					{
						jpegICIinfo = arrayICI[x];
						break;
					}
				}
				if (jpegICIinfo != null)
				{
					ob.Save(destPath, jpegICIinfo, ep);//dFile是压缩后的新路径
					FileInfo fi = new FileInfo(destPath);
					if (fi.Length > 1024 * size)
					{
						flag = flag - 10;
						compressImage_inner(sourcePath, destPath, false);
					}
				}
				else
				{
					ob.Save(destPath, tFormat);
				}
				return;
			}
			catch
			{
				return;
			}
			finally
			{
				iSource.Dispose();
				ob.Dispose();
			}
		}

		#endregion
	}
}
