package songchen.productsearch;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemsGridAdapter extends RecyclerView.Adapter<ItemsGridAdapter.ItemCardHolder> {
	private Context context;
	private List<Item> items;
	private List<Item> wishList;
	private static final int MAX_TITLE_LENGTH = 60;
	private static final int MAX_CONDITION_LENGTH = 15;
	private TextView wishListTotal;
	private TextView wishListNum;

	public ItemsGridAdapter(Context context, TextView wishListTotal, TextView wishListNum) {
		this.context = context;
		wishList = Utils.getWishList(context);
		items = wishList;
		this.wishListTotal = wishListTotal;
		this.wishListNum = wishListNum;
		updateWishListInfo();
	}

	public ItemsGridAdapter(Context context, Item[] items) {
		this.context = context;
		this.items = new ArrayList<>(Arrays.asList(items));
		wishList = Utils.getWishList(context);
	}

	public void onResume() {
		if (wishListTotal != null) { // adapter is used for wish list
			wishList = Utils.getWishList(context);
			items = wishList;
			updateWishListInfo();
			notifyDataSetChanged();
		} else {
			wishList = Utils.getWishList(context);
			notifyDataSetChanged();
		}
	}

	private void updateWishListInfo() {
		wishListNum.setText("Wishlist total (" + wishList.size() + " items):");
		wishListTotal.setText(Utils.getTotalCost(wishList));
	}

	@NonNull
	@Override
	public ItemCardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_card, viewGroup, false);
		return new ItemCardHolder(view);
	}

	private static String shortenString(String str, int thresh) {
    str = str.trim();
		if (str.length() <= thresh) {
			return str;
		}

		for (int i=thresh; i > 0; i--) {
			if (str.charAt(i) == ' ') {
				return str.substring(0, i+1) + "...";
			}
		}
		// can not find a space to break
		return str.substring(0, thresh) + " ...";
	}

	private String zipShippingText(String zip, String shipping) {
		String zipText = "Zip: " + (zip==null? "N/A" : zip);
		return zipText + "\n" + shipping;
	}

	private String foldString(String str, int thresh) {
		str = str.trim();
		if (str.length() <= thresh) {
			return str;
		}

		for (int i=thresh; i > 0; i--) {
			if (str.charAt(i) == ' ') {
				return str.substring(0, i) + "\n" + str.substring(i+1);
			}
		}
		// can not find a space to break
		return str.substring(0, thresh-1) + "-\n" + str.substring(thresh-1);
	}


	@Override
	public void onBindViewHolder(@NonNull ItemCardHolder holder, int i) {
		Picasso.with(context).load(items.get(i).getImageUrl()).into(holder.image);
		holder.title.setText(shortenString(items.get(i).getTitle(), MAX_TITLE_LENGTH));
		holder.zipShipping.setText(zipShippingText(items.get(i).getZip(), items.get(i).getShipping().getCost()));
		holder.condition.setText(foldString(items.get(i).getCondition(), MAX_CONDITION_LENGTH));
		holder.price.setText(items.get(i).getPrice().toString());
		final Item clickedItem = items.get(i);
		if (Utils.inWishList(context, items.get(i))) {
			holder.wishListBtn.setImageResource(R.drawable.ic_cart_remove);
			holder.wishListBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Utils.rmFromWishList(context, clickedItem);
					for (int j=0; j<wishList.size(); j++) {
						if (wishList.get(j).getItemId().equals(clickedItem.getItemId())) {
							wishList.remove(j);
							notifyDataSetChanged();
							if (wishListTotal != null) {
								updateWishListInfo();
								if (wishList.size() == 0) {
									((Landing)context).getSupportFragmentManager().beginTransaction()
										.replace(R.id.wishListItemsGridContainer, ErrorPage.newInstance("No Wishes")).commit();
								}
							}
							return;
						}
					}
				}
			});
		} else {
			holder.wishListBtn.setImageResource(R.drawable.ic_cart_plus);
			holder.wishListBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Utils.addToWishList(context, clickedItem);
					wishList.add(clickedItem);
					notifyDataSetChanged();
				}
			});
		}

		holder.card.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ProductDetail.class);
				intent.putExtra(ProductDetail.ITEM, clickedItem);
				context.startActivity(intent);
			}
		});
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public static class ItemCardHolder extends RecyclerView.ViewHolder {
		private ImageView image;
		private TextView title;
		private TextView zipShipping;
		private TextView condition;
		private TextView price;
		private ImageButton wishListBtn;
		private CardView card;

		public ItemCardHolder(@NonNull View itemView) {
			super(itemView);
			image = itemView.findViewById(R.id.itemCardImage);
			title = itemView.findViewById(R.id.itemCardTitle);
			zipShipping = itemView.findViewById(R.id.itemCardZipShipping);
			condition = itemView.findViewById(R.id.itemCardCondition);
			price = itemView.findViewById(R.id.itemCardPrice);
			wishListBtn = itemView.findViewById(R.id.itemCardWishList);
			card = itemView.findViewById(R.id.itemCard);
		}
	}
}
