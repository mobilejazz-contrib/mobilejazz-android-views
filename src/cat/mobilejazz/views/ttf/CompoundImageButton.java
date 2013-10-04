package cat.mobilejazz.views.ttf;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import cat.mobilejazz.views.R;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class CompoundImageButton extends CompoundButton {

	private int mButtonDrawablePadding;
	private Drawable mButtonDrawable;

	public CompoundImageButton(Context context) {
		this(context, null, 0);
	}

	public CompoundImageButton(Context context, AttributeSet attrs) {
		this(context, attrs, R.style.CompoundButton);
		
		if (!isInEditMode()) {
			TTFViewHelper.initialize(this, context, attrs);
		}
	}

	public CompoundImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CompoundImageButton, defStyle, 0);

		mButtonDrawablePadding = a.getDimensionPixelOffset(R.styleable.CompoundImageButton_buttonPadding, 0);

		a.recycle();
		
		if (!isInEditMode()) {
			TTFViewHelper.initialize(this, context, attrs);
		}
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}

	@Override
	public void setButtonDrawable(Drawable d) {
		if (d != null) {
			if (mButtonDrawable != null) {
				mButtonDrawable.setCallback(null);
				unscheduleDrawable(mButtonDrawable);
			}
			d.setCallback(this);
			d.setState(getDrawableState());
			d.setVisible(getVisibility() == VISIBLE, false);
			mButtonDrawable = d;
			mButtonDrawable.setState(null);
			setMinHeight(mButtonDrawable.getIntrinsicHeight());
		}

		refreshDrawableState();
	}

	@Override
	public int getCompoundPaddingLeft() {
		int padding = super.getCompoundPaddingLeft();
		if (drawableIsLeftOfText()) {
			final Drawable buttonDrawable = mButtonDrawable;
			if (buttonDrawable != null) {
				padding += mButtonDrawablePadding + buttonDrawable.getIntrinsicWidth();
			}
		}
		return padding;
	}

	@Override
	public int getCompoundPaddingRight() {
		int padding = super.getCompoundPaddingRight();
		if (!drawableIsLeftOfText()) {
			final Drawable buttonDrawable = mButtonDrawable;
			if (buttonDrawable != null) {
				padding += mButtonDrawablePadding + buttonDrawable.getIntrinsicWidth();
			}
		}
		return padding;
	}

	protected boolean drawableIsLeftOfText() {
		final int horizontalGravity = getGravity() & Gravity.HORIZONTAL_GRAVITY_MASK;
		switch (horizontalGravity) {
		case Gravity.END:
		case Gravity.RIGHT:
			return false;
		default:
			return true;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		final Drawable buttonDrawable = mButtonDrawable;
		if (buttonDrawable != null) {
			final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
			final int horizontalGravity = getGravity() & Gravity.HORIZONTAL_GRAVITY_MASK;
			final int drawableHeight = buttonDrawable.getIntrinsicHeight();
			final int drawableWidth = buttonDrawable.getIntrinsicWidth();

			final boolean drawableIsLeftOfText = drawableIsLeftOfText();
			final boolean centered = horizontalGravity == Gravity.CENTER_HORIZONTAL
					|| horizontalGravity == Gravity.CENTER;

			int top = 0;
			switch (verticalGravity) {
			case Gravity.BOTTOM:
				top = getHeight() - drawableHeight;
				break;
			case Gravity.CENTER_VERTICAL:
				top = (getHeight() - drawableHeight) / 2;
				break;
			}
			int bottom = top + drawableHeight;

			int left, right;

			if (centered) {
				float halfTextWidth = getPaint().measureText(getText().toString()) * 0.5f;
				float center = (getWidth() + getCompoundPaddingLeft()) * 0.5f;
				if (drawableIsLeftOfText) {
					int textLeft = (int) (center - halfTextWidth) - mButtonDrawablePadding;
					left = textLeft - drawableWidth;
					right = textLeft;
				} else {
					int textRight = (int) (center + halfTextWidth) + mButtonDrawablePadding;
					left = textRight;
					right = textRight + drawableWidth;
				}
			} else {
				left = drawableIsLeftOfText ? 0 : getWidth() - drawableWidth;
				right = drawableIsLeftOfText ? drawableWidth : getWidth();
			}

			buttonDrawable.setBounds(left, top, right, bottom);
			buttonDrawable.draw(canvas);
		}
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();

		if (mButtonDrawable != null) {
			int[] myDrawableState = getDrawableState();

			// Set the state of the Drawable
			mButtonDrawable.setState(myDrawableState);

			invalidate();
		}
	}

	@Override
	protected boolean verifyDrawable(Drawable who) {
		return super.verifyDrawable(who) || who == mButtonDrawable;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void jumpDrawablesToCurrentState() {
		super.jumpDrawablesToCurrentState();
		if (mButtonDrawable != null)
			mButtonDrawable.jumpToCurrentState();
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
		super.onInitializeAccessibilityEvent(event);
		event.setClassName(CompoundImageButton.class.getName());
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
		super.onInitializeAccessibilityNodeInfo(info);
		info.setClassName(CompoundImageButton.class.getName());
	}
}
