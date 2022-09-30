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
package kr.ac.kaist.wala.hybridroid.exception;

public class InternalError extends Exception {

  /** */
  private static final long serialVersionUID = 363458200365316710L;

  private String msg;

  public InternalError(String msg) {
    this.msg = msg;
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return msg;
  }
}
