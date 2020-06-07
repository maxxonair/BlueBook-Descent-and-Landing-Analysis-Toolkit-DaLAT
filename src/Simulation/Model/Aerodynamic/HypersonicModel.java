package Simulation.Model.Aerodynamic;

import FlightElement.SpaceShip;
import Simulation.Model.DataSets.AerodynamicSet;
import Simulation.Model.DataSets.AtmosphereSet;
import Simulation.Model.DataSets.HypersonicSet;
import utils.Mathbox;
import utils.UConst;

public class HypersonicModel {
    
    public static void main(String[] args) {
    	//--------------------------------------------------------------------------
    	//				Module Tester
    	//--------------------------------------------------------------------------
   	 double deg2rad = UConst.PI/180; 
    	AtmosphereSet atmosphereSet = new AtmosphereSet();
    	AerodynamicSet aerodynamicSet = new AerodynamicSet();
    	atmosphereSet.setDensity(0.001);
    	atmosphereSet.setDynamicPressure(100000);
    	atmosphereSet.setMach(5);
    	atmosphereSet.setGamma(1.4);
    	atmosphereSet.setStaticPressure(1000);
    	atmosphereSet.setStaticTemperature(140);;
    	atmosphereSet.setGasConstant(197);
    	aerodynamicSet.setAerodynamicAngleOfAttack(45*deg2rad);
    	aerodynamicSet.setAngleOfSideslip(0*deg2rad);
    	SpaceShip spaceShip = new SpaceShip();
    	spaceShip.getProperties().getAeroElements().setHeatshieldRadius(2.3);
    	spaceShip.getState().settIS(0);
    	HypersonicSet hypersonicSet = hypersonicFlowModel(atmosphereSet, aerodynamicSet, spaceShip);
    	System.out.println("CL:"+hypersonicSet.getCL());
    	System.out.println("CY:"+hypersonicSet.getCY());
    	System.out.println("CD:"+hypersonicSet.getCD());
    	System.out.println("L/D:"+hypersonicSet.getLD());
    	System.out.println("CMx:"+hypersonicSet.getCMx());
    	System.out.println("CMy:"+hypersonicSet.getCMy());
    	System.out.println("CMz:"+hypersonicSet.getCMz());
    }
    
	public static HypersonicSet hypersonicFlowModel(AtmosphereSet atmosphereSet, AerodynamicSet aerodynamicSet, SpaceShip spaceShip) {
	int file=2;
	int NewtonT=1;
	System.out.println("Hypersonic model active | time: "+spaceShip.getState().gettIS()+" [s]");
	HypersonicSet hypersonicSet = new HypersonicSet();
	//--------------------------------------------------------------------------
	//                          NewAero Code of 3D 
	//            Aerodynamic Properties of Axisymmetric Blunt Bodies
	//--------------------------------------------------------------------------
	//
	double RB=spaceShip.getProperties().getAeroElements().getHeatshieldRadius();                          // Heatshield Base Radius
	 int  nop=100; //(!)
	 //--------------------------------------------------------------------------
	double R     = atmosphereSet.getGasConstant(); 
	double gamma = atmosphereSet.getGamma();
	double M     = atmosphereSet.getMach();
	double pinf  = atmosphereSet.getStaticPressure();
	double Vinfp = atmosphereSet.getMach()*Math.sqrt(atmosphereSet.getGamma()*R*atmosphereSet.getStaticTemperature()); 			// Freestream Velocity [m/s]
	double qinf  = atmosphereSet.getDynamicPressure();  		// Freestream dynamic pressure
	double[][] rCG = {{-0.0},{-0.2},{-0.0 }};          					// Center of Mass
	//daoa=20;                   					// Angle of Attack [deg]                  				// Angle of side slip [deg]/Do not use Cd when dbeta  
	double raoa  = aerodynamicSet.getAerodynamicAngleOfAttack();           	 // Angle of Attack [rad]
	double rbeta = aerodynamicSet.getAngleOfSideslip();        				 // Angle of side slip [rad]
	double[][] Vinf= {{Math.sin(raoa)}, {-Math.cos(raoa)*Math.sin(rbeta)}, {Math.cos(raoa)*Math.cos(rbeta)}};     
	       Vinf=Mathbox.Multiply_Scalar_Matrix(-Vinfp, Vinf); 
	       Vinf=Mathbox.normalizeVector(Vinf);       // Normalized Surface Velocity Vector 
	//double unit=1e-4;                  // 
	double Cpmax=2;
	//--------------------------------------------------------------------------
	int indxLength=0;
	double res=0;		// Resolution - length increment on the x-axis along the center line per index step 
	//--------------------------------------------
	double RN, RC;
	
	double[][] Malpha = { {Math.cos(raoa) ,    0   ,  -Math.sin(raoa) } ,
						  {0              ,    1   ,   0              },
						  {Math.sin(raoa) ,    0   , Math.cos(raoa)   }};
	
	double[][] Mbeta =  {{ 1  ,   0             ,    0     }, 
					     { 0  , Math.cos(rbeta) ,  Math.sin(rbeta) },
						 { 0  , -Math.sin(rbeta) ,  Math.cos(rbeta)}} ;
	//--------------------------------------------------------------------------
	// Maximum Cp for Modified Newtonian Flow Theory
	//--------------------------------------------------------------------------
	if (NewtonT==1) {
	Cpmax=2;
	} else if (NewtonT==2) {
	Cpmax=2/(gamma*M*M)*(Math.pow((((gamma+1)*(gamma+1)*M*M)/(4*gamma*M*M-2*(gamma-1))),(gamma/(gamma-1)))
			*((1-gamma+2*gamma*M*M)/(gamma+1))-1);
	}
	//--------------------------------------------------------------------------
	//                       Heatshield Shape Definition 
	//--------------------------------------------------------------------------
	double [][] x = null;
	double [][] z = null;
	if (file==2) {
	//       70 degree Sphere Cone Shield (Input: RB)
	RN=RB/2;           // Nose Radius
	double phi=70;            // Cone Angle
	//[x,z,res]= SphereConeMark2(RB,RN,phi,nop);
	double[][] XZ = SphereConeMark2(RB,RN,phi,nop);
	 x = new double[XZ.length][1];
	 z = new double[XZ.length][1];
	indxLength=XZ.length;
	for(int i=0;i<XZ.length;i++) {
		x[i][0] = XZ[i][0];
		z[i][0] = XZ[i][1];
	}
	 res=2*(RB)/nop;
	} 
	if (file==3) {
	// CUBRC LENS shield (INPUT: RB) original Shield RB = 5[m]
	RN=2.4*RB;
	RC=RB/10;
	double ralpha=Math.asin(RB/RN);
	 rbeta=UConst.PI/2-ralpha;
	//double h=RC*Math.sin(rbeta);
	double d=RC*(1-Math.cos(rbeta));
	 res=2*(RB+d)/nop;
	double runner=0;
	 indxLength = (int) (2*(RB+d)/res);
	x = new double[indxLength][0];
	z = new double[indxLength][0];
	for(int i=0;i<indxLength;i++) {
		x[i][0] = -(RB+d)+runner;
		runner+=res;
	}
	//double C=-RB*Math.sqrt(2.4*2.4-1)+h;
		for (int i=1;i<x.length;i++) {
			if(x[i][0] > RB ) {
			double C1=0; double C2= -(RB-RC+d);
			z[i][0] = C1+Math.sqrt(RC*RC-(x[i][0]+C2)*(x[i][0]+C2));
			}
	
		if(x[i][0] < -RB) {
		double C1=0; double C2= (RB-RC+d);
		z[i][0]=C1+Math.sqrt(RC*RC-(x[i][0]+C2)*(x[i][0]+C2));       
		}
		}
	RB=x[x.length-1][0];
	}
	
	//--------------------------------------------------------------------------
	// x vecter adaption 
	int end0=indxLength;
	int mid0=indxLength/2;
	int istart= 1;
	int iend  = end0 - mid0;
	//--------------------------------------------------------------------------
	//          Create Rotation Matrix (z-axis)
	double rotationalIncrement = 1;
	double deltaRad=rotationalIncrement/180*UConst.PI; //      Rotation by 1 degree in [rad]                                                (!)
	double[][] rotationalMatrixZ= {{Math.cos(deltaRad), -Math.sin(deltaRad), 0 } ,
					{Math.sin(deltaRad), Math.cos(deltaRad), 0 },
					{0,          0,          1}};
	//         Create Grid Vector
	//int indxSize = (int) (2*(RB-res/2)/res);
	int indxSize = (int) (2*RB/res);
	//System.out.println(mid0);
	double[][] GRID = new double[indxSize][1];
	GRID[0][0] = -(RB-res/2);
	for(int i=1;i<indxSize;i++) {
		GRID[i][0]+=res;  
	}  
	double[][] gr = new double[3][end0-mid0];
	int k=0;
	for(int i=mid0;i<end0;i++) {
	gr[0][k]=GRID[i][0]; // X
	gr[1][k]=0;			 // Y
	if(i<end0-1) {		 // Z	
	gr[2][k]=z[i][0];} else {
		gr[2][k]=0;
	}
	k++;
	}
	
	gr = rotateVectorSet(rotationalMatrixZ, gr);
	
	//--------------------------------------------------------------------------
	double[][] p = new double[3][end0-mid0];  // reference cell point vector
	double[][] pn = new double[3][end0-mid0]; // next (i+1) cell point vector
	//System.out.println(end0-mid0);
	k=0;
	for(int i=mid0;i<end0;i++) {
	p[0][k]=x[i][0];  // X
	p[1][k]=0;		  // Y
	p[2][k]=z[i][0];  // Z
	k++;
		}
	pn = rotateVectorSet(rotationalMatrixZ, p);
	for(int i=istart;i<iend-1;i++) {
		//System.out.println(pn[0][i]-p[0][i]);
	}
	
	
	//--------------------------------------------------------------------------
	double[][][] NGR            = new double[360][3][end0-mid0];
	double[][] v1               = new double[3][1];
	double[][] v2               = new double[3][1];
	double[][] normalVector     = new double[3][1];
	double[][] pCurrent         = new double[3][1];
	double[][] pPlusOneCurrent  = new double[3][1];
	double[][] pnCurrent        = new double[3][1];
	double[][] pnPlusOneCurrent = new double[3][1];
	double[][] Vsurf            = new double[3][1];
	double[][][] NP      = new double[360][3][end0-mid0];
	double[][][] NNormal = new double[360][3][end0-mid0];
	
	double[][] NArea   = new double[360][end0-mid0];
	double[][] NCpA    = new double[360][end0-mid0];
	double[][] NCxA    = new double[360][end0-mid0];
	double[][] NCyA    = new double[360][end0-mid0];
	double[][] NCzA    = new double[360][end0-mid0];
	
	double[][][] NVsurf  = new double[360][3][end0-mid0];
	
	double[][] Nphi    = new double[360][end0-mid0];
	double[][] Ndphi   = new double[360][end0-mid0];
	double[][] NCPPM   = new double[360][end0-mid0];
	
	double[][] NPE     = new double[360][end0-mid0];
	double[][] NMx     = new double[360][end0-mid0];       //
	double[][] NMy     = new double[360][end0-mid0];       //
	double[][] NMz     = new double[360][end0-mid0];       //
	
		for(int j=0;j<360;j+=rotationalIncrement) { // From 1 to 360 degree
			NGR = addVectorSet2Container(j, gr, NGR);
		for(int i=istart;i<iend-1;i++) { // from center to max radius
			for(int l=0;l<3;l++) {
				pCurrent[l][0]         = p[l][i];
				pnCurrent[l][0]        = pn[l][i];
				pPlusOneCurrent[l][0]  = p[l][i+1];
				pnPlusOneCurrent[l][0] = pn[l][i+1];
				v1[l][0]  =pnCurrent[l][0] - pCurrent[l][0];
				v2[l][0]=pCurrent[l][0] - p[l][i+1];
			}
	
		normalVector=crossProduct(v1,v2);       // Normal Vector
		normalVector=norm(normalVector);        // Normalized normal vector
		Vsurf=crossProduct(crossProduct(normalVector,Vinf),normalVector);
		Vsurf=norm(Vsurf);
		//------------------------------------------------------------
		double a=getLength(substractVector(pCurrent,pnCurrent));
		double b=getLength(substractVector(pCurrent,pPlusOneCurrent));
		double c=getLength(substractVector(pnPlusOneCurrent,pPlusOneCurrent));
		double s=0.5*(2*b+c-a);
		double h=2/(c-a)*Math.sqrt(s*(s+a-c)*(s-b)*(s-b));
		double A=(a+c)/2*h;
		if(A<0) {A=0;}
		//------------------------------------------------------------
		double cosphi = dotProduct(Vinf,normalVector)/(getLength(normalVector)*getLength(Vinf));
		double phi = Math.acos(cosphi);
		double phia= UConst.PI/2 - phi;
		double dphi= phi*180/UConst.PI;
		//------------------------------------------------------------
		double cppm=Cpmax*(Math.sin(phia))*(Math.sin(phia)); 
		//------------------------------------------------------------
		double nx= -normalVector[0][0];
		double ny= -normalVector[1][0];
		double nz= -normalVector[2][0];
		double sumn = Math.sqrt(nx*nx + ny*ny + nz*nz);
		double xrat=nx/sumn;
		double yrat=ny/sumn;
		double zrat=nz/sumn;
		
		//------------------------------------------------------------
		double[][] fi = new double[3][1];
		double[][] rCGgi = new double[3][1];
			for(int l=0;l<3;l++) {
			 fi[l][0]     = (qinf*cppm+pinf)*A*(-normalVector[l][0]);  // Local Force 
			 rCGgi[l][0]  = gr[l][i] - rCG[l][0];             // Local arm of lever
			}
		double[][] Mi     = crossProduct(rCGgi,fi);           // Local Angular Momentum
		//------------------------------------------------------------
				NP      = addVectorSet2Container(j, p, NP);
				NNormal = addVectorSet2Container(j, normalVector, NNormal);
		
				NArea[j][i]  = A;
				NCpA[j][i]   = cppm*A;
				NCxA[j][i]   = cppm*A*xrat;
				NCyA[j][i]   = cppm*A*yrat;
				NCzA[j][i]   = cppm*A*zrat;
				
				NVsurf  = addVectorSet2Container(j, Vsurf, NVsurf);
				
				Nphi[j][i]    = phi;
				Ndphi[j][i]  = dphi;
				NCPPM[j][i]   = cppm;
				NPE[j][i]     = 	qinf*cppm+pinf;
				NMx[j][i]     = Mi[0][0];       //
				NMy[j][i]     = Mi[1][0];       //
				NMz[j][i]     = Mi[2][0];       //
				
				
				//System.out.println(Vsurf[0][0]);
		}
	//-------------------------------
	// Rotates p and pn by 1 degree 
	p = rotateVectorSet(rotationalMatrixZ, p);
	pn = rotateVectorSet(rotationalMatrixZ, pn);
	gr = rotateVectorSet(rotationalMatrixZ, gr);
	
	for(int l=1;l<p[0].length-1;l++) {
		p[2][l] = z[l][0];
	}
	for(int l=1;l<pn[0].length-1;l++) {
		pn[2][l] = z[l][0];
	}
	for(int l=1;l<gr[0].length-1;l++) {
		gr[2][l] = z[l][0];
	}
	//---------------------------------
	}
	
	double sit1 = NCpA.length;
	double sit2 = NCpA[0].length;
	double VarACp = 0;
	double VarA = 0;
	double VarACx = 0;
	double VarACy = 0;
	double VarACz = 0;
	double VarMx = 0;
	double VarMy = 0;
	double VarMz = 0;
	double nplus = 0;
	double nplusx=0;
	double nplusy=0;
	double nplusz=0;
	double nplusMx=0;
	double nplusMy=0;
	double nplusMz=0;
	
		for(int j=1;j<sit1;j++) {
			for(int i=1;i<sit2;i++) {
				
			if( Double.isNaN(NCpA[j][i]) ) {
			 nplus=0;
			} else {
			 nplus=NCpA[j][i];
			}
			
			if(Double.isNaN(NCxA[j][i])) {
			nplusx=0;
			} else {
			nplusx=NCxA[j][i];
			}
			
			if (Double.isNaN(NCyA[j][i])) {
			nplusy=0;
			} else {
			nplusy=NCyA[j][i];
			}
			
			if (Double.isNaN(NCzA[j][i])) {
			nplusz=0;
			} else {
			nplusz=NCzA[j][i];
			}
			
			if (Double.isNaN(NMx[j][i])) {
			nplusMx=0;
			} else {
			nplusMx=NMx[j][i];
			}
			
			if (Double.isNaN(NMy[j][i])) {
			nplusMy=0;
			} else {
			nplusMy = NMy[j][i];
			}
			
			if (Double.isNaN(NMz[j][i])) {
			nplusMz=0;
			} else {
			nplusMz = NMz[j][i];
			}
			
			VarACx = VarACx + nplusx;
			VarACy = VarACy + nplusy;
			VarACz = VarACz + nplusz;
			VarACp = VarACp + nplus ;
			
			VarMx  = VarMx  + nplusMx;
			VarMy  = VarMy  + nplusMy;
			VarMz  = VarMz  + nplusMz;
					
			VarA   = VarA   + NArea[j][i];
			}
		}
		//--------------------------------------------
		double Cz    = (VarACz/VarA);
		double Cx    = (VarACx/VarA);
		double Cy    = (VarACy/VarA);
		//--------------------------------------------
		if (-1e-9 < Cx && Cx < 1e-9 ) {
		Cx=0;
		}
		if (-1e-9 < Cy && Cy < 1e-9 ) {
		Cy=0;
		}
		if (-1e-9 < Cz && Cz < 1e-9 ){
		Cz=0;
		}
		//--------------------------------------------
		double CA     = -Cz ;
		double CN     =  Cx ;
		double[][] CB = {{Cx},{Cy},{-Cz}};
		double[][] CAer = Mathbox.Multiply_Matrices(Malpha,Mathbox.Multiply_Matrices(Mbeta, CB));
		double CL     = - CAer[0][0];
		double CY     =   CAer[1][0];
		double CD     =   CAer[2][0];
		
		double Cmx    = (VarMx/(qinf*VarA*(2*RB)));
		double Cmy    = (VarMy/(qinf*VarA*(2*RB)));
		double Cmz    = (VarMz/(qinf*VarA*(2*RB)));
		
		//--------------------------------------------
		if (-1e-8 < Cmx && Cx < 1e-8 ) {
		Cmx=0;
		}
		if (-1e-8 < Cmy && Cy < 1e-8 ) {
		Cmy=0;
		}
		if (-1e-8 < Cmz && Cz < 1e-8 ) {
		Cmz=0;
		}
		
		double LD    = CL/CD;
		//--------------------------------------------------------------------------
		hypersonicSet.setCA(CA);
		hypersonicSet.setCD(CD);
		hypersonicSet.setCL(CL);
		hypersonicSet.setCN(CN);
		hypersonicSet.setCx(Cx);
		hypersonicSet.setCY(CY);
		hypersonicSet.setCy(Cy);
		hypersonicSet.setCz(Cz);
		hypersonicSet.setLD(LD);
		hypersonicSet.setCMx(Cmx);
		hypersonicSet.setCMy(Cmy);
		hypersonicSet.setCMz(Cmz);
		
	return hypersonicSet;
	}
	
	public static double[][] rotateVectorSet(double[][] RotationMatrix, double[][] vectorSet){
		double[][] localGridVector = new double[3][1];
		//System.out.println(vectorSet[0].length);
		double[][] result = new double[3][vectorSet[0].length];
		for(int l=1;l<vectorSet[0].length-1;l++) {
			for(int j=0;j<3;j++) {
				localGridVector[j][0] = vectorSet[j][l];
			}
		localGridVector = Mathbox.Multiply_Matrices(RotationMatrix, localGridVector);
			for(int j=0;j<3;j++) {
				result[j][l]=localGridVector[j][0];
			}
		}
		return result;
	}
	
	public static double[][][] addVectorSet2Container(int indx, double[][] vectorSet, double[][][] container){
		for(int l=1;l<vectorSet[0].length-1;l++) {
			for(int j=0;j<3;j++) {
				container[indx][j][l] = vectorSet[j][l];
			}
		}
		return container;
	}
	
	public static double[][] crossProduct(double[][] a, double[][] b){
		double[][] result = new double[3][1];
		result[0][0] = a[1][0]*b[2][0] - a[2][0]*b[1][0];
	    result[1][0] = a[2][0]*b[0][0] - a[0][0]*b[2][0];
	    	result[2][0] = a[0][0]*b[1][0] - a[1][0]*b[0][0];
	    	return result; 
	}
	
	public static double[][] norm(double[][] v){
		double [][] result = new double[3][1];
		double vLength = Math.sqrt(v[0][0]*v[0][0] + v[1][0]*v[1][0] + v[2][0]*v[2][0]);
		for(int i=0;i<3;i++) {
			result[i][0] = v[i][0]/vLength;
		}
		return result; 
	}
	public static double[][] substractVector(double[][] a, double[][] b){
		double [][] result = new double[3][1];
		for(int i=0;i<3;i++) {
			result[i][0] = a[i][0] - b[i][0];
		}
		return result; 
	}
	
	public static double dotProduct(double[][] a, double[][] b){
		double  result = 0;
		for(int i=0;i<3;i++) {
			result += a[i][0] * b[i][0];
		}
		return result; 
	}
	
	public static double getLength(double[][] v){
		double vLength = Math.sqrt(v[0][0]*v[0][0] + v[1][0]*v[1][0] + v[2][0]*v[2][0]);
		return vLength; 
	}
	
	public static double[][] SphereConeMark2(double RB, double RN,double PHI, int nop) {
			//RB=2.25;
			//RN=RB/2;
			double RC=RB/10;
			//PHI=70;
			//nop=100;
			//double D=RB;
			double res=2*(RB)/nop;
			double dtr=UConst.PI/180;
			double rphi=PHI*dtr;
			double ralpha=(90-PHI)*dtr;
			//--------------------------------------------------------------------------
			double d1=RC*Math.sin(rphi);
			//double d2=RN*(1-Math.cos(ralpha));
			double d3=RN*Math.sin(ralpha);
			double d4=RC*(1-Math.cos(rphi));
			double d5=RB-(d3+d4);
			double d6=RN*Math.cos(ralpha);
			double d7=d5*Math.tan(ralpha);
			double gap=d7-d6;
			//double h=d1+d7+d2;
			int indxLength = (int) (2*RB/res);
			double[][] XZ = new double[indxLength][2];
			double[][] x = new double[indxLength][1];
			double runner=0;
			for(int i=0;i<indxLength;i++) {
			 x[i][0]=-RB+runner;
			runner+=res;
			}
			double[][] z = new double[x.length][1];
			for(int i=0;i<x.length;i++) {
				z[i][0] = 0;
			}
			//--------------------------------------------------------------------------
			       double m=d7/(-d3-d4+RB);
			       double n=(d7+d1+m*d3);
			       double[][] y1 = new double[indxLength][1];
			       double[][] y2 = new double[indxLength][1];
			       double[][] y3 = new double[indxLength][1];
			       for(int i=0;i<indxLength;i++) {
			        y1[i][0] = m * x[i][0]+n;
			        y2[i][0] = (gap+d1)+Math.sqrt(Math.abs(RN*RN-x[i][0]*x[i][0])); 
			       m=d7/(d3+d4-RB);
			       n=(d7+d1-m*d3);
			        y3[i][0] = m*x[i][0]+n;
			       }
			//--------------------------------------
			       for (int i=1;i<indxLength;i++) {
			           if (x[i][0] < -d3) {
			               z[i][0] = y1[i][0];
			           } 
			           if (x[i][0] >= -d3 && x[i][0] <= d3) {
			        	    z[i][0]=y2[i][0];
			           }
			           if (x[i][0] > d3 ) {
			        	    z[i][0]=y3[i][0];
			           }
			       }
			       
			for (int i=1;i<indxLength;i++) {
			   if (x[i][0] > (d5+d3)) {
			    double  C1=0;   double  C2= -(RB-RC);
			    z[i][0]=C1+Math.sqrt(RC*RC-(x[i][0]+C2)*(x[i][0]+C2));
			   }
			   
			   if (x[i][0] < - (d5+d3)) {
			     double  C1=0; double C2= (RB-RC);
			     z[i][0]=C1+Math.sqrt(RC*RC-(x[i][0]+C2)*(x[i][0]+C2));    
			   }
			   XZ[i][0] = x[i][0];
			   XZ[i][1] = z[i][0];
			}
			return XZ;
	}
}
