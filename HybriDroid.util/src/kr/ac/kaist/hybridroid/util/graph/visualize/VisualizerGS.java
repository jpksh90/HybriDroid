package kr.ac.kaist.hybridroid.util.graph.visualize;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kr.ac.kaist.hybridroid.util.data.Pair;

import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class VisualizerGS {
	static private VisualizerGS instance;
	private Map<Object, Integer> indexMap;
	private Map<Integer, Set<Integer>> fromTo;
	private GraphTypeGS type;
	private int nodeIndex = 1;
	private Map<Pair<Integer, Integer>, String> edgeLabelMap;
	private Map<Integer, String> shapeMap;
	private Map<Integer, String> colorMap;
	private Graph graph;
	
	public enum GraphTypeGS{
		Digraph,
		Undigraph
	}
	
	public enum BoxTypeGS{
		RECT,
		CIRCLE
	}
	
	public enum BoxColorGS{
		BLACK,
		RED,
		BLUE
	}
	
	static public VisualizerGS getInstance(){
		if(instance == null)
			instance = new VisualizerGS();
		return instance;
	}
	
	private VisualizerGS(){
		System.setProperty("org.graphstream.ui.renderer", "kr.ac.kaist.hybridroid.util.graph.visualizer"
				+ "");
		
		indexMap = new HashMap<Object, Integer>();
		fromTo = new HashMap<Integer, Set<Integer>>();
		edgeLabelMap = new HashMap<Pair<Integer, Integer>, String>();
		shapeMap = new HashMap<Integer, String>();
		colorMap = new HashMap<Integer, String>();
		graph = new SingleGraph("ttt");
		graph.addAttribute("ui.stylesheet", "graph { fill-color: red;}node {size: 10px, 15px;shape: box;fill-color: green;stroke-mode: plain;stroke-color: yellow;}node#A {fill-color: blue;}node:clicked {fill-color: red;}");
	}
	
	public void setShape(Object node, BoxTypeGS shape){
		int nodeIndex;
		
		if(hasIndex(node))
			nodeIndex = getIndex(node);
		else
			nodeIndex = setNewIndex(node);
		
		switch(shape){
		case RECT:
			shapeMap.put(nodeIndex, "box");
			break;
		case CIRCLE:
			shapeMap.put(nodeIndex, "circle");
			break;
		}
	}
	
	public void setColor(Object node, BoxColorGS color){
		int nodeIndex;
		
		if(hasIndex(node))
			nodeIndex = getIndex(node);
		else
			nodeIndex = setNewIndex(node);
		
		switch(color){
		case BLACK:
			break;
		case RED:
			colorMap.put(nodeIndex, "red");
			break;
		case BLUE:
			colorMap.put(nodeIndex, "blue");
			break;
		}
	}
	
	public void fromAtoB(Object a, Object b){
		int aIndex;
		int bIndex;
		
		if(hasIndex(a))
			aIndex = getIndex(a);
		else
			aIndex = setNewIndex(a);
		if(hasIndex(b))
			bIndex = getIndex(b);
		else
			bIndex = setNewIndex(b);
		
		addEdge(aIndex, bIndex);
	}
	
	public void fromAtoB(Object a, Object b, String label){
		fromAtoB(a, b);
		int aIndex = getIndex(a);
		int bIndex = getIndex(b);
		edgeLabelMap.put((Pair<Integer, Integer>)Pair.make(aIndex, bIndex), label);
	}
	
	public void clear(){
		indexMap.clear();
		fromTo.clear();
		shapeMap.clear();
		edgeLabelMap.clear();
		type = null;
		nodeIndex = 1;
		graph.clear();
	}
	
	public void setType(GraphTypeGS type){
		this.type = type;
	}
	
	public void printGraph(String out){
		String path = out;
		String edge = "";
		if(type == null){
			System.err.println("Default graph type is an undirected graph.");
			type = GraphTypeGS.Undigraph;
		}
		else
			switch(type){
			case Digraph:
				edge = "->";
				break;
			case Undigraph:
				edge = "--";
				break;
			}
			
		//print all node labels
		for(Object node : indexMap.keySet()){
			int index = indexMap.get(node);
			String id = "node"+index;
			if(graph.getNode(id) == null){
				Node n = graph.addNode(id);
				n.addAttribute("ui.label", node);
				if(colorMap.containsKey(index)){
					switch(colorMap.get(index)){
					case "red":
						System.out.println("-RED-");
						n.addAttribute("ui.color", Color.RED);
						break;
					case "blue":
						n.addAttribute("ui.color", Color.BLUE);
						break;
					}
				}
			}
		}


		for(int from : fromTo.keySet()){
			Set<Integer> toSet = fromTo.get(from);			
			for(int to : toSet){
				try{
					String id = "edge" + from + "->" + to;
					String fromid = "node" + from;
					String toid = "node" + to;
					if(graph.getEdge(id) == null)
						graph.addEdge(id, fromid, toid, true);
				}catch(EdgeRejectedException e){
					System.err.println("[Warning] an edge already exist between " + from +" and " + to);
				}
			}
		}			
	}
	
	public void display(){
		graph.display();
	}
	
	private void addEdge(int a, int b){
		if(!fromTo.containsKey(a))
			fromTo.put(a, new HashSet<Integer>());
		fromTo.get(a).add(b);
	}
	
	private int setNewIndex(Object node){
		int newIndex = newIndex();
		indexMap.put(node, newIndex);
		return newIndex;
	}
	
	private int getIndex(Object node){
		if(hasIndex(node))
			return indexMap.get(node);
		throw new InternalError("the node has no index: " + node);
	}
	
	private boolean hasIndex(Object node){
		return indexMap.containsKey(node);
	}
	
	private String typeToString(GraphTypeGS type){
		switch(type){
		case Digraph:
			return "digraph";
		case Undigraph:
			return "graph";
		default:
		}
		throw new InternalError("Graph must be either Digraph or Undigraph.");
	}
	
	private int newIndex(){
		return nodeIndex++;
	}
	
	private String getShape(int nodeIndex){
		if(shapeMap.containsKey(nodeIndex))
			return shapeMap.get(nodeIndex);
		return "box";
	}
	
	private boolean hasLabel(int from, int to){
		return edgeLabelMap.containsKey(Pair.make(from, to));
	}
	
	private String getLabel(int from, int to){
		return edgeLabelMap.get(Pair.make(from, to));
	}
	
	private String getColor(int nodeIndex){
		if(colorMap.containsKey(nodeIndex))
			return colorMap.get(nodeIndex);
		return "black";
	}
}