package minijava.typecheck;

public class BasicType extends AllType{
	//此处的name指变量的类型名，int boolean int[] 或 其他表示类的名字
	protected AllType owner;
	
	
	public BasicType(String mTypeName, AllType mOwner, int rowNum, int colNum) {
		name = mTypeName;
		owner = mOwner;
		row = rowNum;
		col = colNum;
	}
	
	
	//找到定义时给的所在类
	public MyClass refWhichClass() {
		MyClass curClass = null;
		if(owner instanceof MyClass) curClass = ((MyClass)owner).allClasses.getMyClass(name);
		else if(owner instanceof MyMethod) curClass = ((MyMethod)owner).owner.allClasses.getMyClass(name);
		return curClass;
	}
	public boolean isSimpleType() {
		return name.equals("int") || name.equals("boolean") || name.equals("int[]");
	}
	//判断是不是类型匹配，或该变量对应的类是待匹配变量类的父类
	public boolean isSameType(BasicType type) {
		if(this.isSimpleType()) {
			if(!type.isSimpleType()) return false;
			return this.name.equals(type.name);
		}
		else {
			if(type.isSimpleType()) return false;
			if(this.refWhichClass() == null) return false;
			if(type.refWhichClass() == null) return false;
			return type.refWhichClass().isSubClassOf(this.refWhichClass().getName());
		}
	}
}