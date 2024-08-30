package com.gxx.networksdkkotlin.activity

import java.util.Collections
import java.util.TreeMap

class test {
    fun testss() {
        val linkHashMap: MutableMap<String, String> = TreeMap()
        linkHashMap["a"] = "ddddd"
        linkHashMap["c"] = "bbbbb"
        linkHashMap["d"] = "aaaaa"
        linkHashMap["b"] = "ccccc"

        //这里将map.entrySet()转换成list
        val list: List<Map.Entry<String, String>> = ArrayList<Map.Entry<String, String>>(linkHashMap.entries)
        //然后通过比较器来实现排序
        Collections.sort(list) { o1, o2 ->
            //升序排序
            o1.key.compareTo(o2.key)
        }

        for ((key, value) in list) {
            println("$key:$value")
        }
    }
}
