package songchen.productsearch;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.wssholmes.stark.circular_score.CircularScoreView;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailShipping extends Fragment {
	private static final String PRODUCT = "product";
	private static final String ITEM = "item";
	private Product product;
	private Item item;
	private static final Map<String, Integer> starColor = new HashMap<>();
	static {
		starColor.put("blue", R.color.blue);
		starColor.put("green", R.color.green);
		starColor.put("purple", R.color.purple);
		starColor.put("red", R.color.red);
		starColor.put("silver", R.color.silver);
		starColor.put("turquoise", R.color.turquoise);
		starColor.put("yellow", R.color.yellow);
	}

	public static DetailShipping newInstance(Product product, Item item) {
		Bundle args = new Bundle();
		args.putParcelable(PRODUCT, product);
		args.putParcelable(ITEM, item);

		DetailShipping fragment = new DetailShipping();
		fragment.setArguments(args);
		return fragment;
	}

	public DetailShipping() {
		// Required empty public constructor
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
		View view = inflater.inflate(R.layout.fragment_detail_shipping, container, false);
		addSoldBy(view, inflater);
		addInfo(view, inflater);
		addReturnPolicy(view, inflater);

		return view;
	}

	private void addReturnPolicy(View view, LayoutInflater inflater) {
		if (product.getReturnPolicy() == null) {
			return;
		}
		ReturnPolicy returnPolicy = product.getReturnPolicy();
		TableLayout table = Utils.makeTableLayout(getContext());
		table.addView(Utils.makeTitleRow(inflater, table, R.drawable.ic_dump_truck,
			"Return Policy"));

		if (returnPolicy.getReturnAccepted() != null) {
			table.addView(Utils.makeDetailShippingRow(inflater, table, "Policy",
				returnPolicy.getReturnAccepted()));
		}
		if (returnPolicy.getReturnWithin() != null) {
			table.addView(Utils.makeDetailShippingRow(inflater, table, "Returns within",
				returnPolicy.getReturnWithin()));
		}
		if (returnPolicy.getRefundMode() != null) {
			table.addView(Utils.makeDetailShippingRow(inflater, table, "Refund Mode",
				returnPolicy.getRefundMode()));
		}
		if (returnPolicy.getShipBy() != null) {
			table.addView(Utils.makeDetailShippingRow(inflater, table, "Shipped by",
				returnPolicy.getShipBy()));
		}

		((LinearLayout)view.findViewById(R.id.detailShippingContainer)).addView(table);
	}

	private void addInfo(View view, LayoutInflater inflater) {
		boolean hasContent = false;
		TableLayout info = Utils.makeTableLayout(getContext());
		info.addView(Utils.makeTitleRow(inflater, info, R.drawable.ic_ferry, "Shipping Info"));
		if (item.getShipping() != null && item.getShipping().getDetailCost() != null) {
			hasContent = true;
			info.addView(Utils.makeDetailShippingRow(inflater, info, "Shipping Cost", item.getShipping().getDetailCost()));
		}
		if (product.getGlobalShipping() != null) {
			hasContent = true;
			info.addView(Utils.makeDetailShippingRow(inflater, info, "Global Shipping",
				(product.getGlobalShipping()? "Yes" : "No")));
		}
		if (product.prettyHandlingTime() != null) {
			hasContent = true;
			info.addView(Utils.makeDetailShippingRow(inflater, info, "Handling Time",
				product.prettyHandlingTime()));
		}
		if (product.getCondition() != null) {
			hasContent = true;
			info.addView(Utils.makeDetailShippingRow(inflater, info, "Condition",
				product.getCondition()));
		}

		if (hasContent) {
			((LinearLayout)view.findViewById(R.id.detailShippingContainer)).addView(info);
		}
	}

	private void addSoldBy(View view, LayoutInflater inflater) {
		if (product.getSeller() == null) {
			return;
		}
		TableLayout soldBy = Utils.makeTableLayout(getContext());
		soldBy.addView(Utils.makeTitleRow(inflater, soldBy, R.drawable.ic_truck, "Sold By"));
		addStoreName(soldBy, inflater);
		addFeedbackScore(soldBy, inflater);
		addPopularity(soldBy, inflater);
		addRatingStar(soldBy, inflater);

		((LinearLayout)view.findViewById(R.id.detailShippingContainer)).addView(soldBy);
	}

	private void addRatingStar(TableLayout soldBy, LayoutInflater inflater) {
		if (product.getSeller().getFeedbackRatingStar() == null) {
			return;
		}
		String color = product.getSeller().getFeedbackRatingStar().toLowerCase();
		boolean shooting = false;
		if (color.contains("shooting")) {
			shooting = true;
			color = color.substring(0, color.length()-8); // Remove trail shooting
		}
		Integer colorId = starColor.get(color);
		if (colorId == null) {
			return;
		}
		TableRow row = (TableRow) inflater.inflate(R.layout.rating_star_row, soldBy, false);

		ImageView star = row.findViewById(R.id.detailShippingRowCell);
		if (shooting) {
			star.setImageResource(R.drawable.ic_star_circle);
		} else {
			star.setImageResource(R.drawable.ic_star_circle_outline);
		}
		DrawableCompat.setTint(star.getDrawable(),ContextCompat.getColor(getContext(),
			colorId));
		soldBy.addView(row);
	}

	private void addPopularity(TableLayout soldBy, LayoutInflater inflater) {
		if (product.getSeller().getPopularity() == null) {
			return;
		}
		TableRow row = (TableRow) inflater.inflate(R.layout.populartiy_row, soldBy, false);
		CircularScoreView popularity = row.findViewById(R.id.detailShippingRowCell);
		popularity.setScore((int)Math.round(product.getSeller().getPopularity()));
		soldBy.addView(row);
	}

	private void addFeedbackScore(TableLayout soldBy, LayoutInflater inflater) {
		if (product.getSeller().getFeedbackScore() == null) {
			return;
		}
		soldBy.addView(Utils.makeDetailShippingRow(inflater, soldBy, "Feedback Score",
			product.getSeller().getFeedbackScore().toString()));
	}

	private void addStoreName(TableLayout soldBy, LayoutInflater inflater) {
		if (product.getSeller().getStoreName() == null || product.getSeller().getStoreUrl() == null) {
			return;
		}
		TableRow row = (TableRow) inflater.inflate(R.layout.detail_shipping_row, soldBy, false);
		((TextView)row.findViewById(R.id.detailShippingRowHeader)).setText("Store Name");
		TextView storeLink = row.findViewById(R.id.detailShippingRowCell);
		storeLink.setText(Html.fromHtml("<a href=\"" + product.getSeller().getStoreUrl()
				+ "\">" + product.getSeller().getStoreName() + "</a>",Html.FROM_HTML_MODE_LEGACY));
		storeLink.setMovementMethod(LinkMovementMethod.getInstance());
		soldBy.addView(row);
	}

}
