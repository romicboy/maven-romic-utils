package com.romic.utils;

import java.nio.charset.Charset;

public class Phone {

	private Http http = null;
	private String url = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=";
	private String number;
	
	public Phone(String number) {
		this.number = number;
		http = new Http();
		http.setCharset(Charset.forName("GB2312"));
	}
	
	/**
	 * 获取ip地址信息
	 * @return
	 */
	public String getInfo() {
		String string = http.get(getApiLink());
		return string;
	}

	private String getApiLink() {
		return url + number;
	}
	
}
