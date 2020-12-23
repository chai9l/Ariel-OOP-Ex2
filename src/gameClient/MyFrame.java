package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class MyFrame extends JFrame {
	private int _ind;
	private Arena _ar;
	private gameClient.util.Range2Range _w2f;
	private int moves = 0;
	private int grade = 0;
	private long time = 0;


	MyFrame() {
		super();
		int _ind = 0;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void update(Arena ar) {
		this._ar = ar;
		importPictures(); //update
		updateFrame();

	}

	private void updateFrame() {

		Range rx = new Range(110, this.getWidth() - 110);
		Range ry = new Range(this.getHeight() - 80, 160);
		Range2D frame = new Range2D(rx, ry);
		directed_weighted_graph g = _ar.getGraph();
		_w2f = Arena.w2f(g, frame);
	}


	public void paint(Graphics g) {
		int w = this.getWidth();
		int h = this.getHeight();
		//update(_ar);
		g.drawImage(background, 0, 0, w, h, this);
		drawPokemons((Graphics2D)g);
		drawGraph((Graphics2D)g);
		drawAgants((Graphics2D)g);
		g.setColor(Color.WHITE);
		g.setFont(g.getFont().deriveFont(15.0f));
		drawInfo(g);
		g.drawString("Grade: " + grade, 20, 50);
		g.drawString("Moves: " + moves, 20, 70);
		g.drawString("Time: 00:" + time, 20, 90);

	}
	private void drawInfo(Graphics g) {
		List<String> str = _ar.get_info();
		String dt = "none";
		for(int i=0;i<str.size();i++) {
			g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
		}

	}

	private void drawGraph(Graphics2D g) {
		directed_weighted_graph gg = _ar.getGraph();
		for (node_data node : gg.getV()) {
			drawNode(node, g);
			for (edge_data edge : gg.getE(node.getKey())) {
				drawEdge(edge, g);
			}
		}
	}


	private void drawPokemons(Graphics2D g) {
		List<CL_Pokemon> fs = _ar.getPokemons();
		if(fs!=null) {
			Iterator<CL_Pokemon> itr = fs.iterator();

			while(itr.hasNext()) {

				CL_Pokemon f = itr.next();
				Point3D c = f.getLocation();
				int r=10;
				Color color = Color.green;
				//g.setColor(Color.green);
				if(f.getType()<0) {
					color = Color.orange;
					//g.setColor(Color.orange);
				}
				if(c!=null) {

					geo_location fp = this._w2f.world2frame(c);
					balbazor(g,fp,f,color);

				}
			}
		}
	}

	private void balbazor(Graphics2D g, geo_location fp, CL_Pokemon f, Color color) {

		int r = 10;
		g.drawImage(balbazor, (int) fp.x() - 30, (int) fp.y() - 30, 9 * r, 8 * r, this);
		//g.drawImage(blur, (int) fp.x() - 36, (int) fp.y() - 78, 120, 50, this);
		g.setColor(color);
		g.drawString("Pokemon", (int) fp.x() - 12, (int) fp.y() - 60);
		g.setColor(Color.WHITE);
		g.drawString("Value:" + f.getValue(), (int) fp.x() - 18, (int) fp.y() - 39);
		
	}

	private void drawAgants(Graphics2D g) {
		List<CL_Agent> rs = _ar.getAgents();
		//	Iterator<OOP_Point3D> itr = rs.iterator();
		g.setColor(Color.red);
		int i=0;
		while(rs!=null && i<rs.size()) {
			geo_location agentLoc = rs.get(i).getLocation();
			int r=15;
			i++;
			if(agentLoc!=null) {
				geo_location fp = this._w2f.world2frame(agentLoc);
				g.drawImage(ash, (int) fp.x() - 20, (int) fp.y() - 20, 4 * r, 4 * r, this);

			}

		}
	}
	private void drawNode(node_data n, Graphics2D g) {
		int r = 12;
		g.setColor(Color.black);
		geo_location pos = n.getLocation();
		geo_location fp = this._w2f.world2frame(pos);
		g.drawImage(pokenode, (int) fp.x() - 15, (int) fp.y() - 30, 4 * r, 4 * r, this);
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
	}
	private void drawEdge(edge_data e, Graphics2D g) {
		int r = 8;
		directed_weighted_graph gg = _ar.getGraph();
		geo_location s = gg.getNode(e.getSrc()).getLocation();
		geo_location d = gg.getNode(e.getDest()).getLocation();
		geo_location s0 = this._w2f.world2frame(s);
		geo_location d0 = this._w2f.world2frame(d);
		g.setStroke(new BasicStroke(7));
		g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
		//	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
	}


	//import images

	static BufferedImage background = null;
	static BufferedImage ash = null;
	static BufferedImage pokenode = null;
	static BufferedImage balbazor = null;


	public static void importPictures() {
		try {

			background = ImageIO.read(new File("pic/background.png"));
			ash = ImageIO.read(new File("pic/ash.png"));
			pokenode = ImageIO.read(new File("pic/pokenode.png"));
			balbazor = ImageIO.read(new File("pic/balba.png"));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setMoves(int moves){
		this.moves = moves;
	}


	public void setTime(long time){
		this.time = time;
	}

}