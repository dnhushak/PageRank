import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class PageRank {
	public static void main(String[] args) {
		int numNodes = Integer.parseInt(args[1]);
		float dampingFactor = 0.15f;
		float iterThresh = .001f;
		int numRuns = 100;
		float alpha = .15f;
		
		// Generate the pages, link table, and a calculator for the
		// "SRC linksTo DST" style
		FileReader inputFile = getFile(args[0]);
		PageNode[] pageList = new PageNode[numNodes];
		LinkTable linkTable = generateLinksFromTo(inputFile, numNodes);
		PageRankCalculator calculator = new PageRankCalculator(pageList,
				linkTable, dampingFactor);

		// Create all of the pages and add them to the list
		for (int i = 0; i < numNodes; i++) {
			pageList[i] = new PageNode(i);
		}
		// Calculate page ranks using power iteration
		calculator.calculatePowerIteration(iterThresh);
		// Print all the page ranks
		for (int i = 0; i < numNodes; i++) {
			//System.out.printf("%.4f\n", pageList[i].getPageRank());
		}

		// Calculate Monte Carlo probabilities
		calculator.calculateMonteCarlo(numRuns, alpha);
		//System.out.println();
		for (int i = 0; i < numNodes; i++) {
			// Print all the page ranks
			//System.out.printf("%.4f\n", pageList[i].getPageRank());
		}

		// Generate the pages, link table, and a calculator for the
		// "DST linksFrom SRC" style
		FileReader inputFile2 = getFile(args[0]);
		PageNode[] pageList2 = new PageNode[numNodes];
		LinkTable linkTable2 = generateLinksToFrom(inputFile2, numNodes);
		PageRankCalculator calculator2 = new PageRankCalculator(pageList2,
				linkTable2, dampingFactor);

		// System.out.println(linkTable2.toString());
		// Create all of the pages and add them to the list
		for (int i = 0; i < numNodes; i++) {
			pageList2[i] = new PageNode(i);
		}
		calculator2.calculatePowerIteration(iterThresh);
		//System.out.println();
		for (int i = 0; i < numNodes; i++) {
			//System.out.printf("%.4f\n", pageList2[i].getPageRank());
		}
		// Calculate Monte Carlo probabilities
		calculator2.calculateMonteCarlo(numRuns, alpha);
		//System.out.println();
		for (int i = 0; i < numNodes; i++) {
			// Print all the page ranks
			//System.out.printf("%.4f\n", pageList2[i].getPageRank());
		}

	}

	public static LinkTable generateLinksFromTo(FileReader inputFile,
			int numPages) {
		LinkTable linkTable = new LinkTable(numPages);
		// Start scanning
		Scanner sc = new Scanner(inputFile);
		String line;
		String[] sArr;
		while (sc.hasNextLine()) {
			line = sc.nextLine(); // read the next line into a String object
			sArr = line.split("\\s+"); // split the string by spaces
			int from = Integer.parseInt(sArr[0]) - 1;
			int to = Integer.parseInt(sArr[1]) - 1;
			linkTable.addLink(from, to);
		}
		return linkTable;
	}

	public static LinkTable generateLinksToFrom(FileReader inputFile,
			int numPages) {
		LinkTable linkTable = new LinkTable(numPages);
		// Start scanning
		Scanner sc = new Scanner(inputFile);
		String line;
		String[] sArr;
		while (sc.hasNextLine()) {
			line = sc.nextLine(); // read the next line into a String object
			sArr = line.split("\\s+"); // split the string by spaces
			int to = Integer.parseInt(sArr[0]) - 1;
			int from = Integer.parseInt(sArr[1]) - 1;
			linkTable.addLink(from, to);
		}
		return linkTable;
	}

	public static FileReader getFile(String filePath) {
		FileReader inputFile = null;
		try {
			// Get input file from args
			inputFile = new FileReader(filePath);
		} catch (FileNotFoundException e) {
			// If file not found, print the directory that the user should place
			// the file in, then terminate
			String current;
			try {
				current = new java.io.File(".").getCanonicalPath();
				System.out.println("Place file in this directory:" + current);
			} catch (IOException e1) {
			}
			System.out.println("Terminating the program.");
			System.exit(-1);
		}
		return inputFile;
	}
}
