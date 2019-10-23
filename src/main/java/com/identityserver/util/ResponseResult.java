package com.identityserver.util;

import java.io.Serializable;

/**
 * 统一响应返回值
 *
 * @param <T>
 */
public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 返回状态码
     */
    private int code;

    /**
     * 返回消息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 默认构造函数，初始化返回状态码为成功，消息信息为成功，数据为空。
     */
    public ResponseResult() {
        this.code = CommonConstants.SUCCESS.intValue();
        this.msg = "success";
    }

    /**
     * 传入参数（数据）构造函数，初始化返回状态码为成功，消息信息为成功，数据为传入数据值。
     *
     * @param data
     */
    public ResponseResult(T data) {
        this.code = CommonConstants.SUCCESS.intValue();
        this.msg = "success";
        this.data = data;
    }

    /**
     * 传入参数（数据，返回消息）构造函数，初始化返回状态码为成功，消息信息为传入消息值，数据为传入数据值。
     *
     * @param data
     * @param msg
     */
    public ResponseResult(T data, String msg) {
        this.code = CommonConstants.SUCCESS.intValue();
        this.msg = "success";
        this.msg = msg;
        this.data = data;
    }

    /**
     * 传入参数（异常信息）构造函数，初始化返回状态码为失败，消息信息为传入异常的消息值，数据为空。
     *
     * @param e 异常
     */
    public ResponseResult(Throwable e) {
        this.msg = e.getMessage();
        this.code = CommonConstants.INNER_ERROR.intValue();
    }

    /**
     * 传入参数（状态码，消息，数据）构造函数，初始化返回状态码为失败，消息信息为传入异常的消息值，数据为空。
     *
     * @param code
     * @param msg
     * @param data
     */
    public ResponseResult(int code, String msg, T data) {
        this.code = CommonConstants.SUCCESS.intValue();
        this.msg = "success";
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 静态方法生成一个结果
     *
     * @param <T>
     * @return
     */
    public static <T> ResponseResult.ResultBuilder<T> builder() {
        return new ResponseResult.ResultBuilder();
    }

    /**
     * 响应结果转换为字符串
     *
     * @return
     */
    public String toString() {
        return "ResponseResult(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
    }

    /**
     * 获取状态码
     *
     * @return
     */
    public int getCode() {
        return this.code;
    }

    /**
     * 设置状态码
     *
     * @param code
     * @return
     */
    public ResponseResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    /**
     * 获取消息值
     *
     * @return
     */
    public String getMsg() {
        return this.msg;
    }

    /**
     * 设置消息值
     *
     * @param msg
     * @return
     */
    public ResponseResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 获取数据值
     *
     * @return
     */
    public T getData() {
        return this.data;
    }

    /**
     * 设置数据值
     *
     * @param data
     * @return
     */
    public ResponseResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 响应结果构造类(链式调用)
     *
     * @param <T>
     */
    public static class ResultBuilder<T> {
        /**
         * 状态码
         */
        private int code;

        /**
         * 结果信息
         */
        private String msg;

        /**
         * 结果数据值
         */
        private T data;

        /**
         * 构造函数
         */
        ResultBuilder() {
        }

        /**
         * 设置状态码
         *
         * @param code
         * @return
         */
        public ResponseResult.ResultBuilder<T> code(int code) {
            this.code = code;
            return this;
        }

        /**
         * 设置返回信息
         *
         * @param msg
         * @return
         */
        public ResponseResult.ResultBuilder<T> msg(String msg) {
            this.msg = msg;
            return this;
        }

        /**
         * 设置数据
         *
         * @param data
         * @return
         */
        public ResponseResult.ResultBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        /**
         * 生成响应结果
         *
         * @return
         */
        public ResponseResult<T> build() {
            return new ResponseResult(this.code, this.msg, this.data);
        }

        /**
         * 返回响应结果字符串
         *
         * @return
         */
        public String toString() {
            return "ResponseResult.RBuilder(code=" + this.code + ", msg=" + this.msg + ", data=" + this.data + ")";
        }
    }
}
