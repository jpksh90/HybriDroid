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
package kr.ac.kaist.wala.hybridroid.types;

import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.Selector;
import com.ibm.wala.types.TypeReference;

/** Created by leesh on 05/01/2017. */
public class HybriDroidTypes {

  public static Selector SETWEBVIEWCLIENT_SELECTOR =
      Selector.make("setWebViewClient(Landroid/webkit/WebViewClient;)V");
  public static TypeReference WEBVIEW_APP_CLASS =
      TypeReference.findOrCreate(ClassLoaderReference.Application, "Landroid/webkit/WebView");
  public static TypeReference WEBVIEW_PRI_CLASS =
      TypeReference.findOrCreate(ClassLoaderReference.Primordial, "Landroid/webkit/WebView");
}
