package utils;
/**
 *
 * @author leonardo
 */
public class Vec3 {
    
    public double x;
    public double y;
    public double z;

    public Vec3() {
    }

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void scale(double s) {
        x *= s;
        y *= s;
        z *= s;
    }
    
    public double getSize() {
        return Math.sqrt(x * x + y * y + z * z);
    }
    
    public void normalize() {
        scale(1 / getSize());
    }
    
    public void rotateY(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double nx = x * c - z * s;
        double nz = x * s + z * c;
        x = nx;
        z = nz;
    }
    
    @Override
    public String toString() {
        return "Vec3{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
    
}
