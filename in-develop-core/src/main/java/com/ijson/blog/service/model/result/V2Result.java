package com.ijson.blog.service.model.result;

import lombok.Data;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/20 1:39 PM
 */
@Data
public class V2Result<T> {

    private int code;
    private String msg;
    private long count;
    private T data;

}
