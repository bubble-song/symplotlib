package sym.symplotlib;
//interface of all plotting methods

import sym.symplotlib.chart2D.GUIChart2D;

public class Plot
{
	GUIChart2D chart;
	
	public Plot()
	{
		chart = new GUIChart2D();
	}
	
	public void plot(Object x, Object y, String format)
	{
		chart.plot(x, y, format);
	}
	
	public void show()
	{
		chart.show();
	}
	
	public void hide()
	{
		chart.hide();
	}
	
	public void setXLabel(String label)
	{
		chart.setXLabel(label);
	}
	
	public void setYLabel(String label)
	{
		chart.setYLabel(label);
	}
	
	public void setTitle(String title)
	{
		chart.setTitle(title);
	}
	
	public void clear()
	{
		chart.clear();
	}
	
	public void setRangeX(double x1, double x2)
	{
		chart.setRangeX(x1, x2);
	}
	
	public void setRangeY(double y1, double y2)
	{
		chart.setRangeY(y1, y2);
	}
	
	public void autoRange()
	{
		chart.autoRange();
	}
}
