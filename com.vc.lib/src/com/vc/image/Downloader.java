package com.vc.image;

import java.io.OutputStream;

public interface Downloader  {
	
	/**
	 * ���������inputStream���outputStream
	 * @param urlString
	 * @param outputStream
	 * @return
	 */
	public boolean downloadToLocalStreamByUrl(String urlString, OutputStream outputStream);
}
