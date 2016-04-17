package kmeans;
import java.util.ArrayList;
public class Driver1 {
	public static void main(String[] args) throws Exception{
		String inputFile = "Archive/file1";
		String outputFile = "outputs/output1.txt";
		
		final int NUMBER_OF_CENTROIDS = 3;
		KMeansIO kmIO = new KMeansIO();
		KMeansClusterer kmc = kmIO.instantiateKMeansClusterer(inputFile);
		kmc.setParameters(NUMBER_OF_CENTROIDS, 92378);
		kmc.cluster();
		/*
		 * part B: centroid at end of clustering
		 */
		String centroidDescription = kmc.centroidString();
		System.out.println("normalized centroids at end of clustering:");
		System.out.println(centroidDescription);
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