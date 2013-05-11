package org.mess110.jrattrack;

import java.io.IOException;

import org.mess110.jrattrack.models.MovieMeta;
import org.mess110.jrattrack.models.RatTrack;
import org.mess110.jrattrack.util.Util;
import org.mess110.jrattrack.util.exceptions.JRatException;

public class App {

	public static void main(String[] args) throws IOException, JRatException {
		String moviePath = "000000.avi";
		MovieMeta meta = new MovieMeta(moviePath, "1");
		//meta.setCircle(176, 115, 120);
		//meta.setRatSize(30);
		//meta.setAnalyzeFps(1);
		meta.readMetaFromDisk();

		//Movie movie = new Movie(meta);
		//movie.extractFrames();

		RatTrack ratTrack = new RatTrack(meta);
		ratTrack.analyze("output/1/frames/177400.png", true);
		//ratTrack.analyzeAll();
	}
}
