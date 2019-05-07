package songchen.productsearch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;



public class Search extends Fragment {
	private static final int TRIGGER_AUTO_COMPLETE = 100;
	private static final int AUTO_COMPLETE_DELAY = 300;
	private AutoCompleteAdapter autoCompleteAdapter;
	private Handler handler;
	private static final int autoCompleteThresh = 1;
	private RequestQueue queue;
	private Gson gson;
	private String currentZip;
	private static final Map<String, String> categoryToId = makeCategoryIdPairs();
	private static final String DEFAULT_DISTANCE = "10";
	public static final String QUERY_STRING = "query string";
	public static final String KEYWORDS = "keywords";

	private static Map<String, String> makeCategoryIdPairs() {
		Map<String, String> categoryToId = new HashMap<>();
		categoryToId.put("Art", "550");
		categoryToId.put("Baby", "2984");
		categoryToId.put("Books", "267");
		categoryToId.put("Clothing, Shoes & Accessories", "11450");
		categoryToId.put("Computers/Tablets & Networking", "58058");
		categoryToId.put("Health & Beauty", "26395");
		categoryToId.put("Music", "11233");
		categoryToId.put("Video Games & Consoles", "1249");
		return categoryToId;
	}


	public Search() {
		// Required empty public constructor
	}

	private void hideNearby() {
		View view = getView();
		view.findViewById(R.id.distanceInput).setVisibility(View.GONE);
		view.findViewById(R.id.clientLocLabel).setVisibility(View.GONE);
		view.findViewById(R.id.clientLocSelections).setVisibility(View.GONE);
	}

	private void showNearby() {
		View view = getView();
		view.findViewById(R.id.distanceInput).setVisibility(View.VISIBLE);
		view.findViewById(R.id.clientLocLabel).setVisibility(View.VISIBLE);
		view.findViewById(R.id.clientLocSelections).setVisibility(View.VISIBLE);
	}

	private void onNearbyClicked(View checkbox) {
		boolean nearby = ((CheckBox)checkbox).isChecked();
		if (nearby) {
			showNearby();
		} else {
			hideNearby();
			((TextInputLayout)getView().findViewById(R.id.zipInputLayout)).setErrorEnabled(false);
		}
	}


	private boolean validateSearchInput() {
		boolean valid = true;
		View view = getView();
		EditText keywordField = view.findViewById(R.id.keywordInput);
		TextInputLayout keywordLayout = view.findViewById(R.id.keywordInputLayout);
		if (keywordField.getText().toString().trim().isEmpty()) {
			keywordLayout.setError("Please enter mandatory field");
			valid = false;
		}
		if (((CheckBox)view.findViewById(R.id.nearbyCheckbox)).isChecked() &&
			((RadioButton)view.findViewById(R.id.clientZip)).isChecked()) {
			String zipcode = ((EditText)view.findViewById(R.id.clientZipInput)).getText().toString().trim();
			if (zipcode.length() != 5) {
				TextInputLayout zipcodeLayout = view.findViewById((R.id.zipInputLayout));
				zipcodeLayout.setError("Please enter mandatory field");
				valid = false;
			}
		}
		return valid;
	}

	private void clearError() {
		View view = getView();
		((TextInputLayout)view.findViewById(R.id.keywordInputLayout)).setErrorEnabled(false);
		((TextInputLayout)view.findViewById(R.id.zipInputLayout)).setErrorEnabled(false);
	}

	private String makeQueryString() {
		View  view = getView();
		String queryString = "";

//		queryString += "keywords=" + Utils.encodeUrl(((EditText) view.findViewById(R.id.keywordInput)).getText().toString().trim());
		try {
			queryString += "keywords=" + URLEncoder.encode(((EditText) view.findViewById(R.id.keywordInput)).getText().toString().trim(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String category = ((Spinner)view.findViewById(R.id.categoryInput)).getSelectedItem().toString();
		queryString += (category == "All" ? "" : "&categoryId=" + categoryToId.get(category));

		int filter = 0;
		int value = 0;

		if (((CheckBox)view.findViewById(R.id.nearbyCheckbox)).isChecked()) {
			queryString += "&buyerPostalCode=";
			if (((RadioGroup)view.findViewById(R.id.clientLocOption)).getCheckedRadioButtonId() == R.id.currLoc) {
				queryString += currentZip;
			} else {
				queryString += ((AutoCompleteTextView)view.findViewById(R.id.clientZipInput)).getText().toString();
			}

			String distance =
				((EditText)view.findViewById(R.id.distanceInput)).getText().toString().trim();
			if (distance.isEmpty()) {
				distance = DEFAULT_DISTANCE;
			}
			queryString += "&itemFilter(" + filter + ").name=MaxDistance&itemFilter(" + filter++ +
				").value=" + distance;
		}

		queryString += "&itemFilter(" + filter + ").name=HideDuplicateItems&itemFilter(" + filter++ +
			").value=true";

		String conditionFilter = "";
		if (((CheckBox)view.findViewById(R.id.New)).isChecked()) {
			conditionFilter += "&itemFilter(" + filter + ").value(" + value++ + ")=New";
		}
		if (((CheckBox)view.findViewById(R.id.Used)).isChecked()) {
			conditionFilter += "&itemFilter(" + filter + ").value(" + value++ + ")=Used";
		}
		if (((CheckBox)view.findViewById(R.id.Unspecified)).isChecked()) {
			conditionFilter += "&itemFilter(" + filter + ").value(" + value++ + ")=Unspecified";
		}

		queryString += value==0? "" : "&itemFilter(" + filter++ + ").name=Condition" + conditionFilter;

		if (((CheckBox)view.findViewById(R.id.freeShip)).isChecked()) {
			queryString += "&itemFilter(" + filter + ").name=LocalPickupOnly&itemFilter(" + filter++ +
				").value=true";
		}
		if (((CheckBox)view.findViewById(R.id.local)).isChecked()) {
			queryString += "&itemFilter(" + filter + ").name=FreeShippingOnly&itemFilter(" + filter++ +
				").value=true";
		}

		return queryString;
	}

	private void onSearchClicked() {
		clearError();
		if (!validateSearchInput()) {
			Toast.makeText(getActivity(), "Please fix all fields with errors",
				Toast.LENGTH_SHORT).show();
			return;
		}
		String queryString = makeQueryString();
		Intent intent = new Intent(getActivity(), SearchResult.class);
		intent.putExtra(QUERY_STRING, queryString);
		intent.putExtra(KEYWORDS,
			((EditText)getView().findViewById(R.id.keywordInput)).getText().toString().trim());
		startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		autoCompleteAdapter = new AutoCompleteAdapter(getContext(),
			android.R.layout.simple_dropdown_item_1line);
		queue = Volley.newRequestQueue(getContext());
		gson = new Gson();
		fetchCurrentZip();
	}

	private void fetchCompleteZips(String incompleteZip) {
		String url = "http://ebay-product-search-songchen.azurewebsites" +
			".net/api/complete-zip?postalcode_startsWith=" + incompleteZip;
		StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				ZipcodeResponse res = gson.fromJson(response, ZipcodeResponse.class);
				if (res.ack.equals("Success")) {
					autoCompleteAdapter.setData(res.getZips());
					autoCompleteAdapter.notifyDataSetChanged();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		});

		queue.add(req);
	}

	private void fetchCurrentZip() {
		String url = "http://ip-api.com/json";
		StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				IpApiResponse res = gson.fromJson(response, IpApiResponse.class);
				currentZip = res.getZip();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				currentZip = "";
			}
		});
		queue.add(req);
	}

	private void onClearClicked() {
		clearError();
		View view = getView();
		((EditText)view.findViewById(R.id.keywordInput)).setText(null);
		((Spinner)view.findViewById(R.id.categoryInput)).setSelection(0);
		((CheckBox)view.findViewById(R.id.nearbyCheckbox)).setChecked(false);
		hideNearby();
		((EditText)view.findViewById(R.id.distanceInput)).setText(null);
		((RadioButton)view.findViewById(R.id.currLoc)).setChecked(true);
		((AutoCompleteTextView)view.findViewById(R.id.clientZipInput)).setText(null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final View view = inflater.inflate(R.layout.fragment_search, container, false);

		Spinner categoryInput = view.findViewById(R.id.categoryInput);
		String[] categories = {"All", "Art", "Baby", "Books", "Clothing, Shoes & Accessories",
			"Computers, Tablets & Networking", "Health & Beauty", "Music", "Video Games & Consoles"};

		//create an adapter to describe how the items are displayed, adapters are used in several places in android.
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
			android.R.layout.simple_spinner_dropdown_item, categories);
		//set the spinners adapter to the previously created one.
		categoryInput.setAdapter(adapter);

		view.findViewById(R.id.nearbyCheckbox).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onNearbyClicked(v);
			}
		});

		view.findViewById(R.id.searchBtn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSearchClicked();
			}
		});

		final AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.clientZipInput);
		autoCompleteTextView.setThreshold(autoCompleteThresh);
		autoCompleteTextView.setAdapter(autoCompleteAdapter);
//		autoCompleteTextView.setOnItemClickListener();
		autoCompleteTextView.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				handler.removeMessages(TRIGGER_AUTO_COMPLETE);
				handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE, AUTO_COMPLETE_DELAY);
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				if (msg.what == TRIGGER_AUTO_COMPLETE) {
					String incompleteZip = autoCompleteTextView.getText().toString().trim();
					if (incompleteZip.length() >= autoCompleteThresh) {
						fetchCompleteZips(incompleteZip);
					}
				}
				return false;
			}
		});

		((RadioGroup)view.findViewById(R.id.clientLocOption)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.currLoc) {
					view.findViewById(R.id.clientZipInput).setEnabled(false);
				} else {
					view.findViewById(R.id.clientZipInput).setEnabled(true);
				}
			}
		});

		view.findViewById(R.id.clearBtn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClearClicked();
			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		hideNearby();
	}
}

