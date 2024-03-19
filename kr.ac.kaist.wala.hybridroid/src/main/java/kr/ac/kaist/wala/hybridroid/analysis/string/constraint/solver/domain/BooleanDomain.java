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
package kr.ac.kaist.wala.hybridroid.analysis.string.constraint.solver.domain;

import kr.ac.kaist.wala.hybridroid.analysis.string.constraint.solver.domain.value.*;

import java.util.HashSet;
import java.util.Set;

public final class BooleanDomain implements IBooleanDomain {

	private static BooleanDomainOp OP_INSTANCE;
		
	private static BooleanDomain domain;
	
	public static BooleanDomain getDomain(){
		if(domain == null)
			domain = new BooleanDomain();
		return domain;
	}
	
	private BooleanDomain(){}
	
	public static BooleanDomainOp op(){
		if(OP_INSTANCE == null)
			OP_INSTANCE = new BooleanDomainOp();
		return OP_INSTANCE;
	}
	
	@Override
	public BooleanDomainOp getOperator(){
		return op();
	}
	
	public static class BooleanDomainOp implements IDomainOp<Boolean, Set<Boolean>>{
		private BooleanDomainOp(){}
		@Override
		public IBooleanValue alpha(Boolean cv) {
			// TODO Auto-generated method stub
			return new BooleanValue(cv);
		}

		@Override
		public IBooleanValue alpha(Set<Boolean> cvs) {
			// TODO Auto-generated method stub
			if(cvs.size() > 1)
				return BooleanTopValue.getInstance();
			else
				return new BooleanValue(cvs.iterator().next());
		}
		
		@Override
		public Set<Boolean> gamma(IValue v) {
			// TODO Auto-generated method stub
			if(v instanceof BooleanBotValue){
				throw new InternalError("Gamma function of String domain operation is applied to StringBotValue.");
			}else if(v instanceof BooleanTopValue){
				Set<Boolean> bs = new HashSet<Boolean>();
				bs.add(true);
				bs.add(false);
				return bs;
			}else if(v instanceof BotValue){
				throw new InternalError("Gamma function of String domain operation is applied to BotValue.");
			}else if(v instanceof TopValue){
				throw new InternalError("Gamma function of String domain operation is applied to TopValue.");
			}else if(v instanceof BooleanValue){
				BooleanValue ssv = (BooleanValue) v;
				Set<Boolean> bs = new HashSet<Boolean>();
				bs.add(ssv.value);
				return bs;
			}else{
				throw new InternalError("String domain operation is not compatible to " + v.getClass().getName());
			}
		}
	}
	
	public static class BooleanValue implements IBooleanValue {
		private boolean value;
		private BooleanDomain domain;
		
		public BooleanValue(Boolean v){
			value = v;
			domain = BooleanDomain.getDomain();
		}
		
		@Override
		public IBooleanValue clone() {
			// TODO Auto-generated method stub 
			return new BooleanValue(value);
		}

		@Override
		public IValue weakUpdate(IValue v) {
			// TODO Auto-generated method stub
			if(v instanceof BotValue)
				return this;
			else if(v instanceof IBooleanValue){
				if(v instanceof BooleanBotValue)
					return this;
				else if(v instanceof BooleanTopValue)
					return v;
				else{
					BooleanValue bv = (BooleanValue) v;
					if(bv.value == value)
						return this;
					else
						return BooleanTopValue.getInstance();
				}
			}else
				return TopValue.getInstance();
		}

		@Override
		public IDomain getDomain() {
			// TODO Auto-generated method stub
			return domain;
		}
		
		@Override
		public boolean equals(Object o){
			if(o instanceof BooleanValue){
				BooleanValue b = (BooleanValue)o;
				if(b.value == this.value)
					return true;
			}
			return false;
		}

		@Override
		public int hashCode(){
			if(value)
				return 1;
			return 0;
		}
	}

	@Override
	public boolean isInstanceof(IValue v) {
		// TODO Auto-generated method stub
		return (v instanceof BooleanValue);
	}	
}
