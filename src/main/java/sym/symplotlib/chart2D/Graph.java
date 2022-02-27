package sym.symplotlib.chart2D;

import sym.symplotlib.font.FontLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.*;
import java.util.ArrayList;

class Graph
{
	DataContainer dataContainer;
	FormatContainer formatContainer;
	LabelTag labelTag;
	public BufferedImage image;
	int cw, ch, gx, gy, gw, gh;
	
	public Graph(DataContainer _dataContainer, FormatContainer _formatContainer)
	{
		dataContainer = _dataContainer;
		formatContainer = _formatContainer;
		
		labelTag = new LabelTag();
	}
	
	public void resize()
	{
		cw = formatContainer.cw;
		ch = formatContainer.ch;
		gx = formatContainer.gx;
		gy = formatContainer.gy;
		gw = formatContainer.gw;
		gh = formatContainer.gh;
		image = new BufferedImage(gw, gh, BufferedImage.TYPE_INT_ARGB);
	}
	
	public void draw(Graphics2D g2)
	{
		myPaint();
		g2.drawImage(image, gx, gy, null);
	}
	
	public void myPaint()
	{
		Graphics2D g2 = image.createGraphics();
		
		{
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
			g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
			{
				//clear
				g2.setColor(new Color(0xFFFFFF));
				g2.fillRect(0, 0, gw, gh);
			}
			{
				//draw grid
				int MARGIN_WIDTH = 8;
				int MARIN_COLOR = 0xAA000000;
				int GRID_COLOR = 0x55000000;
				{
					g2.setStroke(new BasicStroke(1f));
					ArrayList<Double> marksX = formatContainer.marksDoubleX;
					for(int i = 0; i < marksX.size(); i++)
					{
						int px = (int)Math.round(gw * formatContainer.translateX(marksX.get(i)));
						g2.setColor(new Color(GRID_COLOR, true));
						g2.drawLine(px, MARGIN_WIDTH, px, gh - 1 - MARGIN_WIDTH);
						g2.setColor(new Color(MARIN_COLOR, true));
						g2.drawLine(px, 1, px, MARGIN_WIDTH - 1);
						g2.drawLine(px, gh - MARGIN_WIDTH, px, gh - 2);
					}
				}
				{
					ArrayList<Double> marksY = formatContainer.marksDoubleY;
					for(int i = 0; i < marksY.size(); i++)
					{
						int py = (int)Math.round(gh * (1 - formatContainer.translateY(marksY.get(i))));
						g2.setColor(new Color(GRID_COLOR, true));
						g2.drawLine(MARGIN_WIDTH, py, gw - 1 - MARGIN_WIDTH, py);
						g2.setColor(new Color(MARIN_COLOR, true));
						g2.drawLine(1, py, MARGIN_WIDTH - 1, py);
						g2.drawLine(gw - MARGIN_WIDTH, py, gw - 2, py);
					}
				}
			}
		}
		{
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
			{
				//draw data
				for(DataStruct data : dataContainer.dataArray)
				{
					if(data.length == 0)
					{
						continue;
					}
					
					g2.setColor(new Color(data.color));
					
					if(data.lineStyle == "-")
					{
						Path2D.Double path = new Path2D.Double();
						path.moveTo(gw * formatContainer.translateX(data.x[0]), gh * (1 - formatContainer.translateY(data.y[0])));
						for(int i = 1; i < data.length; i++)
						{
							path.lineTo(gw * formatContainer.translateX(data.x[i]), gh * (1 - formatContainer.translateY(data.y[i])));
						}
						g2.draw(path);
					}
					else if(data.lineStyle == "--")
					{
						Stroke dashed = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {9}, 0);
						Path2D.Double path = new Path2D.Double();
						path.moveTo(gw * formatContainer.translateX(data.x[0]), gh * (1 - formatContainer.translateY(data.y[0])));
						for(int i = 1; i < data.length; i++)
						{
							path.lineTo(gw * formatContainer.translateX(data.x[i]), gh * (1 - formatContainer.translateY(data.y[i])));
						}
						g2.draw(dashed.createStrokedShape(path));
					}
					else if(data.lineStyle == ".")
					{
						Ellipse2D.Double shape = new Ellipse2D.Double();
						for(int i = 0; i < data.length; i++)
						{
							shape.setFrame(gw * formatContainer.translateX(data.x[i]) - 2, gh * (1 - formatContainer.translateY(data.y[i])) - 2, 5, 5);
							
							g2.fill(shape);
						}
					}
					else if(data.lineStyle == "*")
					{
						g2.setFont(FontLoader.getFont("times_new_roman", Font.PLAIN, 30));
						for(int i = 0; i < data.length; i++)
						{
							float px = (float)(gw * formatContainer.translateX(data.x[i]));
							float py = (float)(gh * (1 - formatContainer.translateY(data.y[i])));
							g2.drawString("*", px - 8 + 1, py + 15);
						}
					}
					else if(data.lineStyle == "^")
					{
						g2.setFont(FontLoader.getFont("times_new_roman", Font.BOLD, 30));
						for(int i = 0; i < data.length; i++)
						{
							float px = (float)(gw * formatContainer.translateX(data.x[i]));
							float py = (float)(gh * (1 - formatContainer.translateY(data.y[i])));
							g2.drawString("^", px - 8 + 0, py + 15);
						}
					}
				}
			}
		}
		
		labelTag.draw();
		
		{
			//draw border
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
			g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
			g2.setColor(new Color(0x333333));
			g2.drawLine(0, 0, 0, gh - 1);
			g2.drawLine(0, gh - 1, gw - 1, gh - 1);
			g2.drawLine(gw - 1, gh - 1, gw - 1, 0);
			g2.drawLine(gw - 1, 0, 0, 0);
		}
	}
	
	class LabelTag
	{
		ArrayList<Info> infoArray;
		Font font;
		FontMetrics metrics;
		
		final int FONT_SIZE = 20, BORDER_WIDTH_H = 20, BORDER_WIDTH_V = 10, LINE_LENTH = 40, MARGIN_WIDTH = 20;
		int labelWidth, bx, by, bw, bh;
		int labelAscend;
		int labelDescend;
		int labelHeight;
		
		public LabelTag()
		{
			infoArray = new ArrayList<Info>();
			font = FontLoader.getFont("notosans", Font.PLAIN, FONT_SIZE);
		}
		
		public void draw()
		{
			collectInfo();
			calcBounds();
			
			if(infoArray.size() == 0)
			{
				return;
			}
			
			Graphics2D g2 = Graph.this.image.createGraphics();
			{
				//clear
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
				g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
				g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
				
				g2.setColor(new Color(0xAAFFFFFF, true));
				g2.fillRect(bx, by, bw, bh);
			}
			{
				//draw label
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
				g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
				
				{
					g2.setFont(FontLoader.getFont("times_new_roman", Font.PLAIN, 30));
					for(int i = 0; i < infoArray.size(); i++)
					{
						Info info = infoArray.get(i);
						
						double relX = bx + BORDER_WIDTH_H + 0.5 * LINE_LENTH;
						double relY = by + BORDER_WIDTH_V + 0.65 * labelAscend + i * (labelHeight + BORDER_WIDTH_V);
						
						g2.setColor(new Color(info.color));
						
						if(info.lineStyle == "-")
						{
							Line2D.Double line = new Line2D.Double(relX - 0.5 * LINE_LENTH, relY, relX + 0.5 * LINE_LENTH, relY);
							g2.draw(line);
						}
						else if(info.lineStyle == "--")
						{
							Stroke dashed = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {9}, 0);
							Line2D.Double line = new Line2D.Double(relX - 0.5 * LINE_LENTH, relY, relX + 0.5 * LINE_LENTH, relY);
							g2.draw(dashed.createStrokedShape(line));
						}
						else if(info.lineStyle == ".")
						{
							Ellipse2D.Double shape = new Ellipse2D.Double(relX - 2, relY - 2, 5, 5);
							g2.fill(shape);
						}
						else if(info.lineStyle == "*")
						{
							float px = (float)relX;
							float py = (float)relY;
							g2.drawString("*", px - 8 + 1, py + 15);
						}
						else if(info.lineStyle == "^")
						{
							float px = (float)relX;
							float py = (float)relY;
							g2.drawString("^", px - 8 + 0, py + 15);
						}
					}
				}
				{
					g2.setColor(new Color(0x000000));
					g2.setFont(font);
					for(int i = 0; i < infoArray.size(); i++)
					{
						Info info = infoArray.get(i);
						
						double relX = bx + 2 * BORDER_WIDTH_H + LINE_LENTH;
						double relY = by + BORDER_WIDTH_V + 1.0 * labelAscend + i * (labelHeight + BORDER_WIDTH_V);
						
						g2.drawString(info.label, (int)relX, (int)relY);
					}
				}
			}
			{
				//draw border
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
				g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
				g2.setStroke(new BasicStroke(1f));
				
				g2.setColor(new Color(0x333333, false));
				g2.drawLine(bx, by, bx, by + bh - 1);
				g2.drawLine(bx, by + bh - 1, bx + bw - 1, by + bh - 1);
				g2.drawLine(bx + bw - 1, by + bh - 1, bx + bw - 1, by);
				g2.drawLine(bx + bw - 1, by, bx, by);
				
			}
		}
		
		void collectInfo()
		{
			labelWidth = 0;
			infoArray.clear();
			metrics = Graph.this.image.createGraphics().getFontMetrics(font);
			labelAscend = metrics.getAscent();
			labelDescend = metrics.getDescent();
			labelHeight = metrics.getHeight();
			for(DataStruct data : Graph.this.dataContainer.dataArray)
			{
				if(data.label != null)
				{
					Info info = new Info(data.color, data.lineStyle, data.label);
					infoArray.add(info);
					
					int width = metrics.stringWidth(data.label);
					if(labelWidth < width)
					{
						labelWidth = width;
					}
				}
			}
		}
		
		void calcBounds()
		{
			int infoCount = infoArray.size();
			bh = infoCount * (labelHeight + BORDER_WIDTH_V) + BORDER_WIDTH_V;
			bw = labelWidth + LINE_LENTH + 3 * BORDER_WIDTH_H;
			bx = MARGIN_WIDTH;
			by = MARGIN_WIDTH;
		}
		
		class Info
		{
			public final int color;
			public final String lineStyle;
			public final String label;
			
			public Info(int _color, String _lineStyle, String _label)
			{
				color = _color;
				lineStyle = _lineStyle;
				label = _label;
			}
		}
	}
}