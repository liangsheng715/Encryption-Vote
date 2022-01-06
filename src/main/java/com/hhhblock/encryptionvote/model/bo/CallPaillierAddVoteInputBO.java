package com.hhhblock.encryptionvote.model.bo;

import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallPaillierAddVoteInputBO {
  private String voteName;

  private List<String> names;

  private List<String> ciphertexts;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(voteName);
    args.add(names);
    args.add(ciphertexts);
    return args;
  }
}
