package com.example.demo.req;

import lombok.Data;

@Data
public class CreateDatasetsReq {

    private Integer format_type;

    private String name;

    private String space_id;
}
