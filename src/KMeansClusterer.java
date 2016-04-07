import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeSet;

public class KMeansClusterer {
	private ArrayList<Record> records;
	private int numberOfClusters;
	private long seed;
	private boolean hasSetParameters = false;
	private boolean hasPerformedClustering = false;
	private HashMap<Integer, double[]> clusterCentroids;
	private int[] clustersThatRecordsBelongTo;
	private int MAX_ITERATIONS = 100;
	
	public KMeansClusterer(ArrayList<Record> records) {
		this.records = records;
		this.numberOfClusters = 4;
		this.seed = 43546903;
		this.clustersThatRecordsBelongTo = new int[this.records.size()];
	}
	
	public boolean setParameters(int numberOfClusters, int seed){
		if(hasSetParameters == false){
			if(numberOfClusters > this.records.size()){
				System.out.println("error: number of clusters cannot be greater than number of records.");
				return false;
			}
			this.numberOfClusters = numberOfClusters;
			this.seed = seed;
			this.clusterCentroids = new HashMap<>();
			this.hasSetParameters = true;
			return true;
		}else{
			System.out.println("error: you may only set the parameters once for an instance of this class.");
			return false;
		}
	}
	
	private void initializeCentroids(){
		Random rng = new Random(this.seed);
		TreeSet<Integer> selectedIndicesOfRecords = new TreeSet<>();
		int i = 0;
		while(i < numberOfClusters){
			int index = rng.nextInt(this.records.size());
			if(selectedIndicesOfRecords.contains(index) == false){
				clusterCentroids.put(i, records.get(index).getAttrList());
				selectedIndicesOfRecords.add(index);
				i++;
			}
		}
		
	}
	
	public void cluster(){
		if(hasSetParameters == false){
			System.out.println("you must set the parameters before performing k-means clustering.");
			return;
		}
		initializeCentroids();
		int counter = 0;
		while(counter < MAX_ITERATIONS){
			assignToClosestClusters();
			clusterCentroids = computeCentroids();
			counter++;
		}
		hasPerformedClustering = true;
	}
	
	private void assignToClosestClusters(){
		for(int i = 0; i < records.size();i++){
			double[] recordAttrs = records.get(i).getAttrList();
			int closestCluster = Integer.MAX_VALUE;
			double closestDistance = Double.MAX_VALUE;
			for(int j = 0; j < clusterCentroids.size(); j++){
				double[] centroidAttrs = clusterCentroids.get(j);
				
				double dist = euclideanDistance(recordAttrs, centroidAttrs);
				if(dist < closestDistance){
					closestDistance = dist;
					closestCluster = j;
				}
			}//now I have found the cluster that is closest to the record
			clustersThatRecordsBelongTo[i] = closestCluster;
		}
	}
	
	private HashMap<Integer, double[]> computeCentroids(){
		HashMap<Integer, double[]> newCentroids = new HashMap<>();
		int[] quantityInCluster = new int[numberOfClusters];
		//first I get the sum of the cluster, then divide by the mean.
		for(int i = 0; i < records.size(); i++){
			double[] attr = records.get(i).getAttrList();
			int cluster = clustersThatRecordsBelongTo[i];
			if (newCentroids.containsKey(cluster) == false) {
				double[] firstPoint = Arrays.copyOf(attr, attr.length);
				newCentroids.put(cluster, firstPoint);
			} else {//getting the sum of the points in a cluster
				double[] developingCentroid = newCentroids.get(cluster);
				for(int j = 0; j < developingCentroid.length;j++){
					developingCentroid[j] += attr[j];
				}
			}
			quantityInCluster[cluster]++;
		}
		
		for(Integer cluster: clusterCentroids.keySet()){
			if (newCentroids.containsKey(cluster)) {
				double[] theCentroid = newCentroids.get(cluster);
				for(int k = 0; k < theCentroid.length;k++){
					theCentroid[k] /= quantityInCluster[cluster];
				}
			}else{//if the new set of centroids has no records
				double[] origionalAttrs = clusterCentroids.get(cluster);
				double[] sameAttr = Arrays.copyOf(origionalAttrs, origionalAttrs.length);
				newCentroids.put(cluster, sameAttr);			
			}
		}
		return newCentroids;
	}
	
	private double euclideanDistance(double[] p1, double[] p2){
		assert p1.length == p2.length;
		ArrayList<Double> sq_err = new ArrayList<>();
		for(int i = 0; i < p1.length; i++){
			sq_err.add(Math.abs(p1[i] - p2[i]));
		}
		double sum_sq_err = sq_err.stream().map(x -> x * x).reduce( (x, y) -> x + y).get();
		return Math.sqrt(sum_sq_err);
	}
	
	public void writeClusteredRecordsToFile(String fileName){
		if(hasPerformedClustering){
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(fileName);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pw.print(this.toString());
			pw.close();
		}else{
			System.out.println("You must cluster records before writing to a file.");
		}
		
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer("");
		
		for(int i = 0; i < numberOfClusters;i++){
			sb.append(String.format("Cluster %d\n", i));
			for(int k = 0; k < records.size(); k++){
				if (clustersThatRecordsBelongTo[k] == i) {
					sb.append(String.format("%s\n", records.get(k).toString()));
				}
			}
		}
		sb.replace(sb.length() - 1, sb.length(), "");
		return sb.toString();
	}
}
