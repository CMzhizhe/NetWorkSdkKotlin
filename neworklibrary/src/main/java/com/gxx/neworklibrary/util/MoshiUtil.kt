package com.gxx.neworklibrary.util

import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


object MoshiUtil {

    abstract class MoshiTypeReference<T> // 自定义的类，用来包装泛型

    val moshi = Moshi.Builder()
        .add(MoshiArrayListJsonAdapter.FACTORY)
        .addLast(KotlinJsonAdapterFactory()).build()



    inline fun <reified T> toJson(src: T, indent: String = ""): String {
        try {
            val jsonAdapter = moshi.adapter<T>(getGenericType<T>())
            return jsonAdapter.indent(indent).toJson(src)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""

    }

    inline fun <reified T> fromJson(jsonStr: String): T? {
        try {
            val jsonAdapter = moshi.adapter<T>(getGenericType<T>())
            return jsonAdapter.fromJson(jsonStr)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    inline fun <reified T> getGenericType(): Type {
        val type =
            object :
                MoshiTypeReference<T>() {}::class.java
                .genericSuperclass
                .let { it as ParameterizedType }
                .actualTypeArguments
                .first()
        return type

    }

    abstract class MoshiArrayListJsonAdapter<C : MutableCollection<T>?, T> private constructor(
        private val elementAdapter: JsonAdapter<T>
    ) :
        JsonAdapter<C>() {
        abstract fun newCollection(): C

        @Throws(IOException::class)
        override fun fromJson(reader: JsonReader): C {
            val result = newCollection()
            reader.beginArray()
            while (reader.hasNext()) {
                result?.add(elementAdapter.fromJson(reader)!!)
            }
            reader.endArray()
            return result
        }

        @Throws(IOException::class)
        override fun toJson(writer: JsonWriter, value: C?) {
            writer.beginArray()
            for (element in value!!) {
                elementAdapter.toJson(writer, element)
            }
            writer.endArray()
        }

        override fun toString(): String {
            return "$elementAdapter.collection()"
        }

        companion object {
            val FACTORY = Factory { type, annotations, moshi ->
                val rawType = Types.getRawType(type)
                if (annotations.isNotEmpty()) return@Factory null
                if (rawType == ArrayList::class.java) {
                    return@Factory newArrayListAdapter<Any>(
                        type,
                        moshi
                    ).nullSafe()
                }
                null
            }

            private fun <T> newArrayListAdapter(
                type: Type,
                moshi: Moshi
            ): JsonAdapter<MutableCollection<T>> {
                val elementType =
                    Types.collectionElementType(
                        type,
                        MutableCollection::class.java
                    )

                val elementAdapter: JsonAdapter<T> = moshi.adapter(elementType)

                return object :
                    MoshiArrayListJsonAdapter<MutableCollection<T>, T>(elementAdapter) {
                    override fun newCollection(): MutableCollection<T> {
                        return ArrayList()
                    }
                }
            }
        }
    }

}

