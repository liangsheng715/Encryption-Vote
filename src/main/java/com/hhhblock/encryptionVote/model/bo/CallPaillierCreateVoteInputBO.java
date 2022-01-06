package com.hhhblock.encryptionVote.model.bo;

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
public class CallPaillierCreateVoteInputBO {
  private String voteName;

  private List<String> names;

  private String initCiphertext;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(voteName);
    args.add(names);
    args.add(initCiphertext);
    return args;
  }
}
