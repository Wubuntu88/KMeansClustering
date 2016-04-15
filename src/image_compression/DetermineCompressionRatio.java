package image_compression;

import java.io.File;

public class DetermineCompressionRatio {

	public static void main(String[] args) {
		String origFileName = "imagefile";
		String comp8clusters = "ImageOutputs/compressedTextImage8clusters";
		String comp16clusters = "ImageOutputs/compressedTextImage16clusters";
		String comp32clusters = "ImageOutputs/compressedTextImage32clusters";
		String comp64clusters = "ImageOutputs/compressedTextImage64clusters";
		String comp128clusters = "ImageOutputs/compressedTextImage128clusters";
		double ratio8 = compressionRatio(origFileName, comp8clusters);
		double ratio16 = compressionRatio(origFileName, comp16clusters);
		double ratio32 = compressionRatio(origFileName, comp32clusters);
		double ratio64 = compressionRatio(origFileName, comp64clusters);
		double ratio128 = compressionRatio(origFileName, comp128clusters);
		System.out.printf("compression ratio (8 clusters): %.1f %%\n", ratio8 * 100);
		System.out.printf("compression ratio (16 clusters): %.1f %%\n", ratio16 * 100);
		System.out.printf("compression ratio (32 clusters): %.1f %%\n", ratio32 * 100);
		System.out.printf("compression ratio (64 clusters): %.1f %%\n", ratio64 * 100);
		System.out.printf("compression ratio (128 clusters): %.1f %%\n", ratio128 * 100);
	}
	
	public static double compressionRatio(String originalFileName, String compressedFileName){
		File origFile = new File(originalFileName);
		File compFile = new File(compressedFileName);
		
		double ratio = (double)compFile.length() / origFile.length();
		return ratio;
	}
	
	
}
