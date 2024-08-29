package com.gxx.neworklibrary.model

import com.google.gson.reflect.TypeToken
import java.util.LinkedHashMap
import java.util.TreeMap

class ToMapModel{
    //private var paramsList = mutableListOf<Any>()

    /**
      * 添加参数
      */
   /* fun addParam(any: Any){
        paramsList.add(any)
    }*/

    /**
      * 返回 linkedHashMap
      */
    /*fun toLinkedHashMap():LinkedHashMap<String,Any>{
        val linkedHashMap = LinkedHashMap<String,Any>();
        kotlin.runCatching {
            for (any in paramsList) {
                linkedHashMap.putAll(GsonUtil.fromJson(GsonUtil.objToJson(any),object : TypeToken<TreeMap<String, Any>>() {}.type))
            }
        }.onFailure {
            it.printStackTrace();
        }

        return  linkedHashMap
    }*/
}