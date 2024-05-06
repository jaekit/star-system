import java.awt.Color;
import java.util.*;

public class Constellation {
	String name;
	ArrayList<ArrayList<Integer>> stars = new ArrayList<>();
	Color color;
	public Constellation(String name, Color color) {
		this.name = name;
		this.color = color;
	}
}
