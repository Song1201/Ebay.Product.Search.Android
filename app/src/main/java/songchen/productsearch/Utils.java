package songchen.productsearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
	private static final String SHARED_NAME = "productSearchSharedPreference";
	private static final String WISH_LIST = "wishList";
	private static final Gson gson = new Gson();

	public static List<Item> getWishList(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
		List<Item> wishList =
			gson.fromJson(sharedPref.getString(WISH_LIST, "[]"), new TypeToken<List<Item>>(){}.getType());
		return wishList;
	}

	public static void addToWishList(Context context, Item item) {
		SharedPreferences sharedPref = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
		List<Item> wishList =
			gson.fromJson(sharedPref.getString(WISH_LIST, "[]"), new TypeToken<List<Item>>(){}.getType());
		wishList.add(item);
		sharedPref.edit().putString(WISH_LIST, gson.toJson(wishList)).apply();
	}

	public static boolean inWishList(Context context, Item item) {
		SharedPreferences sharedPref = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
		List<Item> wishList =
			gson.fromJson(sharedPref.getString(WISH_LIST, "[]"), new TypeToken<List<Item>>(){}.getType());
		for (Item wish:wishList) {
			if (wish.getItemId().equals(item.getItemId())) {
				return true;
			}
		}
		return false;
	}

	public static void rmFromWishList(Context context, Item item) {
		SharedPreferences sharedPref = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
		List<Item> wishList =
			gson.fromJson(sharedPref.getString(WISH_LIST, "[]"), new TypeToken<List<Item>>(){}.getType());
		for (int i=0; i<wishList.size(); i++) {
			if (wishList.get(i).getItemId().equals(item.getItemId())) {
				wishList.remove(i);
				sharedPref.edit().putString(WISH_LIST, gson.toJson(wishList)).apply();
				return;
			}
		}
	}

	public static String getTotalCost(List<Item> items) {
		if (items.size() == 0) {
			return "$0.00";
		}
		Map<String, Price> currencyIdToPrice = new HashMap<>();
		for (Item item:items) {
			String currencyId = item.getPrice().getCurrencyId();
			Price price = currencyIdToPrice.get(currencyId);
			if (price == null) {
				currencyIdToPrice.put(currencyId, new Price(currencyId, item.getPrice().getValue()));
			} else {
				price.add(item.getPrice().getValue());
			}
		}
		String totalCost = "";
		for (Map.Entry<String, Price> entry:currencyIdToPrice.entrySet()) {
			totalCost += "+" + entry.getValue().toString();
		}
		return totalCost.substring(1);
	}

	public static TableLayout makeTableLayout(Context context) {
		TableLayout tableLayout = new TableLayout(context);
		TableLayout.LayoutParams params =
			new TableLayout.LayoutParams
				(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(16,0, 32, 0);
		tableLayout.setLayoutParams(params);
		return tableLayout;
	}

	public static TableRow makeTitleRow(LayoutInflater inflater, ViewGroup container, int icon,
																			String title) {
		TableRow titleRow = (TableRow) inflater.inflate(R.layout.title_row, container, false);
		((ImageView)titleRow.findViewById(R.id.titleRowIcon)).setImageResource(icon);
		((TextView)titleRow.findViewById(R.id.titleRowText)).setText(title);
		return titleRow;
	}

	public static TableRow makeDetailProductRow(LayoutInflater inflater, ViewGroup container,
																							String header,
																			String cell) {
		TableRow row = (TableRow) inflater.inflate(R.layout.detail_product_table_row, container, false);
		((TextView)row.findViewById(R.id.detailProductRowHeader)).setText(header);
		((TextView)row.findViewById(R.id.detailProductRowCell)).setText(cell);
		return row;
	}

	public static TableRow makeSpecificRow(LayoutInflater inflater, ViewGroup container,
																				 String cell) {
		TableRow row = (TableRow)inflater.inflate(R.layout.specifics_row, container, false);
		((TextView)row.findViewById(R.id.specificCell)).setText("\u2022 " + cell);
		return row;
	}

	public static TableRow makeDetailShippingRow(LayoutInflater inflater, ViewGroup container,
																							 String header, String cell) {
		TableRow row = (TableRow) inflater.inflate(R.layout.detail_shipping_row, container, false);
		((TextView)row.findViewById(R.id.detailShippingRowHeader)).setText(header);
		((TextView)row.findViewById(R.id.detailShippingRowCell)).setText(cell);
		return row;
	}

	public static Item[] toItemsArray(Parcelable[] parcelables) {
		Item[] items = new Item[parcelables.length];
		for (int i=0; i<items.length; i++) {
			items[i] = (Item)parcelables[i];
		}
		return items;
	}
}

class NameComparator implements Comparator<Item> {
	@Override
	public int compare(Item o1, Item o2) {
		return o1.getTitle().compareTo(o2.getTitle());
	}
}

class PriceComparator implements Comparator<Item> {
	@Override
	public int compare(Item o1, Item o2) {
		return Double.compare(o1.getPrice().getValue(), o2.getPrice().getValue());
	}
}

class DaysLeftComparator implements Comparator<Item> {
	@Override
	public int compare(Item o1, Item o2) {
		return o1.getDaysLeft() - o2.getDaysLeft();
	}
}