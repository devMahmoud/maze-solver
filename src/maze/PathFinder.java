package maze;


import java.awt.*;
import java.applet.*;
import java.awt.event.*;


public class PathFinder extends Applet implements MouseListener, MouseMotionListener, Runnable{
	Graphics graph;
	boolean targetfound = false;
	int DELAY = 50;
	int VISITED = 2;
	int WALL = 1;
	int MOVEABLE = 0;
	int map[][] = new int[28][15];
	int startx = 1, starty = 1;
	int endx = 26, endy = 13;
	Button btnGo = new Button(" Go ");
	TextField txtstartx = new TextField("1");
	TextField txtstarty = new TextField("1");
	TextField txtendx = new TextField("26");
	TextField txtendy = new TextField("13");
	TextField txtMessage = new TextField("By Mahmoud Mohamed");
	Button btnRefresh = new Button("Refresh");
	Thread thread = null;
	
	public void init() {
		int i, j;
		graph = getGraphics();
		addMouseListener(this);
		addMouseMotionListener(this);
		add(txtstartx);
		add(txtstarty);
		add(btnGo);
		add(txtendx);
		add(txtendy);
		add(btnRefresh);
		btnGo.addActionListener(new GoButtonListener());
		btnRefresh.addActionListener(new RefreshButtonListener());
		add(txtMessage);
		//thread = new Thread(this);
		
		//Mark all the map squares as MOVEABLE
		for (i = 0; i < 28; i++)
			for (j = 0; j < 15; j++)
				map[i][j] = MOVEABLE;
		
    	//Mark the Horizontal Boundary Walls on the map
		for (i = 0; i < 28; i++)
		{
			map[i][0] = WALL;	
			map[i][14] = WALL;
		}
		//Mark the Vertical Boundary Walls on the map
		for (i = 0; i < 15; i++)
		{
			map[0][i] = WALL;
			map[27][i] = WALL;
		}

	}
	
	public void mouseClicked(MouseEvent e){
	}
	
	public void mouseDragged(MouseEvent e){
		int clickx = e.getX() / 25;
		int clicky = e.getY() / 25;
		map[clickx][clicky] = WALL;
		repaint();
	}
	
	public void mouseMoved(MouseEvent e){
	}
	
	public void mousePressed(MouseEvent e){
		int clickx = e.getX() / 25;
		int clicky = e.getY() / 25;
		map[clickx][clicky] = (map[clickx][clicky] == WALL ? MOVEABLE : WALL);
		repaint();

	}
	
	public void mouseReleased(MouseEvent e){
	}
	
	public void mouseEntered(MouseEvent e){
	}
	
	public void mouseExited(MouseEvent e){
	}

	public void paint(Graphics g) {
		int i, j;
		
		//Draw the map on the screen
		for (i = 0; i < 28; i++)
			for (j = 0; j < 15; j++)
			{
				if (map[i][j] == WALL)
					g.setColor(Color.black);
				else if (i == startx && j == starty)
					g.setColor(Color.blue);
				else if (i == endx && j == endy)
					g.setColor(Color.green);
				else
					g.setColor(Color.white);
				g.fillRect(i*25, j*25, 25, 25);
			}
			
		//Draw Grid	
		g.setColor(Color.black);
		for (i = 0; i <= 375; i += 25)
			g.drawLine(0, i, 700, i);
		for (i = 0; i <= 700; i += 25)
			g.drawLine(i, 0, i, 370);

	}
	
	public void run(){
		txtMessage.setText("Looking for exit");
		TravelNextNode(startx, starty, 1);
		if (targetfound)
			txtMessage.setText("Found exit :)");
		else
			txtMessage.setText("Cound not find exit :(");
	}
	
	void TravelNextNode(int x, int y, int level)
	{
		try{
		repaint();
		thread.sleep(DELAY);
		if (x == endx && y == endy)
			targetfound = true;
		map[x][y] = VISITED;
		startx = x; starty = y;
		if (targetfound) return;
		//Travel Up
		if (map[x][y-1] != WALL && map[x][y-1] != VISITED)
			TravelNextNode(x, y-1, level+1);
		startx = x; starty = y;
		repaint();
		thread.sleep(DELAY);
		if (targetfound) {endx = x; endy = y;return;}
		//Travel Down
		if (map[x][y+1] != WALL && map[x][y+1] != VISITED)
		 	TravelNextNode(x, y+1, level+1);
		startx = x; starty = y;
		repaint();
		thread.sleep(DELAY);
		if (targetfound) {endx = x; endy = y;return;}
		
		if (map[x-1][y] != WALL && map[x-1][y] != VISITED)
			TravelNextNode(x-1, y, level+1);
		startx = x; starty = y;
		repaint();
		thread.sleep(DELAY);
		if (targetfound) {endx = x; endy = y;return;}
		//Travel Right
		if (map[x+1][y] != WALL && map[x+1][y] != VISITED)
			TravelNextNode(x+1, y, level+1);
		startx = x; starty = y;
		repaint();
		thread.sleep(DELAY);
		if (targetfound) {endx = x; endy = y; return;}
		}
		catch(InterruptedException ie){
			System.out.println("Interrupted");
		}
	}
	
	class GoButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			for (int i = 0; i < 28; i++)
				for (int j = 0; j < 15; j++)
					if (map[i][j] == VISITED)
						map[i][j] = MOVEABLE;
			targetfound = false;
			thread = new Thread(PathFinder.this);
			thread.start();
		}
	}
	
	class RefreshButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			startx = Integer.parseInt(txtstartx.getText());
			starty = Integer.parseInt(txtstarty.getText());
			endx   = Integer.parseInt(txtendx.getText());
			endy   = Integer.parseInt(txtendy.getText());
			if (startx > 0 && starty > 0 && startx < 28 && starty < 14 &&
				endx > 0 && endy > 0 && endx < 28 && endy < 14){	
					repaint();
			}
			else
			{
				startx = starty = 1;
				endx = 26; endy = 13;
				txtMessage.setText("Invalid Coordinates");
			}
		}
	}
}
