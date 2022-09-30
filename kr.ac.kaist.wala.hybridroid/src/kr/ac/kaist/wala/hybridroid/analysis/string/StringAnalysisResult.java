/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and KAIST.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * KAIST - initial API and implementation
 *******************************************************************************/
package kr.ac.kaist.wala.hybridroid.analysis.string;

import com.ibm.wala.types.MethodReference;
import com.ibm.wala.util.collections.Pair;

import java.util.HashMap;
import java.util.Map;

public class StringAnalysisResult {
  private int methodNum;
  private int callSiteNum;
  private int posNum;
  private Map<MethodReference, Integer> methodNumMap;
  private Map<Pair<Integer, Integer>, Integer> callSiteMap;
  private Map<Pair<Integer, Integer>, Integer> posMap;
  private Map<Integer, StringValue> valueMap;

  public StringAnalysisResult() {
    methodNumMap = new HashMap<MethodReference, Integer>();
    callSiteMap = new HashMap<Pair<Integer, Integer>, Integer>();
    posMap = new HashMap<Pair<Integer, Integer>, Integer>();
    valueMap = new HashMap<Integer, StringValue>();
    methodNum = 1;
    callSiteNum = 1;
    posNum = 1;
  }

  public void setResult(MethodReference caller, int instIndex, int argPos, String v) {
    if (!methodNumMap.containsKey(caller)) methodNumMap.put(caller, methodNum++);

    int iCaller = methodNumMap.get(caller);
    Pair<Integer, Integer> pCallSite = Pair.make(iCaller, instIndex);

    if (!callSiteMap.containsKey(pCallSite)) callSiteMap.put(pCallSite, callSiteNum++);

    int iCallSite = callSiteMap.get(pCallSite);
    Pair<Integer, Integer> pPos = Pair.make(iCallSite, argPos);

    if (!posMap.containsKey(pPos)) posMap.put(pPos, posNum++);

    int iPos = posMap.get(pPos);

    valueMap.put(iPos, new StringValue(v));
  }

  public StringValue getValueAt(MethodReference mr, int instIndex, int argPos) {
    if (methodNumMap.containsKey(mr)) {
      int iMr = methodNumMap.get(mr);
      Pair<Integer, Integer> pCallSite = Pair.make(iMr, instIndex);
      if (callSiteMap.containsKey(pCallSite)) {
        int iCallSite = callSiteMap.get(pCallSite);
        Pair<Integer, Integer> pPos = Pair.make(iCallSite, argPos);
        if (posMap.containsKey(pPos)) {
          int iPos = posMap.get(pPos);
          if (valueMap.containsKey(iPos)) {
            return valueMap.get(iPos);
          }
        }
      }
    }

    return new StringValue();
  }

  class StringValue {
    private String v;

    public StringValue() {}

    public StringValue(String v) {
      this.v = v;
    }

    public boolean isDecided() {
      return v != null;
    }

    public String value() {
      return v;
    }
  }
}
