/******************************************************************************/
/****************************CUSTOM DCT2 Class*********************************/
/***************Class that implements a custom DCT2 algorithm******************/
/******************************************************************************/
/******************************************************************************/

public class CustomDCT2 {
	
	//Constructor
	public CustomDCT2() {}
	
	
	//Applies Discrete Cosine Transform on a NxM matrix - double
	public void applyDCT(double[][] f){
		
		//Declare an empty DCT matrix
		double[][] dctMatrix = new double[f.length][f[0].length];
		
        for (int u = 0; u < f.length; u++){
          for (int v = 0; v < f[0].length; v++){
			  
				double sum = 0.0;
				//sum will temporarily store the sum of cosine signals 
				for (int i = 0; i < f.length; i++){
				  for (int j = 0; j < f[0].length; j++){            	
					sum = sum + (Math.cos(( ((2 * i + 1) * u * Math.PI)/(2.0 * f.length))) * 
								Math.cos((((2 * j + 1) * v * Math.PI)/(2.0 * f.length))) * f[i][j]);
				  }
				}
				//if 'f' is not an array
				if(f[0].length != 1){
					if(u == 0 && v == 0) //if processing first element of the matrix
						sum = sum / f.length;
					else if (u == 0 || v == 0)
						sum = (sum * Math.sqrt(2.0))/ f.length;
					else {
						sum = sum * (2.0 / (double)f.length);
					}
					dctMatrix[u][v]=sum;
				}
				else{
					if(u == 0 && v == 0)
						sum = sum / Math.sqrt(f.length);
					else
					sum = (sum * Math.sqrt(2.0)) / Math.sqrt(f.length);
					dctMatrix[u][v]=sum;
				}
				
          }
        }
        
		//replace original matrix with DCT matrix
		for (int i = 0; i < f.length; i++){
            for (int j = 0; j < f[0].length; j++){
            	f[i][j] = dctMatrix[i][j];
            }
        }
    }
		
}