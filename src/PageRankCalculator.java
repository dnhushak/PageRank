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
				newRankSum = 0;
				// Sum the page rank share of each incoming page
				for (int j = 0; j < pageList.length; j++) {
					// PageRank(j) * deg+(j)
					newRankSum += pageList[j].getPageRank()
							* table.getTotalInboundLinkShare(j, i);
				}
				// Apply Damping factor
				newRankSum *= dampingFactor;
				// Add 1 - damping factor
				newRankSum += (1 - dampingFactor);
				// Update the page rank and OR= with the continue
				// Iteration checker to see if we need to continue
				// Note that if any of the pageRank updates are greater than the
				// iterLimit, continueIteration will be true for the rest of
				// this iteration, thus calling another one
				continueIteration |= pageList[i].UpdatePageRank(newRankSum,
						iterLimit);
			}
			iterCntr++;
		}
		return iterCntr;
	}

	public float calculateMonteCarlo(int numRuns, float alpha) {
		// Array of hits to count for each page
		float hits[];
		hits = new float[pageList.length];
		// Sum of probability of all hits
		float totalHits = 0;
		// Random number generator
		Random rand = new Random();
		// The current page location of our "random surfer"
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
		// Return the sum of all probabilities of arriving on a page (should
		// equal very close to 1)
		return totalHits;
	}
}
