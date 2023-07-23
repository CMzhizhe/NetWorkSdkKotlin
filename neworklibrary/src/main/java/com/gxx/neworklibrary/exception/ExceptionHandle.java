package com.gxx.neworklibrary.exception;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;

import retrofit2.HttpException;

public class ExceptionHandle {
     static final int UNAUTHORIZED = 401;
     static final int FORBIDDEN = 403;
     static final int NOT_FOUND = 404;
     static final int REQUEST_TIMEOUT = 408;
     static final int INTERNAL_SERVER_ERROR = 500;
     static final int BAD_GATEWAY = 502;
     static final int SERVICE_UNAVAILABLE = 503;
     static final int GATEWAY_TIMEOUT = 504;

    public static ResponeThrowable handleException(Throwable e) {
        ResponeThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            if (httpException.code() == INTERNAL_SERVER_ERROR){//服务器异常
                ex = new ResponeThrowable(e, String.valueOf(ERROR.HTTP_ERROR_500));
            }else{
                ex = new ResponeThrowable(e, String.valueOf(ERROR.HTTP_ERROR));
            }

            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    if (httpException.getMessage()!=null ){
                        ex.message = httpException.getMessage();
                    }else if (httpException.message()!=null){
                        ex.message =httpException.message();
                    } else {
                        ex.message = "网络错误";
                    }
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ResponeThrowable(resultException, resultException.code);
            ex.message = resultException.message;
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException
                || e instanceof JsonSyntaxException) {
            ex = new ResponeThrowable(e, String.valueOf(ERROR.PARSE_ERROR));
            ex.message = "解析错误";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponeThrowable(e, String.valueOf(ERROR.NETWORD_ERROR));
            ex.message = "请检查网络";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponeThrowable(e, String.valueOf(ERROR.SSL_ERROR));
            ex.message = "证书验证失败";
            return ex;
        } else if (e instanceof ConnectTimeoutException){
            ex = new ResponeThrowable(e, String.valueOf(ERROR.TIMEOUT_ERROR));
            ex.message = "连接超时";
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponeThrowable(e, String.valueOf(ERROR.TIMEOUT_ERROR));
            ex.message = "连接超时";
            return ex;
        }else if (e instanceof java.net.UnknownHostException){
            ex = new ResponeThrowable(e, String.valueOf(ERROR.NOADDRESS_ERROR));
            ex.message = "请检查网络";
            return ex;
        }
        else {
            ex = new ResponeThrowable(e, String.valueOf(ERROR.UNKNOWN));
            ex.message = "未知错误";
            return ex;
        }
    }


    /**
     * 约定异常
     */
  public static class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;

        /**
         * 请检查网络
         */
        public static final int NOADDRESS_ERROR = 1007;

        /**
         * 服务器错误
         */
        public static final int HTTP_ERROR_500 = 500;
    }

    public static class ResponeThrowable extends Exception {
        public String code;
        public String message;

        public ResponeThrowable(Throwable throwable, String code) {
            super(throwable);
            this.code = code;
        }
    }

    //unchecked异常会自动传递给 onError
    public static class ServerException extends RuntimeException {
        public String code;
        public String message;
        public String jsonString;

        public ServerException(String code,String message,String jsonString){
            this.code = code;
            this.message = message;
            this.jsonString = jsonString;
        }
    }
}
