package org.mess110.jrattrack.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.xuggle.xuggler.IPacket;

public class Util {

	public static final String OUTPUT = "output";
	public static final String NEWLINE = System.getProperty("line.separator");
	public static final int MAX_RESULTS = 2;

	public static void d(String s) {
		System.out.println(s);
	}

	public static void d(int i) {
		d(String.valueOf(i));
	}

	public static void d(double d) {
		d(String.valueOf(d));
	}

	public static void d(long l) {
		d(String.valueOf(l));
	}

	public static void d(boolean b) {
		d(String.valueOf(b));
	}

	public static void d(float f) {
		d(String.valueOf(f));
	}

	public static void d(File[] files) {
		d(Arrays.toString(files));
	}

	public static File[] contentsOf(String path) {
		return new File(path).listFiles();
	}

	public static void drawSquare(BufferedImage bi, int x, int y, int w, int h) {
		Graphics2D graph = bi.createGraphics();
		graph.setColor(Color.GREEN);
		graph.drawRect(x, y, w, h);
		graph.dispose();
	}

	public static long getTimestamp(IPacket packet) {
		return (long) (packet.getTimeStamp() * packet.getTimeBase().getDouble() * 1000);
	}

	public static void toast(JFrame frame, String s) {
		JOptionPane.showMessageDialog(frame, s);
	}

	public static long getTimestampName(File f) {
		return Long.valueOf(f.getName().replaceAll(".png", ""));
	}
}
