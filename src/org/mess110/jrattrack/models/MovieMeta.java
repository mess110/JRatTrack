package org.mess110.jrattrack.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import org.mess110.jrattrack.util.Util;

public class MovieMeta {

	private String inputPath;
	private String outputPath;
	private int cX;
	private int cY;
	private int cR;
	private int ratSize;
	private ArrayList<PossibleRat> resultSet;
	private int analyzeFps = 1;

	public MovieMeta(String inputPath) {
		mkdir(Util.OUTPUT);
		this.inputPath = inputPath;
		this.resultSet = new ArrayList<PossibleRat>();
		int folderCount = Util.contentsOf(Util.OUTPUT).length + 1;
		prepareFolders(String.valueOf(folderCount));
	}

	public MovieMeta(String inputPath, String folderName) {
		this.inputPath = inputPath;
		this.resultSet = new ArrayList<PossibleRat>();
		prepareFolders(folderName);
	}

	public void setCircle(int cX, int cY, int cR) {
		this.cX = cX;
		this.cY = cY;
		this.cR = cR;
	}

	private void prepareFolders(String folderName) {
		outputPath = Util.OUTPUT + "/" + folderName;
	}

	public void mkdirs() {
		mkdir(getFramesPath());
		mkdir(getProcessedPath());
	}

	public String getFramesPath() {
		return outputPath + "/frames";
	}

	public String getProcessedPath() {
		return outputPath + "/processed";
	}

	public String getOutputLogPath() {
		return outputPath + "/output.log";
	}

	private String getMetaPath() {
		return outputPath + "/meta";
	}

	private void mkdir(String path) {
		File folder = new File(path);
		folder.mkdirs();
	}

	public String getInputPath() {
		return inputPath;
	}

	public File getProcessedEquivalent(File file) {
		String newPath = file.getPath();
		return new File(newPath.replaceAll("frames", "processed"));
	}

	public boolean isInCircle(int x, int y) {
		return (((x - cX) * (x - cX)) + ((y - cY) * (y - cY))) < (cR * cR);
	}

	public String toString() {
		return cX + " - " + cY + " - " + cR;
	}

	public void setRatSize(int i) {
		this.ratSize = i;
	}

	public int getRatSize() {
		return ratSize;
	}

	public void addResultSet(ResultSet<PossibleRat> rs) {
		if (resultSet == null) {
			resultSet = new ArrayList<PossibleRat>();
		}
		for (PossibleRat pr : rs.getElements()) {
			resultSet.add(pr);
		}
	}

	public ArrayList<PossibleRat> getResultSet() {
		return resultSet;
	}

	public void setAnalyzeFps(int i) {
		this.analyzeFps = i;
	}

	public void readMetaFromDisk() {
		File file = new File(getMetaPath());

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			setAnalyzeFps(Integer.valueOf(br.readLine()));
			setRatSize(Integer.valueOf(br.readLine()));
			cX = Integer.valueOf(br.readLine());
			cY = Integer.valueOf(br.readLine());
			cR = Integer.valueOf(br.readLine());
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getAnalyzeFps() {
		return analyzeFps;
	}

	public void readLog(File selectedFile) {
		readLog(selectedFile.getPath());
	}

	public void readLog(String olp) {
		clearResultSet();
		File file = new File(olp);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			String line;
			int count = 0;
			while ((line = br.readLine()) != null) {
				count++;
				switch (count) {
				case 1:
					setAnalyzeFps(Integer.valueOf(line));
					break;
				case 2:
					setRatSize(Integer.valueOf(line));
					break;
				case 3:
					cX = Integer.valueOf(line);
					break;
				case 4:
					cY = Integer.valueOf(line);
					break;
				case 5:
					cR = Integer.valueOf(line);
					break;
				default:
					resultSet.add(new PossibleRat(line));
					break;
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readLog() {
		readLog(getOutputLogPath());
	}

	public void writeLog(String outputLogPath) {
		try {
			FileWriter fstream = new FileWriter(outputLogPath);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(String.valueOf(getAnalyzeFps()) + Util.NEWLINE);
			out.write(String.valueOf(getRatSize()) + Util.NEWLINE);
			out.write(String.valueOf(cX) + Util.NEWLINE);
			out.write(String.valueOf(cY) + Util.NEWLINE);
			out.write(String.valueOf(cR) + Util.NEWLINE);
			for (PossibleRat pr : resultSet) {
				out.write(pr.toString() + Util.NEWLINE);
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeLog() {
		writeLog(getOutputLogPath());
	}

	public void writeMeta() {
		try {
			FileWriter fstream = new FileWriter(getMetaPath());
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(String.valueOf(getAnalyzeFps()) + Util.NEWLINE);
			out.write(String.valueOf(getRatSize()) + Util.NEWLINE);
			out.write(String.valueOf(cX) + Util.NEWLINE);
			out.write(String.valueOf(cY) + Util.NEWLINE);
			out.write(String.valueOf(cR) + Util.NEWLINE);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ImageIcon getRandomFrame() {
		File[] possibleFrames = new File(getFramesPath()).listFiles();
		int r = new Random().nextInt(possibleFrames.length);
		return new ImageIcon(possibleFrames[r].getPath());
	}

	public boolean framesAreExtracted() {
		return new File(getFramesPath()).listFiles().length != 0;
	}

	public int getCircleX() {
		return cX;
	}

	public int getCircleY() {
		return cY;
	}

	public int getCircleR() {
		return cR;
	}

	public boolean isCircleSet() {
		return !(cX == 0 && cY == 0 && cR == 0);
	}

	public void clearResultSet() {
		resultSet.clear();
	}
}
