package ai;

public class Node {

	// TRACK
	private Node parent;

	// POSITION
	private int row;
	private int col;

	// COST
	private int gCost;
	private int hCost;
	private int fCost;

	// Validation
	private boolean open;
	private boolean solid;
	private boolean checked;

	public Node(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public void reset() {
		this.parent = null;
		this.gCost = 0;
		this.hCost = 0;
		this.fCost = 0;
		this.open = false;
		this.solid = false;
		this.checked = false;
	}
	
	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getgCost() {
		return gCost;
	}

	public void setgCost(int gCost) {
		this.gCost = gCost;
	}

	public int gethCost() {
		return hCost;
	}

	public void sethCost(int hCost) {
		this.hCost = hCost;
	}

	public int getfCost() {
		return fCost;
	}

	public void setfCost(int fCost) {
		this.fCost = fCost;
	}

	public boolean isOpen() {
		return open;
	}

	public void setAsOpen() {
		this.open = true;
	}

	public boolean isSolid() {
		return solid;
	}

	public void setAsSolid() {
		this.solid = true;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setAsChecked() {
		this.checked = true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Node [row=");
		builder.append(row);
		builder.append(", col=");
		builder.append(col);
		builder.append(", gCost=");
		builder.append(gCost);
		builder.append(", hCost=");
		builder.append(hCost);
		builder.append(", fCost=");
		builder.append(fCost);
		builder.append(", open=");
		builder.append(open);
		builder.append(", solid=");
		builder.append(solid);
		builder.append(", checked=");
		builder.append(checked);
		builder.append(", parent=");
		builder.append(parent);
		builder.append("]");
		return builder.toString();
	}

}
