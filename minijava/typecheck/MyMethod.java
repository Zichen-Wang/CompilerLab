package minijava.typecheck;
import java.util.*;

public class MyMethod extends AllType{
	//此处的name表示方法的名字
	protected BasicType methodType;
	public MyClass owner;
	public Hashtable<String, BasicType> localVar;
	protected Vector<BasicType> parameters;
	public Vector<String> paraName;
	// 在同一个类中重复定义的方法，那么除第一个方法外都不能进行类型检查，否则会出现无意义的检查和报错
	protected boolean isRepeated;
	
	public MyMethod(String mType, String mName, MyClass cOwner, int rowNum, int colNum) {
		methodType = new BasicType(mType, cOwner, rowNum, colNum);
		name = mName;
		owner = cOwner;
		row = rowNum;
		col = colNum;
		isRepeated = false;
		localVar = new Hashtable<String, BasicType>();
		parameters = new Vector<BasicType>();
		paraName = new Vector<String>();
	}
	
	public boolean getRepeated() {
		return isRepeated;
	}
	
	public void setRepeated() {
		isRepeated = true;
	}
	
	public String insertLocalVar(String varName, BasicType varType) {
		String errorMsg = null;
		if(localVar.get(varName) != null) {
			errorMsg = "variable " + varName + " is already defined in method " + this.name;
		}
		else {
			localVar.put(varName, varType);
		}
		return errorMsg;
	}
	public BasicType getLocalVar(String varName) {
		return localVar.get(varName);
	}
	
	public void insertPara(BasicType varType) {
		parameters.add(varType);
	}
	
	public void insertParaName(String name) {
		paraName.add(name);
	}
	
	public Vector<BasicType> getPara() {
		return parameters;
	}
	
	public BasicType getMethodType() {
		return methodType;
	}
	
	public boolean isEqualVector(RealArgList curArgList) {
		if(curArgList == null) {
			if(this.parameters.size() == 0) {
				return true;
			}
			else {
				return false;
			}
		}
		if(this.parameters.size() != curArgList.myArgus.size())
			return false;
		int len = this.parameters.size();
		for(int i = 0; i < len; i++) {
			BasicType curType1 = this.parameters.get(i);
			BasicType curType2 = curArgList.myArgus.get(i);
			if(!curType1.isSameType(curType2)) return false;
		}
		return true;
	}
	
	public boolean isEqualPara(MyMethod curMethod) {
		if(this.parameters.size() != curMethod.parameters.size())
			return false;
		int len = this.parameters.size();
		for(int i = 0; i < len; i++) {
			String name1 = this.parameters.get(i).getName();
			String name2 = curMethod.parameters.get(i).getName();
			if(!name1.equals(name2)) return false;
		}
		return true;
	}
	
	public String override() {
		String errorMsg = null;
		MyClass fatherClass = owner.getParentClass();
		while(fatherClass != null) {
			Enumeration e = fatherClass.method.elements();
			while(e.hasMoreElements()) {
				MyMethod curMethod = (MyMethod)e.nextElement();
				//名字相同，参数表类型相同，且返回值类型不同
				if(this.name == curMethod.getName() && this.isEqualPara(curMethod)
						&& !curMethod.methodType.isSameType(this.getMethodType())) {
					errorMsg = "method " + this.name + " in " + owner.getName() + " cannot override " + "method " + curMethod.getName() + " in " + fatherClass.getName();
					break;
				}
			}
			if(errorMsg != null) {
				break;
			}
			fatherClass = fatherClass.getParentClass();
		}
		return errorMsg;
	}
	public BasicType findId(String curName) {
		BasicType _ret = null;
		_ret = getLocalVar(curName);
		if(_ret != null) return _ret;
		_ret = owner.getmemberVar(curName);
		if(_ret != null) return _ret;
		MyClass fatherClass = owner.getParentClass();
		while(fatherClass != null) {
			_ret = fatherClass.getmemberVar(curName);
			if(_ret != null) return _ret;
			fatherClass = fatherClass.getParentClass();
		}
		return null;
	}

}
