package com.ssm.common.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClient 工具类
 */
public class HttpClientUtil {
    private static final Logger LGR = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * get 请求
     *
     * @param url   地址
     * @param param 参数 集合
     * @return
     */
    public static String doGet(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            LGR.error("HttpClient Get 错误", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                LGR.error("HttpClient Get 错误", e);
            }
        }
        return resultString;
    }

    /**
     * get 请求
     *
     * @param url 拼接好的 url
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, null);
    }

    /**
     * Post 请求  模拟 from 表单
     *
     * @param url   地址
     * @param param 参数集合
     * @return
     */
    public static String doPost(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "utf-8");
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            LGR.error("HttpClient Post 错误", e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                LGR.error("HttpClient Post 错误", e);
            }
        }

        return resultString;
    }

    /**
     * post 请求，RequestBody 形式
     *
     * @param url
     * @param json 数据
     * @return
     */
    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            LGR.error("HttpClient Post Json 错误", e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                LGR.error("HttpClient Post Json 错误", e);
            }
        }
        return resultString;
    }

    /**
     * 下载文件
     *
     * @param url 地址
     * @param ots 文件保存的流
     */
    public static void downloadFile(String url, FileOutputStream ots) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                response.getEntity().writeTo(ots); // 保存文件
            }
        } catch (Exception e) {
            LGR.error("HttpClient DownloadFile 错误", e);
        } finally {
            HttpClientUtil.closeHttpResponse(response, httpclient);
        }
    }

    /**
     * 获取http 文件的输入流
     *
     * @param url
     * @return
     */
    public static InputStream getFileInputStream(String url) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return response.getEntity().getContent();
            }
        } catch (Exception e) {
            LGR.error("HttpClient DownloadFile 错误", e);
        } finally {
            HttpClientUtil.closeHttpResponse(response, httpclient);
        }
        return null;
    }

    private static void closeHttpResponse(CloseableHttpResponse response, CloseableHttpClient httpclient) {
        try {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        } catch (IOException e) {
            LGR.error("HttpClient DownloadFile 错误", e);
        }
    }

}
