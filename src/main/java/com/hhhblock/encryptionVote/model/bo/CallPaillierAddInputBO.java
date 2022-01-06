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
public class CallPaillierAddInputBO {
  private String _cipher1;

  private String _cipher2;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_cipher1);
    args.add(_cipher2);
    return args;
  }
}
