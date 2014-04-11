public class PageNode {
	private float pageRank;
	private int index;

	PageNode(int initIndex) {
		index = initIndex;
		pageRank = 1;
	}

	public boolean UpdatePageRank(float newPageRank, float iterThreshold) {
		if (Math.abs(newPageRank - pageRank) < iterThreshold) {
			// Reached iteration threshold, return false to stop iteration
			return false;
		}
		pageRank = newPageRank;

		return true;
	}

	public float getPageRank() {
		return pageRank;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public String toString() {
		String string = "[" + index + ", " + pageRank + "]";
		return string;
	}
}
