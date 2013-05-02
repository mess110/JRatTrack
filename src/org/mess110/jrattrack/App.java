package org.mess110.jrattrack;

import java.io.IOException;

import org.mess110.jrattrack.models.MovieMeta;
import org.mess110.jrattrack.util.Util;

public class App {

	public static void main(String[] args) throws IOException {
		String moviePath = "000000.avi";
		MovieMeta meta = new MovieMeta(moviePath, "5");
		//meta.setCircle(176, 115, 120);
		//meta.setRatSize(30);
		//meta.setAnalyzeFps(1);
		meta.readMetaFromDisk();
		Util.d(meta.getAnalyzeFps());

		//Movie movie = new Movie(meta);
		//movie.extractFrames();

		//RatTrack ratTrack = new RatTrack(meta);
		//ratTrack.analyzeAll();
	}
}
