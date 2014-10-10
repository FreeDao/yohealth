package com.vc.cloudbalance.common;

public class SOHelper {
	static {
        System.loadLibrary("hello-jni");
    }
	 public native String getHealth(int sex, int age,int height, double weigth, int dz);
	 
}
