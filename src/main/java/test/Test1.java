package test;

import sym.symplotlib.Plot;

class Test1
{
	public static void main(String[] args)throws Exception
	{
		int length = 100000;
		
		Plot plt = new Plot();
		
		double[] x = new double[length];
		double[] y1 = new double[length];
		double[] y2 = new double[length];
		
		
		for(int i = 0; i < length; i++)
		{
			double _x = 1d / length * (i - length / 2);
			double _y1 = 2 * _x * _x;
			double _y2 = 2 * _x * _x * _x;
		
			x[i] = _x;
			y1[i] = _y1;
			y2[i] = _y2;
		}
		
		plt.plot(x, y1, "label=\"E(x)\";style=-;color=r;");
		plt.plot(x, y1, "label=abc;style=*;");
		// plt.plot(x, y1, "label=abc;style=-;");
		// plt.plot(x, y1, "style=*;");
		// plt.plot(x, y1, "color=r;label=蛤F(x);style=.");
		// plt.plot(x, y1, "color=r;label=haha;style=--");
		// plt.plot(x, y2, "color=g;label=F(x);style=-");
		// plt.plot(x, y2, "color=b;label=F(x);style=*");
		
		plt.autoRange();
		
		plt.setXLabel("x");
		plt.setYLabel("y");
		plt.setTitle("x-y plot");
		
		plt.show();
		 Thread.sleep(3000);
		// plt.hide();
	}
}