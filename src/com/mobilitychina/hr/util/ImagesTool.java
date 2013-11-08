package com.mobilitychina.hr.util;

import java.io.ByteArrayOutputStream;

import sun.misc.BASE64Encoder;


import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

public class ImagesTool {
	/**
	 * 缩放图片
	 * @param b
	 * @return
	 */
	public static  Bitmap initImage(Bitmap b) {
		int oldWidth = b.getWidth();
		int oldHeight = b.getHeight();
		//想要的大小
//		DisplayMetrics dm = new DisplayMetrics();
//		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int needWidth = 105;
		int needHeight = 105;
		// 计算缩放比例
		float scaleWidth = ((float) needWidth) / oldWidth;
		float scaleHeight = ((float) needHeight) / oldHeight;
		// 取得想要缩放的matrix参数
//		float scale =  scaleWidth > scaleHeight ? scaleHeight:scaleWidth;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap  newbm = Bitmap.createBitmap(b, 0, 0, oldWidth, oldHeight, matrix,true);
//
//		ImageView tv = new ImageView(this);
//		tv.setImageBitmap(newbm);
		return newbm;
	}
	/**
	 * 将字符串转换成Bitmap类型
	 * @param string
	 * @return
	 */
	public static  Bitmap stringtoBitmap(String string) {  
		Bitmap bitmap = null;  
		try {  
			byte[] bitmapArray;  
			bitmapArray = Base64.decode(string, Base64.DEFAULT);  
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,  
					bitmapArray.length);  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return bitmap;  

	} 
	/**
	 * 将Bitmap转换成字符串  
	 * @param bitmap
	 * @return
	 */
	public static String bitmaptoString(Bitmap bitmap) {  
		String string = null;  
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();  
		bitmap.compress(CompressFormat.PNG, 100, bStream);  
		byte[] bytes = bStream.toByteArray();
//		string = Base64.encodeToString(bytes, Base64.URL_SAFE);
		// 对字节数组Base64编码
//		BASE64Encoder encoder = new BASE64Encoder();
//		return encoder.encode(bytes);// 返回Base64编码过的字节数组字符串
		return encode(bytes);
//		return string;  
	}
	private static final char[] base64EncodeChars = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
	 
    private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };
 
	 /**
     * 将字节数组编码为字符串
     * 
     * @param data
     */
    public static String encode(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
 
        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }
    /**
     * 将base64字符串解码为字节数组
     * 
     * @param str
     */
    public static byte[] decode(String str) {
        byte[] data = str.getBytes();
        int len = data.length;
        ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
        int i = 0;
        int b1, b2, b3, b4;
 
        while (i < len) {
 
            /* b1 */
            do {
                b1 = base64DecodeChars[data[i++]];
            } while (i < len && b1 == -1);
            if (b1 == -1) {
                break;
            }
 
            /* b2 */
            do {
                b2 = base64DecodeChars[data[i++]];
            } while (i < len && b2 == -1);
            if (b2 == -1) {
                break;
            }
            buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));
 
            /* b3 */
            do {
                b3 = data[i++];
                if (b3 == 61) {
                    return buf.toByteArray();
                }
                b3 = base64DecodeChars[b3];
            } while (i < len && b3 == -1);
            if (b3 == -1) {
                break;
            }
            buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
 
            /* b4 */
            do {
                b4 = data[i++];
                if (b4 == 61) {
                    return buf.toByteArray();
                }
                b4 = base64DecodeChars[b4];
            } while (i < len && b4 == -1);
            if (b4 == -1) {
                break;
            }
            buf.write((int) (((b3 & 0x03) << 6) | b4));
        }
        return buf.toByteArray();
    }
}
