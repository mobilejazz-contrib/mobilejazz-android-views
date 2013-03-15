package cat.mobilejazz.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class GridLayout extends ViewGroup {

	private int mColumnCount;
	private int mRowGap;
	private int mColumnGap;

	public GridLayout(Context context) {
		this(context, null);
	}

	public GridLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GridLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GridLayout);
		try {
			mColumnCount = a.getInt(R.styleable.GridLayout_columnCount, 1);
			mRowGap = a.getDimensionPixelSize(R.styleable.GridLayout_rowGap, 0);
			mColumnGap = a.getDimensionPixelSize(R.styleable.GridLayout_columnGap, 0);
		} finally {
			a.recycle();
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int width = r - l;

		int currentTop = 0;
		int currentLeft = 0;
		int maxChildHeight = 0;

		for (int i = 0; i < getChildCount(); ++i) {
			if (i % mColumnCount == 0) {
				currentLeft = 0;
			}

			View c = getChildAt(i);
			int ch = c.getMeasuredHeight();
			c.layout(currentLeft, currentTop, currentLeft + c.getMeasuredWidth(), currentTop + ch);

			if (ch > maxChildHeight) {
				maxChildHeight = ch;
			}

			if (i % mColumnCount == mColumnCount - 1) {
				currentTop += maxChildHeight + mRowGap;
				maxChildHeight = 0;
			}

			currentLeft += c.getMeasuredWidth() + mColumnGap;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);

		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
			for (int i = 0; i < getChildCount(); ++i) {
				View c = getChildAt(i);
				c.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				measuredWidth += c.getMeasuredWidth();
			}
		} else {
			int idealSize = (measuredWidth - mColumnGap) / mColumnCount - mColumnGap;

			for (int i = 0; i < getChildCount(); ++i) {
				View c = getChildAt(i);
				c.measure(MeasureSpec.makeMeasureSpec(idealSize, MeasureSpec.EXACTLY),
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			}
		}

		if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
			int maxHeight = 0;
			for (int i = 0; i < getChildCount(); ++i) {
				View c = getChildAt(i);

				int ch = c.getMeasuredHeight();
				if (ch > maxHeight) {
					maxHeight = ch;
				}

				if ((i % mColumnCount) == mColumnCount - 1) {
					measuredHeight += maxHeight;
					if (i < getChildCount() - 1) {
						// more to come:
						measuredHeight += mRowGap;
					}
					maxHeight = 0;
				}
			}
			measuredHeight += maxHeight;
		}

		setMeasuredDimension(measuredWidth, measuredHeight);
	}
}
