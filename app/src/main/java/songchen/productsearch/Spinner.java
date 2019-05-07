package songchen.productsearch;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Spinner extends Fragment {
	private static final String MESSAGE = "message";
	private String message;

	public Spinner() {
		// Required empty public constructor
	}

	public static Spinner newInstance(String message) {
		Bundle args = new Bundle();
		args.putString(MESSAGE, message);
		
		Spinner spinner = new Spinner();
		spinner.setArguments(args);
		return spinner;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			message = args.getString(MESSAGE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_spinner, container, false);
		((TextView)view.findViewById(R.id.message)).setText(message);
		return view;
	}

}
