package id.elfadl.instaapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import id.elfadl.instaapp.R;
import id.elfadl.instaapp.databinding.ActivityMainBinding;
import id.elfadl.instaapp.databinding.CustomTabBinding;
import id.elfadl.instaapp.ui.fragment.BaseFragment;
import id.elfadl.instaapp.ui.fragment.Feed;
import id.elfadl.instaapp.ui.fragment.Profile;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private ViewPagerAdapter adapter;
    private final static int REQUEST_CODE = 3498;
    private Feed feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shouldChangeStatusBarTintToDark = true;
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        feed = new Feed();

        setupViewPager(binding.viewpager);

        binding.tabs.setupWithViewPager(binding.viewpager);

        setupTabIcons();

        ViewGroup vg = (ViewGroup) binding.tabs.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);

            if (j == 1) {
                vgTab.setEnabled(false);
            }
        }

        binding.btnAdd.setOnClickListener(v -> startActivityForResult(new Intent(this, AddPost.class), REQUEST_CODE));

    }

    private void setupTabIcons() {
        binding.tabs.getTabAt(0).setIcon(R.drawable.ic_dashboard);
        binding.tabs.getTabAt(1).setIcon(R.drawable.ic_add);
        binding.tabs.getTabAt(2).setIcon(R.drawable.ic_profile);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(feed, "Beranda");
        adapter.addFragment(new BaseFragment(), "Add Post");
        adapter.addFragment(new Profile(), "Akun");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CODE){
                feed.refresh();
            }
        }
    }
}