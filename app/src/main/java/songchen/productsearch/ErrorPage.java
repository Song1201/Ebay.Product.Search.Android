package songchen.productsearch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ErrorPage extends Fragment {
	private static final String MESSAGE = "message";

	public static ErrorPage newInstance(String message) {
		Bundle args = new Bundle();
		args.putString(MESSAGE, message);

		ErrorPage fragment = new ErrorPage();
		fragment.setArguments(args);
		return fragment;
	}


	public ErrorPage() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_error_page, container, false);
		((TextView)view.findViewById(R.id.errorPageMsg)).setText(getArguments().getString(MESSAGE));
		return view;
	}

}
