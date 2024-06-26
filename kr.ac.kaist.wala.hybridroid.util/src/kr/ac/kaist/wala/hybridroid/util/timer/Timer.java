/*******************************************************************************
 * Copyright (c) 2002 - 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package kr.ac.kaist.wala.hybridroid.util.timer;

public class Timer {
  private long start;
  private long end;

  private Timer(long start) {
    this.start = start;
  }

  public static Timer start() {
    return new Timer(System.currentTimeMillis());
  }

  public long end() {
    this.end = System.currentTimeMillis();
    return end - start;
  }
}
