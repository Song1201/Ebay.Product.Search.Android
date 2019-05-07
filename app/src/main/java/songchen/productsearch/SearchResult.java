package songchen.productsearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class SearchResult extends AppCompatActivity {
	private RequestQueue queue;
	private Gson gson;

	private void fetchSearchResult() {
		Intent intent = getIntent();
		String queryString = intent.getStringExtra(Search.QUERY_STRING);
		final String keywords = intent.getStringExtra(Search.KEYWORDS);
		String url = "http://ebay-product-search-songchen.azurewebsites" +
			".net/api/product-search?" + queryString;
		StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				SearchResponse res = gson.fromJson(response, SearchResponse.class);
				if (res.getAck().equals("Success")) {
					getSupportFragmentManager().beginTransaction()
						.replace(R.id.container, FoundItems.newInstance(res.getItems(),
							keywords)).commit();
				} else {
					getSupportFragmentManager().beginTransaction()
						.replace(R.id.container,
							ErrorPage.newInstance("Error: " + res.getErrorId() + " " + res.getMessage())).commit();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				getSupportFragmentManager().beginTransaction()
					.replace(R.id.container,
						ErrorPage.newInstance("Error in getting response from back-end")).commit();
			}
		});
		queue.add(req);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		queue = Volley.newRequestQueue(this);
		gson = new Gson();
		getSupportFragmentManager().beginTransaction()
			.add(R.id.container, Spinner.newInstance("Searching Products ...")).commit();
		fetchSearchResult();
	}


}
