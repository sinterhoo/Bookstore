import java.awt.event.*;
import javax.swing.*;
import java.awt.*;


public class ImagePanel extends JPanel {
	private Image img;
	
	public ImagePanel (Image img) {
		this.img = img;
		setSize(new Dimension(img.getWidth(null),img.getHeight(null)));
		setLayout(null);
	}
	
	public void paintComponent (Graphics g) {
		g.drawImage(img,0,0,null);
		setOpaque(false);
		super.paintComponent(g);
	}

}
