package cat.mobilejazz.views.ttf;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import cat.mobilejazz.utilities.debug.Debug;
import cat.mobilejazz.views.R;

class TTFViewHelper {

	public static void initialize(TextView view, Context context, AttributeSet attributeSet) {
		TypedArray attributes = context.obtainStyledAttributes(attributeSet, R.styleable.TtfCapableView,
				R.attr.typeface, 0);

		String typefaceDesc = attributes.getString(R.styleable.TtfCapableView_typeface);
		if (typefaceDesc != null) {
			Typeface typeface = TypefaceHelper.getTypeface(context, typefaceDesc);
			view.setTypeface(typeface);
		} else {
			Debug.warning("Could not find asset: %s", typefaceDesc);
		}

		boolean allCaps = attributes.getBoolean(R.styleable.TtfCapableView_textAllCaps, false);
		if (allCaps) {
			view.setTransformationMethod(new AllCapsTransformationMethod(context));
		}

		attributes.recycle();
	}
}
