package com.coderising.jvm.constant;

public abstract class ConstantInfo {
	public static final int UTF8_INFO = 1;
	public static final int INTEGER_INFO = 3;
	public static final int FLOAT_INFO = 4;
	public static final int CLASS_INFO = 7;
	public static final int STRING_INFO = 8;
	public static final int FIELD_INFO = 9;
	public static final int METHOD_INFO = 10;
	public static final int NAME_AND_TYPE_INFO = 12;
	protected ConstantPool constantPool;
	
	public ConstantInfo(){
		
	}
	
	public ConstantInfo(ConstantPool pool) {
		this.constantPool = pool;
	}
	public abstract int getType();
	
	public ConstantPool getConstantPool() {
		return constantPool;
	}
	public ConstantInfo getConstantInfo(int index){
		return this.constantPool.getConstantInfo(index);
	}
	
	public abstract void accept(Visitor visitor);
	
	public static interface Visitor{
		public void visitClassInfo(ClassInfo info);
		public void visitFieldRef(FieldRefInfo info);
		public void visitMethodRef(MethodRefInfo info);
		public void visitNameAndType(NameAndTypeInfo info);
		public void visitString(StringInfo info);
		public void visistUTF8(UTF8Info info);
		
	}
	
}
