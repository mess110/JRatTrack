package org.mess110.jrattrack.models;

public class PossibleRat implements Comparable<PossibleRat> {

	public int x, y, w;
	public float sum;
	private String timestamp;

	public PossibleRat(String timestamp, int x, int y, int w, float sum) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.sum = sum;
		this.timestamp = timestamp;
	}

	public PossibleRat(String line) {
		String[] r = line.split(",");
		timestamp = r[0];
		x = Integer.valueOf(r[1]);
		y = Integer.valueOf(r[2]);
		w = Integer.valueOf(r[3]);
		sum = Float.valueOf(r[4]);
	}

	@Override
	public int compareTo(PossibleRat o) {
		if (sum == o.sum) {
			return 0;
		} else if (sum < o.sum) {
			return 1;
		} else {
			return -1;
		}
	}

	public String toString() {
		return timestamp + "," + x + "," + y + "," + w + "," + sum;
	}
}
