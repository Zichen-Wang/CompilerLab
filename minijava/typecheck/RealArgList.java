package minijava.typecheck;
import java.util.*;

public class RealArgList extends AllType {
	/*
	 * 实参表只记录类型
	 */
	public Vector<BasicType> myArgus = new Vector<BasicType>();
	//记录参数表本身出现在哪个方法里
	protected MyMethod owner;
	public void insertArg(BasicType arg) {
		myArgus.add(arg);
	}
	public void setOwner(MyMethod mOwner) {
		owner = mOwner;
	}
	public MyMethod getOwner() {
		return owner;
	}
}