package org.mess110.jrattrack.models;

import java.awt.image.BufferedImage;

import org.mess110.jrattrack.util.Util;

public class IntegralImage {

	private float[][] integralValues;
	private BufferedImage bi;
	private int width;
	private int height;

	public IntegralImage(BufferedImage bi) {
		this.bi = bi;
		this.width = bi.getWidth();
		this.height = bi.getHeight();
		toIntegralImage();
	}

	private int getPixelValue(int x, int y) {
		return bi.getRGB(x, y) == -1 ? 0 : 1;
	}

	private void toIntegralImage() {
		integralValues = new float[width][height];

		integralValues[0][0] = getPixelValue(0, 0);
		for (int i = 1; i < width; i++) {
			integralValues[i][0] = getPixelValue(i, 0)
					+ integralValues[i - 1][0];
		}
		for (int j = 1; j < height; j++) {
			integralValues[0][j] = getPixelValue(0, j)
					+ integralValues[0][j - 1];
		}

		for (int i = 1; i < width; i++) {
			for (int j = 1; j < height; j++) {
				integralValues[i][j] = getPixelValue(i, j)
						+ integralValues[i][j - 1] + integralValues[i - 1][j]
						- integralValues[i - 1][j - 1];
			}
		}
	}

	private float getIntegralValue(int x, int y) {
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		if (x > width - 1) {
			x = width - 1;
		}
		if (y > height - 1) {
			y = height - 1;
		}
		return integralValues[x][y];
	}

	public float getIntegralSum(int x, int y, int s) {
		return getIntegralValue(x + s, y + s) + getIntegralValue(x, y)
				- getIntegralValue(x + s, y) - getIntegralValue(x, y + s);
	}
}
