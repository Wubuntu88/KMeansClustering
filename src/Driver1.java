import java.util.ArrayList;

public class Driver1 {

	public static void main(String[] args) {
		String inputFile = "Archive/file1";
		String outputFile = "output1.txt";
		
		KMeansIO kmIO = new KMeansIO();
		KMeansClusterer kmc = null;
		try {
			kmc = kmIO.instantiateKMeansClusterer(inputFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kmc.setParameters(5, 92378);
		kmc.cluster();
		
		ArrayList<Record> denormalizedRecords = kmc.recordsCopy();
		int[] clustersRecordsBelongTo = kmc.clustersThatRecordsBelongToCopy();
		int numClusters = kmc.getNumberOfClusters();
		kmIO.writeClusteredRecordsToFile(outputFile, denormalizedRecords, clustersRecordsBelongTo, numClusters);
		
		System.out.println(String.format("Clustering records with %s clusters.", kmc.getNumberOfClusters()));
		System.out.println(String.format("sum squared error: %f", kmc.sumSquaredError()));
	}

}
