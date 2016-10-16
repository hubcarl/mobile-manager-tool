package com.blue.sky.common.component.dao;

import java.util.Map;

import com.blue.sky.common.component.entity.HttpResponse;

/**
 * HttpCacheDao
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-11-04
 */
public interface HttpCacheDao {

    /**
     * insert HttpResponse
     * 
     * @param httpResponse
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insertHttpResponse(HttpResponse httpResponse);

    /**
     * get HttpResponse by url
     * 
     * @param url
     * @return
     */
    public HttpResponse getHttpResponse(String url);

    /**
     * get HttpResponses by type
     * 
     * @param type
     * @return
     */
    public Map<String, HttpResponse> getHttpResponsesByType(int type);

    /**
     * delete all http response
     * 
     * @return
     */
    public int deleteAllHttpResponse();
}
