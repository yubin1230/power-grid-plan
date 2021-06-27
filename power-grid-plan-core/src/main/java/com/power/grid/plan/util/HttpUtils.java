package com.power.grid.plan.util;
//httpClient3
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http访问相关的工具类
 */
public class HttpUtils {

	private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

	private static final int TIME = 1000 * 10;


	/**
	 * get请求访问url
	 * @param url
	 * @param codec
	 */
	public static String hpptGetRequest(String url,String codec)
	{
		if(StringUtils.isBlank(url)){
			log.warn("服务器url是空！");
			return null;
		}
		HttpClientParams httpClientParams = new HttpClientParams();
		httpClientParams.setConnectionManagerTimeout(TIME);
		httpClientParams.setSoTimeout(TIME);
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		HttpClient httpClient = new HttpClient(httpClientParams, connectionManager);
		GetMethod gettMethod = new GetMethod(url);
		gettMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
		try {
			gettMethod.addRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(gettMethod);
			if (statusCode == HttpStatus.SC_OK) {
				byte[] data = gettMethod.getResponseBody();
				return new String(data, codec);
			}
		} catch (Exception e) {
			log.error("调用服务时出错", e);
		} finally {
			gettMethod.releaseConnection();
		}

		return null;
	}
}
