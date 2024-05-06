public class Star {
	Vector3 position;
	int id;
	double magnitude;
	double brightness;
	int revisedId;

	public Star(Vector3 position, int id, double magnitude, int revisedId) {
		this.position = position;
		this.id = id;
		this.magnitude = magnitude;
		this.revisedId = revisedId;
		brightness = 15 * Math.pow(10, magnitude/-10.512);
	}
}
