package com.example.demo.req;

import lombok.Data;

@Data
public class BasePageReq {

    /**
     * 当前页码，从 1 开始
     */
    private Integer page = 1;

    /**
     * 每页大小，默认 10
     */
    private Integer size = 10;

}
