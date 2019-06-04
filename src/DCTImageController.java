/******************************************************************************/
/****************************IMAGE CONTROLLER**********************************/
/*********************Perform actions on actual Image**************************/
/******************************************************************************/
/******************************************************************************/

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import org.jtransforms.dct.DoubleDCT_2D;

public class DCTImageController {
	
	private int[][] image;
    private double[][] doubleImage;
	private int width;
	private int height;
	private int F;
	private int d;
	
	
	//Constructor
	public DCTImageController() {}
	
	
	//GETTERS & SETTERS
	public int[][] getImage() {
		return image;
	}
	

	public int getWidth() {
		return width;
	}
	

	public int getHeight() {
		return height;
	}
	
	
	//Compression Method: transforms the image to raster and compress image by F and d parameters
	public BufferedImage compressImage(BufferedImage dctImage, int F, int d) throws Exception{
		
		this.F = F;
		this.d = d;
		
		this.doubleImage = imageToPixelsDouble(dctImage); //transform the image in a matrix (raster)
		
		//Execute compression method, then transforms the matrix in a image
		return this.pixelsToImageDouble(executeCompression(this.doubleImage, this.height, this.width));

	}
	
	
	//Transforms an image to a matrix (raster) - double
	public double[][] imageToPixelsDouble(BufferedImage image) {
		
		Raster raster = image.getData(); //get raster
		
		//get Width & Height
		this.width = raster.getWidth();
		this.height = raster.getHeight();
		
		//Instantiate new matrix [height][width]
		double[][] pixels = new double[this.height][this.width];
		
		//Fill the matrix with pixels
		for(int j = 0; j < this.height; j++) {
			for(int k = 0; k < this.width; k++) {
				pixels[j][k] = raster.getSample(k, j, 0);
			}
		}
		
		return pixels;
		
	}
	

	//Transforms a matrix (raster) in a bufferedImage - double
	public BufferedImage pixelsToImageDouble(double[][] pixels) {
		
		//Instantiate new blank bufferedImage
		BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_BYTE_GRAY);
		
		//Get raster from bufferedImage
		WritableRaster wr = image.getRaster();
		
		//Populate raster
		for(int i = 0; i < this.height; i++) {
			for(int j = 0; j < this.width; j++) {
				wr.setSample(j, i, 0, pixels[i][j]);
			}
		}
		
		//Set image data with data raster
		image.setData(wr);
		return image;
	}
        
	
	//Execute compression on a pixel matrix
	public double[][] executeCompression(double image[][], int height, int width) throws Exception{
		
		//calculate image's pixel blocks by F
		int widthBlocks = (width/F);
		int heightBlocks = (height/F);
		int resultBlocks = widthBlocks*heightBlocks;
		
		//3D array will contain N blocks of image FxF pixels
		double imageBlocks [][][] = new double [resultBlocks][F][F];
		
		//Populate block array
		int i, j, k, r, q, w, a, z; //indexes
		for (i=0; i<heightBlocks; i++)
			for (j=0; j<widthBlocks; j++)
				for(k=z=F*i, q=0; k<z+F && q < F; k++, q++)
					for(r=a=F*j, w=0; r<a+F && w < F; r++, w++)
						imageBlocks[(i*widthBlocks)+j][q][w] = image[k][r];
		
		
		//Instantiate new JTransform DoubleDCT_2D matrix [F][F]
		DoubleDCT_2D idct2 = new DoubleDCT_2D(2, 2);
		try {
			idct2 = new DoubleDCT_2D(F, F);
		}catch (Exception e){}
			

		//Execute DCT2 on image's blocks
		for(i=0; i<resultBlocks; i++)	
			idct2.forward(imageBlocks[i], true);
		
		//Reset frequency component Ckl when k+l >= d
		for(k=0; k<resultBlocks; k++)
			for(i = 0; i < F; i++) {
				for(j = 0; j < F; j++) {
					if(i+j >= d)
						imageBlocks[k][i][j] = 0;
				}    
			}
		
		
		//Execute inverse DCT2 on image's blocks
		for(i=0; i<resultBlocks; i++)
			idct2.inverse(imageBlocks[i], true);
		
		//reset negative values and set to 255 values higher than 255
		for(k=0; k<resultBlocks; k++)
			for(i = 0; i < F; i++) {
				for(j = 0; j < F; j++) {
					if(imageBlocks[k][i][j] > 255.0)
						imageBlocks[k][i][j] = 255;
					else if(imageBlocks[k][i][j] < 0.0) {
						imageBlocks[k][i][j] = 0;
					}
				}    
			}
		
		//Instanciate new result matrix
		double [][] result = new double [heightBlocks*F][widthBlocks*F];

		
		//re-populate original matrix	
		for (i=0; i<heightBlocks; i++)
			for (j=0; j<widthBlocks; j++)
				for(k=z=F*i, q=0; k<z+F && q < F; k++, q++)
					for(r=a=F*j, w=0; r<a+F && w < F; r++, w++)
						result[k][r] = imageBlocks[(i*widthBlocks)+j][q][w];
		
		
		//new image size
		this.width = widthBlocks*F;
		this.height = heightBlocks*F;

		return result;          
		
	}
        
	//Print matrix's content - double
	public void printMatrix(double[][] matrix, int n) {
		
		if(n == 1)
			System.out.println("Custom Matrix: ");
		else
			System.out.println("Jtransform Matrix: ");
		
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[0].length; j++)
				System.out.print(matrix[i][j] + " ");
			System.out.println("");
		}
	}
}