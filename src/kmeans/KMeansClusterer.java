package kmeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeSet;

public class KMeansClusterer {
    private ArrayList<Record> records;

    public ArrayList<Record> recordsCopy() {
	ArrayList<Record> copy = new ArrayList<>();
	for (Record record : this.records) {
	    copy.add(new Record(record.getAttrList()));
	}
	return copy;
    }

    private int numberOfClusters;

    public int getNumberOfClusters() {
	return this.numberOfClusters;
    }

    private long seed;
    private boolean hasSetParameters = false;
    private boolean hasPerformedClustering = false;
    private HashMap<Integer, double[]> clusterCentroids;

    public HashMap<Integer, double[]> clusterCentroidsCopy() {
	HashMap<Integer, double[]> centroidsCopy = new HashMap<>();
	for (Integer key : this.clusterCentroids.keySet()) {
	    double[] centroid = this.clusterCentroids.get(key);
	    centroidsCopy.put(key, Arrays.copyOf(centroid, centroid.length));
	}
	return centroidsCopy;
    }

    private int[] clustersThatRecordsBelongTo;

    public int[] clustersThatRecordsBelongToCopy() {
	return Arrays.copyOf(this.clustersThatRecordsBelongTo, this.clustersThatRecordsBelongTo.length);
    }

    private int MAX_ITERATIONS = 10;

    public KMeansClusterer(ArrayList<Record> records) {
	this.records = records;
	this.numberOfClusters = 4;
	this.seed = 43546903;
	this.clustersThatRecordsBelongTo = new int[this.records.size()];
    }

    public boolean setParameters(int numberOfClusters, int seed) {
	if (this.hasSetParameters == false) {
	    if (numberOfClusters > this.records.size()) {
		System.out.println("error: number of clusters cannot be greater than number of records.");
		return false;
	    }
	    this.numberOfClusters = numberOfClusters;
	    this.seed = seed;
	    this.clusterCentroids = new HashMap<>();
	    this.hasSetParameters = true;
	    return true;
	} else {
	    System.out.println("error: you may only set the parameters once for an instance of this class.");
	    return false;
	}
    }

    private void initializeCentroids() {
	Random rng = new Random(this.seed);
	TreeSet<Integer> selectedIndicesOfRecords = new TreeSet<>();
	int i = 0;
	while (i < this.numberOfClusters) {
	    int index = rng.nextInt(this.records.size());
	    if (selectedIndicesOfRecords.contains(index) == false) {
		this.clusterCentroids.put(i, this.records.get(index).getAttrList());
		selectedIndicesOfRecords.add(index);
		i++;
	    }
	}
    }

    public void cluster() {
	if (this.hasPerformedClustering == false) {
	    if (this.hasSetParameters == false) {
		System.out.println("you must set the parameters before performing k-means clustering.");
		return;
	    }
	    this.initializeCentroids();
	    int counter = 0;
	    while (counter < this.MAX_ITERATIONS) {
		this.assignToClosestClusters();
		this.clusterCentroids = this.computeCentroids();
		counter++;
		System.out.println(String.format("Iteration %d: ", counter));
		System.out.println(this.centroidString());
	    }
	    this.hasPerformedClustering = true;
	} else {
	    System.out.println("You may not perform clustering twice.");
	    System.out.println("Instead, recreate a new KMeansClusterer.");
	}
    }

    private void assignToClosestClusters() {
	for (int i = 0; i < this.records.size(); i++) {
	    double[] recordAttrs = this.records.get(i).getAttrList();
	    int closestCluster = Integer.MAX_VALUE;
	    double closestDistance = Double.MAX_VALUE;
	    for (int j = 0; j < this.clusterCentroids.size(); j++) {
		double[] centroidAttrs = this.clusterCentroids.get(j);

		double dist = this.euclideanDistance(recordAttrs, centroidAttrs);
		if (dist < closestDistance) {
		    closestDistance = dist;
		    closestCluster = j;
		}
	    } // now I have found the cluster that is closest to the record
	    this.clustersThatRecordsBelongTo[i] = closestCluster;
	}
    }

    private HashMap<Integer, double[]> computeCentroids() {
	HashMap<Integer, double[]> newCentroids = new HashMap<>();
	int[] quantityInCluster = new int[this.numberOfClusters];
	// first I get the sum of the cluster, then divide by the mean.
	for (int i = 0; i < this.records.size(); i++) {
	    double[] attr = this.records.get(i).getAttrList();
	    int cluster = this.clustersThatRecordsBelongTo[i];
	    if (newCentroids.containsKey(cluster) == false) {
		double[] firstPoint = Arrays.copyOf(attr, attr.length);
		newCentroids.put(cluster, firstPoint);
	    } else {// getting the sum of the points in a cluster
		double[] developingCentroid = newCentroids.get(cluster);
		for (int j = 0; j < developingCentroid.length; j++) {
		    developingCentroid[j] += attr[j];
		}
	    }
	    quantityInCluster[cluster]++;
	}

	for (Integer cluster : this.clusterCentroids.keySet()) {
	    if (newCentroids.containsKey(cluster)) {
		double[] theCentroid = newCentroids.get(cluster);
		for (int k = 0; k < theCentroid.length; k++) {
		    theCentroid[k] /= quantityInCluster[cluster];
		}
	    } else {// if the new set of centroids has no records
		double[] origionalAttrs = this.clusterCentroids.get(cluster);
		double[] sameAttr = Arrays.copyOf(origionalAttrs, origionalAttrs.length);
		newCentroids.put(cluster, sameAttr);
	    }
	}
	return newCentroids;
    }

    private double euclideanDistance(double[] p1, double[] p2) {
	assert p1.length == p2.length;
	ArrayList<Double> sq_err = new ArrayList<>();
	for (int i = 0; i < p1.length; i++) {
	    sq_err.add(Math.abs(p1[i] - p2[i]));
	}
	double sum_sq_err = sq_err.stream().map(x -> x * x).reduce((x, y) -> x + y).get();
	return Math.sqrt(sum_sq_err);
    }

    public double sumSquaredError() {
	double sum = 0.0;
	int recordIndex = 0;
	for (Record record : this.records) {
	    double[] attrs = record.getAttrList();
	    int clusterOfRecord = this.clustersThatRecordsBelongTo[recordIndex];
	    double[] centroid = this.clusterCentroids.get(clusterOfRecord);
	    double dist = this.euclideanDistance(attrs, centroid);
	    sum += Math.pow(dist, 2);
	    recordIndex++;
	}
	return sum;
    }

    public String centroidString() {
	StringBuffer sb = new StringBuffer("");
	for (Integer cluster : this.clusterCentroids.keySet()) {
	    sb.append(String.format("Cluster %d: ", cluster));
	    double[] centroid = this.clusterCentroids.get(cluster);
	    for (double number : centroid) {
		sb.append(String.format("%.2f, ", number));
	    }
	    sb.replace(sb.length() - 2, sb.length(), "\n");// replace ", " with
							   // "\n" at end
	}
	return sb.toString();
    }

    @Override
    public String toString() {
	StringBuffer sb = new StringBuffer("");

	for (int i = 0; i < this.numberOfClusters; i++) {
	    sb.append(String.format("Cluster %d\n", i));
	    for (int k = 0; k < this.records.size(); k++) {
		if (this.clustersThatRecordsBelongTo[k] == i) {
		    sb.append(String.format("%s\n", this.records.get(k).toString()));
		}
	    }
	}
	sb.replace(sb.length() - 1, sb.length(), "");
	return sb.toString();
    }

}
