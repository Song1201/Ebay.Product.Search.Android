package songchen.productsearch;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class LandingPagerAdapter extends FragmentStatePagerAdapter {
	public LandingPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		if (i == 0) {
			return new Search();
		} else if (i == 1) {
			return new WishList();
		} else {
			return null;
		}
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		if (position == 0) {
			return "Search";
		} else if (position == 1) {
			return "Wish List";
		} else {
			return null;
		}
	}
}
