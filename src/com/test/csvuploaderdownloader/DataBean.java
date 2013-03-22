package com.test.csvuploaderdownloader;

import java.util.List;

public class DataBean {
	private String testcase = null;
	private String result = null;
	private String message = null;

	// public DataBean() {
	// }
	// public DataBean(String testcase, String result, String message) {
	// super();
	// this.testcase = testcase;
	// this.result = result;
	// this.message = message;
	// this.sections = sections;
	// }
	public String getTestcase() {
		return testcase;
	}

	public void setTestcase(String testcase) {
		this.testcase = testcase;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toString() {
		return String.format("testcase:%s,result:%s,message:%s", testcase,
				result, message);
	}
}
