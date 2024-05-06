public class Vector3 {
	double x;
	double y;
	double z;

	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String toString() {
		return String.format("(%.6f, %.6f, %.6f)", x, y, z);
	}
}
