package sym.symplotlib.font;

import java.awt.Font;
import java.io.File;
import java.util.Map;
import java.util.HashMap;

public class FontLoader
{
	static Map<String, Font> fontMap;
	static
	{
		fontMap = new HashMap<>();
		try
		{
			ClassLoader loader = FontLoader.class.getClassLoader();
			
			fontMap.put("times_new_roman", Font.createFont(Font.TRUETYPE_FONT, loader.getResourceAsStream("font/Times_New_Roman.ttf")));
			fontMap.put("arial", Font.createFont(Font.TRUETYPE_FONT, loader.getResourceAsStream("font/Arial.ttf")));
			fontMap.put("notosans", Font.createFont(Font.TRUETYPE_FONT, loader.getResourceAsStream("font/NotoSansSC-Regular.otf")));
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		
	}
	
	public static Font getFont(String fontName, int style, float size)
	{
		return fontMap.get(fontName).deriveFont(style, size);
	}
	
	public static Font getDefaultFont(String fontName, int style, int size)
	{
		return new Font(fontName, style, size);
	}
}