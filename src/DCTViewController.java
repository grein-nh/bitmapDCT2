/**************************************************************************/
/****************************VIEW CONTROLLER*******************************/
/****************Take actions from View and manage operations**************/
/**************************************************************************/
/**************************************************************************/

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import org.jtransforms.dct.DoubleDCT_1D;
import org.jtransforms.dct.DoubleDCT_2D;


public class DCTViewController {

	private DCTImageController imageController;
	private final CustomDCT2 dctExecutor;
	private long originalImageSize;
	private long compressedImageSize;
	
	//Constructor
	public DCTViewController() {
		this.imageController = new DCTImageController();
		this.dctExecutor = new CustomDCT2();
	}
	
	
	//********************GETTERS - SETTERS*********************
	public long getOriginalImageSize() {
		return originalImageSize;
	}
	
	
	public long getCompressedImageSize() {
		return compressedImageSize;
	}
	
	public void setOriginalImageSize (long originalImageSize) {
		this.originalImageSize = originalImageSize;
	}
	
	//**********************************************************
	
	
	//This method converts an image in a matrix, applies the DCT2 (Jtransform) algorithm to the matrix 
	//Then re-converts the DCT2 matrix in a image
	public BufferedImage getDCT2Image(BufferedImage originalImage) {
		
		imageController = new DCTImageController();
		
		//Transform the image in a matrix [N][N]
		double[][] DCT2ImageToConvert = imageController.imageToPixelsDouble(originalImage); //give control to imageController
		
		//Instantiate DCT2 class
		DoubleDCT_2D dct2dtest = new DoubleDCT_2D(DCT2ImageToConvert.length, DCT2ImageToConvert[0].length);
		
		//calculate DCT2 matrix
		dct2dtest.forward(DCT2ImageToConvert, true); //true param
		
		//Transform the DCT2 matrix in an image
		BufferedImage DCT2ImageConverted = imageController.pixelsToImageDouble(DCT2ImageToConvert);
		
		return DCT2ImageConverted;
		
	}
	
	
	//Compress a bufferedImage 
	public BufferedImage compressImage(BufferedImage dctImage, int F, int d) throws Exception{
		
		//give control to the image controller - compress image method
		BufferedImage outputImage = this.imageController.compressImage(dctImage, F, d);
		
		//Create a test file container
		File outputImageFile = new File("DCTimage.jpg");
		//Trying to "write" the compressed bufferedImage in the test file container
		//If cannot write the image on the file, the compressing process didn't go well
		try {
			ImageIO.write(outputImage, "jpg", outputImageFile);
		} catch (IOException e){ 
			System.out.println("Error during compressing process");} 
		
		this.compressedImageSize = outputImageFile.length();
		
		return outputImage;
		
	}
	
	
	//DCT2 Test - Verify Jtransform/Custom DCT2 algorithm's scaling
	public String[] executeTest() {
		
			//Instantiate DoubleDCT_2D (Jtransform) matrix 8x8
			DoubleDCT_2D dct2dtest = new DoubleDCT_2D(8, 8);
			
			//Declare an original test matrix 8x8
			double [][] matrix = new double [][] {
				{231,32, 233, 161, 24, 71, 140, 245},
				{247, 40, 248, 245, 124, 204, 36, 107},
				{234, 202, 245, 167, 9, 217, 239, 173},
				{193, 190, 100, 167, 43, 180, 8, 70},
				{11, 24, 210, 177, 81, 243, 8, 112},
				{97, 195, 203, 47, 125, 114, 165, 181},
				{193, 70, 174, 167, 41, 30, 127, 245},
				{87, 149, 57, 192, 65, 129, 178, 228}
			};
			
			//Declare a copy of the original test matrix 8x8
			double [][] matrixCopy = new double [][] {
				{231,32, 233, 161, 24, 71, 140, 245},
				{247, 40, 248, 245, 124, 204, 36, 107},
				{234, 202, 245, 167, 9, 217, 239, 173},
				{193, 190, 100, 167, 43, 180, 8, 70},
				{11, 24, 210, 177, 81, 243, 8, 112},
				{97, 195, 203, 47, 125, 114, 165, 181},
				{193, 70, 174, 167, 41, 30, 127, 245},
				{87, 149, 57, 192, 65, 129, 178, 228}
			};
			
			
			//Instantiate DoubleDCT_1D (Jtransform) array [8]
			DoubleDCT_1D dct1dtest = new DoubleDCT_1D(8);
			
			//Declare new array [8]
			double[] array = new double[]{231, 32, 233, 161, 24, 71, 140, 245};
			
			//Final string for Jtransform DCT2 test result 
			String testJtransform = "<html>Matrix [8x8]: <br/><br/>";
			
			//Saving the original matrix in the final string
			for(int i = 0; i < matrix.length; i++) {
				for(int j = 0; j < matrix[0].length; j++){
					testJtransform += matrix[i][j] + " ";
				}
				testJtransform = testJtransform + "<br>";
			}
			
			//Execute DCT2 on the original matrix (Jtransform)
			dct2dtest.forward(matrix,true);
			
			//Declare new matrix (array copy) 8x1
			double[][] arrayCopy = new double[8][1];
			
			//Fill the matrix with the 1D array
			for(int i = 0; i < array.length; i++) {
				arrayCopy[i][0] = array[i];
			}
		
			//Save the original 1D array in the final string
			testJtransform += "<br/><br/>Array [8]: <br/><br/>";
			for(int i = 0; i < array.length; i++) {
				testJtransform += array[i] + " ";
			}
			
			//Save the calculated DCT2 matrix in the final string
			testJtransform += "<br/><br/><br/>Matrix [8x8] after JTransform DCT2 Execution: <br/><br/>";
			for(int i = 0; i < matrix.length; i++) {
				for(int j = 0; j < matrix[0].length; j++) {
					testJtransform += String.format("%6.3e", matrix[i][j]) + " ";
				}
				testJtransform = testJtransform + "<br/>";
			}
			
			//Execute DCT1 on the original array
			dct1dtest.forward(array, true);
			
			//Save the calculated DCT1 array in the final string
			testJtransform += "<br/><br/>Array [8] after Jtransform DCT1 Execution: <br/><br/>";
			for(int i = 0; i < array.length; i++) {
				testJtransform += String.format("%6.3e", array[i]) + " ";
			}
			testJtransform += "</html>";

			
			//Final string for custom DCT2 test 
			String testDctLowperf = "<html>Matrix [8x8]: <br/><br/>";
			
			//Save the original matrix (copy) in the final string 
			for(int i = 0; i < matrixCopy.length; i++) {
				for(int j = 0; j < matrixCopy[0].length; j++){
					testDctLowperf += matrixCopy[i][j] + " ";
				}
				testDctLowperf = testDctLowperf + "<br>";
			}
			
			//Save the original array (copy) in the final string 
			testDctLowperf += "<br/><br/>Array [8]: <br/><br/>";
			for(int i = 0; i < arrayCopy.length; i++) {
				testDctLowperf += arrayCopy[i][0] + " ";
			}
			
			//Apply custom DCT2 on original matrix (copy)
			applyDCT(matrixCopy);
					
			//Apply custom DCT1 on original array (copy)
			applyDCT(arrayCopy);

			//Save the calculated DCT2 Matrix (copy) in the final string
			testDctLowperf += "<br/><br/><br/>Matrix [8x8] after Custom DCT2 Execution: <br/><br/>";
			for(int i = 0; i < matrixCopy.length; i++) {
				for(int j = 0; j < matrixCopy[0].length; j++) {
					testDctLowperf += String.format("%6.3e", matrixCopy[i][j]) + " ";
				}
				testDctLowperf = testDctLowperf + "<br/>";
			}
			
			//Save the calculated DCT1 Array (copy) in the final string
			testDctLowperf += "<br/><br/>Array [8] afer Custom DCT1 Execution: <br/><br/>";
			for(int i = 0; i < array.length; i++) {
				testDctLowperf += String.format("%6.3e", arrayCopy[i][0]) + " ";
			}
			testDctLowperf += "</html>";
			
			//compose final output string
			String[] outputString = new String[] {testJtransform, testDctLowperf};
			
			return outputString;
	}
	
	
	//Execute custom DCT2 on a matrix [n][m]
	public double[][] applyDCT (double[][] matrix) {
		this.dctExecutor.applyDCT(matrix); //execute DCT2 - give control to CustomDCT2 class 
		return matrix;
	}
	
	//Execute custom DCT2 on a matrix [n][n]
	public double[][] applyDCT (int n) {
		
		//Declare NxN matrix
		double matrix[][] = new double[n][n]; //matrix for custom DCT
		double matrixJtransform [][] = new double[n][n]; //matrix for Jtransform DCT
		DoubleDCT_2D dct2dtest = new DoubleDCT_2D(n, n);
                
		long seed = 1;
		Random r = new Random(seed); //random number
		
		//Fill matrices with random longs 
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[0].length; j++) {
				double randomValue = r.nextInt(255);
				matrix[i][j] = randomValue;
				matrixJtransform[i][j] = randomValue;
			}
		}
                
		//Custom DCT2 on random matrix
		long startTimeLowperf = System.nanoTime(); //start counting
		this.dctExecutor.applyDCT(matrix); //apply custom DCT2 on matrix
		long endTimeLowperf = System.nanoTime(); //stop counting
		long durationLowperf = (endTimeLowperf - startTimeLowperf)/1000;
		System.out.println("Execution time of custom DCT2: " + durationLowperf);
		
		//Jtransform DCT2 on random matrix 
		long startTimeJtransform = System.nanoTime();
		dct2dtest.forward(matrixJtransform, true); //apply Jtransform DCT2 on matrix
		long endTimeJtransform = System.nanoTime();
		long durationJtransform = (endTimeJtransform - startTimeJtransform)/1000;
		System.out.println("Execution time of Jtransform DCT2: " + durationJtransform);
		
		//Paint timing Graph
		DCTGraph.paintGraph(durationLowperf, durationJtransform);

		return matrix;
	}

}	
	
	