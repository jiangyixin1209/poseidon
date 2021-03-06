package top.jiangyixin.poseidon.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * @author jiangyixin
 */
public class HttpUtils {
	private final static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	/**
	 * Post 请求超时时间和读取数据的超时时间均为2000ms。
	 *
	 * @param urlPath       post请求地址
	 * @param parameterData post请求参数
	 * @return String json字符串，成功：code=1001，否者为其他值
	 * @throws Exception 链接超市异常、参数url错误格式异常
	 */
	public static String doPost(String urlPath, String parameterData) throws Exception {
		// 避免null引起的空指针异常
		if (null == urlPath || null == parameterData) {
			return "";
		}
		URL localUrl = new URL(urlPath);
		URLConnection connection = localUrl.openConnection();
		HttpURLConnection httpUrlConnection = (HttpURLConnection) connection;

		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setRequestMethod("POST");
		httpUrlConnection.setRequestProperty("Accept-Charset", "utf-8");
		httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		httpUrlConnection.setRequestProperty("Content-Length", String.valueOf(parameterData.length()));
		httpUrlConnection.setConnectTimeout(18000);
		httpUrlConnection.setReadTimeout(18000);

		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuilder resultBuffer = new StringBuilder();
		String tempLine = null;

		try {
			outputStream = httpUrlConnection.getOutputStream();
			outputStreamWriter = new OutputStreamWriter(outputStream);

			outputStreamWriter.write(parameterData.toString());
			outputStreamWriter.flush();

			if (httpUrlConnection.getResponseCode() >= 300) {
				throw new Exception("HTTP Request is not success, Response code is " + httpUrlConnection.getResponseCode());
			}
			// 真正的发送请求到服务端
			inputStream = httpUrlConnection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			reader = new BufferedReader(inputStreamReader);

			while ((tempLine = reader.readLine()) != null) {
				resultBuffer.append(tempLine);
			}
		} finally {
			if (outputStreamWriter != null) {
				outputStreamWriter.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
			if (reader != null) {
				reader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return resultBuffer.toString();
	}

	public static String doPost(String url, Map<String, Object> params) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			sb.append(entry.getKey())
					.append("=")
					.append(entry.getValue())
					.append("&");
		}
		// no matter for the last '&' character
		return doPost(url, sb.toString());
	}

	/**
	 * 向指定URL发送GET方法的请求
	 *
	 * @param url   发送请求的URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		StringBuilder result = new StringBuilder();
		BufferedReader in = null;
		try {
			String urlNameString = url;
			if (!"".equals(param)) {
				urlNameString = urlNameString + "?" + param;
			}
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();

			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			logger.warn("发送GET请求出现异常！", e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				logger.warn("fail to close inputStream.", e2);
			}
		}
		return result.toString();
	}

	public static String sendGet(String url, Map<String, Object> params) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			sb.append(entry.getKey())
					.append("=")
					.append(entry.getValue())
					.append("&");
		}
		// no matter for the last '&' character
		return sendGet(url, sb.toString());
	}
}
