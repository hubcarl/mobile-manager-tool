package com.blue.sky.common.component.service;

import java.io.Serializable;

import com.blue.sky.common.component.service.impl.ImageSDCardCache;

/**
 * File name rule, used when saving images in {@link ImageSDCardCache}
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-7-6
 */
public interface FileNameRule extends Serializable {

    /**
     * get file name, include suffix, it's optional to include folder.
     * 
     * @param imageUrl the url of image
     * @return
     */
    public String getFileName(String imageUrl);
}
