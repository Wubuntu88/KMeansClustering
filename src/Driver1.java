
public class Driver1 {

	public static void main(String[] args) {
		KMeansIO kmIO = new KMeansIO();
		KMeansClusterer kmc = null;
		try {
			kmc = kmIO.instantiateKMeansClusterer("Archive/file1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(kmc.toString());
		kmc.setParameters(4, 92378);
		kmc.cluster();
		System.out.println(kmc);
	}

}
