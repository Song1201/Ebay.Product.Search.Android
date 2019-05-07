package songchen.productsearch;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsGrid extends Fragment {
	private static final String ITEMS = "items";
	public static final int FOUND_ITEMS = 0;
	public static final int WISH_LIST = 1;
	private static final String MODE = "mode";
	private ItemsGridAdapter adapter;

	public static ItemsGrid newInstance(Item[] items, int mode) {

		Bundle args = new Bundle();
		args.putParcelableArray(ITEMS, items);
		args.putInt(MODE, mode);

		ItemsGrid fragment = new ItemsGrid();
		fragment.setArguments(args);
		return fragment;
	}

	private static Item[] extractItemsFromArgs(Bundle args) {
		Parcelable[] temp = args.getParcelableArray(ITEMS);
		Item[] items = new Item[temp.length];
		for (int i=0; i<items.length; i++) {
			items[i] = (Item)temp[i];
		}
		return items;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_items_grid, container, false);
		RecyclerView itemsGrid = view.findViewById(R.id.itemsGrid);
		Bundle args = getArguments();
		int mode = args.getInt(MODE);
		if (mode == FOUND_ITEMS) {
			Item[] items = extractItemsFromArgs(args);
			adapter = new ItemsGridAdapter(getContext(), items);
		} else {
			adapter =
				new ItemsGridAdapter(getContext(),
					(TextView)getActivity().findViewById(R.id.wishListTotal),
					(TextView)getActivity().findViewById(R.id.wishListNum));
		}
		itemsGrid.setLayoutManager(new GridLayoutManager(getContext(),2));
		itemsGrid.setAdapter(adapter);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.onResume();
	}

	public ItemsGrid() {
		// Required empty public constructor
	}
}
