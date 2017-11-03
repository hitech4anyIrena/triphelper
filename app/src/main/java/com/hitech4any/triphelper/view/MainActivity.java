package com.hitech4any.triphelper.view;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hitech4any.triphelper.R;
import com.hitech4any.triphelper.controller.PlaceBatteryReceiver;
import com.hitech4any.triphelper.controller.SearchIntentService;
import com.hitech4any.triphelper.interfaces.THAdapterListener;
import com.hitech4any.triphelper.model.Values;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by hitech4any on 18/09/2017.
 */


public class MainActivity extends AppCompatActivity implements THAdapterListener, LocationListener, SearchView.OnQueryTextListener, PlaceBatteryReceiver.OnBatteryChangedListener {

    private Timer timer;
    private boolean gotLocation = false;
    private LocationManager locationManager;
    private String providerName;
    private double locationLat;
    private double locationLon;
    private SharedPreferences sp;
    private int periodicity;
    private int radius;
    private String measure;
    private MenuItem searchMenuItem;
    private SearchView mSearchView;
    private ViewPager pager;
    private SectionsPagerAdapter pagerAdapter;
    private PlaceBatteryReceiver receiver;

    //--- Life cicle of activity methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        //-- ViewPager
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
        //--
        if (savedInstanceState != null) { // NOT running for the first time
            locationLat = savedInstanceState.getDouble(Values.LOCATION_LAT);
            locationLon = savedInstanceState.getDouble(Values.LOCATION_LON);
            gotLocation = savedInstanceState.getBoolean(Values.GOT_LOCATION);
        }
        receiver = new PlaceBatteryReceiver(this);
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
        getProvider();

    }

    @Override
    protected void onStart() {
        super.onStart();
        periodicity = Integer.parseInt((sp.getString(this.getString(R.string.periodicity_key), Values.DIF_PERIODICITY))) * 1000;
        measure = sp.getString(this.getString(R.string.distance_key), Values.DIF_MEASURE);
        radius = Values.getRadius(Double.parseDouble(sp.getString(this.getString(R.string.radius_key), Values.DIF_RADIUS)), measure.equalsIgnoreCase(getResources().getStringArray(R.array.distance)[1]));
        //Log.e("array",getResources().getStringArray(R.array.distance)[1]);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(Values.LOCATION_LAT, locationLat);
        outState.putDouble(Values.LOCATION_LON, locationLon);
        outState.putBoolean(Values.GOT_LOCATION, gotLocation);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    //---Location methods
    private void getProvider() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        providerName = LocationManager.GPS_PROVIDER;
        try {
            locationManager.requestLocationUpdates(providerName, periodicity, Values.LOCATION_UPD_RADIUS, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        timer = new Timer(Values.PROVIDER);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (gotLocation == false) {
                    try {
                        locationManager.removeUpdates(MainActivity.this);
                        providerName = LocationManager.NETWORK_PROVIDER;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    locationManager.requestLocationUpdates(providerName, periodicity, Values.LOCATION_UPD_RADIUS, MainActivity.this);
                                } catch (SecurityException e) {
                                }
                            }
                        });
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        // schedule the timer to run the task after 10 seconds from now
        timer.schedule(task, new Date(System.currentTimeMillis() + Values.TIMER));


    }

    @Override
    public void onLocationChanged(Location location) {
        gotLocation = true;
        timer.cancel();
        locationLat = location.getLatitude();
        locationLon = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
        getProvider();
    }

    //---Menu methods
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        boolean flag = super.onCreateOptionsMenu(menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        return flag;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainMenuSettings:
                Intent intent = new Intent(this, PrefsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!!Values.checkInternetConnection(this, this) || !gotLocation) {
            Toast.makeText(MainActivity.this, R.string.needProvider, Toast.LENGTH_SHORT).show();
            return true;
        }
        Intent intent = new Intent(this, SearchIntentService.class);
        intent.putExtra(Values.KEYWORD, query);
        intent.putExtra(Values.LOCATION_LAT, locationLat);
        intent.putExtra(Values.LOCATION_LON, locationLon);
        intent.putExtra(Values.RADIUS, radius);
        //Log.e("onQueryTextSubmit", getLocationLat()+","+ getLocationLon());
        startService(intent);
        mSearchView.clearFocus();
        mSearchView.setQuery("", false);
        mSearchView.onActionViewCollapsed();
        mSearchView.onActionViewExpanded();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    //---THAdapterListener implementation
    @Override
    public void onClick(int position) {
        int posItem = pager.getCurrentItem();
        switch (posItem) {
            case 0:
            case 2:
                FrameFragment curFrag = (FrameFragment) pagerAdapter.getRegisteredFragment(posItem);
                curFrag.onClick(position, this);
                break;
        }
    }

    @Override
    public void onInfClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {
        int posItem = pager.getCurrentItem();
        switch (posItem) {
            case 0:
            case 2:
                FrameFragment curFrag = (FrameFragment) pagerAdapter.getRegisteredFragment(posItem);
                //Log.e("main", position + "");
                curFrag.remove(position);
                break;
        }
    }

    @Override
    public void onFavoriteClick(int position) {
        int posItem = pager.getCurrentItem();
        switch (posItem) {
            case 0:
                FrameFragment curFrag = (FrameFragment) pagerAdapter.getRegisteredFragment(posItem);
                curFrag.addFv(position);
                break;
        }
    }

    @Override
    public String getMeasure() {
        return measure;
    }

    @Override
    public double getLocationLat() {
        return locationLat;
    }

    @Override
    public double getLocationLon() {
        return locationLon;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public void onRefresh() {
        int posItem = pager.getCurrentItem();
        switch (posItem) {
            case 0:
            case 2:
                FrameFragment curFrag = (FrameFragment) pagerAdapter.getRegisteredFragment(posItem);
                curFrag.refreshList();
                break;
        }
    }

    //---Battery
    @Override
    public void batteryChanged(int info) {
        Toast.makeText(MainActivity.this, info, Toast.LENGTH_SHORT).show();
    }

    //---PagerAdapter

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MainFragment();
                case 1:
                    return new AdvSearchFragment();
                case 2:
                    return new FavoriteFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getText(R.string.list_tab);
                case 1:
                    return getResources().getText(R.string.search_tab);
                case 2:
                    return getResources().getText(R.string.favorite_tab);
            }
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

}