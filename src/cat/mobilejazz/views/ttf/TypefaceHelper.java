package cat.mobilejazz.views.ttf;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceHelper {

	private static final HashMap<String, Typeface> typefaces = new HashMap<String, Typeface>();

	public static Typeface getTypeface(Context context, String name) {
		context = context.getApplicationContext();
		if (typefaces.containsKey(name)) {
			return typefaces.get(name);
		}

		Typeface typeface = Typeface.createFromAsset(context.getAssets(), name);

		if (typeface != null) {
			typefaces.put(name, typeface);
		}

		return typeface;
	}
}