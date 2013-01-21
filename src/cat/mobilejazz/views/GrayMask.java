package cat.mobilejazz.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class GrayMask extends FrameLayout {
	
	private View mMask;
	private View mContent;

	public GrayMask(Context context) {
		super(context);
	}

	public GrayMask(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GrayMask(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mContent = getChildAt(0);

		mMask = new View(getContext());
		PorterDuffColorFilter filter = new PorterDuffColorFilter(0x77ffffff, PorterDuff.Mode.MULTIPLY);
		mMask.setBackgroundColor(0x88ffffff);
		mMask.getBackground().setColorFilter(filter);
		mMask.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		addView(mMask);}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(), mContent.getMeasuredHeight());
	}

}
