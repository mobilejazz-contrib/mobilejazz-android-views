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

	public ExclusiveButtons() {
		items = new ArrayList<CompoundButton>();
		currentSelection = null;
	}

	public void addItem(CompoundButton item) {
		items.add(item);
		if (currentSelection == null) {
			item.setChecked(true);
			currentSelection = item;
		}
		item.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked && buttonView != currentSelection) {
			currentSelection.setChecked(false);
			buttonView.setChecked(true);
			currentSelection = buttonView;
		}
	}

}
