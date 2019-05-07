package songchen.productsearch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailPhoto extends Fragment {
	private static final String PHOTOS = "photos";

	public static DetailPhoto newInstance(String[] photos) {
		Bundle args = new Bundle();
		args.putStringArray(PHOTOS, photos);

		DetailPhoto fragment = new DetailPhoto();
		fragment.setArguments(args);
		return fragment;
	}

	public DetailPhoto() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_detail_photo, container, false);
		LinearLayout photoContainer = view.findViewById(R.id.detailPhotoContainer);
		String[] photos = getArguments().getStringArray(PHOTOS);
		for (String photoUrl:photos) {
			ImageView photoView = (ImageView) inflater.inflate(R.layout.detail_photo_image, photoContainer, false);
			Picasso.with(getContext()).load(photoUrl).into(photoView);
			photoContainer.addView(photoView);
		}

		return view;
	}

}
