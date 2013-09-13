package cat.mobilejazz.views.ttf;

import android.content.Context;
import android.util.AttributeSet;

public class Button extends android.widget.Button {

	public Button(Context context) {
		super(context);
	}

	public Button(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode()) {
			TTFViewHelper.initialize(this, context, attrs);
		}
	}

	public Button(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (!isInEditMode()) {
			TTFViewHelper.initialize(this, context, attrs);
		}
	}
}