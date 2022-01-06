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
public class PaillierPrecompiledPaillierAddInputBO {
  private String cipher1;

  private String cipher2;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(cipher1);
    args.add(cipher2);
    return args;
  }
}
