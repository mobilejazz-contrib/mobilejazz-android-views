package cat.mobilejazz.views.ttf;

import android.content.Context;
import android.util.AttributeSet;

public class CheckBox extends android.widget.CheckBox {

	public CheckBox(Context context) {
		super(context);
	}

	public CheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		TTFViewHelper.initialize(this, context, attrs);
	}

	public CheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TTFViewHelper.initialize(this, context, attrs);
	}
}
