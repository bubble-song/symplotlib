package sym.symplotlib.chart2D;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.ArrayList;

class Chart2D extends JFrame
{
	DataContainer dataContainer;
	FormatContainer formatContainer;
	MyCanvas canvas;
	
	public Chart2D(DataContainer _dataContainer, FormatContainer _formatContainer)
	{
		dataContainer = _dataContainer;
		formatContainer = _formatContainer;
		canvas = new MyCanvas();
		
		this.setLayout(new BorderLayout());
		this.add(canvas);
		this.pack();
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void myPaint()
	{
		this.setTitle("Chart2D");
		canvas.myPaint();
	}
	
	class MyCanvas extends JPanel
	{
		int cw, ch, gx, gy, gw, gh;
		Graph graph;
		Text text;
		
		public MyCanvas()
		{
			graph = new Graph(dataContainer, formatContainer);
			text = new Text(dataContainer, formatContainer);
			
			resize();
			
			this.setPreferredSize(new Dimension(formatContainer.cw, formatContainer.ch));
			
			MouseHandler mh = new MouseHandler();
			this.addMouseListener(mh);
			this.addMouseMotionListener(mh);
			this.addMouseWheelListener(mh);
		}
		
		void updateSize()
		{
			int w = this.getWidth();
			int h = this.getHeight();
			if(formatContainer.cw != w || formatContainer.ch != h)
			{
				formatContainer.updateSize(w, h);
				resize();
			}
		}
		
		void resize()
		{
			cw = formatContainer.cw;
			ch = formatContainer.ch;
			gx = formatContainer.gx;
			gy = formatContainer.gy;
			gw = formatContainer.gw;
			gh = formatContainer.gh;
			graph.resize();
			text.resize();
		}
		
		public void myPaint()
		{
			this.repaint();
		}
		
		@Override
		public void paint(Graphics g)
		{
			synchronized(dataContainer.dataArray)
			{
				updateSize();
				formatContainer.generateMarks();
				
				{
					//clear
					Graphics2D g2 = (Graphics2D)g.create();
					g2.setColor(new Color(0xFFFFFF));
					g2.fillRect(0, 0, cw, ch);
				}
				{
					//draw graph
					Graphics2D g2 = (Graphics2D)g.create();
					graph.draw(g2);
				}
				{
					//draw text
					Graphics2D g2 = (Graphics2D)g.create();
					text.draw(g2);
				}
			}
		}
		
		class MouseHandler extends MouseAdapter implements MouseWheelListener
		{
			double oldX, oldY;
			String stat = "free";
			@Override
			public void mouseMoved(MouseEvent me)
			{
				double relX = (double)(me.getX() - gx) / gw;
				double relY = 1 - (double)(me.getY() - gy) / gh;
				if(relX >= 0 && relX <= 1 && relY >= 0 && relY <= 1)
				{
					MyCanvas.this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				}
				else
				{
					MyCanvas.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
			@Override
			public void mousePressed(MouseEvent me)
			{
				double relX = (double)(me.getX() - gx) / gw;
				double relY = 1 - (double)(me.getY() - gy) / gh;
				if(relX >= 0 && relX <= 1 && relY >= 0 && relY <= 1)
				{
					if(me.getButton() == MouseEvent.BUTTON1)
					{
						stat = "busy-left";
					}
					else if(me.getButton() == MouseEvent.BUTTON3)
					{
						stat = "busy-right";
					}
					else
					{
						return;
					}
					oldX = relX;
					oldY = relY;
				}
			}
			@Override
			public void mouseReleased(MouseEvent me)
			{
				if(me.getButton() == MouseEvent.BUTTON1 && stat == "busy-left")
				{
					stat = "free";
				}
				else if(me.getButton() == MouseEvent.BUTTON3 && stat == "busy-right")
				{
					stat = "free";
				}
			}
			@Override
			public void mouseDragged(MouseEvent me)
			{
				double relX = (double)(me.getX() - gx) / gw;
				double relY = 1 - (double)(me.getY() - gy) / gh;
				double dx = relX - oldX;
				double dy = relY - oldY;
				oldX = relX;
				oldY = relY;
				if(stat == "busy-left")
				{
					Chart2D.this.formatContainer.move(-dx, -dy);
					MyCanvas.this.repaint();
				}
				else if(stat == "busy-right")
				{
					Chart2D.this.formatContainer.zoom(4 * dx, 4 * dy, 0.5, 0.5);
					MyCanvas.this.repaint();
				}
			}
			@Override
			public void mouseWheelMoved(MouseWheelEvent mwe)
			{
				double relX = (double)(mwe.getX() - gx) / gw;
				double relY = 1 - (double)(mwe.getY() - gy) / gh;
				if(relX >= 0 && relX <= 1 && relY >= 0 && relY <= 1)
				{
					if(stat == "free")
					{
						double zoomX = 0, zoomY = 0;
						if(mwe.getWheelRotation() == 1)
						{
							zoomX = -0.2;
							zoomY = -0.2;
						}
						else if(mwe.getWheelRotation() == -1)
						{
							zoomX = +0.2;
							zoomY = +0.2;
						}
						Chart2D.this.formatContainer.zoom(zoomX, zoomY, relX, relY);
						MyCanvas.this.repaint();
					}
				}
			}
		}
	}
}