package minijava.typecheck;
import java.util.*;

public class MyClass extends AllType{
	//此处的name指的是类的名字
	protected String parentClassName;
	public AllClasses allClasses;
	public Hashtable<String, BasicType> memberVar;
	public Hashtable<String, MyMethod> method;
	// 重复定义的类，那么除第一个类外都不能进行类型检查，否则会出现无意义的检查和报错
	protected boolean isRepeated;
	
	public MyClass(String mClassName, AllClasses mAll, int rowNum, int colNum) {
		name = mClassName;
		allClasses = mAll;
		row = rowNum;
		col = colNum;
		parentClassName = null;
		isRepeated = false;
		memberVar = new Hashtable<String, BasicType>();
		method = new Hashtable<String, MyMethod>();
	}
	
	public boolean getRepeated() {
		return isRepeated;
	}
	
	public void setRepeated() {
		isRepeated = true;
	}
	
	public String insertMemberVar(String varName, BasicType obj) {
		String errorMsg = null;
		if(memberVar.get(varName) != null) {
			errorMsg = "variable " + varName + " is already defined in class " + this.name;
		}
		else {
			memberVar.put(varName, obj);
		}
		return errorMsg;
	}
	
	public BasicType getmemberVar(String varName) {
		return memberVar.get(varName);
	}
	
	
	public String insertMethod(String methodName, MyMethod obj) {
		String errorMsg = null;
		if(method.get(methodName) != null) {
			errorMsg = "method " + methodName + " is already defined in class " + this.name;
		}
		else {
			method.put(methodName, obj);
		}
		return errorMsg;
	}
	//在本类中查找方法，只需给出类名字
	public MyMethod getMethod(String methodName) {
		return method.get(methodName);
	}
	
	//后期为了方便取消了继承重载，转Piglet只需要方法名在父类和本类中找方法
	
	public MyMethod getMethod_Piglet(String methodName) {
		MyMethod _ret = null;
		_ret = this.getMethod(methodName);
		if(_ret != null) return _ret;
		MyClass fatherClass = this.getParentClass();
		while(fatherClass != null) {
			_ret = fatherClass.getMethod(methodName);
			if(_ret != null) return _ret;
			fatherClass = fatherClass.getParentClass();
		}
		return null;
	}
	
	//根据方法调用来寻找是否存在合适的方法，包括父类中的，根据方法名和参数类型表
	public MyMethod getMethod(String methodName, RealArgList methodArgu) {
		MyMethod _ret = null;
		_ret = this.getMethod(methodName);
		if(_ret != null && _ret.isEqualVector(methodArgu)) return _ret;
		MyClass fatherClass = this.getParentClass();
		while(fatherClass != null) {
			_ret = fatherClass.getMethod(methodName);
			if(_ret != null && _ret.isEqualVector(methodArgu)) return _ret;
			fatherClass = fatherClass.getParentClass();
		}
		return null;
	}
	
	
	public String setParentName(String className) {
		String errorMsg = null;
		if(parentClassName != null) {
			errorMsg = "father class is already defined";
		}
		else {
			parentClassName = className;
		}
		return errorMsg;
	}
	
	public MyClass getParentClass() {
		if(parentClassName == null) return null;
		return allClasses.getMyClass(parentClassName);
	}
	//检查循环继承，返回0代表这个类发生了循环继承，1代表别的类发生了，2代表未发生
	public int checkLoop() {
		Hashtable<String, Boolean> vis = new Hashtable<String, Boolean>();
		MyClass fatherClass = this.getParentClass();
		while(fatherClass != null) {
			//在此处发生循环继承，那么打印错误信息
			if(fatherClass.getName().equals(this.name)) {
				return 0;
			}
			//转圈回到另一个类，不是自己，此时也是出现了圈，但不是这个类发生的循环继承
			else if(vis.get(fatherClass.getName()) != null){
				return 1;
			}
			else {
				vis.put(fatherClass.getName(), true);
				fatherClass = fatherClass.getParentClass();
			}
		}
		return 2;
	}
	
	public boolean isSubClassOf(String mClass) {
		MyClass curClass = this;
		while(curClass != null) {
			if(mClass.equals(curClass.getName())) {
				return true;
			}
			curClass = curClass.getParentClass();
		}
		return false;
	}
	
	public BasicType findId(String curName) {
		BasicType _ret = null;
		_ret = this.getmemberVar(curName);
		if(_ret != null) return _ret;
		MyClass fatherClass = this.getParentClass();
		while(fatherClass != null) {
			_ret = fatherClass.getmemberVar(curName);
			if(_ret != null) return _ret;
			fatherClass = fatherClass.getParentClass();
		}
		return null;
	}

}