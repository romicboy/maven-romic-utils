package com.romic.utils;

public class Ip {

	private Http http = null;
	@SuppressWarnings("unused")
	private String url_taobao = "http://ip.taobao.com/service/getIpInfo.php?ip=";
	private String url_sina = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=";
	private String ip;

	public Ip(String ip) {
		this.ip = ip;
		http = new Http();
	}

	/**
	 * 获取ip地址信息
	 * @return
	 */
	public String getAddressInfo() {
		String string = http.get(getApiLink());
		return string;
	}

	private String getApiLink() {
		return url_sina + ip;
	}
}
