package utils;

public class Quaternion extends Object implements Cloneable{

    public double x;
    public double y;
    public double z;
    public double w;
    
    private static final Quaternion quatTmp1 = new Quaternion();
    private static final Quaternion quatTmp2 = new Quaternion();
    private static final Quaternion quatTmp3 = new Quaternion();
    private static final Quaternion quatTmp4 = new Quaternion();
            
    public Quaternion() {
    	// Set to default 
    	this.w=1;
    	this.x=0;
    	this.y=0;
    	this.z=0;    	
    }

    public Quaternion(double angle, Vec3 rotationAxis) {
        x = rotationAxis.x * Math.sin(angle / 2);
        y = rotationAxis.y * Math.sin(angle / 2);
        z = rotationAxis.z * Math.sin(angle / 2);
        w = Math.cos(angle / 2);
    }
    
    public Quaternion(double w, double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void set(double angle, Vec3 rotationAxis) {
        x = rotationAxis.x * Math.sin(angle / 2);
        y = rotationAxis.y * Math.sin(angle / 2);
        z = rotationAxis.z * Math.sin(angle / 2);
        w = Math.cos(angle / 2);
    }

    public void set (double w, double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public void set (Quaternion q) {
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.w = q.w;
    }
    
    public double getSize() {
        return Math.sqrt(w * w + x * x + y * y + z * z);
    }
    
    public void normalize() {
        double sizeInverse = 1 / getSize();
        x *= sizeInverse;
        y *= sizeInverse;
        z *= sizeInverse;
        w *= sizeInverse;
    }
    
    public void multiply(Quaternion qb, Quaternion result) {
        Quaternion qa = this;
        quatTmp4.w = (qa.w * qb.w) - (qa.x * qb.x) - (qa.y * qb.y) - (qa.z * qb.z);
        quatTmp4.x = (qa.x * qb.w) + (qa.w * qb.x) + (qa.y * qb.z) - (qa.z * qb.y);
        quatTmp4.y = (qa.y * qb.w) + (qa.w * qb.y) + (qa.z * qb.x) - (qa.x * qb.z);
        quatTmp4.z = (qa.z * qb.w) + (qa.w * qb.z) + (qa.x * qb.y) - (qa.y * qb.x);
        result.set(quatTmp4);
    }
    
    public void conjugate() {
        x = -x;
        y = -y;
        z = -z;
    }
    
    public void inverse() {
        double sizeInverse = 1 / getSize();
        x *= sizeInverse;
        y *= -sizeInverse;
        z *= -sizeInverse;
        w *= -sizeInverse;
    }
    
    // P' = Q.P.Q*
    public void rotate(Vec3 point, Vec3 rotatedPoint) {
        quatTmp1.set(0, point.x, point.y, point.z);
        multiply(quatTmp1, quatTmp2);
        quatTmp1.set(this);
        quatTmp1.conjugate();
        quatTmp2.multiply(quatTmp1, quatTmp3);
        
        rotatedPoint.x = quatTmp3.x;
        rotatedPoint.y = quatTmp3.y;
        rotatedPoint.z = quatTmp3.z;
    }
    
    public double[][] getCosineMatrix(){
    	double[][] cosineMatrix = new double[3][3];
    	
    	cosineMatrix[0][0] = w*w + x*x - y*y - z*z;
    	cosineMatrix[1][0] = 2*( x*y + w*z);
    	cosineMatrix[2][0] = 2*( x*z - w*y);
    	
    	cosineMatrix[0][1] = 2*( x*y - w*z);
    	cosineMatrix[1][1] = w*w - x*x + y*y - z*z;
    	cosineMatrix[2][1] = 2*( w*x + y*z);
    							
    	cosineMatrix[0][2] = 2*( w*y + x*z);
    	cosineMatrix[1][2] = 2*( y*z - w*x);
    	cosineMatrix[2][2] = w*w - x*x - y*y + z*z;
    			   	
    	return cosineMatrix;
    }
    
	@Override
	public Object clone() throws CloneNotSupportedException {

	    return super.clone();
	}
    
}
