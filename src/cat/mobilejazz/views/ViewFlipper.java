package cat.mobilejazz.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

/** This is a workaround for Android project issue 6191 **/
public class ViewFlipper extends android.widget.ViewFlipper {

	public ViewFlipper(Context context) {
		super(context);
	}
	
	public ViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDetachedFromWindow() {
		int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel >= Build.VERSION_CODES.ECLAIR_MR1 && apiLevel < Build.VERSION_CODES.HONEYCOMB) {
			try {
				super.onDetachedFromWindow();
			} catch (IllegalArgumentException e) {
				//Debug.warning("Android project  issue 6191  workaround.");
				/* Quick catch and continue on api level 7, the Eclair 2.1 */
			} finally {
				super.stopFlipping();
			}
		} else {
			super.onDetachedFromWindow();
		}
	}

}
