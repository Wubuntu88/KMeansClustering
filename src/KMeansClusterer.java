import java.util.ArrayList;

public class KMeansClusterer {
	ArrayList<Record> records;
	int numberOfClusters;
	long seed;
	boolean hasSetParameters = false;
	public KMeansClusterer(ArrayList<Record> records) {
		this.records = records;
		this.numberOfClusters = 4;
		this.seed = 43546903;
	}
	public boolean setParameters(int numberOfClusters, int seed){
		if(hasSetParameters == false){
			this.numberOfClusters = numberOfClusters;
			this.seed = seed;
			return true;
		}
		return false;
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
