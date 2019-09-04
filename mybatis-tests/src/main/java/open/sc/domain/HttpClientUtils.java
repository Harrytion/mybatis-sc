package open.sc.domain;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLSocketFactory;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Jackrams
 *
 */
public class HttpClientUtils {
    private static PoolingHttpClientConnectionManager cm;  
    private static String EMPTY_STR = "";  
    private static String UTF_8 = "UTF-8";  
    private static ThreadLocal<HttpPost> t1=new ThreadLocal<HttpPost>();
    private static ThreadLocal<CloseableHttpClient> t2=new ThreadLocal<CloseableHttpClient>();
    private static void init() {  
        if (cm == null) {  
            cm = new PoolingHttpClientConnectionManager();
            // 整个连接池最大连接数
            cm.setMaxTotal(50);
            // 每路由最大连接数，默认值是2
            cm.setDefaultMaxPerRoute(5);
        }  
    }  
  
    /** 
     * 通过连接池获取HttpClient 
     *  
     * @return 
     */

    static {
        init();
    }
    private static CloseableHttpClient getHttpClient() {  
     //   init();
        return HttpClients.custom().setConnectionManager(cm).build();  
    }  
  
    /** 
     *  
     * @param url 
     * @return 
     */  
    public static String httpGetRequest(String url) throws Exception{
        HttpGet httpGet = new HttpGet(url);  
        return getResult(httpGet);  
    }
    public static String httpGetRequestGBK(String url) throws Exception{
        HttpGet httpGet = new HttpGet(url);
        return getResult(httpGet,Charset.forName("gbk"));
    }

    public static String httpGetRequesGBKtWithFirefox(String url) throws Exception{
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0");
        return getResult(httpGet,Charset.forName("gbk"));
    }
    public static String httpGetRequestWithFirefox(String url) throws Exception{
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0");
        return getResult(httpGet);
    }

    public static byte[] httpGetRequesGBKtWithFirefoxGetByteArray(String url) throws Exception{
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0");
        return getByteArrayResult(httpGet);
    }
    public static String httpGetRequestWithFirefoxWithNewClient(String url) throws Exception{
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0");
        return getResultWithNewClient(httpGet,Charset.defaultCharset());
    }


    public static String httpGetRequest(String url, Map<String, Object> params) throws Exception{
        URIBuilder ub = new URIBuilder();  
        ub.setPath(url);  
  
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);  
  
        HttpGet httpGet = new HttpGet(ub.build());  
        return getResult(httpGet);  
    }

    public static String httpPostRequestWithFirefox(String url)throws Exception{

        HttpPost post=new HttpPost(url);
        post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0");
        return getResult(post);

    }
    public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws Exception {
        URIBuilder ub = new URIBuilder();  
        ub.setPath(url);  
  
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);  
  
        HttpGet httpGet = new HttpGet(ub.build());  
        for (Map.Entry<String, Object> param : headers.entrySet()) {  
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));  
        }  
        return getResult(httpGet);  
    }  
  
    public static String httpPostRequest(String url) throws Exception { 
    	HttpPost post = t1.get();
		if(post==null){
			 post = new HttpPost(url);  
			 t1.set(post);
    	}
		post.setURI(new URI(url));
        return getResult(post);  
    }  
  
    public static String httpPostRequest(String url, Map<String, Object> params) throws  Exception {
    	HttpPost post = t1.get();
		if(post==null){
			 post = new HttpPost(url); 
			 t1.set(post);
    	}
		post.setURI(new URI(url));
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        post.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));  
        return getResult(post);  
    }  
  
    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws Exception {
        HttpPost httpPost = new HttpPost(url);  
        httpPost.setHeader("Accept", "application/json");
        for (Map.Entry<String, Object> param : headers.entrySet()) {  
        	httpPost.removeHeaders(param.getKey());
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));  
        }  
  
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));  
  
        return getResult(httpPost);  
    }  
  
    public static String httpPostRequest(String url, Map<String, Object> headers, String params)  
            throws Exception {
        HttpPost httpPost = new HttpPost(url);  
        httpPost.setHeader("Accept", "application/json");
        for (Map.Entry<String, Object> param : headers.entrySet()) {  
        	httpPost.removeHeaders(param.getKey());
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));  
        }  
  
         StringEntity pairs=new StringEntity(params, Charset.forName("utf8"));
        httpPost.setEntity(pairs);  
  
        return getResult(httpPost);  
    }  
    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> param : params.entrySet()) {  
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }  
  
        return pairs;  
    }  


    private static String getResult(HttpRequestBase request)throws Exception{
                return  getResult(request,Charset.defaultCharset());
    }
    /** 
     * 处理Http请求 
     *  
     * @param request 
     * @return 
     */  
    private static String getResult(HttpRequestBase request,Charset charset) throws Exception{
        // CloseableHttpClient httpClient = HttpClients.createDefault();  
        CloseableHttpClient httpClient = null;  
         httpClient = t2.get();
       //  System.out.println(httpClient);
         if(httpClient==null){
        	 httpClient=getHttpClient();
        	 t2.set(httpClient);
         }

       // System.out.println(httpClient);
        return getResultWithClient(httpClient,request,charset);
    }
    private static String getResultWithNewClient(HttpRequestBase request,Charset charset) throws Exception{

        CloseableHttpClient httpClient = t2.get();
        if(httpClient==null){
            httpClient=getHttpClient();
            t2.set(httpClient);
            System.out.println(Thread.currentThread());
        }
        return getResultWithClient(httpClient,request,charset);
    }


    public static String getResultWithClient(CloseableHttpClient httpClient,HttpRequestBase request ,Charset charset) throws Exception{
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            //   System.out.println(response.getStatusLine().getStatusCode());
       //     SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory()
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // long len = entity.getContentLength();// -1 表示长度未知
                String result = EntityUtils.toString(entity,charset);

                response.close();
                // httpClient.close();
                return result;
            }
        } catch (Exception e) {
            throw e;
        } finally {
        //
        }
        return EMPTY_STR;
    }
    private static byte[] getByteArrayResult(HttpRequestBase request) throws Exception{
        // CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = null;
        httpClient = t2.get();
        //  System.out.println(httpClient);
        if(httpClient==null){
            httpClient=getHttpClient();
            t2.set(httpClient);
        }
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            //   System.out.println(response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // long len = entity.getContentLength();// -1 表示长度未知
                //String result = EntityUtils(entity);
               byte[] result = EntityUtils.toByteArray(entity);

                response.close();
                // httpClient.close();
                return result;
            }
        } catch (Exception e) {
            throw e;
        } finally {

        }

        return EMPTY_STR.getBytes();
    }
}  