package com.hhhblock.encryptionvote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse {

    public static final String OK = "200";

    private String code;
    private String message;
    private Object data;

    public static CommonResponse ok(Object data) {
        return new CommonResponse(OK, "", data);
    }

    public static CommonResponse fail(String code, Exception ex) {
        return new CommonResponse(code, ex.getMessage(), null);
    }

    public static CommonResponse fail(String code, String message) {
        return new CommonResponse(code, message, null);
    }
}
