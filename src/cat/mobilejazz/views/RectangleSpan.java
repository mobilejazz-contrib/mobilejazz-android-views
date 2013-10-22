package cat.mobilejazz.views;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.style.LineHeightSpan;
import android.text.style.ReplacementSpan;

public class RectangleSpan extends ReplacementSpan implements LineHeightSpan {

	private static float sProportion = 0;

	private int fillColor;
	private int strokeColor;
	private float strokeWidth;
	private int textColor;
	private float padding;

	private float height;
	private FontMetricsInt fontMetrics;

	private Rect bounds;
	private Rect tag;

	protected RectangleSpan() {
		this.bounds = new Rect();
		this.tag = new Rect();
	}

	protected void init(int textColor, int fillColor, int strokeColor, float strokeWidth, float padding, float height) {
		this.textColor = textColor;
		this.fillColor = fillColor;
		this.strokeColor = strokeColor;
		this.strokeWidth = strokeWidth;
		this.padding = padding;
		this.height = height;
	}

	/**
	 * 
	 * @param padding
	 *            the left and right padding in pixels
	 */
	public RectangleSpan(int textColor, int fillColor, int strokeColor, float strokeWidth, float padding, float height) {
		this();
		init(textColor, fillColor, strokeColor, strokeWidth, padding, height);
	}

	public RectangleSpan(int textColor, int fillColor, int strokeColor, float strokeWidth, float padding) {
		this();
		init(textColor, fillColor, strokeColor, strokeWidth, padding, 0f);
	}

	@Override
	public int getSize(Paint paint, CharSequence text, int start, int end, FontMetricsInt fm) {
		paint.getTextBounds(text.toString(), start, end, bounds);
		return (int) (bounds.width() + 2 * padding);
	}

	@Override
	public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
			Paint paint) {

		canvas.save();

		// paint.setTextSize(paint.getTextSize() * 0.7f);

		String str = text.subSequence(start, end).toString();

		paint.getTextBounds(str, 0, str.length(), bounds);

		FontMetrics fm = paint.getFontMetrics();
		tag.set((int) x, (int) (top), (int) (x + bounds.right + 2 * padding),(int)( bottom ));

		paint.setColor(fillColor);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(tag, paint);

		paint.setColor(strokeColor);
		paint.setStrokeWidth(strokeWidth);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(tag, paint);

		paint.setColor(textColor);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawText(text, start, end, x + padding, y + (fm.top+fm.descent)/4 , paint);

		canvas.restore();

	}

	public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int v, Paint.FontMetricsInt fm) {
		// Should not get called, at least not by StaticLayout.
		chooseHeight(text, start, end, spanstartv, v, fm, new TextPaint());
	}

	public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int v, Paint.FontMetricsInt fm,
			TextPaint paint) {
		int size = (int) height;
        if (paint != null) {
            size *= paint.density;
        }

        if (fm.bottom - fm.top < size) {
            fm.top = fm.bottom - size;
            fm.ascent = fm.ascent - size;
        } else {
            if (sProportion == 0) {
                /*
                 * Calculate what fraction of the nominal ascent
                 * the height of a capital letter actually is,
                 * so that we won't reduce the ascent to less than
                 * that unless we absolutely have to.
                 */

                Paint p = new Paint();
                p.setTextSize(100);
                Rect r = new Rect();
                p.getTextBounds("AbcdEfG", 0, 7, r);
                

                sProportion = (r.top) / p.ascent();
            }

            int need = (int) Math.ceil(-fm.top * sProportion);
            
            if (size - fm.descent >= need) {
                /*
                 * It is safe to shrink the ascent this much.
                 */

                fm.top = fm.bottom - size;
                fm.ascent = fm.descent - size;
            } else if (size >= need) {
                /*
                 * We can't show all the descent, but we can at least
                 * show all the ascent.
                 */

                fm.top = fm.ascent = -need;
                fm.bottom = fm.descent = fm.top + size;
            } else {
                /*
                 * Show as much of the ascent as we can, and no descent.
                 */

                fm.top = fm.ascent = -size;
                fm.bottom = fm.descent = 0;
            }
            
		}
		fontMetrics = fm;
	}

}