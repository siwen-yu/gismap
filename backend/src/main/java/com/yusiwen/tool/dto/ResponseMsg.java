package com.yusiwen.tool.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yusiwen
 * @date 2020/7/21 15:26
 */
public class ResponseMsg {
    private int code;
    private String msg;
    private Map<String, Object> data = new HashMap<>();

    public ResponseMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResponseMsg success(Map<String, Object> data) {
        ResponseMsg msg = new ResponseMsg(1, "成功");
        msg.setData(data);
        return msg;
    }

    public static ResponseMsg success(String key, Object value) {
        Map<String, Object> data = new HashMap<>();
        data.put(key, value);
        return success(data);
    }

    public static ResponseMsg success(String key1, Object value1, String key2, Object value2, String key3, Object value3) {
        Map<String, Object> data = new HashMap<>();
        data.put(key1, value1);
        data.put(key2, value2);
        data.put(key3, value3);
        return success(data);
    }
    public static ResponseMsg failed(String errMsg) {
        return new ResponseMsg(0, errMsg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
