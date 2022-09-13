package sym.symplotlib.chart2D;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.regex.*;

public class DataContainer
{
	public ArrayList<DataStruct> dataArray;
	public String title;
	public String xLabel;
	public String yLabel;
	
	public DataContainer()
	{
		title = "TITLE";
		xLabel = "X_LABEL";
		yLabel = "Y_LABEL";
		dataArray = new ArrayList<DataStruct>();
	}
	
	public void appendData(Object arrayX, Object arrayY, String format)
	{
		DataStruct data = DataContainerTool.constructData(arrayX, arrayY, format);
		synchronized(dataArray)
		{
			dataArray.add(data);
		}
	}
	
	public void removeData(int index)
	{
		synchronized(dataArray)
		{
			dataArray.remove(index);
		}
	}
	
	public void clear()
	{
		synchronized(dataArray)
		{
			dataArray.clear();
		}
		DataContainerTool.clear();
	}
}

class DataContainerTool
{
	static Map<String, Integer> colorMap;
	static ArrayList<Integer> defaultColorList;
	static int defaultColorCount;
	static Map<String, String> lineStyleMap;
	
	static
	{
		defaultColorCount = 0;
		
		colorMap = new HashMap<>();
		colorMap.put("r", 0xFF0000);
		colorMap.put("g", 0x00FF00);
		colorMap.put("b", 0x0000FF);
		colorMap.put("c", 0x00FFFF);
		colorMap.put("m", 0xFF00FF);
		colorMap.put("y", 0xFFFF00);
		colorMap.put("k", 0x000000);
		colorMap.put("w", 0xFFFFFF);
		
		defaultColorList = new ArrayList<>();
		defaultColorList.add(0x1F77B4);
		defaultColorList.add(0xFF7F0E);
		defaultColorList.add(0x2CA02C);
		defaultColorList.add(0xD62728);
		defaultColorList.add(0x9467BD);
		defaultColorList.add(0x8C564B);
		defaultColorList.add(0xE377C2);
		defaultColorList.add(0x7F7F7F);
		defaultColorList.add(0xBCBD22);
		defaultColorList.add(0x17BECF);
		
		lineStyleMap = new HashMap<>();
		lineStyleMap.put("-", "-");
		lineStyleMap.put("--", "--");
		lineStyleMap.put(".", ".");
		lineStyleMap.put("*", "*");
		lineStyleMap.put("^", "^");
	}
	
	public static DataStruct constructData(Object arrayX, Object arrayY, String format)
	{
		DataStruct data = new DataStruct();
		
		double[] x = translateArray(arrayX);
		double[] y = translateArray(arrayY);
		if(x.length != y.length)
		{
			throw new RuntimeException(String.format("data lenth mismatch: x = %d, y = %d", x.length, y.length));
		}
		else
		{
			data.length = x.length;
			data.x = x;
			data.y = y;
			processFormat(data, format);
			return data;
		}
	}
	
	public static void clear()
	{
		defaultColorCount = 0;
	}
	
	static double[] translateArray(Object _array)
	{
		double[] ans;
		if(_array instanceof double[])
		{
			double[] array = (double[])_array;
			ans = new double[array.length];
			for(int i = 0; i < array.length; i++)
			{
				ans[i] = (double)array[i];
			}
		}
		else if(_array instanceof float[])
		{
			float[] array = (float[])_array;
			ans = new double[array.length];
			for(int i = 0; i < array.length; i++)
			{
				ans[i] = (double)array[i];
			}
		}
		else if(_array instanceof long[])
		{
			long[] array = (long[])_array;
			ans = new double[array.length];
			for(int i = 0; i < array.length; i++)
			{
				ans[i] = (double)array[i];
			}
		}
		else if(_array instanceof int[])
		{
			int[] array = (int[])_array;
			ans = new double[array.length];
			for(int i = 0; i < array.length; i++)
			{
				ans[i] = (double)array[i];
			}
		}
		else
		{
			throw new RuntimeException("array type not recognized: " + _array.toString());
		}
		return ans;
	}
	
	static String match(String format, String key)
	{
		Pattern p = Pattern.compile(key + "=([^;]*);");
		Matcher m = p.matcher(format);
		if(m.find() == true)
		{
			String value = m.group(1);
			return value;
		}
		else
		{
			return null;
		}
	}
	
	static void processFormat(DataStruct data, String format)
	{
		// String[] args = format.split(";", 0);
		// for(String arg : args)
		// {
		// 	if(arg.equals(""))
		// 	{
		// 		continue;
		// 	}
		// 	else if(arg.startsWith("color="))
		// 	{
		// 		processColor(data, arg.substring(6));
		// 	}
		// 	else if(arg.startsWith("style="))
		// 	{
		// 		processLineStyle(data, arg.substring(6));
		// 	}
		// 	else if(arg.startsWith("label="))
		// 	{
		// 		processLabel(data, arg.substring(6));
		// 	}
		// 	else
		// 	{
		// 		throw new RuntimeException(String.format("argument \"%s\" is unknown!", arg));
		// 	}
		// }
		processColor(data, match(format, "color"));
		processLineStyle(data, match(format, "style"));
		processLabel(data, match(format, "label"));
	}
	
	static boolean isInteger(String strNum)
	{
		try
		{
			Integer d = Integer.decode(strNum);
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}
	
	static void processColor(DataStruct data, String arg)
	{
		// System.out.println(arg);
		if(arg == null)
		{
			data.color = defaultColorList.get(defaultColorCount % defaultColorList.size());
			defaultColorCount++;
		}
		else if(isInteger(arg))
		{
			data.color = Integer.decode(arg);
		}
		else if(colorMap.get(arg) != null)
		{
			data.color = colorMap.get(arg);
		}
		else
		{
			throw new RuntimeException(String.format("color \"%s\" is unknown!", arg));
		}
	}
	
	static void processLineStyle(DataStruct data, String arg)
	{
		if(arg == null)
		{
			data.lineStyle = "-";
		}
		else if(lineStyleMap.get(arg) != null)
		{
			data.lineStyle = lineStyleMap.get(arg);
		}
		else
		{
			throw new RuntimeException(String.format("line style \"%s\" is unknown!", arg));
		}
	}
	
	static void processLabel(DataStruct data, String arg)
	{
		data.label = arg;
	}
}