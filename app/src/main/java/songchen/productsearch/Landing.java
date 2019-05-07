package songchen.productsearch;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Landing extends AppCompatActivity {
	private ViewPager viewPager;
	private LandingPagerAdapter landingPagerAdapter;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landing);
		viewPager = findViewById(R.id.pager);
		landingPagerAdapter = new LandingPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(landingPagerAdapter);

		TabLayout tabLayout = findViewById(R.id.tab_layout);
		tabLayout.setupWithViewPager(viewPager);

	}

}
