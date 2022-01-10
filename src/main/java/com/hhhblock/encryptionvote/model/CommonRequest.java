package com.hhhblock.encryptionvote.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonRequest {

    private String voteName;
    private List<String> names;
    private List<String> plaintexts;
    private String name;

}
