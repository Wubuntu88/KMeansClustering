package image_compression;

import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kmeans.KMeansClusterer;
import kmeans.Record;

public class ImageCompression {

    public static void main(String[] args) throws Exception {
	String inputFile = "imagefile";
	//String clusterOutputs = "ClusterOutputs/8Clusters.txt";
	String compressedImageFile = "ImageOutputs/compressedTextImage128clusters";

	int origRows = 512;
	int origCols = 512;

	final int NUMBER_OF_CENTROIDS = 128;
	ArrayList<Record> records = new ArrayList<>();
	List<String> lines = Files.readAllLines(Paths.get(inputFile), Charset.defaultCharset());
	for (String line : lines) {
	    String[] comps = line.split(" ");
	    for (int i = 0; i < comps.length - 1; i += 2) {
		double x1 = Double.parseDouble(comps[i]);
		double y1 = Double.parseDouble(comps[i + 1]);
		Record record = new Record(new double[] { x1, y1 });
		records.add(record);
	    }
	}
	KMeansClusterer kMeansClusterer = new KMeansClusterer(records);
	kMeansClusterer.setParameters(NUMBER_OF_CENTROIDS, 92378);
	kMeansClusterer.cluster();

	int[] clustersThatRecordsBelongTo = kMeansClusterer.clustersThatRecordsBelongToCopy();
	HashMap<Integer, double[]> clusterCentroids = kMeansClusterer.clusterCentroidsCopy();

	int numberOfColumnsAfterCompression = 512 / 2;
	writeCompressedImageToFile(compressedImageFile, clustersThatRecordsBelongTo, clusterCentroids,
		numberOfColumnsAfterCompression);
	// System.out.println(kMeansClusterer);
	// ImageCompression.writeClusteredRecordsToFile(outputFile,
	// kMeansClusterer);
    }

    public static void writeCompressedImageToFile(String fileName, int[] clustersThatRecordsBelongTo,
	    HashMap<Integer, double[]> clusterCentroids, int numberOfCols) throws Exception {
	StringBuffer sBuffer = new StringBuffer("");
	int clusterArrayIndex = 0;
	while (clusterArrayIndex < clustersThatRecordsBelongTo.length) {
	    for (int colIndex = 0; colIndex < numberOfCols; colIndex++, clusterArrayIndex++) {
		sBuffer.append(String.format("%d ", clustersThatRecordsBelongTo[clusterArrayIndex]));
	    }
	    sBuffer.replace(sBuffer.length() - 1, sBuffer.length(), "\n");
	} // now I have all of the points written using their clusters
	  // I must not add on the cluster labels and the centroids that
	  // correspond to them
	for (Integer key : clusterCentroids.keySet()) {
	    double[] centroid = clusterCentroids.get(key);
	    sBuffer.append(String.format("%d %f %f\n", key, centroid[0], centroid[1]));
	}
	sBuffer.replace(sBuffer.length() - 1, sBuffer.length(), "");

	PrintWriter pWriter = new PrintWriter(fileName);
	pWriter.print(sBuffer);
	pWriter.close();
    }

    public static void writeClusteredRecordsToFile(String fileName, KMeansClusterer kmc) throws Exception {
	PrintWriter pw = new PrintWriter(fileName);
	pw.print(String.format("%d clusters\n", kmc.getNumberOfClusters()));
	pw.print(kmc.toString());
	pw.close();
    }
}
/*
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
