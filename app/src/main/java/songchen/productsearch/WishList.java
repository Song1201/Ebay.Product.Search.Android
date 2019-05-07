package songchen.productsearch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class WishList extends Fragment {
	private ItemsGrid itemsGrid;
	private ErrorPage errorPage;

	public WishList() {
		// Required empty public constructor
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_wish_list, container, false);
		errorPage = ErrorPage.newInstance("No Wishes");
		itemsGrid = ItemsGrid.newInstance(null, ItemsGrid.WISH_LIST);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (Utils.getWishList(getContext()).size() == 0) {
			getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.wishListItemsGridContainer, errorPage).commit();
		} else {
			getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.wishListItemsGridContainer, itemsGrid).commit();
		}
	}
}
