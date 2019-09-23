
package Toolbox;


public class Mathbox{
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
    @SuppressWarnings("unused")
	public static double[][] Addup_Matrices(double[][] ...M){
        boolean error_missmatch = false;
        int Rows = 0, Columns = 0; 
      //---------------------------------------------------------------------
      //   check that all input matrices have the same dimensions: 
      //---------------------------------------------------------------------
        for(double[][] I: M) {
        	Rows = I.length;
        	Columns = I[0].length;
        	for(double[][] J: M) {
                if(Rows!=J.length || Columns!=J[0].length) {
                	System.out.println("ERROR: Matrix dimensions do not match "+Rows+", "+Columns);
                	error_missmatch=true;
                }
        	}
        }
       //---------------------------------------------------------------------
    	double[][] C = new double[Rows][Columns];
        if (error_missmatch) {
        	//System.out.println("ERROR: Matrix dimensions do not match "+Rows+", "+Columns);
        	return C; 
        } else {
	        for (int i = 0; i < Rows; i++) { // aRow
	            for (int j = 0; j < Columns; j++) { // bColumn
	            	for(double[][] I: M) {
	                    C[i][j] = C[i][j] + I[i][j];
	            	}
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
	public static double[] Spherical2Cartesian_Velocity(double[] X) {
		double[] result = new double[3];
		result[0]  =  X[0] * Math.cos(X[1]) * Math.cos(X[2]);
		result[1]  =  X[0] * Math.cos(X[1]) * Math.sin(X[2]);
		result[2]  = -X[0] * Math.sin(X[1]);
		// Filter small errors from binary conversion: 
		for(int i=0;i<result.length;i++) {if(Math.abs(result[i])<1E-9) {result[i]=0; }}
		if(result[0]==-0.0) {result[0]=0;}
		if(result[1]==-0.0) {result[1]=0;}
		if(result[2]==-0.0) {result[2]=0;}
		return result; 
		}		
	public static double[] Cartesian2Spherical_Velocity(double[] X) {
		double[] result = new double[3];
		result[1] = -Math.atan(X[2]/(Math.sqrt(X[0]*X[0] + X[1]*X[1])));
		result[0] =  Math.sqrt(X[0]*X[0] + X[1]*X[1] + X[2]*X[2]);
		result[2] =  Math.atan2(X[1],X[0]);
		/*
		if(X[1]<0 && X[0]>0) {
			result[2]= PI + Math.abs(result[2]);
		} else if (X[1]<0 && X[0]<0) {
			result[2]= PI/2 + Math.abs(result[2]);
		}
		*/
		// Filter small errors from binary conversion: 
		for(int i=0;i<result.length;i++) {if(Math.abs(result[i])<1E-9) {result[i]=0; }}
		return result; 
		}
	public static double[] Spherical2Cartesian_Position(double[] r_spherical) {
		double[] result = new double[3];
		result[0] = r_spherical[2]*Math.cos(r_spherical[0])*Math.cos(r_spherical[1]);
		result[1] = r_spherical[2]*Math.cos(r_spherical[1])*Math.sin(r_spherical[0]);
		result[2] = r_spherical[2]*Math.sin(r_spherical[1]);
		return result;
	}
}