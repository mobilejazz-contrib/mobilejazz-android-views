package cat.mobilejazz.views;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

/**
 * 
 * @author dinony
 * 
 */
public class BubbleSpinner extends Spinner implements OnClickListener {

	private static final String _TAG = "BubbleSpinner";

	private BubblePopup _popup;
	private DropDownAdapter _tmp_ddAdapter;

	// center the tip position according to the anchor
	private static final int TIPPOS_CENTER = 0;
	private static final int TIPPOS_LEFT = -1;
	private static final int TIPPOS_RIGHT = 1;

	private int _tipPosition;
	private float _tipBottomLength;
	private float _rectOffset;
	private float _xRadius;
	private float _yRadius;
	private String _customSelectorRes;
	private int _paddingLeftRight;

	private int _popupWidth;

	public BubbleSpinner(Context context) {
		this(context, null);
	}

	public BubbleSpinner(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.spinnerStyle);
	}

	@SuppressWarnings("deprecation")
	public BubbleSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.BubbleSpinner, 0, 0);

		try {
			// get bubble popup parameters (second param - default values)
			_tipPosition = a.getInteger(R.styleable.BubbleSpinner_tipPosition,
					TIPPOS_CENTER);
			_tipBottomLength = a.getFloat(
					R.styleable.BubbleSpinner_tipBottomLength, 10.0f);
			_rectOffset = a.getFloat(R.styleable.BubbleSpinner_rectOffset,
					10.0f);
			_xRadius = a.getFloat(R.styleable.BubbleSpinner_xRadius, 2.0f);
			_yRadius = a.getFloat(R.styleable.BubbleSpinner_yRadius, 2.0f);
			_paddingLeftRight = a.getInteger(
					R.styleable.BubbleSpinner_paddingLeftRight, 0);

			_customSelectorRes = a
					.getString(R.styleable.BubbleSpinner_customSelector);
			if (_customSelectorRes != null) {
				// TODO load drawable res
				// InputStream is = null;
				// Drawable d = null;
				// try {
				// is =
				// context.getResources().getAssets().open(_customSelectorRes);
				// d = Drawable.createFromResourceStream(
				// context.getResources(), null, is,
				// _customSelectorRes);
				// } catch (Throwable e) {
				// // TODO handle
				// }
				// if (d == null) {
				// Log.v(_TAG, ";_;");
				// } else {
				// Log.v(_TAG, "Yeah");
				// }
			} else
				_customSelectorRes = "";

		} finally {
			a.recycle();
		}

		// TODO do not use disp width for layout
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		_popupWidth = disp.getWidth();

		_popup = new BubblePopup(context, attrs, defStyle);

		// base constr could call setAdapter before _popup is initialized
		if (_tmp_ddAdapter != null) {
			_popup.setAdapter(_tmp_ddAdapter);
			_tmp_ddAdapter = null;
		}
	}

	@Override
	public void setPopupBackgroundDrawable(Drawable background) {
		// TODO Bubble spinner has a fixed bg drawable
	}

	@Override
	public void setPopupBackgroundResource(int resId) {
		// TODO Bubble spinner has a fixed bg drawable
	}

	@Override
	public void setDropDownVerticalOffset(int pixels) {
		_popup.setVerticalOffset(pixels);
	}

	@Override
	public void setDropDownHorizontalOffset(int pixels) {
		_popup.setHorizontalOffset(pixels);
	}

	@Override
	public void setDropDownWidth(int pixels) {
		// TODO drop down width should fill parent
	}

	@Override
	public int getDropDownWidth() {
		return _popupWidth;
	}

	@Override
	public void setAdapter(SpinnerAdapter adapter) {
		super.setAdapter(adapter);
		if (_popup != null)
			_popup.setAdapter(new DropDownAdapter(adapter));
		else
			_tmp_ddAdapter = new DropDownAdapter(adapter);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();

		if (_popup != null && _popup.isShowing()) {
			_popup.dismiss();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (_popup != null
				&& MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
			// either fill or max width allowed
			setMeasuredDimension(Math.max(_popupWidth,
					MeasureSpec.getSize(widthMeasureSpec)), getMeasuredHeight());
		}
	}

	@Override
	public boolean performClick() {

		if (!_popup.isShowing()) {
			_popup.show();
		}

		return true;
	}

	/**
	 * <p>
	 * Wrapper class for an Adapter. Transforms the embedded Adapter instance
	 * into a ListAdapter.
	 * </p>
	 */
	private static class DropDownAdapter implements ListAdapter, SpinnerAdapter {
		private SpinnerAdapter _spAdapter;
		private ListAdapter _lAdapter;

		/**
		 * <p>
		 * Creates a new ListAdapter wrapper for the specified adapter.
		 * </p>
		 * 
		 * @param adapter
		 *            the Adapter to transform into a ListAdapter
		 */
		public DropDownAdapter(SpinnerAdapter adapter) {
			this._spAdapter = adapter;
			if (adapter instanceof ListAdapter) {
				this._lAdapter = (ListAdapter) adapter;
			}
		}

		public int getCount() {
			return _spAdapter == null ? 0 : _spAdapter.getCount();
		}

		public Object getItem(int position) {
			return _spAdapter == null ? null : _spAdapter.getItem(position);
		}

		public long getItemId(int position) {
			return _spAdapter == null ? -1 : _spAdapter.getItemId(position);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			return getDropDownView(position, convertView, parent);
		}

		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			return (_spAdapter == null) ? null : _spAdapter.getDropDownView(
					position, convertView, parent);
		}

		public boolean hasStableIds() {
			return _spAdapter != null && _spAdapter.hasStableIds();
		}

		public void registerDataSetObserver(DataSetObserver observer) {
			if (_spAdapter != null) {
				_spAdapter.registerDataSetObserver(observer);
			}
		}

		public void unregisterDataSetObserver(DataSetObserver observer) {
			if (_spAdapter != null) {
				_spAdapter.unregisterDataSetObserver(observer);
			}
		}

		/**
		 * If the wrapped SpinnerAdapter is also a ListAdapter, delegate this
		 * call. Otherwise, return true.
		 */
		public boolean areAllItemsEnabled() {
			final ListAdapter adapter = _lAdapter;
			if (adapter != null) {
				return adapter.areAllItemsEnabled();
			} else {
				return true;
			}
		}

		/**
		 * If the wrapped SpinnerAdapter is also a ListAdapter, delegate this
		 * call. Otherwise, return true.
		 */
		public boolean isEnabled(int position) {
			final ListAdapter adapter = _lAdapter;
			if (adapter != null) {
				return adapter.isEnabled(position);
			} else {
				return true;
			}
		}

		public int getItemViewType(int position) {
			return 0;
		}

		public int getViewTypeCount() {
			return 1;
		}

		public boolean isEmpty() {
			return getCount() == 0;
		}
	}

	/**
	 * Similar to the SpinnerPopup interface in Spinner. The bubble popup
	 * implements a popup selection interface.
	 */
	private interface ISpinnerPopup {
		public void setAdapter(ListAdapter adapter);

		public void show();

		public void dismiss();

		/**
		 * @return true if the popup is showing, false otherwise.
		 */
		public boolean isShowing();

		/**
		 * Set hint text to be displayed to the user. This should provide a
		 * description of the choice being made.
		 * 
		 * @param hintText
		 *            Hint text to set.
		 */
		public void setPromptText(CharSequence hintText);

		public CharSequence getHintText();

		/**
		 * Specify the horizonal or vertical offset of the popup
		 * 
		 * @param px
		 *            Offset in pixels.
		 */
		public void setVerticalOffset(int px);

		public void setHorizontalOffset(int px);

		public Drawable getBackground();

		public void setBackground(Drawable drawable);

		public int getVerticalOffset();

		public int getHorizontalOffset();
	}

	/**
	 * BubblePopup Consists of a small triangle and a rounded rectangle denoting
	 * the "speech bubble" popup.
	 * 
	 */
	protected class BubblePopup extends IcsListPopupWindow {

		private static final int _INIT = 0;
		private static final int _FIRST = 1;
		private static final int _LAST = 2;
		private CharSequence _hinttext;
		private ListAdapter _ladapter;

		private Paint _selectorColor;
		private Paint _bgColor;
		private Paint _fontColor;
		private ShapeDrawable _selector;
		private StateListDrawable _sld; // bg drawable ;_; HACK
		private ShapeDrawable _stdbubble;
		private LayerDrawable _bubbleFirst;
		private LayerDrawable _bubbleLast;

		private boolean _initDrawable = false;
		private boolean _init = false;

		public BubblePopup(Context context, AttributeSet attrs, int defStyleAttr) {
			super(context, attrs, defStyleAttr);

			setAnchorView(BubbleSpinner.this);
			setModal(true); // dismiss popup if clicked outside
			setPromptPosition(POSITION_PROMPT_ABOVE); // optional prompt view
														// position
			setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					BubbleSpinner.this.setSelection(position);

					BubbleSpinner.this.performItemClick(view, position,
							_ladapter.getItemId(position));

					dismiss();
				}
			});
		}

		@Override
		public void setAdapter(ListAdapter adapter) {
			super.setAdapter(adapter);
			_ladapter = adapter;
		}

		@Override
		public void show() {
			if (!_initDrawable)
				initDrawable();

			setInputMethodMode(ListPopupWindow.INPUT_METHOD_NOT_NEEDED);
			super.show();

			if (!_init)
				init();

			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			setSelection(BubbleSpinner.this.getSelectedItemPosition());
		}

		private void init() {
			final ListView lv = getListView();
			if (lv != null) {
				final int listUpperIndx = lv.getCount() - 1;
				lv.setDividerHeight(0);

				lv.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							/**
							 * ;_; HACK: Selection / Bg drawable Need to change
							 * the bg drawable to fix the selection marking. The
							 * selector does not cover the tip of the speech
							 * bubble, therefore, another bg drawable is loaded
							 * where the tip and upper part of the speech bubble
							 * looks selected TT
							 */
							int sIndx = lv.pointToPosition((int) event.getX(),
									(int) event.getY()); // get selected item
							// from move pos

							if (sIndx == 0) {
								_sld.setState(new int[] { android.R.attr.state_first });
								lv.setSelection(0);
							} else if (sIndx == listUpperIndx) {
								_sld.setState(new int[] { android.R.attr.state_last });
							} else {
								_sld.setState(new int[] { android.R.attr.state_enabled });
							}
						}
						return false;
					}
				});
			}
		}

		private void initDrawable() {
			Resources r = getResources();
			_selectorColor = new Paint(Paint.ANTI_ALIAS_FLAG);
			_selectorColor.setColor(r.getColor(R.color.spinner_itemSelected));

			_bgColor = new Paint(Paint.ANTI_ALIAS_FLAG);
			_bgColor.setColor(r.getColor(R.color.spinner_bg));

			_fontColor = new Paint(Paint.ANTI_ALIAS_FLAG);
			_fontColor.setColor(r.getColor(R.color.spinner_font));

			/**
			 * ;_; HACK: Alignment of speech bubble tip
			 * 
			 */
			float tipPos = 0.0f;
			float x = getLeft();
			float spinWidth = getMeasuredWidth();
			switch (_tipPosition) {
			case TIPPOS_CENTER:
				tipPos = x + (0.5f * spinWidth);
				break;
			case TIPPOS_LEFT:
				tipPos = x + (0.25f * spinWidth);
				break;
			case TIPPOS_RIGHT:
				tipPos = x + (0.75f * spinWidth);
				break;
			}

			Path bubblePath = new Path();
			bubblePath.moveTo(tipPos, 0);
			bubblePath.lineTo(tipPos + (_tipBottomLength / 2), _rectOffset);
			bubblePath.lineTo(tipPos - (_tipBottomLength / 2), _rectOffset);
			bubblePath.lineTo(tipPos, 0);
			bubblePath.addRoundRect(
					new RectF(0, _rectOffset, _popupWidth, 200), _xRadius,
					_yRadius, Path.Direction.CCW);

			_stdbubble = new ShapeDrawable(new PathShape(bubblePath,
					_popupWidth, 200));
			_stdbubble.getPaint().setColor(_bgColor.getColor());

			/**
			 * ;_; HACK: Bg drawable / anchor alignment A magic vertical offset
			 * is used to align the bg drawable padding with the anchor of the
			 * listview in ListPopupWindow In buildDropDown() a vertical offset
			 * is obtained from the bg to align the content
			 */
			_stdbubble.setPadding(new Rect(0, (int) _rectOffset, 0, 0));

			/**
			 * ;_; HACK: Bg drawable / selection For the first and last element
			 * in the list the selector does not cover the whole bg drawable.
			 * So, there are 3 bg drawables. _stdbubble is as bg drawable when
			 * interior items are selected. For the first and last element
			 * _bubbleFirst and _bubbleLast is used. These additional drawables
			 * fake the effect of a perfectly matching selector for the first
			 * and last items.
			 */
			Path firstUpper = new Path();
			firstUpper.moveTo(tipPos, 0);
			firstUpper.lineTo(tipPos + (_tipBottomLength / 2), _rectOffset);
			firstUpper.lineTo(tipPos - (_tipBottomLength / 2), _rectOffset);
			firstUpper.lineTo(tipPos, 0);
			firstUpper.addRoundRect(
					new RectF(0, _rectOffset, _popupWidth, 200), _xRadius,
					_yRadius, Path.Direction.CCW);
			ShapeDrawable bubbleFirstUpper = new ShapeDrawable(new PathShape(
					firstUpper, _popupWidth, 200));
			bubbleFirstUpper.getPaint().setColor(_selectorColor.getColor());

			Path firstLower = new Path();
			firstLower.addRoundRect(new RectF(0, _rectOffset + 10, _popupWidth,
					200), _xRadius, _yRadius, Path.Direction.CCW);

			ShapeDrawable bubbleFirstLower = new ShapeDrawable(new PathShape(
					firstLower, _popupWidth, 200));
			bubbleFirstLower.getPaint().setColor(_bgColor.getColor());
			_bubbleFirst = new LayerDrawable(new Drawable[] { bubbleFirstUpper,
					bubbleFirstLower });

			Path lastUpper = new Path();
			lastUpper.moveTo(tipPos, 0);
			lastUpper.lineTo(tipPos + (_tipBottomLength / 2), _rectOffset);
			lastUpper.lineTo(tipPos - (_tipBottomLength / 2), _rectOffset);
			lastUpper.lineTo(tipPos, 0);
			lastUpper.addRoundRect(new RectF(0, _rectOffset, _popupWidth, 200),
					_xRadius, _yRadius, Path.Direction.CCW);
			ShapeDrawable bubbleLastUpper = new ShapeDrawable(new PathShape(
					lastUpper, _popupWidth, 200));
			bubbleLastUpper.getPaint().setColor(_bgColor.getColor());

			Path lastLower = new Path();
			lastLower.addRoundRect(new RectF(0, _rectOffset + 130, _popupWidth,
					200), _xRadius, _yRadius, Path.Direction.CCW);

			ShapeDrawable bubbleLastLower = new ShapeDrawable(new PathShape(
					lastLower, _popupWidth, 200));
			bubbleLastLower.getPaint().setColor(_selectorColor.getColor());
			_bubbleLast = new LayerDrawable(new Drawable[] { bubbleLastUpper,
					bubbleLastLower });

			/**
			 * ;_; HACK: Use a state list drawable. These three states
			 * correspond to item selection type. Mostly interior list elements
			 * are selected, which is reflected by state_enabled - the standard
			 * bg drawable is shown. state_first and state_last correspond to
			 * the first and last item selections, where the drawable is
			 * altered.
			 * 
			 */
			_sld = new StateListDrawable();
			_sld.addState(new int[] { android.R.attr.state_enabled },
					_stdbubble);
			_sld.addState(new int[] { android.R.attr.state_first },
					_bubbleFirst); // use state_enabled state
			_sld.addState(new int[] { android.R.attr.state_last }, _bubbleLast); // use
																					// state_expaned

			setBackgroundDrawable(_sld);

			/**
			 * ;_; HACK: after setting a padding value the dropdown bg drawable
			 * and the list items are aligned. To correct the anchor position a
			 * magic number is used to offset the dropdown popup.
			 */
			setVerticalOffset((int) (_rectOffset / 8));
			setContentWidth(_popupWidth);

			if (_customSelectorRes.compareTo("") != 0) {
				// selector drawable specified in xml

			} else {
				// create a std drawable
				Path selectorPath = new Path();
				selectorPath.addRoundRect(new RectF(0, 0, _popupWidth, 200),
						_xRadius, _yRadius, Path.Direction.CCW);
				_selector = new ShapeDrawable(new PathShape(selectorPath,
						_popupWidth, 200));
				_selector.getPaint().setColor(_selectorColor.getColor());
			}

			setListSelector(_selector);
		}
	}
}