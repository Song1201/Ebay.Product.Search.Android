package songchen.productsearch;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter {
	private List<String> suggestions;

	public AutoCompleteAdapter(Context context, int resource) {
		super(context, resource);
		suggestions = new ArrayList<>();
	}

	public void setData(List<String> data) {
		suggestions.clear();
		suggestions.addAll(data);
	}

	@Override
	public int getCount() {
		return suggestions.size();
	}

	@Override
	public String getItem(int position) {
		return suggestions.get(position);
	}
}
