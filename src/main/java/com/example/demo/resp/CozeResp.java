package com.example.demo.resp;

import com.example.demo.entity.DocumentInfos;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CozeResp<T> {

    private int code;
    private String msg;
    private T data;
    private Detail detail;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DocumentInfos> document_infos;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer total;

    @Data
    public static class Detail {
        private String logid;
    }

}
