package org.mess110.jrattrack.models;

public class ResultInterpretor {

	private MovieMeta meta;

	public ResultInterpretor(MovieMeta meta) {
		this.meta = meta;
	}

	public String toString() {
		return "to be implemented" + meta.getAnalyzeFps();
	}
}
