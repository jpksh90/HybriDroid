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
package kr.ac.kaist.wala.hybridroid.util.print;

import java.io.*;

/**
 * Print messages from input stream asynchronously. This supports two type of message; Standard-out
 * and Standard-error.
 *
 * @author Sungho Lee
 */
public class AsyncPrinter extends Thread {
  public static final int PRINT_OUT = 1;
  public static final int PRINT_ERR = 2;

  private BufferedReader reader;
  private PrintStream printer;

  /**
   * Unique constructor for AsyncPrinter.
   *
   * @param reader the input stream for printing.
   * @param type the type of printing. now, only support standard-out and standard-error.
   */
  public AsyncPrinter(InputStream reader, int type) {
    this.reader = new BufferedReader(new InputStreamReader(reader));
    setPrinter(type);
  }

  /**
   * Set printing type for AsyncPrinter.
   *
   * @param type the type of printing. now, only support standard-out and standard-error.
   */
  private void setPrinter(int type) {
    switch (type) {
      case PRINT_OUT:
        printer = System.out;
        break;
      case PRINT_ERR:
        printer = System.err;
        break;
      default:
    }
  }

  /**
   * Start printing messages from the input stream. Must stop this operation using stop method when
   * no more need to print messages for preventing the resource waste.
   */
  @Override
  public void run() {
    // TODO Auto-generated method stub
    String s;

    try {
      while ((s = reader.readLine()) != null) printer.println(s);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
