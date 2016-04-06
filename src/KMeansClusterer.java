import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class KMeansClusterer {
	ArrayList<Record> records;
	int numberOfClusters;
	long seed;
	boolean hasSetParameters = false;
	HashMap<Integer, double[]> clusterCentroids;
	int[] clustersThatRecordsBelongTo;
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
	
	private void initializeCentroids(HashMap<Integer, double[]> centroids){
		Random rng = new Random(this.seed);
		for(int i = 0; i < this.numberOfClusters;i++){
			int index = rng.nextInt(this.records.size());
			if (clusterCentroids.containsKey(index) == false) {
				clusterCentroids.put(index, records.get(index).getAttrList());
			}
		}
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
	
	private void computeCentroids(){
		
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
	
	public String toString(){
		StringBuffer sb = new StringBuffer("");
		for (Record record : records) {
			sb.append(record.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}
