package com.jnu.booklistapplication;

import android.app.usage.ConfigurationStats;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout; //标签布局
    private ViewPager2 mViewPage; //ViewPaper
    private String[] tabTitles;     //tab的标题
    private List<Fragment> mFragments = new ArrayList<>(); //ViewPage2的Fragment容器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        // ViewPage设置自定义Adapter
        mViewPage.setAdapter(new MyFragmentView(this));
        // TabLayout与ViewPage建立联系的中介TabLayoutMediator
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(mTabLayout, mViewPage,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabTitles[position]);
                    }
                });
        // 设置生效
        tabLayoutMediator.attach();
    }

    private void initView() {
        mTabLayout = findViewById(R.id.tl_tab);
        mViewPage = findViewById(R.id.vp_content);
        tabTitles = new String[]{"图书", "新闻", "卖家"};
        BookFragment bookFragment = new BookFragment();
        WebViewFragment newsFragment = new WebViewFragment();
        MapFragment salerFragment = new MapFragment();
        mFragments.add(bookFragment);
        mFragments.add(newsFragment);
        mFragments.add(salerFragment);
    }

    //自定义的Adapter
    private class MyFragmentView extends FragmentStateAdapter {
        public MyFragmentView(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    /*
    //右上角菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;// true：允许创建的菜单显示出来，false：创建的菜单将无法显示。
    }

    //右上角菜单的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.it_menu_add:
                Intent intent;
                intent = new Intent(BookFragment.newInstance().getActivity(), AddBookActivity.class);
                AddActivityResultLauncher.launch(intent);
                break;
        }
        return true;
    }
    */

}