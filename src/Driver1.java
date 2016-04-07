
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
		//System.out.println(kmc.toString());
		kmc.setParameters(5, 92378);
		kmc.cluster();
		kmc.writeClusteredRecordsToFile(outputFile);
	}

}
