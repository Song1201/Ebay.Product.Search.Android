package songchen.productsearch;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ProductDetailPagerAdapter extends FragmentStatePagerAdapter {
	private Fragment[] fragments = new Fragment[4];
	private Fragment changedPage;
	private FragmentManager fm;

	public ProductDetailPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fm = fm;
		fragments[0] = Spinner.newInstance("Searching Product Details ...");
		fragments[1] = Spinner.newInstance("Searching Shipping ...");
		fragments[2] = Spinner.newInstance("Searching Photos ...");
		fragments[3] = Spinner.newInstance("Searching Similar Items ...");
		changedPage = null;
	}

	@Override
	public Fragment getItem(int i) {
		return fragments[i];
	}

	public void setItem(int i, Fragment fragment) {
		fm.beginTransaction().remove(fragments[i]);
		changedPage = fragments[i];
		fragments[i] = fragment;
		notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(@NonNull Object object) {
		if (changedPage == object) {
			changedPage = null;
			return POSITION_NONE;
		}
		return POSITION_UNCHANGED;
	}

	@Override
	public int getCount() {
		return 4;
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		if (position == 0) {
			return "PRODUCT";
		} else if (position == 1) {
			return "SHIPPING";
		} else if (position == 2) {
			return "PHOTOS";
		} else if (position == 3) {
			return "SIMILAR";
		} else {
			return null;
		}
	}
}
