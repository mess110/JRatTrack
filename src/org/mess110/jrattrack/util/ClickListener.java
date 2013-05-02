package org.mess110.jrattrack.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.mess110.jrattrack.Main;

public class ClickListener implements MouseListener {

	private Main main;

	public ClickListener(Main main) {
		this.main = main;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		main.textCenterX.setText(String.valueOf(e.getX()));
		main.textCenterY.setText(String.valueOf(e.getY()));
		main.drawCircle();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}
}
