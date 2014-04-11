import java.util.Random;

public class LinkTable {
	int[][] table;
	int numNodes;

	/*
	 * Implements an adjacency matrix to indicate a link between pages
	 * 
	 * Each row represents "from these pages..." and each colum is
	 * "...to these pages"
	 * 
	 * For example:
	 * 
	 * x| 0 1 2 
	 * ---------
	 * 0|   1 
	 * 1| 1   1 
	 * 2|
	 * 
	 * Shows a link from 0 to 1, from 1 to 0, and from 1 to 2, and no links
	 * outbound from 2
	 */
	LinkTable(int initNumNodes) {
		table = new int[initNumNodes][initNumNodes];
		numNodes = initNumNodes;
	}

	public void addLink(int fromIndex, int toIndex) {
		// If the from and to are not the same (ignore self links)
		if (fromIndex != toIndex) {
			// Set the index value to one (don't increment, this ignores
			// multiples of the same links)
			table[fromIndex][toIndex] = 1;
		}
	}

	public int getLinks(int fromIndex, int toIndex) {
		return table[fromIndex][toIndex];
	}

	public int getTotalOutboundLinks(int fromIndex) {
		// Start the counter at 0
		int outbound = 0;
		// For every column in said row...
		for (int i = 0; i < numNodes; i++) {
			// Grab that column's value and add it to the counter
			outbound += table[fromIndex][i];
		}
		return outbound;
	}

	public int getTotalInboundLinks(int toIndex) {
		// Start counter at 0
		int outbound = 0;
		// For every row in said column...
		for (int i = 0; i < numNodes; i++) {
			// Grab that row's value and add it to the counter
			outbound += table[i][toIndex];
		}
		return outbound;
	}

	/*
	 * Returns the % of links from a page are to another page
	 * 
	 * (this is the Di in the summation equation for page rank power iteration)
	 */
	public float getTotalInboundLinkShare(int fromIndex, int toIndex) {
		// Default at 0
		float share = 0;
		// Divide by the total number of links outbound from "from"
		float total = (float) getTotalOutboundLinks(fromIndex);
		if (total != 0) {
			// Get the number of links from "from" to "to"
			share = (float) table[fromIndex][toIndex];
			share /= total;
		}
		return share;
	}

	@Override
	public String toString() {
		String row = "";
		for (int i = 0; i < numNodes; i++) {
			for (int j = 0; j < numNodes; j++) {
				row += "[" + table[i][j] + "]";
			}
			row += "\n";
		}
		return row;
	}

	public int getRandomLink(int index) {
		Random rand = new Random();
		// Get an index of the link (randomly decided between the set of
		// outbound links from this index)
		int linkIndex = (int) Math.floor(rand.nextFloat()
				* (this.getTotalOutboundLinks(index) - 1));
		// i is the counter of valid links;
		int i = 0;
		// j is the column counter
		int j = 0;
		while (i < linkIndex) {
			i += table[index][j];
			j++;
		}
		return i;
	}
}
