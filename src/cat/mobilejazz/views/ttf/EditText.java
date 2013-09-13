package cat.mobilejazz.views.ttf;

import android.content.Context;
import android.util.AttributeSet;

public class EditText extends android.widget.EditText {

	public EditText(Context context) {
		super(context);
	}

	public EditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode()) {
			TTFViewHelper.initialize(this, context, attrs);
		}
	}

	public EditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (!isInEditMode()) {
			TTFViewHelper.initialize(this, context, attrs);
		}
	}
}