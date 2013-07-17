package cat.mobilejazz.views.ttf;

import android.content.Context;
import android.util.AttributeSet;

public class TextView extends android.widget.TextView {

	public TextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TTFViewHelper.initialize(this, context, attrs);
	}
}