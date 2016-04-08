import java.util.ArrayList;

public class Driver2 {

	public static void main(String[] args) {
		String inputFile = "Archive/file2";
		String outputFile = "output2.txt";
		
		final int NUMBER_OF_CENTROIDS = 4;
		KMeansIO kmIO = new KMeansIO();
		KMeansClusterer kmc = null;
		try {
			kmc = kmIO.instantiateKMeansClusterer(inputFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kmc.setParameters(NUMBER_OF_CENTROIDS, 92378);
		kmc.cluster();
		
		/*
		 * part C: sum squared error
		 */
		System.out.println(String.format("Clustering records with %s clusters.", kmc.getNumberOfClusters()));
		System.out.println(String.format("sum squared error: %f", kmc.sumSquaredError()));
		
		
		/*
		 * Writing summary to file (part A)
		 */
		ArrayList<Record> denormalizedRecords = kmc.recordsCopy();
		int[] clustersRecordsBelongTo = kmc.clustersThatRecordsBelongToCopy();
		int numClusters = kmc.getNumberOfClusters();
		kmIO.writeClusteredRecordsToFile(outputFile, denormalizedRecords, clustersRecordsBelongTo, numClusters);
		
		
	}

}
