package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import main.GamePanel;

public class PathFinder {

	private GamePanel gp;
	private Node[][] nodes;
	private List<Node> openList = new ArrayList<>();
	private List<Node> pathList = new ArrayList<>();
	private Node startNode, goalNode, currentNode;
	private boolean goalReached = false;
	private int step = 0;

	public PathFinder(GamePanel gp) {
		this.gp = gp;

		this.instantiateNodes();
	}

	private void instantiateNodes() {
		this.nodes = new Node[this.gp.getMaxWorldRow()][this.gp.getMaxWorldCol()];

		for (int row = 0; row < this.nodes.length; row++) {
			for (int col = 0; col < this.nodes[row].length; col++) {
				this.nodes[row][col] = new Node(row, col);
			}
		}
	}

	public void reset() {
		// Reset open, solid, checked
		for (int row = 0; row < this.nodes.length; row++) {
			for (int col = 0; col < this.nodes[row].length; col++) {
				this.nodes[row][col].reset();
			}
		}

		// Reset other settings
		this.openList.clear();
		this.pathList.clear();
		this.startNode = null;
		this.goalNode = null;
		this.currentNode = null;
		this.goalReached = false;
		this.step = 0;
	}

	public void setNodes(int startRow, int startCol, int goalRow, int goalCol) {
		this.reset();

		// Set Start and Goal node
		this.startNode = this.nodes[startRow][startCol];
		this.currentNode = this.startNode;
		this.goalNode = this.nodes[goalRow][goalCol];
		this.openList.add(this.currentNode);

		// CHECK TILES SET SOLID NODE
		for (int row = 0; row < this.gp.getTileManager().getMapTileNum()[this.gp.getCurrentMap()].length; row++) {
			for (int col = 0; col < this.gp.getTileManager().getMapTileNum()[this.gp
					.getCurrentMap()][row].length; col++) {
				int tileIndex = this.gp.getTileManager().getMapTileNum()[this.gp.getCurrentMap()][row][col];
				if (this.gp.getTileManager().getTiles()[tileIndex].isCollision()) {
					this.nodes[row][col].setAsSolid();
				}
			}
		}

		// CHECK ITILES SET SOLID NODE
		for (int i = 0; i < this.gp.getInteractiveTiles()[this.gp.getCurrentMap()].length; i++) {
			if (Objects.nonNull(this.gp.getInteractiveTiles()[this.gp.getCurrentMap()][i])
					&& this.gp.getInteractiveTiles()[this.gp.getCurrentMap()][i].isDestructible()) {

				int row = this.gp.getInteractiveTiles()[this.gp.getCurrentMap()][i].getWorldY() / this.gp.getTileSize();
				int col = this.gp.getInteractiveTiles()[this.gp.getCurrentMap()][i].getWorldX() / this.gp.getTileSize();
				this.nodes[row][col].setAsSolid();
			}
		}

		// SET COST
		for (int row = 0; row < this.nodes.length; row++) {
			for (int col = 0; col < this.nodes[row].length; col++) {
				this.getCost(this.nodes[row][col]);
			}
		}

	}

	private void getCost(Node node) {

		// G Cost
		int xDistance = Math.abs(node.getCol() - this.currentNode.getCol());
		int yDistance = Math.abs(node.getRow() - this.currentNode.getRow());
		node.setgCost(xDistance + yDistance);

		// H Cost
		xDistance = Math.abs(node.getCol() - this.goalNode.getCol());
		yDistance = Math.abs(node.getRow() - this.goalNode.getRow());
		node.sethCost(xDistance + yDistance);

		// F Cost
		node.setfCost(node.getgCost() + node.gethCost());

	}

	public boolean search() {
		while (!this.goalReached && this.step < 500) {

			int row = this.currentNode.getRow();
			int col = this.currentNode.getCol();

			// Check the current node
			this.currentNode.setAsChecked();
			this.openList.remove(this.currentNode);

			// Open the Up node
			if (row - 1 >= 0) {
				this.openNode(this.nodes[row - 1][col]);
			}

			// Open the left node
			if (col - 1 >= 0) {
				this.openNode(this.nodes[row][col - 1]);
			}

			// Open the down node
			if (row + 1 < this.nodes.length) {
				this.openNode(this.nodes[row + 1][col]);
			}

			// Open the right node
			if (col + 1 < this.nodes[row].length) {
				this.openNode(this.nodes[row][col + 1]);
			}

			// Scan the openList and find the best node by the cost
			int bestNodeIndex = 0;
			int bestNodefCost = Integer.MAX_VALUE;

			for (int i = 0; i < this.openList.size(); i++) {
				// Check if this node's F cost is better
				if (this.openList.get(i).getfCost() < bestNodefCost) {
					bestNodeIndex = i;
					bestNodefCost = this.openList.get(i).getfCost();
				}
				// If F cost is equals, check the G cost
				else if (this.openList.get(i).getfCost() == bestNodefCost) {
					if (this.openList.get(i).getgCost() < this.openList.get(bestNodeIndex).getgCost()) {
						bestNodeIndex = i;
					}
				}
			}

			// If there is no node in the openList, end the loop
			if (this.openList.isEmpty()) {
				break;
			}

			// After the loop, openList[bestNodeIndex] is the nest step (= currentNode)
			this.currentNode = this.openList.get(bestNodeIndex);
			if (this.currentNode == this.goalNode) {
				this.goalReached = true;
				this.trackThePath();
			}
			this.step++;
		}
		return this.goalReached;
	}

	public void openNode(Node node) {

		if (!node.isOpen() && !node.isSolid() && !node.isChecked()) {
			node.setAsOpen();
			node.setParent(this.currentNode);
			this.openList.add(node);
		}

	}

	public void trackThePath() {

		Node current = this.goalNode;

		while (current != this.startNode) {
			this.pathList.add(0, current);
			current = current.getParent();
		}

	}

	public List<Node> getPathList() {
		return pathList;
	}

}
