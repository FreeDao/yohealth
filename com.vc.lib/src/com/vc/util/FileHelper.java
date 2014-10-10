package com.vc.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.R.integer;
import android.graphics.Bitmap;
import android.util.Base64;

public class FileHelper {
	/**
	 * <p>将文件转成base64 字符串</p>
	 * @param path 文件路径
	 * @return
	 * @throws Exception
	 */
	public static String encodeBase64File(String path)  {
		Bitmap bitmap = null;
		
        try {
        	File file= new File(path);
        	FileInputStream stream = new FileInputStream(file);
            
            byte[] buffer = new byte[(int)file.length()+100];
            int length = stream.read(buffer);
            return Base64.encodeToString(buffer, 0,length,Base64.DEFAULT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        } finally {
           
        }
	}
	/**
	 * <p>将文件转成base64 字符串</p>
	 * @param path 文件路径
	 * @return
	 * @throws Exception
	 */
	public static byte[] encodeByteFile(String path)  {
		Bitmap bitmap = null;
		
        try {
        	File file= new File(path);
        	FileInputStream stream = new FileInputStream(file);
            
            byte[] buffer = new byte[(int)file.length()+100];
            int length = stream.read(buffer);
            stream.close();
            return buffer;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        } finally {
           
        }
	}
	
	public static String BytetoStreamString(byte[] bytes)
	{
		try {
			//byte[] buffer = new byte[(int)file.length()+100];
           // int length = stream.read(bytes);
            return Base64.encodeToString(bytes, 0,bytes.length,Base64.DEFAULT);
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}
	

}
