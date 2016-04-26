package kmeans;

import java.util.ArrayList;

public class KMeansOptimizer {

    public static void main(String[] args) throws Exception {
	String inputFile = "Archive/file1";
	KMeansIO kmIO = new KMeansIO(inputFile);
	int optimalNumber = optimalNumberOfClusters(kmIO);
	System.out.println(String.format("clusters: %d", optimalNumber));
    }

    public static int optimalNumberOfClusters(KMeansIO kmIO) {
	int clusterIterations = 20;
	ArrayList<Double> sumSquaredErrors = new ArrayList<>();
	StringBuffer sBuffer = new StringBuffer("");
	for (int numClusters = 2; numClusters <= clusterIterations; numClusters++) {
	    KMeansClusterer kmc = kmIO.instantiateKMeansClusterer();
	    kmc.setParameters(numClusters, 92378);
	    kmc.cluster();
	    double sse = kmc.sumSquaredError();
	    sumSquaredErrors.add(sse);
	    sBuffer.append(String.format("%d,%.2f\n", numClusters, sse));
	}
	System.out.println(sBuffer);

	ArrayList<Double> differences = new ArrayList<>();
	for (int i = 0; i < sumSquaredErrors.size() - 1; i++) {
	    double first = sumSquaredErrors.get(i);
	    double second = sumSquaredErrors.get(i + 1);
	    differences.add(Math.abs(first - second));
	}

	for (int i = differences.size() - 1; i >= 0; i--) {
	    System.out.println(String.format("diff[%d]: %.2f", i, differences.get(i)));
	}

	for (int i = differences.size() - 5; i > 0; i--) {
	    double stdevOfPrevDiffs = standardDeviationOfNumbersAfterIndex(differences, i);
	    System.out.println(String.format("i: %d, stdev: %.2f", i, stdevOfPrevDiffs));
	    if (differences.get(i - 1) > 4 * stdevOfPrevDiffs) {
		// add 1 because diff is indexed at one less than the sse array
		// add another 1 since I start my numclusters at 2 instead of 1
		return i + 1 + 1;
	    }
	}
	return -1;
    }

    private static double standardDeviationOfNumbersAfterIndex(ArrayList<Double> differences, int index) {
	double result = 0.0;
	for (int i = index; i < differences.size(); i++) {
	    result += Math.pow(differences.get(i), 2);
	}
	result /= differences.size() - index;
	result = Math.sqrt(result);
	return result;
    }

}
