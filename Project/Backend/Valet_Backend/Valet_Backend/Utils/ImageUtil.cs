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
		/// 压缩图片 由外部调用的方法
		/// </summary>
		/// <param name="picPath"></param>
		public static bool compressImage(string picPath)
		{
			try
			{
				Thread thread = new Thread(new ParameterizedThreadStart(compressImage_thread));
				thread.Start(picPath);
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
		/// <param name="picPath"></param>
		private static void compressImage_thread(object picPath)
		{
			compressImage(Config.tempDir, picPath as string);
		}
		/// <summary>
		/// 压缩图片的内部递归调用函数
		/// </summary>
		/// <param name="sourcePath">原图片地址</param>
		/// <param name="dFile">压缩后保存图片地址</param>
		/// <param name="isFirstTime">是否是第一次调用</param>
		/// <returns></returns>
		private static void compressImage(string sourcePath, string dFile, bool isFirstTime = true)
		{
			int flag = Config.picCompressQuality,size = 300;

			//如果是第一次调用，原始图像的大小小于要压缩的大小，则直接复制文件，并且返回true
			FileInfo firstFileInfo = new FileInfo(sourcePath);
			if (isFirstTime == true && firstFileInfo.Length < size * 1024)
			{
				firstFileInfo.CopyTo(dFile);
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
					ob.Save(dFile, jpegICIinfo, ep);//dFile是压缩后的新路径
					FileInfo fi = new FileInfo(dFile);
					if (fi.Length > 1024 * size)
					{
						flag = flag - 10;
						compressImage(sourcePath, dFile, false);
					}
				}
				else
				{
					ob.Save(dFile, tFormat);
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
