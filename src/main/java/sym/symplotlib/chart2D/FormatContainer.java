package sym.symplotlib.chart2D;

import java.util.ArrayList;

class FormatContainer
{
	public String title;
	public String xLabel;
	public String yLabel;
	public double x1, x2, y1, y2;
	public int cw, ch;
	public int gx, gy, gw, gh;
	
	double spacingX = 70, spacingY = 50;
	
	public ArrayList<Double> marksDoubleX;
	public ArrayList<Double> marksDoubleY;
	public ArrayList<String> marksStringX;
	public ArrayList<String> marksStringY;
	public String markX;
	public String markY;
	
	public FormatContainer()
	{
		title = "TITLE";
		xLabel = "X_LABEL";
		yLabel = "Y_LABEL";
		x1 = -1;
		x2 = 1;
		y1 = -1;
		y2 = 1;
		
		marksDoubleX = new ArrayList<Double>();
		marksDoubleY = new ArrayList<Double>();
		marksStringX = new ArrayList<String>();
		marksStringY = new ArrayList<String>();
		
		
		updateSize(800, 600);
	}
	
	public void updateSize(int _cw, int _ch)
	{
		cw = _cw;
		ch = _ch;
		gx = (int)(cw * 0.15);
		gy = (int)(ch * 0.12);
		gw = (int)(cw * 0.80);
		gh = (int)(ch * 0.75);
	}
	
	public double translateX(double x)
	{
		return (x - x1) / (x2 - x1);
	}
	public double translateY(double y)
	{
		return (y - y1) / (y2 - y1);
	}
	
	public double translateXR(double ratio)
	{
		return x2 * ratio + x1 * (1 - ratio);
	}
	public double translateYR(double ratio)
	{
		return y2 * ratio + y1 * (1 - ratio);
	}
	
	public void move(double ratioX, double ratioY)
	{
		{
			double oldX1 = x1;
			double oldX2 = x2;
			x1 = oldX1 + (oldX2 - oldX1) * ratioX;
			x2 = oldX2 + (oldX2 - oldX1) * ratioX;
		}
		{
			double oldY1 = y1;
			double oldY2 = y2;
			y1 = oldY1 + (oldY2 - oldY1) * ratioY;
			y2 = oldY2 + (oldY2 - oldY1) * ratioY;
		}
	}
	
	public void zoom(double zoomX, double zoomY, double centerRatioX, double centerRatioY)
	{
		{
			double zoomR = 1 / Math.exp(zoomX);
			double oldX1 = x1;
			double oldX2 = x2;
			x1 = oldX1 + centerRatioX * (1 - zoomR) * (oldX2 - oldX1);
			x2 = oldX2 - (1 - centerRatioX) * (1 - zoomR) * (oldX2 - oldX1);
		}
		{
			double zoomR = 1 / Math.exp(zoomY);
			double oldY1 = y1;
			double oldY2 = y2;
			y1 = oldY1 + centerRatioY * (1 - zoomR) * (oldY2 - oldY1);
			y2 = oldY2 - (1 - centerRatioY) * (1 - zoomR) * (oldY2 - oldY1);
		}
	}
	
	public void generateMarks()
	{
		markX = generateMarks(x1, x2, gw / spacingX, marksDoubleX, marksStringX);
		markY = generateMarks(y1, y2, gh / spacingY, marksDoubleY, marksStringY);
		// marksDoubleY = getMarksDouble(y1, y2, gh / spacingY);
		// marksStringX = prettifyMarks(marksDoubleX);
		// marksStringY = prettifyMarks(marksDoubleY);
	}
	
	// public ArrayList<Double> getMarksDoubleX()
	// {
	// 	return marksDoubleX;
	// }
	// public ArrayList<Double> getMarksDoubleY()
	// {
	// 	return marksDoubleY;
	// }
	// public ArrayList<String> getMarksStringX()
	// {
	// 	return marksStringX;
	// }
	// public ArrayList<String> getMarksStringY()
	// {
	// 	return marksStringY;
	// }
	
	static String generateMarks(double start, double end, double markCount, ArrayList<Double> marksDouble, ArrayList<String> marksString)
	{
		marksDouble.clear();
		marksString.clear();
		
		double range = end - start;
		double delta = range / markCount;
		if(delta <= 0)
		{
			throw new RuntimeException(String.format("range (5f, %f) end <= start, unable to generate marks!", start, end));
		}
		else
		{
			double exponent = Math.floor(Math.log10(delta));
			double base = Math.pow(10, exponent);
			int digit = 0;
			if(1.0 * base >= 0.999 * delta)
			{
				delta = 1.0 * base;
				digit = 0;
			}
			else if(2.5 * base >= 0.999 * delta)
			{
				delta = 2.5 * base;
				digit = 1;
			}
			else if(5.0 * base >= 0.999 * delta)
			{
				delta = 5.0 * base;
				digit = 0;
			}
			else if(10.0 * base >= 0.999 * delta)
			{
				exponent += 1;
				digit = 0;
				delta = 10.0 * base;
			}
			double root = Math.floor(start / delta) * delta;
			while(root <= end + range * 1e-5)
			{
				if(root >= start - range * 1e-5)
				{
					marksDouble.add(Double.valueOf(root));
				}
				root += delta;
			}
			
			double max = Math.max(Math.abs(marksDouble.get(0)), Math.abs(marksDouble.get(marksDouble.size() - 1)));
			double exponent1 = Math.floor(Math.log10(max) + 1e-6);
			
			if(max == 0)
			{
				exponent1 = 0;
				digit = 0;
				
				for(int i = 0; i < marksDouble.size(); i++)
				{
					String mark = String.format("%." + digit + "f", marksDouble.get(i));
					marksString.add(mark);
				}
				return null;
			}
			else if(exponent1 > -5 && exponent1 < 5)
			{
				//normal representation
				digit -= (int)exponent;
				if(digit < 0)
				{
					digit = 0;
				}
				else if(digit > 5)
				{
					digit = 5;
				}
				for(int i = 0; i < marksDouble.size(); i++)
				{
					String mark = String.format("%." + digit + "f", marksDouble.get(i));
					marksString.add(mark);
				}
				return null;
			}
			else
			{
				//scientific notation representation
				
				// digit -= (int)exponent;
				// exponent = Math.floor(Math.log10(max) + 1e-6);
				// digit += (int)exponent;
				digit += (int)(exponent1 - exponent);
				
				if(digit < 0)
				{
					digit = 0;
				}
				else if(digit > 5)
				{
					digit = 5;
				}
				
				for(int i = 0; i < marksDouble.size(); i++)
				{
					String mark = String.format("%." + digit + "f", marksDouble.get(i) / Math.pow(10, exponent1));
					marksString.add(mark);
				}
			}
			String exp = String.format("e%d", (int)exponent1);
			return exp;
		}
	}
}