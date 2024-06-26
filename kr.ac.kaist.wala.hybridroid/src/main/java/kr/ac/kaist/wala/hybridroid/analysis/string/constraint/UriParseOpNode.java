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
package kr.ac.kaist.wala.hybridroid.analysis.string.constraint;

import kr.ac.kaist.wala.hybridroid.analysis.string.constraint.solver.domain.value.IValue;
import kr.ac.kaist.wala.hybridroid.analysis.string.constraint.solver.model.IOperationModel;
import kr.ac.kaist.wala.hybridroid.analysis.string.constraint.solver.model.UriParseOpSetModel;

public class UriParseOpNode implements IOperatorNode {

  private static IOperationModel m;

  static {
    m = UriParseOpSetModel.getInstance();
  }

  public UriParseOpNode() {}

  @Override
  public String toString() {
    return "Uri.parse";
  }

  @Override
  public IValue apply(IValue... args) {
    // TODO Auto-generated method stub
    return m.apply(args);
  }
}
