package nguyenduchai.cse.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import nguyenduchai.cse.com.adapter.AdapterViewGraphView;
import nguyenduchai.cse.com.adapter.AdapterViewPageReport;
import nguyenduchai.cse.com.personalfinancialmanagementv2.R;

public class GraphViewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private FragmentPagerAdapter adapterFragmentPagerAdapter;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_view);
        getControlView();
        setSupportActionBar(toolbar);
        doSetViewPagers();
        getDataFromMain();
        setTitle(title);

    }
    private void doSetViewPagers() {
        adapterFragmentPagerAdapter = new AdapterViewGraphView(getSupportFragmentManager(), getResources().getStringArray(R.array.tab_title_report));
        viewPager.setAdapter(adapterFragmentPagerAdapter);
    }

    private void getDataFromMain() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("DATA");
        title = bundle.getString("TITLE");

    }

    private void getControlView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager)findViewById(R.id.view_pager_report);
    }

}
