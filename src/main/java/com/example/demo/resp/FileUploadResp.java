package com.example.demo.resp;

import lombok.Data;

@Data
public class FileUploadResp {

    private String id;
    private String file_name;
    private Integer bytes;
    private Integer created_at;
}
