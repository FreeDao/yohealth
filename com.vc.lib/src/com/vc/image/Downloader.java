package com.vc.image;

import java.io.OutputStream;

public interface Downloader  {
	
	/**
	 * ÇëÇóÍøÂçµÄinputStreamÌî³äoutputStream
	 * @param urlString
	 * @param outputStream
	 * @return
	 */
	public boolean downloadToLocalStreamByUrl(String urlString, OutputStream outputStream);
}
