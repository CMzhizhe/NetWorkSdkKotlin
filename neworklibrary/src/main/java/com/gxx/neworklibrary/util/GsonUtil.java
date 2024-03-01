package  com.gxx.neworklibrary.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GsonUtil {
    private static Gson gson = new Gson();

    private GsonUtil() {
    }

    public static <T> T fromJson(String json, Type type){
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String objToJson(Object object) {
        try {
            return gson.toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String mapToJson(Map<String, Object> map) {
        try {
            return gson.toJson(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String listToJson(List<? extends Object> list) {
        try {
            return gson.toJson(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static <T> T jsonToObj(String json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转成list
     * 解决泛型在编译期类型被擦除导致报错
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }

    /**
     * 转成list中有map的
     *
     * @param json
     * @return
     */
    public static <T> List<Map<String, T>> jsonToListMaps(String json) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(json,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * 读取本地JSON
     *
     * @param context
     * @param fileName .JSON文件名称
     * @return
     */
    public static String getLocalJson(Context context, String fileName) {
        StringBuilder stringBuffer = new StringBuilder();
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String str = null;
            while ((str = br.readLine()) != null) {
                stringBuffer.append(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }

    /**
     * 根据KEY获取值
     *
     * @param json
     * @param key
     * @return
     */
    public static String getJsonArrayStr(String json, String key) {
        String result = "";
        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement element = jsonParser.parse(json);
            JsonObject jsonObject = element.getAsJsonObject();
            result = jsonObject.get(key).getAsJsonArray().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据KEY获取值
     *
     * @param json
     * @param key
     * @return
     */
    public static String getValueForKey(String json, String key) {
        String result = "";
        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement element = jsonParser.parse(json);
            JsonObject jsonObject = element.getAsJsonObject();
            result = jsonObject.get(key).getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T> List<T> fromJsonFile(Context context, String fileName, Class<T> clazz) {
        String jsonStr = getLocalJson(context, fileName);
        return jsonToList(jsonStr, clazz);
    }
}
