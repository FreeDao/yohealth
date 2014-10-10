package com.vc.util;

import android.util.Log;

public class LogUtils {
	private static final boolean DEBUG = true;
	public static final String TAG = "VC";

	public static void e(String message) {
		if (DEBUG) {
			StackTraceElement[] elements = Thread.currentThread()
					.getStackTrace();
			if (elements.length < 4) {
				Log.e(TAG, "Stack to shallow");
			} else {
				String fullClassName = elements[3].getClassName();
				String className = fullClassName.substring(fullClassName
						.lastIndexOf(".") + 1);
				String methodName = elements[3].getMethodName();
				int lineNumber = elements[3].getLineNumber();
				Log.e(TAG, message + " at " + className + "." + methodName
						+ "():" + lineNumber);
			}
		}
	}

	public static void d(String message) {
		if (DEBUG) {
			StackTraceElement[] elements = Thread.currentThread()
					.getStackTrace();
			if (elements.length < 3) {
				Log.e(TAG, "Stack to shallow");
			} else {
				String fullClassName = elements[3].getClassName();
				String className = fullClassName.substring(fullClassName
						.lastIndexOf(".") + 1);
				String methodName = elements[3].getMethodName();
				int lineNumber = elements[3].getLineNumber();
				Log.d(TAG, message + " at " + className + "." + methodName
						+ "():" + lineNumber);
			}
		}
	}

	public static void i(String message) {
		if (DEBUG) {
			StackTraceElement[] elements = Thread.currentThread()
					.getStackTrace();
			if (elements.length < 3) {
				Log.e(TAG, "Stack to shallow");
			} else {
				String fullClassName = elements[3].getClassName();
				String className = fullClassName.substring(fullClassName
						.lastIndexOf(".") + 1);
				String methodName = elements[3].getMethodName();
				int lineNumber = elements[3].getLineNumber();
				Log.i(TAG, message + " at " + className + "." + methodName
						+ "():" + lineNumber);
			}
		}
	}

	public static void w(String message) {
		if (DEBUG) {
			StackTraceElement[] elements = Thread.currentThread()
					.getStackTrace();
			if (elements.length < 3) {
				Log.e(TAG, "Stack to shallow");
			} else {
				String fullClassName = elements[3].getClassName();
				String className = fullClassName.substring(fullClassName
						.lastIndexOf(".") + 1);
				String methodName = elements[3].getMethodName();
				int lineNumber = elements[3].getLineNumber();
				Log.w(TAG, message + " at " + className + "." + methodName
						+ "():" + lineNumber);
			}
		}
	}

	public static void v(String message) {
		if (DEBUG) {
			StackTraceElement[] elements = Thread.currentThread()
					.getStackTrace();
			if (elements.length < 3) {
				Log.e(TAG, "Stack to shallow");
			} else {
				String fullClassName = elements[3].getClassName();
				String className = fullClassName.substring(fullClassName
						.lastIndexOf(".") + 1);
				String methodName = elements[3].getMethodName();
				int lineNumber = elements[3].getLineNumber();
				Log.v(TAG, message + " at " + className + "." + methodName
						+ "():" + lineNumber);
			}
		}
	}

}
