package cat.mobilejazz.views.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.text.style.ReplacementSpan;

/**
 * Span to insert an arbitrary amount of horizontal padding.
 * 
 * @author Hannes Widmoser
 * 
 */
public class PaddingSpan extends ReplacementSpan {

	private int mPadding;

	/**
	 * Creates a new instance of {@link PaddingSpan}.
	 * 
	 * @param paddingDp
	 *            the size of the padding in pixels.
	 */
	public PaddingSpan(int padding) {
		mPadding = padding;
	}

	@Override
	public int getSize(Paint paint, CharSequence text, int start, int end, FontMetricsInt fm) {
		return mPadding;
	}

	@Override
	public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
			Paint paint) {
		// nothing to draw
	}

}
