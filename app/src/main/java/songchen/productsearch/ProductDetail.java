package songchen.productsearch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

public class ProductDetail extends AppCompatActivity {
	public static final String ITEM = "item";
	private Item item;
	private ViewPager viewPager;
	private ProductDetailPagerAdapter pagerAdapter;
	private RequestQueue queue;
	private static final Gson gson = new Gson();
	private Product product;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.product_detail_action_bar, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.facebookBtn) {
			try {
				String url =
					"https://www.facebook.com/sharer/sharer.php?u=" + URLEncoder.encode(product.getShareUrl(), "UTF-8") +
						"&quote=" + URLEncoder.encode("Buy " + product.getTitle() + " at " + product.getPrice().toString() + " from Ebay!",
						"UTF-8");
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(browserIntent);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		item = getIntent().getParcelableExtra(ITEM);
		setTitle(item.getTitle());
		viewPager = findViewById(R.id.productDetailPager);
		pagerAdapter = new ProductDetailPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(pagerAdapter);
		setTabsIcon();
		queue = Volley.newRequestQueue(this);
		fetchProduct();
		fetchPhoto();
		fetchSimilar();
		setupWishListBtn();

	}

	private void setupWishListBtn() {
		FloatingActionButton wishListBtn = findViewById(R.id.detailWishListBtn);
		if (Utils.inWishList(this, item)) {
			wishListBtn.setImageResource(R.drawable.ic_cart_remove);
		} else {
			wishListBtn.setImageResource(R.drawable.ic_cart_plus);
		}
		wishListBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Context context = v.getContext();
				if (Utils.inWishList(v.getContext(), item)) {
					Utils.rmFromWishList(context, item);
					((FloatingActionButton)v).setImageResource(R.drawable.ic_cart_plus);
				} else {
					Utils.addToWishList(context, item);
					((FloatingActionButton)v).setImageResource(R.drawable.ic_cart_remove);
				}
			}
		});
	}


	private void fetchSimilar() {
		String url = "";
		try {
			url = "http://ebay-product-search-songchen.azurewebsites" +
				".net/api/similar-product/" + URLEncoder.encode(item.getItemId(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				SearchResponse res = gson.fromJson(response, SearchResponse.class);
				if (res.getAck().equals("Success")) {
					Item[] items = res.getItems();
					if (items.length == 0) {
						pagerAdapter.setItem(3,
							ErrorPage.newInstance("Sorry, we can not find any similar items."));
					} else {
						pagerAdapter.setItem(3, DetailSimilar.newInstance(items));
					}
				} else {
					pagerAdapter.setItem(3, ErrorPage.newInstance("Error: " + res.getErrorId() + " " + res.getMessage()));
				}
				setTabsIcon();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				pagerAdapter.setItem(3, ErrorPage.newInstance("Error in getting response from back-end"));
				setTabsIcon();
			}
		});
		queue.add(req);
	}

	private void setTabsIcon() {
		TabLayout tabLayout = findViewById(R.id.productDetailTabs);
		tabLayout.setupWithViewPager(viewPager);
		tabLayout.getTabAt(0).setIcon(R.drawable.ic_information_variant);
		tabLayout.getTabAt(1).setIcon(R.drawable.ic_truck_delivery);
		tabLayout.getTabAt(2).setIcon(R.drawable.ic_google);
		tabLayout.getTabAt(3).setIcon(R.drawable.ic_equal);
	}

	private void fetchPhoto() {
		String url = "";
		try {
			url = "http://ebay-product-search-songchen.azurewebsites" +
				".net/api/product-photo/?q=" + URLEncoder.encode(item.getTitle(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			getSupportFragmentManager().beginTransaction()
				.replace(R.id.container,
					ErrorPage.newInstance("Error in encoding photo query string")).commit();
		}
		StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				PhotoResponse res = gson.fromJson(response, PhotoResponse.class);
				if (res.getAck().equals("Success")) {
					String[] photos = res.getPhotos();
					if (photos.length == 0) {
						pagerAdapter.setItem(2, ErrorPage.newInstance("Sorry, we can not find any photo."));
					} else {
						pagerAdapter.setItem(2, DetailPhoto.newInstance(photos));
					}
				} else {
					pagerAdapter.setItem(2, ErrorPage.newInstance("Error: " + res.getErrorId() + " " + res.getMessage()));
				}
				setTabsIcon();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				pagerAdapter.setItem(2, ErrorPage.newInstance("Error in getting response from back-end"));
				setTabsIcon();
			}
		});
		queue.add(req);
	}

	private void fetchProduct() {
		String url = "http://ebay-product-search-songchen.azurewebsites" +
			".net/api/product-detail/" + item.getItemId();
		StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				DetailResponse res = gson.fromJson(response, DetailResponse.class);
				if (res.getAck().equals("Success")) {
					product = res.getProduct();
					pagerAdapter.setItem(0, DetailProduct.newInstance(product, item));
					pagerAdapter.setItem(1, DetailShipping.newInstance(product, item));
					setTabsIcon();
				} else {
					pagerAdapter.setItem(0,
						ErrorPage.newInstance("Error: " + res.getErrorId() + " " + res.getMessage()));
					pagerAdapter.setItem(1,
						ErrorPage.newInstance("Error: " + res.getErrorId() + " " + res.getMessage()));
				}
				setTabsIcon();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				pagerAdapter.setItem(0,
					ErrorPage.newInstance("Error in getting response from back-end"));
				pagerAdapter.setItem(1,
					ErrorPage.newInstance("Error in getting response from back-end"));
				setTabsIcon();
			}
		});
		queue.add(req);
	}
}
