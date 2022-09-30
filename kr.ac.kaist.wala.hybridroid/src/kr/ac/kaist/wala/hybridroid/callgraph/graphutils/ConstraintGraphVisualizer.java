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
package kr.ac.kaist.wala.hybridroid.callgraph.graphutils;

import kr.ac.kaist.wala.hybridroid.analysis.string.constraint.*;
import kr.ac.kaist.wala.hybridroid.util.graph.visualize.Visualizer;
import kr.ac.kaist.wala.hybridroid.util.graph.visualize.Visualizer.BoxType;

import java.io.File;

public class ConstraintGraphVisualizer {
  private Visualizer vis;

  public ConstraintGraphVisualizer() {}

  public File visualize(ConstraintGraph graph, String out, IBox... spots) {
    vis = Visualizer.getInstance();
    vis.clear();
    vis.setType(Visualizer.GraphType.Digraph);

    if (spots != null) for (IBox spot : spots) vis.setColor(spot, Visualizer.BoxColor.RED);

    for (IConstraintNode from : graph) {
      if (from instanceof IBox) {
        vis.setShape(from, Visualizer.BoxType.RECT);
      } else {
        vis.setShape(from, BoxType.CIRCLE);
      }
      for (IConstraintEdge outEdge : graph.getOutEdges(from)) {
        IConstraintNode to = outEdge.to();

        if (to instanceof IBox) {
          vis.setShape(to, BoxType.RECT);
        } else {
          vis.setShape(to, BoxType.CIRCLE);
        }

        if (outEdge instanceof OrderedEdge)
          vis.fromAtoB(from, to, ((OrderedEdge) outEdge).getOrder() + "");
        else vis.fromAtoB(from, to);
      }
    }
    vis.printGraph(out);
    File outFile = new File(out);
    return outFile;
  }
}
