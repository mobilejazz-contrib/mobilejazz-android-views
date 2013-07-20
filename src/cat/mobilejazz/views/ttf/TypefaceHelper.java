package cat.mobilejazz.views.ttf;

import java.util.HashMap;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;

public class TypefaceHelper {

	private static final HashMap<String, Typeface> typefaces = new HashMap<String, Typeface>();

	public static Typeface getTypeface(Context context, String name) {
		return getTypeface(context.getApplicationContext().getAssets(), name);
	}

	public static Typeface getTypeface(Resources resources, String name) {
		return getTypeface(resources.getAssets(), name);
	}

	public static Typeface getTypeface(AssetManager assets, String name) {
		if (typefaces.containsKey(name)) {
			return typefaces.get(name);
		}

		Typeface typeface = Typeface.createFromAsset(assets, name);

		if (typeface != null) {
			typefaces.put(name, typeface);
		}

		return typeface;
	}
}