package com.yucei.admin.common.utils;

/**
 * @author wyong
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2018/10/9
 */

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;

@Slf4j
@SuppressWarnings("unused")
public class Posturl {

    static int Timeout = 30000;

    private static void getRequestconfig(HttpGet httpget,int second){
        //配置请求的超时设置
        second = second>Timeout ? second : Timeout;
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(second)
                .setConnectTimeout(second)
                .setSocketTimeout(second).build();
        httpget.setConfig(requestConfig);
    }
    private static void postRequestconfig(HttpPost httppost,int second){
        //配置请求的超时设置
        second = second > Timeout ? second : Timeout;
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(second)
                .setConnectTimeout(second)
                .setSocketTimeout(second).build();
        httppost.setConfig(requestConfig);
    }
    private static void postRequestconfig(HttpPost httppost){
        //配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout)
                .setConnectTimeout(Timeout)
                .setSocketTimeout(Timeout).build();
        httppost.setConfig(requestConfig);
    }
    public static String getRequest(String url) {
        StringBuilder sb = new StringBuilder();
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(url);
        getRequestconfig(httpget,10000);

        try{
            CloseableHttpResponse response = httpclient.execute(httpget);
            int statusOk = response.getStatusLine().getStatusCode();
            if(log.isInfoEnabled()){
                log.info(url+"result-statusCode=>>"+statusOk);
            }
            if(statusOk == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                BufferedInputStream instream = new BufferedInputStream(entity.getContent());
                byte[] chars = new byte[2048];
                int len=0;
                while((len=instream.read(chars))!=-1){
                    sb.append(new String(chars,0,len));
                }
            }else{
                log.error(url+"result->网络异常.."+statusOk);
            }
            if(log.isInfoEnabled()){
                log.info(url+"-result->"+sb.toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            httpget.releaseConnection();
            try {
                httpclient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * @param url
     * @param data
     * @param encode
     *            指定头编码
     * @param second 指定超时
     * @return
     */
    public static String postRequest(String url, Map<String, Object> data,
                                     String encode,int second) {
        if(StringUtils.isBlank(encode)){
            encode = "utf8";
        }
        StringBuilder sb = new StringBuilder();
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(url);
        postRequestconfig(httppost,second);
        try{
            if (data != null) {
                List<NameValuePair> dataary = new ArrayList<NameValuePair>();
                for (String mapKey : data.keySet()) {
                    // 填入各个表单域的值
                    String vString = data.get(mapKey) == null ? "" : data.get(
                            mapKey).toString();
                    NameValuePair data1 = new BasicNameValuePair(mapKey, vString);
                    dataary.add(data1);
                }
                // 将表单的值放入postMethod中
                httppost.setEntity(new UrlEncodedFormEntity(dataary,encode));
            }
            CloseableHttpResponse response = httpclient.execute(httppost);
            int statusOk = response.getStatusLine().getStatusCode();
            if(log.isInfoEnabled()){
                log.info(url+"result-statusCode=>>"+statusOk);
            }
            if(statusOk == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                BufferedInputStream instream = new BufferedInputStream(entity.getContent());
                byte[] chars = new byte[2048];
                int len=0;
                while((len=instream.read(chars))!=-1){
                    sb.append(new String(chars,0,len,encode));
                }
                instream.close();
                response.close();
            }else{
                log.error(url+"result->网络异常.."+statusOk);
            }
            if(log.isInfoEnabled()){
                log.info(url+"--result->"+sb.toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            httppost.releaseConnection();
            try {
                httpclient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    /**
     * @param url
     * @param data
     * @param encode
     *            指定头编码
     * @return
     */
    public static String postRequest(String url, Map<String, Object> data,
                                     String encode) {
        if(StringUtils.isBlank(encode)){
            encode = "utf8";
        }
        StringBuilder sb = new StringBuilder();
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(url);
        postRequestconfig(httppost);
        try{
            if (data != null) {
                List<NameValuePair> dataary = new ArrayList<NameValuePair>();
                for (String mapKey : data.keySet()) {
                    // 填入各个表单域的值
                    String vString = data.get(mapKey) == null ? "" : data.get(mapKey).toString();
                    NameValuePair data1 = new BasicNameValuePair(mapKey, vString);
                    dataary.add(data1);
                }
                // 将表单的值放入postMethod中
                httppost.setEntity(new UrlEncodedFormEntity(dataary,encode));
            }
            CloseableHttpResponse response = httpclient.execute(httppost);
            int statusOk = response.getStatusLine().getStatusCode();
            if(log.isInfoEnabled()){
                log.info(url+"result-statusCode=>>"+statusOk);
            }
//	        Header[] headers = response.getAllHeaders();
//            for(Header h : headers){
//                log.error("header->"+h.getName()+","+h.getValue());
//            }
//             if(statusOk == HttpStatus.SC_OK){
            HttpEntity entity = response.getEntity();
            BufferedInputStream instream = new BufferedInputStream(entity.getContent());
            byte[] chars = new byte[2048];
            int len=0;
            while((len=instream.read(chars))!=-1){
                sb.append(new String(chars,0,len,encode));
            }
            instream.close();
            response.close();
//	        }else{
//	        	log.error(url+"result->网络异常.."+statusOk);
//
//	        }
            if(log.isInfoEnabled()){
                log.info(url+"result->"+sb.toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            httppost.releaseConnection();
            try {
                httpclient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    //
    public static String postRequest(String url, Map<String, Object> data) {
        return postRequest(url, data, "utf8");
    }
    public static String postRequest(String url, Map<String, Object> data,int second) {
        return postRequest(url, data, "utf8",second);
    }

    public static String postRequest(String url, Map<String, Object> data,
                                     Map<String, String> cookies, String domain,String encode) {
        if(StringUtils.isBlank(encode)){
            encode = "utf8";
        }
        StringBuilder sb = new StringBuilder();
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(url);
        postRequestconfig(httppost);

        try{
            if (data != null) {
                List<NameValuePair> dataary = new ArrayList<NameValuePair>();
                for (String mapKey : data.keySet()) {
                    // 填入各个表单域的值
                    String vString = data.get(mapKey) == null ? "" : data.get(
                            mapKey).toString();
                    NameValuePair data1 = new BasicNameValuePair(mapKey, vString);
                    dataary.add(data1);
                }
                // 将表单的值放入postMethod中
                httppost.setEntity(new UrlEncodedFormEntity(dataary,encode));
            }
            if(cookies!=null && !cookies.isEmpty()){
                String cookie = "";
                for (String cookieName : cookies.keySet()) {
                    //Cookie c = new Cookie(domain, cookieName,cookies.get(cookieName), "/", -1, true);
                    cookie = cookieName+"="+cookies.get(cookieName)+",";
                }
                cookie = "path=/";
                if(StringUtils.isNotBlank(domain)){
                    cookie = cookie+",domain="+domain;
                }
//				if(cookie.endsWith(",")){
//					cookie = cookie.substring(0, cookie.length()-1);
//				}
                httppost.addHeader(new BasicHeader("Cookie",cookie));
            }
            CloseableHttpResponse response = httpclient.execute(httppost);
            int statusOk = response.getStatusLine().getStatusCode();
            if(log.isInfoEnabled()){
                log.info(url+"result-statusCode=>>"+statusOk);
            }
            if(statusOk == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                BufferedInputStream instream = new BufferedInputStream(entity.getContent());
                byte[] chars = new byte[2048];
                int len=0;
                while((len=instream.read(chars))!=-1){
                    sb.append(new String(chars,0,len,encode));
                }
            }else{
                log.error(url+"result->网络异常.."+statusOk);
            }
            if(log.isInfoEnabled()){
                log.info(url+"result->"+sb.toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            httppost.releaseConnection();
            try {
                httpclient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return sb.toString();

    }
    public static String postRequest(String url, Map<String, Object> data,
                                     Map<String, String> cookies,Map<String,String> header, String domain,String encode) {
        if(StringUtils.isBlank(encode)){
            encode = "utf8";
        }
        StringBuilder sb = new StringBuilder();
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(url);
        postRequestconfig(httppost);

        try{
            if (data != null) {
                List<NameValuePair> dataary = new ArrayList<NameValuePair>();
                for (String mapKey : data.keySet()) {
                    // 填入各个表单域的值
                    String vString = data.get(mapKey) == null ? "" : data.get(
                            mapKey).toString();
                    NameValuePair data1 = new BasicNameValuePair(mapKey, vString);
                    dataary.add(data1);
                }
                // 将表单的值放入postMethod中
                httppost.setEntity(new UrlEncodedFormEntity(dataary,encode));
            }
            if(cookies!=null && !cookies.isEmpty()){
                String cookie = "";
                for (String cookieName : cookies.keySet()) {
                    //Cookie c = new Cookie(domain, cookieName,cookies.get(cookieName), "/", -1, true);
                    cookie = cookieName+"="+cookies.get(cookieName)+",";
                }
                cookie = "path=/";
                if(StringUtils.isNotBlank(domain)){
                    cookie = cookie+",domain="+domain;
                }
//				if(cookie.endsWith(",")){
//					cookie = cookie.substring(0, cookie.length()-1);
//				}
                httppost.addHeader(new BasicHeader("Cookie",cookie));
                if(header!=null && !header.isEmpty()){
                    Iterator<String> it = header.keySet().iterator();
                    while(it.hasNext()){
                        String key = it.next();
                        String dataHeader = header.get(key);
                        httppost.addHeader(new BasicHeader(key,dataHeader));
                    }
                }
            }
            CloseableHttpResponse response = httpclient.execute(httppost);
            int statusOk = response.getStatusLine().getStatusCode();
            if(log.isInfoEnabled()){
                log.info(url+"result-statusCode=>>"+statusOk);
            }
            if(statusOk == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                BufferedInputStream instream = new BufferedInputStream(entity.getContent());
                byte[] chars = new byte[2048];
                int len=0;
                while((len=instream.read(chars))!=-1){
                    sb.append(new String(chars,0,len,encode));
                }
            }else{
                log.error(url+"result->网络异常.."+statusOk);
            }
            if(log.isInfoEnabled()){
                log.info(url+"result->"+sb.toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            httppost.releaseConnection();
            try {
                httpclient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return sb.toString();

    }
    public static String postRequestJson(String url,Map<String,String> mp,String contentType,String charset){
        if (StringUtils.isBlank(charset)) {
            charset = "UTF-8";
        }
        if (StringUtils.isBlank(contentType)) {
            contentType = "application/json;charset=" + charset;
        }
        StringBuilder sb = new StringBuilder();
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(url);
        postRequestconfig(httppost);
        httppost.addHeader("Content-Type", contentType);
        try{
            if(mp!=null && mp.size()>0){
                httppost.setEntity(new StringEntity(JSON.toJSONString(mp),charset));
            }
            CloseableHttpResponse response = httpclient.execute(httppost);
            int statusOk = response.getStatusLine().getStatusCode();
            if(log.isInfoEnabled()){
                log.info(url+"result-statusCode=>>"+statusOk);
            }
            if(statusOk == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                BufferedInputStream instream = new BufferedInputStream(entity.getContent());
                byte[] chars = new byte[2048];
                int len=0;
                while((len=instream.read(chars))!=-1){
                    sb.append(new String(chars,0,len,charset));
                }
            }else{
                log.error(url+"result->网络异常.."+statusOk);
            }
            log.error(url+"result->"+sb.toString());
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            httppost.releaseConnection();
            try {
                httpclient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    public static String postRequestJson(String url,String jsonstring,String contentType,String charset){
        if (StringUtils.isBlank(charset)) {
            charset = "UTF8";
        }
        if (StringUtils.isBlank(contentType)) {
            contentType = "application/json;charset=" + charset;
        }
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(url);
        postRequestconfig(httppost);
        httppost.addHeader("Content-Type", contentType);
        String resultstring = "";
        StringBuilder sb = new StringBuilder();
        try {
            if(StringUtils.isNotBlank(jsonstring)){
                httppost.setEntity(new StringEntity(jsonstring,  ContentType.create(contentType,  Charset.forName(charset))));
            }
            CloseableHttpResponse response = httpclient.execute(httppost);
            int statusOk = response.getStatusLine().getStatusCode();
            if(statusOk == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                BufferedInputStream instream = new BufferedInputStream(entity.getContent());
                byte[] chars = new byte[2048];
                int len=0;
                while((len=instream.read(chars))!=-1){
                    sb.append(new String(chars,0,len,charset));
                }
            }else{
                log.error(url+"result->网络异常.."+statusOk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public static String postRequest(String url, String xmlstring,String contentType, String charset) {
        if (StringUtils.isBlank(charset)) {
            charset = "UTF8";
        }
        if (StringUtils.isBlank(contentType)) {
            contentType = "text/html;charset=" + charset;
        }
        StringBuilder sb = new StringBuilder();
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(url);
        postRequestconfig(httppost);
        httppost.addHeader("Content-Type", contentType);
        try{
            if(StringUtils.isNotBlank(xmlstring)){
                httppost.setEntity(new StringEntity(xmlstring,ContentType.create(contentType,Charset.forName(charset))));
            }
            CloseableHttpResponse response = httpclient.execute(httppost);
            int statusOk = response.getStatusLine().getStatusCode();
            if(log.isInfoEnabled()){
                log.info(url+"result-statusCode=>>"+statusOk);
            }
            if(statusOk == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                BufferedInputStream instream = new BufferedInputStream(entity.getContent());
                byte[] chars = new byte[2048];
                int len=0;
                while((len=instream.read(chars))!=-1){
                    sb.append(new String(chars,0,len,charset));
                }
            }else{
                log.error(url+"result->网络异常.."+statusOk);
            }
            log.error(url+"result->"+sb.toString());
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            httppost.releaseConnection();
            try {
                httpclient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    public static Map<String,String> postRequest2Map(String url, String xmlstring,String contentType, String charset) {
        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity entityParams = new StringEntity(xmlstring,"utf-8");
            httpPost.setEntity(entityParams);
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if(response != null && response.getEntity() != null){
                return XMLUtil.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
            }
        }catch(Exception e) {
            return new HashMap<String,String>();
        }
        return new HashMap<String,String>();
    }
    public static void getFile(String fileurl, String destFileName)
            throws ClientProtocolException, IOException {
        // 生成一个httpclient对象
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(fileurl);
        getRequestconfig(httpget,10000);

        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();
        File file = new File(destFileName);
        try {
            FileOutputStream fout = new FileOutputStream(file);
            int l = -1;
            byte[] tmp = new byte[1024];
            while ((l = in.read(tmp)) != -1) {
                fout.write(tmp, 0, l);
                // 注意这里如果用OutputStream.write(buff)的话，图片会失真，大家可以试试
            }
            fout.flush();
            fout.close();
        } finally {
            // 关闭低层流。
            in.close();
        }
        httpclient.close();
    }
    /**
     * 微信客服消息，上传文件接口使用
     * @param url 上传路径
     * @param file 上传文件url地址
     * @param filename 对应的上传文件名
     * @return
     */
    public static String postfile(String url,String file,String filename){
        StringBuilder sb = new StringBuilder();
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httppost = new HttpPost(url);
        postRequestconfig(httppost);

        try {
            HttpEntity entityfile = MultipartEntityBuilder.create()
                    .addBinaryBody("file", new File(file), ContentType.DEFAULT_BINARY, file).addTextBody("filename",filename)
                    .build();
            httppost.setEntity(entityfile);
            HttpResponse response = httpclient.execute(httppost);
            int statusOk = response.getStatusLine().getStatusCode();
            if(log.isInfoEnabled()){
                log.info(url+"result-statusCode=>>"+statusOk);
            }
            if(statusOk == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                BufferedInputStream instream = new BufferedInputStream(entity.getContent());
                byte[] chars = new byte[2048];
                int len=0;
                while((len=instream.read(chars))!=-1){
                    sb.append(new String(chars,0,len));
                }
            }else{
                log.error(url+"result->网络异常.."+statusOk);
            }
            if(log.isInfoEnabled()){
                log.info(url+"result->"+sb.toString());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[] { trustManager }, null);
        return sc;
    }

    public static String postsend(String url,Map<String,Object> map){
        return postsend(url,map,"UTF-8");
    }
    public static String postsend(String url, Map<String,Object> map,String encoding)  {
        String body = "";
        try {
            //采用绕过验证的方式处理https请求
            SSLContext sslcontext = createIgnoreVerifySSL();

            // 设置协议http和https对应的处理socket链接工厂的对象
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslcontext))
                    .build();
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            HttpClients.custom().setConnectionManager(connManager);

            //创建自定义的httpclient对象
            CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();

            //创建post方式请求对象
            HttpPost httpPost = new HttpPost(url);

            //装填参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if(map!=null){
                for (Entry<String, Object> entry : map.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
            }
            //设置参数到请求对象中
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));

            System.out.println("请求地址："+url);
            System.out.println("请求参数："+nvps.toString());

            //设置header信息
            //指定报文头【Content-type】、【User-Agent】
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            //执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = client.execute(httpPost);
            //获取结果实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity, encoding);
            }
            EntityUtils.consume(entity);
            //释放链接
            response.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return body;
    }
    public static void postresult() {

    }
    public static void main(String[] args) {
        String s = "https://cash.yeepay.com/cashier/std?sign=Hm3WxbEXrxR6mjlWC5ce7AGUoQMxeMhpzwvXXmjBRa8D7SZv72S4_JZvF66hB7gUQJCX97JZFlSklS6JN63T3TwZccNH5ImtZgPGLQg4Rd8m6PZNQ7iqLW56XcrQxFV7NLKg61jHken-02-1bzqRqarcSCKUV10QH8-MHdlSpmvm6pS7dk-swqWiPcSUCDvZXGXAfMMmIODhvkNUOp9BvmXX0L8XrHDPDIeLTxax5dRNYs8pf2Nm4ujgh3kxismvyKEoWoLa_OcNEAUJ7-uFTAWFQRfRyrpiqxVJnCp0RKmkyHxAIKUbUbzs_c5mpsog3Lu8rHpK3WguGlMpDKycfg$SHA256&merchantNo=10020645134&token=BD8C72CECA267E648733CC2EF0838E411596F824FE914D665B20F6E6F300C943&timestamp=1523968331&directPayType=&cardType=&userNo=8a80d8ef62be16660162c321cd8d0009&userType=IMEI&ext={\"appId\":\"\",\"openId\":\"\",\"clientId\":\"\"}";
        System.out.println(StringUtils.replace(s, "\\", ""));

    }
}

