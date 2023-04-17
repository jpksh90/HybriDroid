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
package kr.ac.kaist.wala.hybridroid.analysis.string.constraint.solver.model;

import com.ibm.wala.util.debug.Assertions;
import java.util.HashSet;
import java.util.Set;
import kr.ac.kaist.wala.hybridroid.analysis.string.constraint.solver.domain.IntegerSetDomain;
import kr.ac.kaist.wala.hybridroid.analysis.string.constraint.solver.domain.IntegerSetDomain.IntegerSetValue;
import kr.ac.kaist.wala.hybridroid.analysis.string.constraint.solver.domain.StringSetDomain.StringSetValue;
import kr.ac.kaist.wala.hybridroid.analysis.string.constraint.solver.domain.value.*;

public class LengthOpSetModel implements IOperationModel {

  private static LengthOpSetModel instance;

  public static LengthOpSetModel getInstance() {
    if (instance == null) instance = new LengthOpSetModel();
    return instance;
  }

  @Override
  public IValue apply(IValue... args) {
    // TODO Auto-generated method stub
    if (args.length != 1) Assertions.UNREACHABLE("LengthOp must have only one arg: " + args);

    IValue str = args[0];

    if (str instanceof TopValue) {
      return IntegerTopValue.getInstance();
    } else if (str instanceof BotValue) {
      return IntegerBotValue.getInstance();
    } else if (str instanceof StringSetValue) {
      Set<String> ss = (Set<String>) str.getDomain().getOperator().gamma(str);
      Set<Integer> res = new HashSet<Integer>();
      for (String s : ss) {
        res.add(s.length());
      }
      return IntegerSetDomain.getDomain().getOperator().alpha(res);
    } else if (str instanceof IntegerSetValue) { // str is 'null' case
      Set<Integer> ss =
          ((Set<Integer>) (((IntegerSetValue) str).getDomain().getOperator().gamma(str)));
      if (ss.size() == 1 && ss.contains(0)) return IntegerBotValue.getInstance();
      else if (CRASH)
        Assertions.UNREACHABLE("Incorrect args(arg1: " + str.getClass().getName() + ")");
      else return BotValue.getInstance();
    } else if (CRASH)
      Assertions.UNREACHABLE("Argument type is not correct(" + str.getClass().getName() + ")");
    else return BotValue.getInstance();

    return null;
  }
}
