package com.gxx.neworklibrary.util;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @date: 2019/7/19 0019
 * @author: gaoxiaoxiong
 * @description:文字，图片都转body的工具类
 **/
public class MultipartBodyUtils {


    /**
     * @date: 2019/7/19 0019
     * @author: gaoxiaoxiong
     * @description:将文字装到body里面
     * @keyStr 为参数的名字
     * @value 为值
     * return MultipartBody.Part
     **/
    public MultipartBody.Part toRequestBodyOfText(String keyStr, String value) {
        return MultipartBody.Part.createFormData(keyStr, value);
    }

    /**
     * @date: 2019/7/19 0019
     * @author: gaoxiaoxiong
     * @description:文字放到body里面 return RequestBody
     **/
    public RequestBody toRequestBodyOfText(String value) {
        RequestBody body = RequestBody.create(value, MediaType.parse("text/plain"));
        return body;
    }


    /**
     * @date: 2019/7/19 0019
     * @author: gaoxiaoxiong
     * @description:将图片放到body里面
     * @keyStr 为参数的名字
     * @pFile 为值
     **/
    public MultipartBody.Part toRequestBodyOfImages(String keyStr, File pFile) {
        RequestBody requestBody = RequestBody.create(pFile, MediaType.parse("multipart/form-data"));
        return MultipartBody.Part.createFormData(keyStr, pFile.getName(), requestBody);
    }


    /**
     * @date: 2019/7/19 0019
     * @author: gaoxiaoxiong
     * @description:多图片上传
     * @keyStr 作为上传的名字name，多图上传需要加上[]  比如后台需要名字为files的，我们这里的keyStr要为files[]
     **/
    public List<MultipartBody.Part> ImagesToMultipartBodyParts(String keyStr, List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
            MultipartBody.Part part = MultipartBody.Part.createFormData(keyStr, file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }


    /**
     * @date 创建时间: 2022/3/12
     * @auther gaoxiaoxiong
     * @description 将json转为RequestBody
     **/
    public RequestBody jsonToRequestBody(String json) {
        return RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
    }

}
