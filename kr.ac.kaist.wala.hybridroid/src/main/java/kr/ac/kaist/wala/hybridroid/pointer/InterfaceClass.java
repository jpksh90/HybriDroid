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
package kr.ac.kaist.wala.hybridroid.pointer;

import com.ibm.wala.cast.loader.AstClass;
import com.ibm.wala.cast.tree.CAstSourcePositionMap.Position;
import com.ibm.wala.classLoader.*;
import com.ibm.wala.classLoader.IMethod.SourcePosition;
import com.ibm.wala.core.util.strings.Atom;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.shrike.shrikeBT.Constants;
import com.ibm.wala.types.FieldReference;
import com.ibm.wala.types.Selector;
import com.ibm.wala.types.TypeName;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.types.annotations.Annotation;


import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.*;

public class InterfaceClass extends AstClass implements IClass {
	private static final Map<Atom, IField> emptyField = Collections.emptyMap();
	private static final Map<Selector, IMethod> emptyMethod = Collections.emptyMap();
	
	private static final Map<IClass, InterfaceClass> classes;
	private static IClass jsrootClass;
	
	private final IClass javaClass;
	private final Map<Atom, IField> mFields;

	static{
		classes = new HashMap<IClass, InterfaceClass>();
	}
	
	public static InterfaceClass wrapping(final IClass javaClass, final IClass superClass){
		if(jsrootClass == null)
			jsrootClass = superClass;
		
		if(classes.keySet().contains(javaClass))
			return classes.get(javaClass);
		
		final Position pos = new Position(){

			@Override
			public int getFirstLine() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getLastLine() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getFirstCol() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getLastCol() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getFirstOffset() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getLastOffset() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int compareTo(SourcePosition o) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public URL getURL() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Reader getReader() throws IOException {
				// TODO Auto-generated method stub
				return javaClass.getSource();
			}
		};

		
		InterfaceClass c = new InterfaceClass(pos, javaClass);
		classes.put(javaClass, c);
		
		return c;
	}

	protected InterfaceClass(Position pos, IClass targetClass) {
		super(pos, targetClass.getReference().getName(), targetClass.getClassLoader(), (short) 0, emptyField, emptyMethod);
		
		mFields = new HashMap<Atom, IField>();
		this.javaClass = targetClass;
	}

	public void addMethodAsField(IMethod method){
		FieldReference fr = FieldReference.findOrCreate(javaClass.getClassLoader().getReference(), 
				javaClass.getReference().getName().toString(), 
				method.getName().toString(), 
				method.getReference().getName().toString());
		IField f = new FieldImpl(javaClass, fr, getAccessFlagForMethod(method), (Collection)Collections.emptySet());
		mFields.put(method.getName(), f);
	}
	
	private short getAccessFlagForMethod(IMethod method){
		if(method.isPrivate()){
			return Constants.ACC_PRIVATE;
		}else if(method.isProtected()){
			return Constants.ACC_PROTECTED;
		}else if(method.isPublic()){
			return Constants.ACC_PUBLIC;
		}else{
			//throw new InternalError("Access flag cannot be set: " + method);
			return Constants.ACC_PUBLIC;
		}
	}
	
	@Override
    public String toString() {
      return "JAVA_JS_BRIDGE:" + getReference().toString();
    }

	@Override
	public IClassHierarchy getClassHierarchy() {
		// TODO Auto-generated method stub
		return javaClass.getClassHierarchy();
	}

	@Override
	public IClassLoader getClassLoader() {
		// TODO Auto-generated method stub
		return javaClass.getClassLoader();
	}

	@Override
	public boolean isInterface() {
		// TODO Auto-generated method stub
		return javaClass.isInterface();
	}

	@Override
	public boolean isAbstract() {
		// TODO Auto-generated method stub
		return javaClass.isAbstract();
	}

	@Override
	public boolean isPublic() {
		// TODO Auto-generated method stub
		return javaClass.isPublic();
	}

	@Override
	public boolean isPrivate() {
		// TODO Auto-generated method stub
		return javaClass.isPrivate();
	}

	@Override
	public int getModifiers() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return javaClass.getModifiers();
	}

	@Override
	public IClass getSuperclass() {
		// TODO Auto-generated method stub
		return jsrootClass;
	}

	@Override
	public Collection<IClass> getDirectInterfaces() {
		// TODO Auto-generated method stub
		return (Collection<IClass>) javaClass.getDirectInterfaces();
	}

	@Override
	public Collection<IClass> getAllImplementedInterfaces() {
		// TODO Auto-generated method stub
		return javaClass.getAllImplementedInterfaces();
	}

	@Override
	public IMethod getMethod(Selector selector) {
		// TODO Auto-generated method stub
		return javaClass.getMethod(selector);
	}

	@Override
	public IField getField(Atom name) {
		// TODO Auto-generated method stub
		IField f = javaClass.getField(name);
		if (f == null) {
			if (mFields.containsKey(name))
				return mFields.get(name);
			else //TODO: get warning message for absent property read.
				return super.getField(name);
		}
			
		return f;
	}

	@Override
	public IField getField(Atom name, TypeName type) {
		// TODO Auto-generated method stub
		return this.getField(name);
	}

	@Override
	public TypeReference getReference() {
		// TODO Auto-generated method stub
		return javaClass.getReference();
	}

	@Override
	public String getSourceFileName() throws NoSuchElementException {
		// TODO Auto-generated method stub
		return javaClass.getSourceFileName();
	}

	@Override
	public Reader getSource() throws NoSuchElementException {
		// TODO Auto-generated method stub
		return javaClass.getSource();
	}

	@Override
	public IMethod getClassInitializer() {
		// TODO Auto-generated method stub
		return javaClass.getClassInitializer();
	}

	@Override
	public boolean isArrayClass() {
		// TODO Auto-generated method stub
		return javaClass.isArrayClass();
	}

	@Override
	public Collection<? extends IMethod> getDeclaredMethods() {
		// TODO Auto-generated method stub
		return javaClass.getDeclaredMethods();
	}

	@Override
	public Collection<IField> getAllInstanceFields() {
		// TODO Auto-generated method stub
		return javaClass.getAllInstanceFields();
	}

	@Override
	public Collection<IField> getAllStaticFields() {
		// TODO Auto-generated method stub
		return javaClass.getAllStaticFields();
	}

	@Override
	public Collection<IField> getAllFields() {
		// TODO Auto-generated method stub
		Collection<IField> mfset = mFields.values();
		Collection<IField> fset = javaClass.getAllFields();
		fset.addAll(mfset);
		return fset;
	}

	@Override
	public Collection<? extends IMethod> getAllMethods() {
		// TODO Auto-generated method stub
		return javaClass.getAllMethods();
	}

	@Override
	public Collection<IField> getDeclaredInstanceFields() {
		// TODO Auto-generated method stub
		return javaClass.getDeclaredInstanceFields();
	}

	@Override
	public Collection<IField> getDeclaredStaticFields() {
		// TODO Auto-generated method stub
		return javaClass.getDeclaredStaticFields();
	}

	@Override
	public TypeName getName() {
		// TODO Auto-generated method stub
		return javaClass.getName();
	}

	@Override
	public boolean isReferenceType() {
		// TODO Auto-generated method stub
		return javaClass.isReferenceType();
	}

	@Override
	public Collection<Annotation> getAnnotations() {
		// TODO Auto-generated method stub
		return javaClass.getAnnotations();
	}

}
