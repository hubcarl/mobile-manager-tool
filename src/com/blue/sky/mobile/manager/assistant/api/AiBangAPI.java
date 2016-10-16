package com.blue.sky.mobile.manager.assistant.api;

/**
 * Created by Administrator on 2014/11/19.
 */
public class AiBangAPI {

    public final static String HOTS_URL = "http://openapi.aibang.com/bus";

    public final static String URL_KEY = "?app_key=e5cd91b8395f39a6db8ba4f243e93ccd&alt=json";

    public final static String PATH_URL = HOTS_URL + "/lines" + URL_KEY;

    public final static String STAT_URL = HOTS_URL + "/stats" + URL_KEY;

    public final static String TRANSFER_URL = HOTS_URL + "/transfer" + URL_KEY;
}
