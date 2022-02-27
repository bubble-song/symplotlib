package test;

class Test2
{
	public static void main(String[] args)throws Exception
	{
		double x1 = Double.MAX_VALUE, x2 = Double.MIN_VALUE;
		
		System.out.println(Double.doubleToLongBits(x1));
		System.out.println(Double.doubleToLongBits(x2));
		double p = 1 - 1;
		
		
		x1 = p;
		x2 = p;
		
		System.out.println(x1 == x2);
	}
}