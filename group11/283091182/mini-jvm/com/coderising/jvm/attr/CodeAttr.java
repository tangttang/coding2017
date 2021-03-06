package com.coderising.jvm.attr;

import com.coderising.jvm.clz.ClassFile;
import com.coderising.jvm.cmd.ByteCodeCommand;
import com.coderising.jvm.cmd.CommandParser;
import com.coderising.jvm.constant.ConstantPool;
import com.coderising.jvm.loader.ByteCodeIterator;


public class CodeAttr extends AttributeInfo {
	private int maxStack ;
	private int maxLocals ;
	private int codeLen ;
	private String code;
	public String getCode() {
		return code;
	}

	private ByteCodeCommand[] cmds ;
	public ByteCodeCommand[] getCmds() {
		return cmds;
	}
	private LineNumberTable lineNumTable;
	private LocalVariableTable localVarTable;
	private StackMapTable stackMapTable;
	
	public CodeAttr(int attrNameIndex, int attrLen, int maxStack, int maxLocals, int codeLen,String code,ByteCodeCommand[] cmds) {
		super(attrNameIndex, attrLen);
		this.maxStack = maxStack;
		this.maxLocals = maxLocals;
		this.codeLen = codeLen;
		this.code = code;
		this.cmds = cmds;
	}

	public void setLineNumberTable(LineNumberTable t) {
		this.lineNumTable = t;
	}

	public void setLocalVariableTable(LocalVariableTable t) {
		this.localVarTable = t;		
	}
	
	public static CodeAttr parse(ClassFile clzFile,ConstantPool pool, ByteCodeIterator iter){
		int attrNameIdx = iter.nextU2AsInt();
		System.out.println("AttrName="+pool.getUTF8String(attrNameIdx));
		int attrLen = iter.nextU4AsInt();
		int maxStack = iter.nextU2AsInt();
		int maxLocal = iter.nextU2AsInt();
		int codeLen = iter.nextU4AsInt();
		String code = iter.getBytesAsHexString(codeLen);
		ByteCodeCommand[] cmds = CommandParser.parse(clzFile, code);
		CodeAttr codeAttr = new CodeAttr(attrNameIdx, attrLen, maxStack, maxLocal, codeLen, code,cmds);
		
		int exceptionTblLen = iter.nextU2AsInt();
		if(exceptionTblLen>0){
			throw new RuntimeException("ExceptionTable not supported!");
		}
		int subAttrCount = iter.nextU2AsInt();
		for(int j=0;j<subAttrCount;j++){
			int subAttrNameIdx = iter.nextU2AsInt();
			String subAttrName = pool.getUTF8String(subAttrNameIdx);
			iter.back(2);
			if(CodeAttr.LINE_NUM_TABLE.equals(subAttrName)){
				LineNumberTable lineNumTable= LineNumberTable.parse(iter);
				codeAttr.setLineNumberTable(lineNumTable);
			}else if(CodeAttr.LOCAL_VAR_TABLE.equals(subAttrName)){
				LocalVariableTable localVarTable = LocalVariableTable.parse(iter);
				codeAttr.setLocalVariableTable(localVarTable);
			}else if(CodeAttr.STACK_MAP_TABLE.equals(subAttrName)){
				StackMapTable stackMapTable = StackMapTable.parse(iter);
				codeAttr.setStackMapTable(stackMapTable);
			}
			
		}
		return codeAttr;
	}
	private void setStackMapTable(StackMapTable t) {
		this.stackMapTable = t;
		
	}

	public String toString(ConstantPool pool){
		return "";
	}
	
	
	
}