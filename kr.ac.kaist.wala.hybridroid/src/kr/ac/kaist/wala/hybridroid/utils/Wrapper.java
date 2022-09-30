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
package kr.ac.kaist.wala.hybridroid.utils;

public class Wrapper<T> {
  private T obj;

  public Wrapper() {}

  public Wrapper(T obj) {
    this.obj = obj;
  }

  public T getObject() {
    return obj;
  }

  public void setObject(T obj) {
    this.obj = obj;
  }

  public boolean has() {
    return obj != null;
  }
}
