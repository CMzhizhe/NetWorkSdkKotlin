package com.gxx.networksdkkotlin.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AssetsHttpConfigRead {
    /**
     * 读取assets目录下的json文件
     *
     * @param context  上下文
     * @param fileName 文件名称
     * @return
     */
    public static String readHttpConfig(Context context, String fileName) {
        StringBuilder builder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets(); // 获得assets资源管理器（assets中的文件无法直接访问，可以使用AssetManager访问）
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(fileName), "UTF-8"); // 使用IO流读取json文件内容
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

}
