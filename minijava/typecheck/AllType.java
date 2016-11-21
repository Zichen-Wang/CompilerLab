package minijava.typecheck;

public class AllType {
	protected String name;
	protected int row, col;
	public AllType() {}
	public AllType(String _name, int _row, int _col) {
		name = _name;
		row = _row;
		col = _row;
	}
	
	public String getName() {
		return name;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
}