package sym.symplotlib.chart2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.*;
import java.util.ArrayList;

class Text
{
	DataContainer dataContainer;
	FormatContainer formatContainer;
	
	int cw, ch, gx, gy, gw, gh;
	int AXIS_SIZE = 16;
	int LABEL_SIZE = 20;
	int TITLE_SIZE = 24;
	Font fontAxis, fontLabel, fontTitle;
	
	public Text(DataContainer _dataContainer, FormatContainer _formatContainer)
	{
		dataContainer = _dataContainer;
		formatContainer = _formatContainer;
		
		fontAxis = new Font("notosans", Font.PLAIN, AXIS_SIZE);
		fontLabel = new Font("notosans", Font.PLAIN, LABEL_SIZE);
		fontTitle = new Font("notosans", Font.PLAIN, TITLE_SIZE);
	}
	
	public void resize()
	{
		cw = formatContainer.cw;
		ch = formatContainer.ch;
		gx = formatContainer.gx;
		gy = formatContainer.gy;
		gw = formatContainer.gw;
		gh = formatContainer.gh;
	}
	
	public void draw(Graphics2D g2)
	{
		{
			//draw axis
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(new Color(0x000000));
			g2.setFont(fontAxis);
			{
				ArrayList<Double> marksDoubleX = formatContainer.marksDoubleX;
				ArrayList<String> marksStringX = formatContainer.marksStringX;
				for(int i = 0; i < marksDoubleX.size(); i++)
				{
					double px = gw * formatContainer.translateX(marksDoubleX.get(i));
					double py = gh + 5;
					drawString(g2, marksStringX.get(i), gx + px, gy + py, 0, 1);
				}
				
				if(formatContainer.markX != null)
				{
					double px = gw;
					double py = gh + 5 + getFontHeight(g2, fontAxis);
					drawString(g2, formatContainer.markX, gx + px, gy + py, 1, 1);
				}
			}
			{
				ArrayList<Double> marksDoubleY = formatContainer.marksDoubleY;
				ArrayList<String> marksStringY = formatContainer.marksStringY;
				for(int i = 0; i < marksDoubleY.size(); i++)
				{
					double px = -10;
					double py = gh * (1 - formatContainer.translateY(marksDoubleY.get(i)));
					drawString(g2, marksStringY.get(i), gx + px, gy + py, 1, 0.4);
				}
				
				if(formatContainer.markY != null)
				{
					double px = -10;
					double py = -getFontHeight(g2, fontAxis);
					drawString(g2, formatContainer.markY, gx + px, gy + py, 1, 0.4);
				}
			}
		}
		{
			//draw label
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(new Color(0x000000));
			g2.setFont(fontLabel);
			if(formatContainer.xLabel != null)
			{
				double px = 0.5 * gw;
				double py = gh + getFontHeight(g2, fontAxis) + 10;
				drawString(g2, formatContainer.xLabel, gx + px, gy + py, 0, 1);
			}
			if(formatContainer.yLabel != null)
			{
				double width = getMarksWidth(g2, fontAxis, formatContainer.marksStringY);
				
				double px = -20 - width;
				double py = 0.5 * gh;
				AffineTransform o = g2.getTransform();
				AffineTransform n = new AffineTransform();
				n.setToRotation(Math.toRadians(-90), gx + px, gy + py);
				g2.setTransform(n);
				drawString(g2, formatContainer.yLabel, gx + px, gy + py, 0, -1);
				g2.setTransform(o);
			}
		}
		if(formatContainer.title != null)
		{
			//draw title
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(new Color(0x000000));
			g2.setFont(fontTitle);
			{
				double px = 0.5 * gw;
				double py = -10;
				drawString(g2, formatContainer.title, gx + px, gy + py, 0, -1);
			}
		}
	}
	
	void drawString(Graphics2D g2, String text, double _x, double _y, double _relX, double _relY)
	{
		FontMetrics metrics = g2.getFontMetrics();
		float x = (float)_x, y = (float)_y, relX = (float)_relX, relY = (float)_relY;
		float dx, dy;
		{
			float ascent = metrics.getAscent();
			float descent = metrics.getDescent();
			float width = metrics.stringWidth(text);
			
			dx = -0.5f * width * (relX + 1);
			
			if(relY >= 0)
			{
				dy = ascent * relY;
			}
			else
			{
				dy = descent * relY;
			}
		}
		g2.drawString(text, x + dx, y + dy);
	}
	
	int getFontHeight(Graphics2D g2, Font font)
	{
		FontMetrics metrics = g2.getFontMetrics(font);
		return metrics.getHeight();
	}
	
	double getMarksWidth(Graphics2D g2, Font font, ArrayList<String> marks)
	{
		double width = 0;
		FontMetrics metrics = g2.getFontMetrics(font);
		for(int i = 0; i < marks.size(); i++)
		{
			double _width = metrics.stringWidth(formatContainer.marksStringY.get(i));
			if(_width > width)
			{
				width = _width;
			}
		}
		return width;
	}
}