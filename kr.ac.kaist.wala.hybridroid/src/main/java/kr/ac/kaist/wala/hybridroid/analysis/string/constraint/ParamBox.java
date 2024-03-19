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

public class ParamBox implements IBox {
	private CGNode node;
	private int var;
	
	public ParamBox(CGNode node, int var){
		if(var < 1)
			throw new InternalError("parameter variable cannot be less than 1: var[" + var + "] in " + node);
		this.node = node;
		this.var = var;
	}
	
	@Override
	public String toString(){
		String str = "ParamBox[";
		str += var;
		str += "] of " + node.getMethod().getName().toString();
		return str;
	}
	
	public int getVar(){
		return var;
	}
	
	@Override
	public <T> T visit(IBoxVisitor<T> v){
		return v.visit(this);
	}

	@Override
	public CGNode getNode() {
		// TODO Auto-generated method stub
		return node;
	}
	
	@Override
	public int hashCode(){
		return node.hashCode() + var;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof ParamBox){
			ParamBox v = (ParamBox)o;
			if(node.equals(v.getNode()) && var == v.getVar())
				return true;
		}
		return false;
	}
}
