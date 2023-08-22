package org.tvapp.db.bean;



import static org.tvapp.base.Constants.ERROR_CODE;
import static org.tvapp.base.Constants.SUCCESS_CODE;

import com.google.gson.Gson;

public class BaseResult {
    private String code;
    private Object data;
    private String msg;

    public BaseResult() {
    }

    public BaseResult(String code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static String successResult(Object data){
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(SUCCESS_CODE);
        baseResult.setData(data);
        baseResult.setMsg("success");
        return new Gson().toJson(baseResult);
    }

    public static String errorResult(String msg){
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(ERROR_CODE);
        baseResult.setMsg(msg);
        return new Gson().toJson(baseResult);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
