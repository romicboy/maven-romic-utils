package com.romic.utils;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class HttpTest {

	@Test
	public void testAjax() {
		Http http = new Http();
//		String html = http.ajax("http://h5.romic.meimi.me/test/index.html");
//		String html = http.ajax("http://h5.romic.meimi.me/index/test3.html");
		String html = http.ajax("http://m.weibo.cn/u/3757006010");
		System.out.println(html);
	}

	/**
	 * 
	 */
	@Test
	public void testGetString() {
		Http http = new Http();
//		String html = http.get("http://weixinshu.com/app/preview/21058/cover?bookType=wbbook&author=2812694710&source=meimi_app");
		String html = http.get("http://m.weibo.cn/u/3757006010");
		if (html.equals("")) {
			fail("Not yet implemented");
		}
		Loger.info("html", html);
//		try {
//			String compress = ZipUtil.compress(html);
//			System.out.println(html.length());
//			System.out.println(compress.length());
//			String unCompress = ZipStrUtil.unCompress(compress);
//			System.out.println(unCompress);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	@Test
	public void testGetStringListOfNameValuePair() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "123");Http http = new Http();
		String html = http.get("http://h5.romic.meimi.me/test/index.html", params);
		if (html.equals("")) {
			fail("Not yet implemented");
		}
		Loger.info("html", html);
	}

	@Test
	public void testGetStringListOfNameValuePairMapOfStringString() throws IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "123");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("version", "1.3");
		Http http = new Http();
		String html = http.get("http://h5.romic.meimi.me/test/index.html", params, headers);
		if (html.equals("")) {
			fail("Not yet implemented");
		}
		Loger.info("html", html);
	}

	@Test
	public void testPostString() {
		Http http = new Http();
		String html = http.post("http://m.mei.me/test/index.html");
		if (html.equals("")) {
			fail("Not yet implemented");
		}
		Loger.info("html", html);
	}

	@Test
	public void testPostStringListOfNameValuePair() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("url", "http://tool.chinaz.com/map.aspx#fz");
		Http http = new Http();
		String html = http.post("http://dwz.cn/create.php", params);
		if (html.equals("")) {
			fail("Not yet implemented");
		}
		Loger.info("html", html);
	}

	@Test
	public void testPostStringListOfNameValuePairMapOfStringString() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "123");
		params.put("name", "xiaoming");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("version", "1.4");
		Http http = new Http();
		String html = http.post("http://m.mei.me/test/index.html", params, headers);
		if (html.equals("")) {
			fail("Not yet implemented");
		}
		Loger.info("html", html);
	}

}
