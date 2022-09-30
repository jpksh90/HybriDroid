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
package kr.ac.kaist.wala.hybridroid.models;

import com.ibm.wala.cast.js.ipa.callgraph.JavaScriptEntryPoints;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.core.util.strings.Atom;
import com.ibm.wala.dalvik.ipa.callgraph.impl.AndroidEntryPoint;
import com.ibm.wala.dalvik.util.AndroidEntryPointLocator;
import com.ibm.wala.dalvik.util.AndroidEntryPointLocator.LocatorFlags;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.impl.ComposedEntrypoints;
import com.ibm.wala.ipa.callgraph.impl.DefaultEntrypoint;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.types.*;
import com.ibm.wala.util.collections.HashSetFactory;
import kr.ac.kaist.wala.hybridroid.callgraph.AndroidHybridAnalysisScope;
import kr.ac.kaist.wala.hybridroid.util.data.None;
import kr.ac.kaist.wala.hybridroid.util.data.Option;
import kr.ac.kaist.wala.hybridroid.util.data.Some;

import java.util.*;

public class AndroidHybridAppModel {

  private static Map<String, InstanceKey> interfMap;

  static {
    interfMap = new HashMap<String, InstanceKey>();
  }

  public static void addJSInterface(String name, InstanceKey objKey) {
    interfMap.put(name, objKey);
  }

  public static Option<InstanceKey> findJSInterface(String name) {
    Option<InstanceKey> opInstKey =
        (interfMap.containsKey(name))
            ? new Some<InstanceKey>(interfMap.get(name))
            : new None<InstanceKey>();
    return opInstKey;
  }

  public static void checkJSInterfaces() {
    Set<String> sSet = interfMap.keySet();
    for (String s : sSet) System.out.println("# name: " + s + "\n# obj: " + interfMap.get(s));
  }

  public static Collection<InstanceKey> getJSInterfaces() {
    return interfMap.values();
  }

  public static int numberOfJSInterface() {
    return interfMap.size();
  }

  public static ComposedEntrypoints getEntrypoints(
      final IClassHierarchy cha,
      AndroidHybridAnalysisScope scope,
      AnalysisOptions option,
      AnalysisCache cache) {
    Iterable<Entrypoint> jsRoots =
        new JavaScriptEntryPoints(cha, cha.getLoader(scope.getJavaScriptLoader()));
    Iterable<Entrypoint> entrypoints = null;

    if (cha.lookupClass(
            TypeReference.findOrCreate(
                ClassLoaderReference.Primordial, "Lgeneratedharness/GeneratedAndroidHarness"))
        == null) {
      Set<LocatorFlags> flags = HashSetFactory.make();
      flags.add(LocatorFlags.INCLUDE_CALLBACKS);
      flags.add(LocatorFlags.EP_HEURISTIC);
      flags.add(LocatorFlags.CB_HEURISTIC);
      AndroidEntryPointLocator eps = new AndroidEntryPointLocator(flags);
      List<AndroidEntryPoint> es = eps.getEntryPoints(cha);

      final List<Entrypoint> entries = new ArrayList<Entrypoint>();
      for (AndroidEntryPoint e : es) {
        entries.add(e);
      }

      entrypoints =
          new Iterable<Entrypoint>() {
            @Override
            public Iterator<Entrypoint> iterator() {
              return entries.iterator();
            }
          };
    } else {
      IClass root =
          cha.lookupClass(
              TypeReference.find(
                  ClassLoaderReference.Primordial, "Lgeneratedharness/GeneratedAndroidHarness"));
      IMethod rootMethod =
          root.getMethod(
              new Selector(
                  Atom.findOrCreateAsciiAtom("androidMain"),
                  Descriptor.findOrCreate(null, TypeName.findOrCreate("V"))));
      Entrypoint droidelEntryPoint = new DefaultEntrypoint(rootMethod, cha);

      final List<Entrypoint> entry = new ArrayList<Entrypoint>();
      entry.add(droidelEntryPoint);

      entrypoints =
          new Iterable<Entrypoint>() {
            @Override
            public Iterator<Entrypoint> iterator() {
              return entry.iterator();
            }
          };
    }
    // is the order of entrypoints important?
    return new ComposedEntrypoints(jsRoots, entrypoints);
    //		return new ComposedEntrypoints(jsRoots, entrypoints);
    //		return new ComposedEntrypoints(entrypoints, jsRoots);
  }
}
