package image_compression;

import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageDecompression {

	public static void main(String[] args) throws Exception {
		String inputFile = "ImageOutputs/compressedTextImage128clusters";
		String outputFile = "ImageOutputs/decompressedTextImage128clusters";

		decompressImageAndSave(inputFile, outputFile);
	}

	public static void decompressImageAndSave(String inputFile, String outputFile) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(inputFile), Charset.defaultCharset());
		int numberOfCols = 512 / 2;
		ArrayList<Integer> compressedImagePixels = new ArrayList<>();
		// ArrayList<Double> decompressedImagePixels = new ArrayList<>();
		HashMap<Integer, double[]> clusterCentroids = new HashMap<>();
		for (String line : lines) {
			String[] comps = line.split(" ");
			if (comps.length > 3) {// dealing with compressed image
				for (String comp : comps) {
					compressedImagePixels.add(Integer.parseInt(comp));
				}
			} else {// dealing with cluster centroids
				Integer key = Integer.parseInt(comps[0]);
				double c1 = Double.parseDouble(comps[1]);
				double c2 = Double.parseDouble(comps[2]);
				double[] centroid = new double[] { c1, c2 };
				clusterCentroids.put(key, centroid);
			}
		}

		int ithPixel = 0; // pixel in compressed image
		StringBuffer sBuffer = new StringBuffer("");
		while (ithPixel < compressedImagePixels.size()) {
			for (int colIndex = 0; colIndex < numberOfCols; colIndex++, ithPixel++) {
				double[] centroid = clusterCentroids.get(compressedImagePixels.get(ithPixel));
				sBuffer.append(String.format("%d %d ", Math.round(centroid[0]), Math.round(centroid[1])));
			}
			sBuffer.replace(sBuffer.length() - 1, sBuffer.length(), "\n");
		}
		sBuffer.replace(sBuffer.length() - 1, sBuffer.length(), "");

		PrintWriter pWriter = new PrintWriter(outputFile);
		pWriter.print(sBuffer);
		pWriter.close();
	}
}