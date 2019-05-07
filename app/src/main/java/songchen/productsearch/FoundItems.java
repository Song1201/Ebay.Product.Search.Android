package songchen.productsearch;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FoundItems extends Fragment {
	private static final String ITEMS = "items";
	private static final String KEYWORDS = "keywords";

	public FoundItems() {
		// Required empty public constructor
	}

	public static FoundItems newInstance(Item[] items, String keywords) {
		Bundle args = new Bundle();
		args.putParcelableArray(ITEMS, items);
		args.putString(KEYWORDS, keywords);

		FoundItems fragment = new FoundItems();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private static Item[] extractItemsFromArgs(Bundle args) {
		Parcelable[] temp = args.getParcelableArray(ITEMS);
		Item[] items = new Item[temp.length];
		for (int i=0; i<items.length; i++) {
			items[i] = (Item)temp[i];
		}
		return items;
	}

	private void setTopInfo(View view, String keywords, int numItems) {
		String standOutColor = "#" + Integer.toHexString(ContextCompat.getColor(getContext()
			, R.color.btnOrange) & 0x00ffffff);
		String topInfo =
			"Showing <font color=" + standOutColor + ">" + numItems + "</font> " +
				"results for <font color=" + standOutColor + ">" + keywords + "</font>";
		((TextView)view.findViewById(R.id.topInfo)).setText(Html.fromHtml(topInfo
			, Html.FROM_HTML_MODE_LEGACY));
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_found_items, container,
			false);

		Bundle args = getArguments();
		Item[] items = extractItemsFromArgs(args);
		setTopInfo(view, args.getString(KEYWORDS), items.length);

		getActivity().getSupportFragmentManager().beginTransaction()
			.add(R.id.foundItemsItemsGridContainer, ItemsGrid.newInstance(items, ItemsGrid.FOUND_ITEMS)).commit();

		return view;
	}

}
