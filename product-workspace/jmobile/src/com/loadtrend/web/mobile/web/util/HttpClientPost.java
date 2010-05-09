package com.loadtrend.web.mobile.web.util;


import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpClientPost {
	
    private HttpClient httpClient = new HttpClient();
    
    public boolean get(String url, StringBuffer resultString, String encoding) {
          // ����GET������ʵ��
          GetMethod getMethod = new GetMethod(url);
          //ʹ��ϵͳ�ṩ��Ĭ�ϵĻָ�����
          getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
            new DefaultHttpMethodRetryHandler());
          try {
           //ִ��getMethod
           int statusCode = httpClient.executeMethod(getMethod);
           if (statusCode != HttpStatus.SC_OK) {
               resultString.append(getMethod.getStatusLine());
               return false;
           }
           //��ȡ���� 
           String responseBody = getMethod.getResponseBodyAsString();
           resultString.append(new String(responseBody.getBytes("iso-8859-1"), encoding));
           //��������
           return true;
          } catch (HttpException e) {
           //�����������쳣��������Э�鲻�Ի��߷��ص�����������
           e.printStackTrace();
           resultString.append(e.getMessage());
          } catch (IOException e) {
           //���������쳣
           e.printStackTrace();
           resultString.append(e.getMessage());
          } catch (Exception e) {
           //���������쳣
           e.printStackTrace();
           resultString.append(e.getMessage());
          } finally {
           //�ͷ�����
           getMethod.releaseConnection();
          }
          return false;
    }
    
    public boolean get(String url, StringBuffer resultString) {
    	return this.get(url, resultString, "UTF-8");
    }
    
    public boolean post(String url, StringBuffer resultString) {
        return this.post(url, null, resultString, "UTF-8");
    }

    /**
     * Return true if post success, or false. Whether true or false, the resultString will be set the result.
     * @param url target url to post.
     * @param nameValuePairs name value pairs. Could be null.
     * @param resultString result string.
     * @return true or false.
     */
    public boolean post(String url, NameValuePair[] nameValuePairs, StringBuffer resultString, String encoding) {
        PostMethod postMethod = new PostMethod(url);
        if (nameValuePairs != null) postMethod.setRequestBody(nameValuePairs);
        try {
            int statusCode = this.httpClient.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                byte[] responseBody = postMethod.getResponseBody();
                resultString.append(new String(responseBody, encoding));
                return true;
            } else {
                // HttpClient����Ҫ����ܺ�̷����������POST��PUT�Ȳ����Զ�����ת��
                // 301����302
                if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || 
                    statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                    // ��ͷ��ȡ��ת��ĵ�ַ
                    Header locationHeader = postMethod.getResponseHeader("location");
                    String location = null;
                    if (locationHeader != null) {
                       location = locationHeader.getValue();
                       // The page was redirected to: location
                       resultString.append(location);
                       return true;
                    } else {
                       resultString.append("The page was redirected, but the location field value is null.");
                       return false;
                    }
                }
            }
            resultString.append(postMethod.getStatusLine());
            return false;
        } catch (HttpException e) {
            e.printStackTrace();
            resultString.append(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            resultString.append(e.getMessage());
        } finally {
            postMethod.releaseConnection();
        }
        return false;
    }
}
