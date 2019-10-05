package Model.Aerodynamic;

import Toolbox.Mathbox;

public class HypersonicModel {
    public static double PI = 3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808;
	public void hypersonicFlowModel(double M, double gamma, double Tinf, double density, double daoa) {
int file=2;
int NewtonT=1;
//--------------------------------------------------------------------------
//                          NewAero Code of 3D 
//            Aerodynamic Properties of Axisymmetric Blunt Bodies
//--------------------------------------------------------------------------
//
//NewtonT=2;                  //
//file=2;                     //
double RB=2;                      // Heatshield Base Radius
 int  nop=100; //(!)
//gamma=1.05;                // Mean(Freestream,Shock Layer)HeatCapacityRatio
//M=22.2;                    // Freestream Ma Number 
//double Tinf=200;                   // 
//double rhoinf=0.0009;              //
double R=197;                      // 
double pinf=density*R*Tinf;         // 
double Vinfp=M*Math.sqrt(gamma*R*Tinf); 			// Freestream Velocity [m/s]
double qinf = 0.5*density*Vinfp*Vinfp;   		// Freestream dynamic pressure
//rCG=[-0.1;0;-.5];          					// Center of gravity
//daoa=20;                   					// Angle of Attack [deg]
double dbeta=0;                    				// Angle of side slip [deg]/Do not use Cd when dbeta  
double raoa=daoa*PI/180;           				// Angle of Attack [rad]
double rbeta=dbeta*PI/180;        				 // Angle of side slip [rad]
double[][] Vinf= {{Math.sin(raoa)}, {-Math.cos(raoa)*Math.sin(rbeta)}, {Math.cos(raoa)*Math.cos(rbeta)}};     
       Vinf=Mathbox.Multiply_Scalar_Matrix(-Vinfp, Vinf); 
       Vinf=Mathbox.normalizeVector(Vinf);       // Normalized Surface Velocity Vector 
double unit=1e-4;                  // 
double Cpmax=2;
//--------------------------------------------------------------------------
double CA ;
double CN  ;
double[] CB = {0,0,0};
double CAer ;
double CL ;
double CY ;
double CD ;
double Cmx ;
double Cmy ;
double Cmz ;
//--------------------------------------------
double RN, RBC, RC;

double[][] Malpha = { {Math.cos(raoa),   0 ,  Math.sin(raoa) } ,
{0   ,    1   ,   0      },
{-Math.sin(raoa) ,  0  , Math.cos(raoa)}};

Malpha=Mathbox.Inverse_Matrix(Malpha);

double[][] Mbeta =  {{     1    ,   0     ,    0     }, 
{ 0  , Math.cos(rbeta), -Math.sin(rbeta) },
{0  , Math.sin(rbeta) , Math.cos(rbeta)}} ;

Mbeta=Mathbox.Inverse_Matrix(Mbeta);
//--------------------------------------------------------------------------
// Maximum Cp for Modified Newtonian Flow Theory
if (NewtonT==1) {
Cpmax=2;
} else if (NewtonT==2) {
Cpmax=2/(gamma*M*M)*(Math.pow((((gamma+1)*(gamma+1)*M*M)/(4*gamma*M*M-2*(gamma-1))),(gamma/(gamma-1)))
		*((1-gamma+2*gamma*M*M)/(gamma+1))-1);
}
//--------------------------------------------------------------------------
//                       Heatshield Shape Definition 
//--------------------------------------------------------------------------

if (file==2) {
//       70 degree Sphere Cone Shield (Input: RB)
RN=RB/2;           // Nose Radius
double phi=70;            // Cone Angle
//[x,z,res]= SphereConeMark2(RB,RN,phi,nop);
//[x,z,res,~]= SphereConeMark2(RB,RN,phi,nop);

} 
if (file==3) {
// CUBRC LENS shield (INPUT: RB) original Shield RB = 5[m]
RN=2.4*RB;
RC=RB/10;
double ralpha=Math.asin(RB/RN);
 rbeta=PI/2-ralpha;
double h=RC*Math.sin(rbeta);
double d=RC*(1-Math.cos(rbeta));
double res=2*(RB+d)/nop;
double runner=0;
int indxLength = (int) (2*(RB+d)/res);
double [][] x = new double[indxLength][0];
double [][] z = new double[indxLength][0];
for(int i=0;i<indxLength;i++) {
	x[i][0] = -(RB+d)+runner;
	runner+=res;
}
double C=-RB*Math.sqrt(2.4*2.4-1)+h;
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
mid0=find(x==0);       // Centerline integration start
end0=find(x==x(end));  // Centerline integration end 
istart= 1;
iend  = end0 - mid0+1;
//--------------------------------------------------------------------------
//          Create Rotation Matrix (z-axis)
delta=1/180*PI; //                                                        (!)
Mt=[cos(delta) -sin(delta) 0;...
sin(delta) cos(delta) 0;...
0          0          1];
//         Create Grid Vector
gri=-(RB-res/2):res:(RB-res/2);
gri=gri.';             
gr(1,:)=gri(mid0:end);
gr(2,:)=0;
gr(3,:)=z(mid0:end-1);
for k=1:iend-1
gr(:,k)=Mt*gr(:,k);
end
//--------------------------------------------------------------------------
clear p NVec
p(1,:)=x(mid0:end);
p(2,:)=0;
p(3,:)=z(mid0:end);
for (int k=1;k<iend;k++) {
pn(:,k)=Mt*p(:,k);
}
//--------------------------------------------------------------------------
for(int j=1;j<360;j++) { // From 1 to 360 degree
NGR(j,:,:)=gr;
for(int i=istart;i<iend-1;i++) { // from center to max radius
v1=pn(:,i)-p(:,i);    // Tangential Vector
v2=p(:,i)-p(:,i+1);   // Radial Vector 
n=cross(v1,v2);       // Normal Vector
n=n/norm(n);          // Normalized normal vector
Vsurf=cross(cross(n,Vinf),n);
Vsurf=Vsurf/norm(Vsurf);
//------------------------------------------------------------
a=norm(p(:,i)-pn(:,i));
b=norm(p(:,i)-p(:,i+1));
c=norm(pn(:,i+1)-p(:,i+1));
s=0.5*(2*b+c-a);
h=2/(c-a)*sqrt(s*(s+a-c)*(s-b)^2);
A=(a+c)/2*h;
//------------------------------------------------------------
cosphi = dot(Vinf,n)/(norm(n)*norm(Vinf));
phi = acos(cosphi);
phia= pi/2 - phi;
dphi= phi*180/pi;
//------------------------------------------------------------
cppm=Cpmax*(sin(phia))^2; 
//------------------------------------------------------------
nx= -n(1,1);
ny= -n(2,1);
nz= -n(3,1);
sumn=sqrt(abs(nx)^2+abs(ny)^2+abs(nz)^2);
xrat=nx/sumn;
yrat=ny/sumn;
zrat=nz/sumn;
//------------------------------------------------------------
fi     = (qinf.*cppm+pinf)*A*(-n);  // Local Force 
rCGgi  = gr(:,i) - rCG;             // Local Lever Arm
Mi     = cross(rCGgi,fi);           // Local Angular Momentum
//------------------------------------------------------------
NP(j,i,:)      = p(:,i);
NNormal(j,i,:) = n;
NArea(j,i,:)   = A;
NCpA(j,i,:)    = cppm*A;
NCxA(j,i,:)    = cppm*A*xrat;
NCyA(j,i,:)    = cppm*A*yrat;
NCzA(j,i,:)    = cppm*A*zrat;
NVsurf(j,i,:)  = Vsurf;
Nphi(j,i,:)    = phi;
Ndphi(j,i,:)   = dphi;
NCPPM(j,i,:)   = cppm;
NPE(j,i,:)     = .5*rhoinf.*Vinfp^2.*cppm+pinf;
NMx(j,i,:)     = Mi(1,1);       //
NMy(j,i,:)     = Mi(2,1);       //
NMz(j,i,:)     = Mi(3,1);       //
}
//-------------------------------
// Rotates p and pn by 1 degree 
for(int k=1;k<iend;k++) {
p(:,k)=Mt*p(:,k);
pn(:,k)=Mt*pn(:,k);
if(k==iend) {
} else {
gr(:,k)=Mt*gr(:,k);
end
end
p(3,:)=z(mid0:end);
pn(3,:)=z(mid0:end);
gr(3,:)=z(mid0:end-1);
//---------------------------------
}
sit=size(NCpA);
sit1=sit(1);
sit2=sit(2);
VarACp=0;
VarA=0;
VarACx=0;
VarACy=0;
VarACz=0;
VarMx =0;
VarMy =0;
VarMz =0;
	for(int j=1;j<sit1;j++) {
		for(int i=1;i<sit2;i++) {
		if(isnan(NCpA(j,i))) {
		nplus=0;
		} else {
		nplus=NCpA(j,i);
		}
		
		if(isnan(NCxA(j,i))) {
		nplusx=0;
		} else {
		nplusx=NCxA(j,i);
		}
		
		if (isnan(NCyA(j,i))) {
		nplusy=0;
		} else {
		nplusy=NCyA(j,i);
		}
		
		if (isnan(NCzA(j,i))) {
		nplusz=0;
		} else {
		nplusz=NCzA(j,i);
		}
		
		if (isnan(NMx(j,i,:))) {
		nplusMx=0;
		} else {
		nplusMx=NMx(j,i,:);
		}
		
		if (isnan(NMy(j,i,:))) {
		nplusMy=0;
		} else {
		nplusMy=NMy(j,i,:);
		}
		
		if (isnan(NMz(j,i,:))) {
		nplusMz=0;
		} else {
		nplusMz=NMz(j,i,:);
		}
		
		VarACx = VarACx + nplusx;
		VarACy = VarACy + nplusy;
		VarACz = VarACz + nplusz;
		VarACp = VarACp + nplus ;
		VarMx  = VarMx  + nplusMx;
		VarMy  = VarMy  + nplusMy;
		VarMz  = VarMz  + nplusMz;
		VarA   = VarA   + NArea(j,i);
		}
	}
//--------------------------------------------
Cz    = real(VarACz/VarA);
Cx    = real(VarACx/VarA);
Cy    = real(VarACy/VarA);
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
CA  = -Cz ;
CN  =  Cx ;
CB = [Cx;Cy;-Cz];
CAer = Malpha*Mbeta*CB;
CAer = real(CAer);
CL = - CAer(1);
CY = CAer(2);
CD = CAer(3);
Cmx   = real(VarMx/(qinf*VarA*(2*RB)));
Cmy   = real(VarMy/(qinf*VarA*(2*RB)));
Cmz   = real(VarMz/(qinf*VarA*(2*RB)));
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
}
	}

	}
	
	public double[] SphereConeMark2(double RB, double RN,double PHI, int nop) {
			//RB=2.25;
			//RN=RB/2;
			double RC=RB/10;
			//PHI=70;
			//nop=100;
			double D=RB;
			double res=2*(RB)/nop;
			double dtr=PI/180;
			double rphi=PHI*dtr;
			double ralpha=(90-PHI)*dtr;
			//--------------------------------------------------------------------------
			double d1=RC*Math.sin(rphi);
			double d2=RN*(1-Math.cos(ralpha));
			double d3=RN*Math.sin(ralpha);
			double d4=RC*(1-Math.cos(rphi));
			double d5=RB-(d3+d4);
			double d6=RN*Math.cos(ralpha);
			double d7=d5*Math.tan(ralpha);
			double gap=d7-d6;
			double h=d1+d7+d2;
			int indxLength = (int) (2*RB/res);
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
			       double y1=m*x+n;
			       double y2=(gap+d1)+sqrt(Math.abs(RN*RN-x*x)); 
			       m=d7/(d3+d4-RB);
			       n=(d7+d1-m*d3);
			       double y3=m*x+n;
			//--------------------------------------
			       for (int i=1;i<x.length();i++) {
			           if (x(i)<-d3) {
			               z(i,1)=y1(i,1);
			           } 
			           if (x(i)>=-d3 && x(i)<=d3) {
			               z(i,1)=y2(i);
			           }
			           if (x(i,1)>d3) {
			               z(i,1)=y3(i);
			           }
			       }
			       
			for (int i=1;x.length();i++) {
			   if (x(i,1) > (d5+d3)) {
			      C1=0;    C2= -(RB-RC);
			      z(i,1)=C1+sqrt(RC^2-(x(i,1)+C2)^2);
			   }
			   
			   if (x(i,1) < - (d5+d3)) {
			      C1=0; C2= (RB-RC);
			      z(i,1)=C1+sqrt(RC^2-(x(i,1)+C2)^2);    
			   }
			}
	}
}
