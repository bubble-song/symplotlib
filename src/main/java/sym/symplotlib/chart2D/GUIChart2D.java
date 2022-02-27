package sym.symplotlib.chart2D;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.ArrayList;

public class GUIChart2D
{
	DataContainer dataContainer;
	Chart2D chart;
	FormatContainer formatContainer;
	
	public GUIChart2D()
	{
		dataContainer = new DataContainer();
		formatContainer = new FormatContainer();
	}
	
	public void plot(Object x, Object y, String format)
	{
		dataContainer.appendData(x, y, format);
	}
	
	public void show()
	{
		if(chart == null || chart.isDisplayable() == false)
		{
			chart = new Chart2D(dataContainer, formatContainer);
		}
		chart.myPaint();
	}
	
	public void hide()
	{
		chart.dispose();
	}
	
	public void setXLabel(String label)
	{
		formatContainer.xLabel = label;
	}
	
	public void setYLabel(String label)
	{
		formatContainer.yLabel = label;
	}
	
	public void setTitle(String title)
	{
		formatContainer.title = title;
	}
	
	public void clear()
	{
		dataContainer.clear();
	}
	
	public void setRangeX(double x1, double x2)
	{
		formatContainer.x1 = x1;
		formatContainer.x2 = x2;
	}
	
	public void setRangeY(double y1, double y2)
	{
		formatContainer.y1 = y1;
		formatContainer.y2 = y2;
	}
	
	public void autoRange()
	{
		double xMin = +Double.MAX_VALUE, xMax = -Double.MAX_VALUE, yMin = +Double.MAX_VALUE, yMax = -Double.MAX_VALUE;
		
		for(DataStruct data : dataContainer.dataArray)
		{
			for(int i = 0; i < data.length; i++)
			{
			
				double x = data.x[i];
				double y = data.y[i];
				if(Double.isInfinite(x) || Double.isInfinite(y) || Double.isNaN(x) || Double.isNaN(y))
				{
					continue;
				}
				if(xMin > x)
				{
					xMin = x;
				}
				if(xMax < x)
				{
					xMax = x;
				}
				if(yMin > y)
				{
					yMin = y;
				}
				if(yMax < y)
				{
					yMax = y;
				}
			}
		}
		
		if(xMin > xMax || yMin > yMax)
		{
			xMin = -1;
			xMax = +1;
			yMin = -1;
			yMax = +1;
			new Exception("auto set axis failed!").printStackTrace();
		}
		else
		{
			if(xMin - xMax == 0)
			{
				xMin -= 1;
				xMax += 1;
			}
			if(yMin - yMax == 0)
			{
				yMin -= 1;
				yMax += 1;
			}
		}
		formatContainer.x1 = xMin - 0.1 * (xMax - xMin);
		formatContainer.x2 = xMax + 0.1 * (xMax - xMin);
		formatContainer.y1 = yMin - 0.1 * (yMax - yMin);
		formatContainer.y2 = yMax + 0.1 * (yMax - yMin);
	}
}
