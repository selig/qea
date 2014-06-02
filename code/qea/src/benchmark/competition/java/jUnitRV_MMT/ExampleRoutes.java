package benchmark.competition.java.jUnitRV_MMT;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

// Java - Team3 - Bench5

public class ExampleRoutes {

	public static void main(String... argv) {
		ExampleRoutes r = new ExampleRoutes();
		for (int i = 0; i < 5; ++i) {
			r.chooseNewRoute();
		}
		for (int i = 0; i < 1000; ++i) {
			if ((i + 1) % 10 == 0) {
				System.out.println(i + 1);
			}
			r.freeSomeRoute();
			r.chooseNewRoute();
		}
	}

	public final Random random = new Random(0);
	public Set<Route> routes = new HashSet<>();
	private double minX = 0;
	private double maxX = 0;
	private double minY = 0;
	private double maxY = 0;

	public void blockRoute(Route route) {
		routes.add(route);
		updateLimits();
	}

	public void freeRoute(Route route) {
		routes.remove(route);
		updateLimits();
	}

	public void freeSomeRoute() {
		Route route = routes.iterator().next();
		freeRoute(route);
	}

	public void chooseNewRoute() {
		double pos1 = (random.nextDouble() - 0.5) * 100;
		double pos2;
		int direction = random.nextInt(4);
		if (routes.isEmpty()) {
			pos2 = (random.nextDouble() - 0.5) * 100;
		} else {
			double shift = random.nextDouble() * 10.0;
			switch (direction) {
			case 0:
				pos2 = maxX + shift;
				break;
			case 1:
				pos2 = minY - shift;
				break;
			case 2:
				pos2 = minX - shift;
				break;
			case 3:
				pos2 = maxY + shift;
				break;
			default:
				throw new Error();
			}
		}
		double pos3 = pos1 + (random.nextDouble() - 0.5) * 10.0 + 1.0;
		double pos4;
		switch (direction) {
		case 0:
		case 3:
			pos4 = pos2 + random.nextDouble() * 10.0;
			break;
		case 1:
		case 2:
			pos4 = pos2 - random.nextDouble() * 10.0;
			break;
		default:
			throw new Error();
		}
		Route route;
		switch (direction) {
		case 0:
		case 2:
			route = new Route(pos2, pos1, pos4, pos3);
			break;
		case 1:
		case 3:
			route = new Route(pos1, pos2, pos3, pos4);
			break;
		default:
			throw new Error();
		}
		blockRoute(route);
	}

	private void updateLimits() {
		minX = Double.POSITIVE_INFINITY;
		maxX = Double.NEGATIVE_INFINITY;
		minY = Double.POSITIVE_INFINITY;
		maxY = Double.NEGATIVE_INFINITY;
		for (Route route : routes) {
			minX = Math.min(minX, route.getFromX());
			minX = Math.min(minX, route.getToX());
			maxX = Math.max(maxX, route.getFromX());
			maxX = Math.max(maxX, route.getToX());
			minY = Math.min(minY, route.getFromY());
			minY = Math.min(minY, route.getToY());
			maxY = Math.max(maxY, route.getFromY());
			maxY = Math.max(maxY, route.getToY());
		}
	}

	public static class Route {

		private final double fromX;
		private final double fromY;
		private final double toX;
		private final double toY;

		public Route(double fromX, double fromY, double toX, double toY) {
			this.fromX = fromX;
			this.fromY = fromY;
			this.toX = toX;
			this.toY = toY;
		}

		public double getFromX() {
			return fromX;
		}

		public double getFromY() {
			return fromY;
		}

		public double getToX() {
			return toX;
		}

		public double getToY() {
			return toY;
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 47
					* hash
					+ (int) (Double.doubleToLongBits(this.fromX) ^ (Double
							.doubleToLongBits(this.fromX) >>> 32));
			hash = 47
					* hash
					+ (int) (Double.doubleToLongBits(this.fromY) ^ (Double
							.doubleToLongBits(this.fromY) >>> 32));
			hash = 47
					* hash
					+ (int) (Double.doubleToLongBits(this.toX) ^ (Double
							.doubleToLongBits(this.toX) >>> 32));
			hash = 47
					* hash
					+ (int) (Double.doubleToLongBits(this.toY) ^ (Double
							.doubleToLongBits(this.toY) >>> 32));
			return hash;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Route other = (Route) obj;
			if (Double.doubleToLongBits(this.fromX) != Double
					.doubleToLongBits(other.fromX)) {
				return false;
			}
			if (Double.doubleToLongBits(this.fromY) != Double
					.doubleToLongBits(other.fromY)) {
				return false;
			}
			if (Double.doubleToLongBits(this.toX) != Double
					.doubleToLongBits(other.toX)) {
				return false;
			}
			if (Double.doubleToLongBits(this.toY) != Double
					.doubleToLongBits(other.toY)) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return "Route{" + "fromX=" + fromX + ", fromY=" + fromY + ", toX="
					+ toX + ", toY=" + toY + '}';
		}

	}
}