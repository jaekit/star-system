import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import java.util.*;
import java.io.File;

public class StarApp implements KeyListener {
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
	double directionX = 0.0;
	double directionY = 0.0;

	public StarApp() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1100, 1100);
		frame.setVisible(true);
		frame.add(panel);
		frame.addKeyListener(this);
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
			s.position = incrementPhi(s.position.x, s.position.y, s.position.z, directionX);
			Vector3 position = incrementPhi(s.position.z, s.position.x, s.position.y, -directionY);
			s.position = new Vector3(position.y, position.z, position.x);
		}
	}

	public Vector3 incrementPhi(double x, double y, double z, double delta) {
		double distance = Math.sqrt(Math.pow(z, 2) + Math.pow(x, 2));
		double theta = Math.acos(y);
		double phi = Math.signum(x) * Math.acos(z / distance);

		// increment phi
		phi += delta * 0.01;

		// convert back to cartesian coordinates.
		z = Math.sin(theta) * Math.cos(phi);
		x = Math.sin(theta) * Math.sin(phi);
		y = Math.cos(theta);
		return new Vector3(x, y, z);
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

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			directionY = -1;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			directionY = 1;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			directionX = -1;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			directionX = 1;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			if (directionY == -1) {
				directionY = 0;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (directionY == 1) {
				directionY = 0;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (directionX == -1) {
				directionX = 0;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (directionX == 1) {
				directionX = 0;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
