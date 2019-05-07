package songchen.productsearch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailSimilar extends Fragment {
	private static final String ITEMS = "items";
	private static final String[] SORT = {"Default", "Name", "Price", "Days"};
	private static final String[] ORDER = {"Ascending", "Descending"};
	private SimilarItemAdapter adapter;
	private List<Item> items;

	public static DetailSimilar newInstance(Item[] items) {
		Bundle args = new Bundle();
		args.putParcelableArray(ITEMS, items);

		DetailSimilar fragment = new DetailSimilar();
		fragment.setArguments(args);
		return fragment;
	}
	public DetailSimilar() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_detail_similar, container, false);

		Spinner sortSpinner = view.findViewById(R.id.sortSpinner);
		ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(getActivity(),
			android.R.layout.simple_spinner_dropdown_item, SORT);
		//set the spinners adapter to the previously created one.
		sortSpinner.setAdapter(sortAdapter);
		sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				sortItems();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});

		Spinner orderSpinner = view.findViewById(R.id.orderSpinner);
		ArrayAdapter<String> orderAdapter = new ArrayAdapter<String>(getActivity(),
			android.R.layout.simple_spinner_dropdown_item, ORDER);
		//set the spinners adapter to the previously created one.
		orderSpinner.setAdapter(orderAdapter);
		orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				sortItems();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});



		Item[] temp = Utils.toItemsArray(getArguments().getParcelableArray(ITEMS));
		items = new ArrayList<>(Arrays.asList(temp));
		adapter = new SimilarItemAdapter(getContext(), items);
		RecyclerView similarItemContainer = view.findViewById(R.id.similarItemsContainer);
		similarItemContainer.setLayoutManager(new LinearLayoutManager(getContext()));
		similarItemContainer.setAdapter(adapter);

		return view;
	}

	private void sortItems() {
		String sort = ((Spinner)getView().findViewById(R.id.sortSpinner)).getSelectedItem().toString();
		String order = ((Spinner)getView().findViewById(R.id.orderSpinner)).getSelectedItem().toString();
		List<Item> sorted = new ArrayList<>(items);
		if (sort.equals("Default")) {
			(getView().findViewById(R.id.orderSpinner)).setEnabled(false);
			adapter.setItems(sorted);
			return;
		}
		(getView().findViewById(R.id.orderSpinner)).setEnabled(true);
		if (sort.equals("Name")) {
			Collections.sort(sorted, new NameComparator());
		} else if (sort.equals("Price")) {
			Collections.sort(sorted, new PriceComparator());
		} else if (sort.equals("Days")) {
			Collections.sort(sorted, new DaysLeftComparator());
		}
		if (order.equals("Descending")) {
			Collections.reverse(sorted);
		}
		adapter.setItems(sorted);
	}

}
