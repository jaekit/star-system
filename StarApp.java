import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.File;

public class StarApp {
	JFrame frame = new JFrame();
	JPanel panel = new JPanel() {
		public void paintComponent(Graphics g) {
			// Background
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 1100, 1100);

			// Stars
			g.setColor(Color.WHITE);
			for (Star s: stars.values()) {
				g.fillOval((int)(s.position.x * 500 + 550 - s.brightness/2), (int)(s.position.y * 500 + 520 - s.brightness/2), (int)s.brightness, (int)s.brightness);
			}
			
			// Constellations
			for (Constellation c: constellations) {
				g.setColor(c.color);
				for (ArrayList<Integer> lines: c.stars) {
					for(int i = 0; i < lines.size() - 1; i++) {
						Vector3 p1 = stars.get(lines.get(i)).position;
						Vector3 p2 = stars.get(lines.get(i + 1)).position;
						g.drawLine((int)(p1.x * 500 + 550), (int)(p1.y * 500 + 520), (int)(p2.x * 500 + 550), (int)(p2.y * 500 + 520));
					}
				}
			}
		}
	};
	HashMap<Integer, Star> stars = new HashMap<>();
	ArrayList<Constellation> constellations = new ArrayList<>();

	public StarApp() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1100, 1100);
		frame.setVisible(true);
		frame.add(panel);
	}

	public void start() {
		loadStars();
		loadConstellations2();
		while(true) {
			update(1/60.0);
			frame.repaint();
			try {
				Thread.sleep(1000/60);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

	public void update(double delta) {
		for (Star s : stars.values()) {
			// convert to spherical coordinates.
			double distance = Math.sqrt(Math.pow(s.position.z, 2) + Math.pow(s.position.x, 2));
			double theta = Math.acos(s.position.y);
			double phi = Math.signum(s.position.x) * Math.acos(s.position.z / distance);

			// increment phi
			phi += delta * 0.1;

			// convert back to cartesian coordinates.
			double z = Math.sin(theta) * Math.cos(phi);
			double x = Math.sin(theta) * Math.sin(phi);
			double y = Math.cos(theta);
			s.position = new Vector3(x, y, z);
		}
	}

	public void loadStars() {
		try {
			Scanner scanner = new Scanner(new File("src/stars.txt"));
			while(scanner.hasNextLine()) {
				double x = scanner.nextDouble();
				double y = scanner.nextDouble();
				double z = scanner.nextDouble();
				int id = scanner.nextInt();
				double magnitude = scanner.nextDouble();
				int revisedId = scanner.nextInt();
				scanner.nextLine();
				stars.put(id, new Star(new Vector3(x, y, z), id, magnitude, revisedId));
			}
			scanner.close();
		} catch(Exception e) {
			System.err.println(e);
		}
	}

	public void loadConstellations2() {
		try {
			Scanner scanner = new Scanner(new File("src/constellations.txt"));
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] split = line.split(" = ");
				String name = split[0];
				String[] lines = split[1].split(";");
				Constellation c = new Constellation(name, new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255)));
				for(int i = 0; i < lines.length; i++) {
					lines[i] = lines[i].substring(1, lines[i].length() - 1); // remove brackets
					String[] points = lines[i].split(", ");
					ArrayList<Integer> pointsInt = new ArrayList<>();
					for(String point : points) {
						pointsInt.add(Integer.parseInt(point));
					}
					c.stars.add(pointsInt);
				}
				constellations.add(c);
			}
			scanner.close();
		} catch(Exception e) {
			System.err.println(e);
		}
	}

	public static void main(String[] args) {
		StarApp app = new StarApp();
		app.start();
	}
}
