package songchen.productsearch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URLEncoder;
import java.util.List;

public class SimilarItemAdapter extends RecyclerView.Adapter<SimilarItemAdapter.itemRowViewHolder> {
	private Context context;
	private List<Item> items;

	public SimilarItemAdapter(Context context, List<Item> items) {
		this.context = context;
		this.items = items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public itemRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_row, viewGroup, false);
		return new SimilarItemAdapter.itemRowViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull itemRowViewHolder holder, int i) {
		Picasso.with(context).load(items.get(i).getImageUrl()).into(holder.image);
		holder.title.setText(items.get(i).getTitle());
		holder.shipping.setText(items.get(i).getShipping().getCost());
		holder.daysLeft.setText(items.get(i).getDaysLeft() + (items.get(i).getDaysLeft() > 1? " Days" +
			" Left" : " Day Left"));
		holder.price.setText(items.get(i).getPrice().toString());
		final Item clickedItem = items.get(i);
		holder.itemRow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println(clickedItem.getViewItemUrl());
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickedItem.getViewItemUrl()));
				context.startActivity(browserIntent);
			}
		});
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	class itemRowViewHolder extends RecyclerView.ViewHolder {
		private ImageView image;
		private TextView title;
		private TextView shipping;
		private TextView daysLeft;
		private TextView price;
		private CardView itemRow;

		public itemRowViewHolder(@NonNull View itemView) {
			super(itemView);
			image = itemView.findViewById(R.id.itemRowImage);
			title = itemView.findViewById(R.id.itemRowTitle);
			shipping = itemView.findViewById(R.id.itemRowShipping);
			daysLeft = itemView.findViewById(R.id.itemRowDaysLeft);
			price = itemView.findViewById(R.id.itemRowPrice);
			itemRow = itemView.findViewById(R.id.itemRow);
		}
	}

}
