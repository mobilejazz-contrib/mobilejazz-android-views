package cat.mobilejazz.views.span;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;
import cat.mobilejazz.views.ttf.TypefaceHelper;

/**
 * A span object that allows to set a custom font from an asset file.
 */
public class CustomTypefaceSpan extends TypefaceSpan {

	private final Typeface mTypeface;

	public CustomTypefaceSpan(Context context, String fontFileName) {
		// just a placeholder. family is not used:
		super("serif");
		mTypeface = TypefaceHelper.getTypeface(context, fontFileName);
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		apply(ds, mTypeface);
	}

	@Override
	public void updateMeasureState(TextPaint paint) {
		apply(paint, mTypeface);
	}

	private static void apply(Paint paint, Typeface tf) {
		int oldStyle;

		Typeface old = paint.getTypeface();
		if (old == null) {
			oldStyle = 0;
		} else {
			oldStyle = old.getStyle();
		}

		int fake = oldStyle & ~tf.getStyle();

		if ((fake & Typeface.BOLD) != 0) {
			paint.setFakeBoldText(true);
		}

		if ((fake & Typeface.ITALIC) != 0) {
			paint.setTextSkewX(-0.25f);
		}

		paint.setTypeface(tf);
	}

}