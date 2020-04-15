/*
 * Copyright (C) 2015. Keegan小钢（http://keeganlee.me）
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sky.sdk.net.http;

import java.util.List;

/**
 * Created by SKY on 15/12/9 下午8:54.
 * Api响应结果的封装类.
 */
public class ApiResponse<T> {
    private int code;    // 返回状态码,"0"代表成功
    private String msg;  // 返回信息
    private T obj;           // 单个对象
    private T objList;       // 数组对象
    private int currentPage; // 当前页数
    private int pageSize;    // 每页显示数量
    private int maxCount;    // 总条数
    private int maxPage;     // 总页数

    private List<T> data;
    private int status;    // 返回状态码,"1"代表成功

    public ApiResponse() {
    }

    public ApiResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public List<T> getData() {
        return data;
    }

    public boolean getStatus() {
        return status == 1;
    }

    public boolean isSuccess() {
        return code == 0;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public T getObj() {
        return obj;
    }

    public T getObjList() {
        return objList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getMaxPage() {
        return maxPage;
    }
}
