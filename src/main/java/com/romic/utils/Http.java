package com.romic.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.AllClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;

@SuppressWarnings("deprecation")
public class Http {

	private Charset charset = Charset.forName("UTF-8");
	
	public String ajax(String url) {
		File file = new File(System.getProperty("java.io.tmpdir") + "codes.js");
		if (!file.exists()) {
			try {
				file.createNewFile();
				FileOutputStream out = new FileOutputStream(file, true);
				out.write(
						"var system=require('system');var address=system.args[1];var page=require('webpage').create();page.open(address,function(status){if(status==='success'){console.log(page.content)}phantom.exit()});"
								.getBytes("utf-8"));
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Loger.info("method", "ajax");
		Loger.info("url", url);
		Runtime rt = Runtime.getRuntime();
		String html = "";
		try {
			Process p = rt.exec("phantomjs " + file.getAbsolutePath() + " " + url);
			html = IOUtils.toString(p.getInputStream(), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return html;
	}

	public String get(String url) {
		return this.get(url, null, null);
	}

	public String get(String url, Map<String, String> params) {
		return this.get(url, params, null);
	}

	@SuppressWarnings("resource")
	public String get(String url, Map<String, String> params, Map<String, String> headers) {
		Loger.info("method", "get");
		if (null != params) {
			Loger.info("params", params.toString());
			url += "?";
			Iterator<String> iterator = params.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				url += key + "=" + params.get(key);
			}
		}
		Loger.info("url", url);
		String html = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpRequestBase httpGet = new HttpGet(url);
		try {
			httpGet = addHeader(httpGet, headers);
			HttpResponse httpResponses = httpClient.execute(httpGet);
			html = responsetoString(httpResponses);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpGet.abort();
			httpClient.getConnectionManager().shutdown();
		}
		return html;
	}
	
	public String post(String url) {
		return this.post(url, new HashMap<String, String>(), null);
	}

	public String post(String url, Map<String, String> params) {
		return this.post(url, params, null);
	}

	@SuppressWarnings("resource")
	public String post(String url, Map<String, String> params, Map<String, String> headers) {
//		Loger.info("method", "post");
//		Loger.info("url", url);
		String html = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		try {
			if (null != params && !params.isEmpty()) {
//				Loger.info("params", params.toString());
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				Iterator<String> iterator = params.keySet().iterator();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					nameValuePairs.add(new BasicNameValuePair(key, params.get(key)));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, charset));
			}
			httpPost = (HttpPost) addHeader(httpPost, headers);
			HttpResponse httpResponses = httpClient.execute(httpPost);
			html = responsetoString(httpResponses);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpPost.abort();
			httpClient.getConnectionManager().shutdown();
		}
		return html;
	}
	
	public String post(String url, String content, Map<String, String> headers) {
		String html = null;
		try {
			URL localURL = new URL(url);
			HttpURLConnection httpURLConnection = (HttpURLConnection) localURL.openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setDoOutput(true);
			if (null != headers && !headers.isEmpty()) {
				Iterator<String> iterator = headers.keySet().iterator();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					String value = headers.get(key);
					httpURLConnection.setRequestProperty(key, value);
				}
			}
			httpURLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
			OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
			out.write(content);
			out.flush();
			out.close();
			InputStream inputStream = null;
			if (httpURLConnection.getResponseCode() != HttpStatus.SC_OK) {
				inputStream = httpURLConnection.getErrorStream();
			} else {
				inputStream = httpURLConnection.getInputStream();
			}
			if (null != httpURLConnection.getContentEncoding() && httpURLConnection.getContentEncoding().equals("gzip")) {
				inputStream = new GZIPInputStream(inputStream);
			}
			html = IOUtils.toString(inputStream,charset);
			IOUtils.closeQuietly(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return html;
	}

	/**
	 * 添加头信息到请求中
	 * 
	 * @param http
	 * @param headers
	 * @return
	 */
	private HttpRequestBase addHeader(HttpRequestBase http, Map<String, String> headers) {
		if (headers == null)
			headers = new HashMap<String, String>();
//		Loger.info("headers", headers.toString());
		if (!headers.containsKey("Referer"))
			headers.put("Referer", "http://webscan.360.cn/tools/dnslookup");
		if (!headers.containsKey("X-Requested-With"))
			headers.put("X-Requested-With", "XMLHttpRequest");
		if (!headers.containsKey("Content-Type"))
			headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		if (!headers.containsKey("Accept"))
			headers.put("Accept", "*/*");
		if (!headers.containsKey("Accept-Encoding"))
			headers.put("Accept-Encoding", "gzip, deflate, sdch");
		if (!headers.containsKey("Accept-Language"))
			headers.put("Accept-Language", "zh-cn,en-us;q=0.7,en;q=0.3");
		if (!headers.containsKey("Pragma"))
			headers.put("Pragma", "no-cache");
		if (!headers.containsKey("Cache-Control"))
			headers.put("Cache-Control", "no-cache");
		Iterator<String> iterator = headers.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = headers.get(key);
			http.addHeader(key, value);
		}
		return http;
	}

	/**
	 * 获得最终的地址（包括301或者302等跳转后的地址）
	 * @param from 原始地址
	 * @return 最终的地址
	 */
	@SuppressWarnings({ "resource" })
	public String getFinalURL(String from) {
		HttpClient client = new DefaultHttpClient();
		String to = "";
		HttpGet httpget = new HttpGet(from);
		HttpParams params = client.getParams();
		params.setParameter(AllClientPNames.HANDLE_REDIRECTS, false);
		try {
			HttpResponse response = client.execute(httpget);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 301 || statusCode == 302) {
				Header[] hs = response.getHeaders("Location");
				for (Header h : hs) {
					to = h.getValue();
				}
			}
		} catch (Exception e) {
			to = "";
		} finally {
			httpget.abort();
			client.getConnectionManager().shutdown();
		}
		return to;
	}

	/**
	 * HttpResponse转字符串
	 * @param httpResponses
	 * @return
	 */
	private String responsetoString(HttpResponse httpResponses) throws IOException {
		String html = null;
		if (httpResponses.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			Loger.info("Http Status Code", String.valueOf(httpResponses.getStatusLine().getStatusCode()));
		}
		HttpEntity entity = httpResponses.getEntity();
		InputStream inputStream = entity.getContent();
		Header contentEncoding = entity.getContentEncoding();
		if (null != contentEncoding && contentEncoding.getValue().equals("gzip")) {
			inputStream = new GZIPInputStream(inputStream);
		}
		html = IOUtils.toString(inputStream,charset);
		IOUtils.closeQuietly(inputStream);
		return html;
	}
	
	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}
}