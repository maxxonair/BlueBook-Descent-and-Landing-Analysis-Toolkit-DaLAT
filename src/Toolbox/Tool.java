
package Toolbox;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Tool{
	public static double LinearInterpolate( double atm_x[] , double atm_y[] , double xx)
	{
	double yvalue=0;
	double y1 = 0,y2 = 0,x1 = 0,x2 = 0;
	int lines=atm_x.length;
	//............................................
	for(int ii=lines-1;ii>0;ii--)
	{
	if(atm_x[ii]>xx){
	y1 = atm_y[ii];
	x1 = atm_x[ii];
	}
	}
	//............................................
	for(int ii=0;ii<lines-1;ii++)
	{
	if(atm_x[ii]<xx){
	y2 = atm_y[ii];
	x2 = atm_x[ii];
	}
	}
	//...........................................
	if(xx<=atm_x[0]){
	y1 = atm_y[0];
	y2 = atm_y[1];
	x1 = atm_x[0];
	x2 = atm_x[1];
	//System.out.println("low limit reached");
	}
	if (xx>=atm_x[lines-1])
	{
	y1 = atm_y[lines-2];
	y2 = atm_y[lines-1];
	x1 = atm_x[lines-2];
	x2 = atm_x[lines-1];
	//System.out.println("high limit reached");
	}
    yvalue = y1 + ( y2 - y1 ) * ( xx - x1 ) / ( x2 - x1 ) ;
	//System.out.println(x1 + "|" + x2+ "|" +y1+ "|" +y2+ "|" +yvalue+ "|" +lines);
	return yvalue;
	}
	//---------------------------------------------------------------------------------------------------------------------
	//
	//			Read data from external file: 
	//
	//---------------------------------------------------------------------------------------------------------------------
    public static double[] READ_INTEGRATOR_INPUT(String IntegratorInputFile) throws IOException{
    double[] readINP = new double[10];
    String INPUT_FILE = IntegratorInputFile;
    	double InitialState = 0;
	    FileInputStream fstream = null;
	    try{
	    fstream = new FileInputStream(INPUT_FILE);
	    } catch(IOException eIO) { System.out.println(eIO);}
        DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        int k = 0;
        try {
        while ((strLine = br.readLine()) != null )   {
        	String[] tokens = strLine.split(" ");
        	InitialState = Double.parseDouble(tokens[0]);
        	readINP[k]= InitialState;
        	k++;
        }
    		fstream.close();
    		in.close();
    		br.close();
        System.out.println("READ: Integrator setup successful.");
        } catch(NullPointerException eNPE) { System.out.println(eNPE); System.out.println("Error: Integrator setup read failed.");}
        return readINP;
    }
    
    public static double[] READ_PROPULSION_INPUT(String PropulsionInputFile) throws IOException{
    double[] readINP = new double[10];
    String INPUT_FILE = PropulsionInputFile;
    	double InitialState = 0;
	    FileInputStream fstream = null;
	    try{
	    fstream = new FileInputStream(INPUT_FILE);
	    } catch(IOException eIO) { System.out.println(eIO);}
        DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        int k = 0;
        try {
        while ((strLine = br.readLine()) != null )   {
        	String[] tokens = strLine.split(" ");
        	try {
        	InitialState = Double.parseDouble(tokens[0]);
        	} catch (java.lang.NumberFormatException eNFE) {
        		System.out.println("ERROR: Read proplsion value failed. Index: "+k );
        	}
        	readINP[k]= InitialState;
        	k++;
        }
        fstream.close();
        in.close();
        br.close();
        
        System.out.println("READ: Propulsion setup successful.");
        } catch(NullPointerException eNPE) { System.out.println(eNPE); System.out.println("Error: Propulsion setup read failed.");}
        return readINP;
    }
    
    public static double[] READ_SPACECRAFT_INPUT(String SpacecraftInputFile) throws IOException{
    double[] readINP = new double[10];
    String INPUT_FILE = SpacecraftInputFile;
    	double InitialState = 0;
	    FileInputStream fstream = null;
	    try{
	    fstream = new FileInputStream(INPUT_FILE);
	    } catch(IOException eIO) { System.out.println(eIO);}
        DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        int k = 0;
        try {
        while ((strLine = br.readLine()) != null )   {
        	String[] tokens = strLine.split(" ");
        	InitialState = Double.parseDouble(tokens[0]);
        	readINP[k]= InitialState;
        	k++;
        }
        fstream.close();
        in.close();
        br.close();
        System.out.println("READ: Spacecraft setup successful.");
        } catch(NullPointerException eNPE) { System.out.println(eNPE); System.out.println("Error: Spacecraft setup read failed.");}
        return readINP;
    }
	//---------------------------------------------------------------------------------------------------------------------
	//
	//			Linear Algebra service functions:  
	//
	//---------------------------------------------------------------------------------------------------------------------    
    public static double[][] Multiply_Matrices(double[][] A, double[][] B) {
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
        }

        double[][] C = new double[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                C[i][j] = 0.00000;
            }
        }

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        return C;
    }
    public static double[][] Multiply_Scalar_Matrix(double scalar, double[][] M){
    	int rows    = M.length;
    	int columns = M[0].length; 
    	double[][] result = new double[rows][columns];
        for (int i = 0; i < rows; i++) { // aRow
            for (int j = 0; j < columns; j++) { // bColumn
            	result[i][j] = M[i][j] * scalar; 
            }
            }
    	return result; 
    }
    public static double[][] Substract_Matrices(double[][] A, double[][] B){
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;
    	double[][] C = new double[aRows][aColumns];
    	
        if (aRows!=bRows || aColumns!=bColumns) {
        	System.out.println("ERROR: Matrix dimensions do not match");
        	return C; 
        } else {
	        for (int i = 0; i < aRows; i++) { // aRow
	            for (int j = 0; j < bColumns; j++) { // bColumn
	                    C[i][j] = A[i][j] - B[i][j];
	                }
	            }
	            return C; 
        }
    }
    public static double[][] Addup_Matrices(double[][] A, double[][] B){
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;
    	double[][] C = new double[aRows][aColumns];
    	
        if (aRows!=bRows || aColumns!=bColumns) {
        	System.out.println("ERROR: Matrix dimensions do not match");
        	return C; 
        } else {
	        for (int i = 0; i < aRows; i++) { // aRow
	            for (int j = 0; j < bColumns; j++) { // bColumn
	                    C[i][j] = A[i][j] + B[i][j];
	                }
	            }
	            return C; 
        }
    }
    public static double[][] Inverse_Matrix(double[][] A){
        int aRows = A.length;
        int aColumns = A[0].length;
    	double[][] X = new double[aRows][aColumns];
    			
    			return X; 
    }
}