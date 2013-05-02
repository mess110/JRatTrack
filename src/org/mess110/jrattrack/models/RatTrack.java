package org.mess110.jrattrack.models;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.mess110.jrattrack.util.Util;
import org.mess110.jrattrack.util.exceptions.CircleNotSet;
import org.mess110.jrattrack.util.exceptions.InvalidRatSize;
import org.mess110.jrattrack.util.exceptions.JRatException;

public class RatTrack {

	private MovieMeta meta;
	private final int BLACK = (new Color(0, 0, 0, 0)).getRGB();
	private final int WHITE = (new Color(255, 255, 255, 255)).getRGB();

	public RatTrack(MovieMeta meta) throws JRatException {
		this.meta = meta;
		if (meta.getRatSize() == 0) {
			throw new InvalidRatSize();
		}
		if (!meta.isCircleSet()) {
			throw new CircleNotSet();
		}
	}

	public void analyze(String path, boolean saveAnalyzedFrames)
			throws IOException {
		analyze(new File(path), saveAnalyzedFrames);
	}

	public void analyze(String path) throws IOException {
		analyze(path, false);
	}

	public void analyze(File file) throws IOException {
		analyze(file, false);
	}

	public void analyze(File file, boolean saveAnalyzedFrames)
			throws IOException {
		BufferedImage bi = toBufferedImage(file);
		ratFilter(bi);
		ResultSet<PossibleRat> resultSet = new ResultSet<PossibleRat>(2);
		for (int x = 0; x < bi.getWidth(); x += getMeta().getRatSize()) {
			for (int y = 0; y < bi.getHeight(); y += getMeta().getRatSize()) {
				resultSet.push(new PossibleRat(String.valueOf(Util
						.getTimestampName(file)), x, y, meta.getRatSize(), sum(
						bi, x, y)));
			}
		}
		meta.addResultSet(resultSet);
		if (saveAnalyzedFrames) {
			for (PossibleRat pr : resultSet.getElements()) {
				Util.drawSquare(bi, pr.x, pr.y, pr.w, pr.w);
			}
			saveToDisk(bi, file);
		}
	}

	public int sumSum(File file, int x, int y) {
		try {
			BufferedImage bi = toBufferedImage(file);
			ratFilter(bi);
			return sum(bi, x, y);
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public int sum(BufferedImage bi, int x, int y) {
		int sum = 0;
		for (int i = x; i < x + meta.getRatSize(); i++) {
			for (int j = y; j < y + meta.getRatSize(); j++) {
				try {
					if (meta.isInCircle(i, j)) {
						sum += bi.getRGB(i, j) == -1 ? 0 : 1;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					sum += 0;
				}
			}
		}
		return sum;
	}

	private void ratFilter(BufferedImage bi) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int v = bi.getRGB(x, y);
				if (isSwimmingPool(v)) {
					bi.setRGB(x, y, WHITE);
				} else {
					if (meta.isInCircle(x, y)) {
						bi.setRGB(x, y, BLACK);
					} else {
						bi.setRGB(x, y, WHITE);
					}
				}
			}
		}
	}

	private boolean isSwimmingPool(int v) {
		Color c = new Color(v);
		return c.getRed() < 20 && c.getGreen() < 20 && c.getBlue() < 20;
	}

	private BufferedImage toBufferedImage(File file) throws IOException {
		return ImageIO.read(file);
	}

	private void saveToDisk(BufferedImage bi, File file) throws IOException {
		ImageIO.write(bi, "png", meta.getProcessedEquivalent(file));
	}

	public void analyzeAll() throws IOException {
		File[] frames = Util.contentsOf(meta.getFramesPath());
		for (File f : frames) {
			analyze(f);
		}
	}

	public MovieMeta getMeta() {
		return meta;
	}
}
