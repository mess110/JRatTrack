package org.mess110.jrattrack.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.mess110.jrattrack.Main;
import org.mess110.jrattrack.models.MovieMeta;
import org.mess110.jrattrack.models.RatTrack;

public class ClickListener implements MouseListener {

	private Main main;

	public ClickListener(Main main) {
		this.main = main;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			main.textCenterX.setText(String.valueOf(e.getX()));
			main.textCenterY.setText(String.valueOf(e.getY()));
			main.drawCircle();
			break;
		case MouseEvent.BUTTON3:
			Image img = main.getImage();
			BufferedImage bufferedImage = new BufferedImage(img.getWidth(null),
					img.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufferedImage.createGraphics();
			g.drawImage(img, null, null);
			
			try {
				ImageIO.write(bufferedImage, "png", new File("test.png"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		default:
			break;
		}
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
