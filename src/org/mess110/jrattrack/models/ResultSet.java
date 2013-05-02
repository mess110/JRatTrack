package org.mess110.jrattrack.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ResultSet<E extends Comparable<E>> {

	private ArrayList<E> top;
	private int maxSize;

	public ResultSet(int maxSize) {
		this.top = new ArrayList<E>();
		this.maxSize = maxSize;
	}

	public void push(E element) {
		top.add(element);
		sort();
	}

	private void clean() {
		while (top.size() > maxSize) {
			top.remove(top.size() - 1);
		}
	}

	private void sort() {
		Collections.sort(top, new Comparator<E>() {
			@Override
			public int compare(E a, E b) {
				return a.compareTo(b);
			}
		});
		clean();
	}
	
	public void println() {
		for (E e : top) {			
			System.out.println(e);
		}
	}

	public ArrayList<E> getElements() {
		return top;
	}
}
