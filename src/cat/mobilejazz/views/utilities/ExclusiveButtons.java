package cat.mobilejazz.views.utilities;

import java.util.ArrayList;
import java.util.List;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * This class allows to mark a set of {@link CompoundButton}s as exclusive,
 * meaning that at any point in time only one of the may be selected. In
 * particular, if another checkable in the group is selected, the old selection
 * is removed.
 * 
 * @author Hannes Widmoser
 * 
 */
public class ExclusiveButtons implements OnCheckedChangeListener {

	private List<CompoundButton> items;
	private CompoundButton currentSelection;

	private OnCheckedChangeListener listener;
	private boolean canDeselectCurrent;

	public ExclusiveButtons() {
		items = new ArrayList<CompoundButton>();
		currentSelection = null;
		canDeselectCurrent = false;
	}

	public ExclusiveButtons(boolean canDeselectCurrent) {
		this.canDeselectCurrent = canDeselectCurrent;
	}

	public void addItem(CompoundButton item) {
		items.add(item);
		if (currentSelection == null && canDeselectCurrent) {
			item.setChecked(true);
			currentSelection = item;
		}
		item.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked && buttonView != currentSelection) {
			if (currentSelection != null)
				currentSelection.setChecked(false);
			buttonView.setChecked(true);
			currentSelection = buttonView;
		} else if (canDeselectCurrent && buttonView == currentSelection && !isChecked) {
			currentSelection.setChecked(isChecked);
			currentSelection = null;
		}
		if (listener != null) {
			listener.onCheckedChanged(buttonView, isChecked);
		}
	}

	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		this.listener = listener;
	}

}
