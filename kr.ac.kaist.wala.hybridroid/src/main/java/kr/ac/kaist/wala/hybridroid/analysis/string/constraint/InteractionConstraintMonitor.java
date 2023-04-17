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

import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.types.ClassLoaderReference;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class InteractionConstraintMonitor implements IConstraintMonitor {

  public static final int CLASSTYPE_ALL = 1;
  public static final int CLASSTYPE_APPLICATION = 2;
  public static final int CLASSTYPE_PRIMORDIAL = 3;

  public static final int NODETYPE_ALL = 1;
  public static final int NODETYPE_PARAM = 2;
  public static final int NODETYPE_VAR = 3;
  public static final int NODETYPE_CONSTANT = 4;
  public static final int NODETYPE_NONE = 5;

  private static final String CMD_PATH = "path";
  private static final String CMD_GO = "go";
  private static final String CMD_PRINT_NODE = "print";
  private static final String CMD_RESULT = "result";
  private static final String CMD_JUMP = "jump";

  private final int classtype;
  private final int nodetype;
  private final CallGraph cg;
  private int jumpIterNum;

  public InteractionConstraintMonitor(CallGraph cg, int classtype, int nodetype) {
    this.classtype = classtype;
    this.nodetype = nodetype;
    this.cg = cg;
    jumpIterNum = -1;
  }

  @Override
  public void monitor(int iter, ConstraintGraph graph, IBox b, Set<IBox> boxes) {
    // TODO Auto-generated method stub
    if (classtype == CLASSTYPE_APPLICATION) {
      if (b.getNode()
              .getMethod()
              .getDeclaringClass()
              .getClassLoader()
              .getReference()
              .equals(ClassLoaderReference.Application)
          == false) return;
    } else if (classtype == CLASSTYPE_PRIMORDIAL) {
      if (b.getNode()
              .getMethod()
              .getDeclaringClass()
              .getClassLoader()
              .getReference()
              .equals(ClassLoaderReference.Primordial)
          == false) return;
    }

    if (nodetype == NODETYPE_PARAM) {
      if (!(b instanceof ParamBox)) return;
    } else if (nodetype == NODETYPE_VAR) {
      if (!(b instanceof VarBox)) return;
    } else if (nodetype == NODETYPE_CONSTANT) {
      if (!(b instanceof ConstBox)) return;
    } else if (nodetype == NODETYPE_NONE) return;

    if (jumpIterNum != -1) {
      if (jumpIterNum != iter) return;
      else if (jumpIterNum == iter) jumpIterNum = -1;
    }

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String r = "";
    List<String> cmd = Collections.emptyList();
    do {
      try {
        r = br.readLine();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      cmd = parse(r);
    } while (exec(graph, b, cmd, boxes, iter));
  }

  private List<String> parse(String r) {
    List<String> tokens = new ArrayList<String>();
    StringTokenizer st = new StringTokenizer(r);

    while (st.hasMoreTokens()) {
      String t = st.nextToken();
      tokens.add(t);
    }
    return tokens;
  }

  private boolean exec(
      ConstraintGraph graph, IConstraintNode b, List<String> cmd, Set<IBox> boxes, int iter) {
    if (cmd.size() == 0) return false;

    String mainCmd = cmd.get(0);
    try {
      if (mainCmd.equalsIgnoreCase(CMD_PATH)) {
        printPath(graph, b);
      } else if (mainCmd.equalsIgnoreCase(CMD_GO)) return false;
      else if (mainCmd.equalsIgnoreCase(CMD_PRINT_NODE)) {
        String cn = cmd.get(1);
        if (cn.equals("it") && b instanceof IBox) {
          IBox box = (IBox) b;
          printNode(box.getNode());
        } else {
          String mn = cmd.get(2);
          int args = Integer.parseInt(cmd.get(3));
          CGNode node = findNode(cn, mn, args);

          if (node != null) {
            printNode(node);
          }
        }
      } else if (mainCmd.equalsIgnoreCase(CMD_RESULT)) {
        int index = 1;
        System.out.println("====Result====");
        for (IBox box : boxes) {
          System.out.println("(" + (index++) + ") " + box);
        }
        System.out.println("==============");
      } else if (mainCmd.equals(CMD_JUMP)) {
        String target = cmd.get(1);
        jumpIterNum = Integer.parseInt(target);
        if (jumpIterNum < iter) {
          System.err.println("cannot jump the index.");
          jumpIterNum = -1;
        }

        return false;
      } else {
        System.err.println("no command for it.");
      }
    } catch (Exception e) {
      System.err.println("error command.");
    }
    return true;
  }

  private CGNode findNode(String className, String methodName, int args) {
    for (CGNode node : cg) {
      String cn = node.getMethod().getDeclaringClass().getName().getClassName().toString();
      String mn = node.getMethod().getName().toString();

      if (cn.equals(className)
          && mn.equals(methodName)
          && node.getMethod().getNumberOfParameters() == args) return node;
    }
    System.err.println("cannot find the node: " + className + "." + methodName + "(" + args + ")");
    return null;
  }

  private void printNode(CGNode node) {
    SSAInstruction[] insts = node.getIR().getInstructions();

    System.out.println("#Node: " + node);
    for (int i = 0; i < insts.length; i++) System.out.println("\t(" + (i + 1) + ") " + insts[i]);
  }

  private void printPath(ConstraintGraph graph, IConstraintNode b) {
    List<IConstraintNode> succs = new ArrayList<IConstraintNode>();
    System.out.println("#" + b);
    for (IConstraintEdge outEdge : graph.getOutEdges(b)) {
      IConstraintNode succ = outEdge.to();
      if (outEdge instanceof PropagationEdge) System.out.println("\t-(=)-> " + succ);
      if (outEdge instanceof OrderedEdge) {
        OrderedEdge e = (OrderedEdge) outEdge;
        System.out.println("\t-(" + e.getOrder() + ")-> " + succ);
      }
      succs.add(succ);
    }

    for (IConstraintNode before : succs) printPath(graph, before);
  }
}
