import java.util.Random;

public class PageRankCalculator {
	private PageNode[] pageList;
	private LinkTable table;
	private float dampingFactor;

	public PageRankCalculator(PageNode[] initPageList, LinkTable initTable,
			float initDampingFactor) {
		pageList = initPageList;
		table = initTable;
		dampingFactor = initDampingFactor;
	}

	public int calculatePowerIteration(float iterLimit) {
		boolean continueIteration = true;
		float newRankSum;
		int iterCntr = 0;
		// Check if we've reached the iteration Limit
		while (continueIteration) {
			continueIteration = false;
			// Update the pagerank of every page
			for (int i = 0; i < pageList.length; i++) {
				// Sum the page rank share of each incoming page
				newRankSum = 0;
				for (int j = 0; j < pageList.length; j++) {
					newRankSum += pageList[j].getPageRank()
							* table.getTotalInboundLinkShare(j, i);
				}
				newRankSum *= dampingFactor;
				newRankSum += (1 - dampingFactor);
				continueIteration |= pageList[i].UpdatePageRank(newRankSum,
						iterLimit);
			}
			iterCntr++;
		}
		return iterCntr;
	}

	public float calculateMonteCarlo(int numRuns, float alpha) {
		float hits[];
		hits = new float[pageList.length];
		float totalHits = 0;
		Random rand = new Random();
		int currentPageIndex;

		// For each page...
		for (int i = 0; i < pageList.length; i++) {
			// Start on a page
			currentPageIndex = i;
			// Run numRuns times...
			for (int j = 0; j < numRuns; j++) {
				// Randomly determine whether or not to stop
				while (rand.nextFloat() < alpha) {
					// If not stopping, Randomly jump to another page
					currentPageIndex = table.getRandomLink(currentPageIndex);
				}
				// Increment the counter for this page because we've stopped
				// here
				hits[currentPageIndex]++;
			}
		}
		// For each page...
		for (int i = 0; i < pageList.length; i++) {
			// Divide the number of hits by the total number of runs
			// (normalizing all of the probabilities)
			hits[i] /= (float) pageList.length * numRuns;
			totalHits += hits[i];
			// Uptate all of the pageNodes pagerank
			pageList[i].UpdatePageRank(hits[i], 0);
		}

		return totalHits;
	}
}
