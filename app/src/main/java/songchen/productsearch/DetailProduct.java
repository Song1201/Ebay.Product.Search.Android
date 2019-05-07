package songchen.productsearch;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailProduct extends Fragment {
	private static final String PRODUCT = "product";
	private static final String ITEM = "item";
	private Product product;
	private Item item;

	public static DetailProduct newInstance(Product product, Item item) {
		Bundle args = new Bundle();
		args.putParcelable(PRODUCT, product);
		args.putParcelable(ITEM, item);
		
		DetailProduct fragment = new DetailProduct();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		product = getArguments().getParcelable(PRODUCT);
		item = getArguments().getParcelable(ITEM);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_detail_product, container, false);
		setGallery(view, inflater);
		((TextView)view.findViewById(R.id.detailProductTitle)).setText(product.getTitle());
		((TextView)view.findViewById(R.id.detailProductPrice)).setText(product.getPrice().toString());
		((TextView)view.findViewById(R.id.detailProductShipping)).setText("With " + item.getShipping().getCost());
		makeHighlight(view, inflater);
		makeSpecifics(view, inflater);

		return view;
	}

	private void makeSpecifics(View view, LayoutInflater inflater) {
		if (product.getSpecifics() == null) {
			return;
		}
		TableLayout specifics = Utils.makeTableLayout(getContext());
		specifics.addView(Utils.makeTitleRow(inflater, specifics, R.drawable.ic_wrench,
			"Specifications"));
		if (product.getBrand() != null) {
			specifics.addView(Utils.makeSpecificRow(inflater, specifics,
				StringUtils.capitalize(product.getBrand())));
		}
		for (NameValuePair pair:product.getSpecifics()) {
			if (!pair.getName().toLowerCase().equals("brand")) {
				specifics.addView(Utils.makeSpecificRow(inflater, specifics,
					StringUtils.capitalize(String.join(" ", pair.getValue()))));
			}
		}

		((LinearLayout)view.findViewById(R.id.detailProductDivided)).addView(specifics);
	}

	private void makeHighlight(View view, LayoutInflater inflater) {
		boolean hasHighlight = false;
		TableLayout highlight = Utils.makeTableLayout(getContext());
		highlight.addView(Utils.makeTitleRow(inflater, highlight, R.drawable.ic_information,
			"Highlights"));
		if (product.getSubtitle() != null) {
			hasHighlight = true;
			highlight.addView(Utils.makeDetailProductRow(inflater, highlight, "Subtitle",
				product.getSubtitle()));
		}
		if (product.getPrice() != null) {
			hasHighlight = true;
			highlight.addView(Utils.makeDetailProductRow(inflater, highlight, "Price",
				product.getPrice().toString()));
		}
		if (product.getBrand() != null) {
			hasHighlight = true;
			highlight.addView(Utils.makeDetailProductRow(inflater, highlight, "Brand",
				product.getBrand()));
		}

		if (hasHighlight) {
			((LinearLayout)view.findViewById(R.id.detailProductDivided)).addView(highlight);
		}
	}

	private void setGallery(View view, LayoutInflater inflater) {
		if (product.getImageUrl() == null) {
			view.findViewById(R.id.detailProductHorizontalScroll).setVisibility(View.GONE);
			return;
		}
		LinearLayout gallery = view.findViewById(R.id.detailProductGallery);
		for (String imageUrl:product.getImageUrl()) {
			ImageView image = (ImageView)inflater.inflate(R.layout.gallery_image, gallery, false);
			Picasso.with(getContext()).load(imageUrl).into(image);
			gallery.addView(image);
		}
	}

	public DetailProduct() {
		// Required empty public constructor
	}

}
