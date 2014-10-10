package com.vc.image;
 
import android.net.Uri;


/*
 * ͼ����Ľӿڣ�������ͼ����Ӧ�еĹ���
 * */
public interface IImageView {
	
	
	/*
	 * ͼ������ٷ��� ����bitmap�Ļ��պͻ�������
	 * @param isClearCache �Ƿ��������
	 * */
	abstract void dispose(boolean isClearCache);
	
	/*
	 * ����ָ��������urlͼƬ
	 * @param url url Ĭ�ϻ�������
	 * */
	abstract void setImageUrl(String url);
	
	/*
	 * ����ָ��������urlͼƬ
	 * @param url url 
	 * @param ������ͼƬ
	 * */
	abstract void setImageUrlNotCache(String url);
	
	/*
	 * ����ָ��������urlͼƬ
	 * @param url url Ĭ�ϻ�������
	 * @param faileResid ����ʧ��ʱ��ʾ����ԴͼƬ
	 * */
	abstract void setImageUrl(String url,int faileResid);
	
	/*
	 * ����ָ������Դ
	 * @param res ��Դid 
	 * */
	abstract void loadRes(int res);
	
	/*
	 * ����᷵�ص�uri�м���ͼƬ
	 * @param uri uri
	 * */
	abstract void loadUri(Uri uri);
	
	/*
	 * ����᷵�ص�uri�м���ͼƬ
	 * @param fileName �ļ���
	 * */
	abstract void loadUri(String fileName);
	
	/*
	 * ָ��ͼƬ�Ƿ�˫ָ���� �Ŵ� ��С ���� �˹���Ĭ�� ��ԭͼ Ϊ��С �ֻ���Ļ Ϊ���  ������Χ
	 * @param isScale  
	 * */
	abstract void setScaleEnable(boolean isScale);
	
	
	/*
	 * ��ͼƬ�Ŵ�������Ļ���� ��С��ԭ����С
	 * @param isScale  
	 * */
	abstract void toggleFillScreen();
	
	
	/*
	 * �����������ͼƬʱȡ������ͼƬ���첽�߳�
	 * */	
	abstract void cancelLoadingTask();
	
	
	/*
	 * �ͻ�����Ⱦ
	 * @param res ��ͼ
	 * */
	abstract void clientRender(int res);

	/**
	* @Title: setImageUrlMemoryCache
	* @Description: TODO
	* @param @param url    
	* @return void    
	* @throws
	*/
	void setImageUrlMemoryCache(String url);
	
}
