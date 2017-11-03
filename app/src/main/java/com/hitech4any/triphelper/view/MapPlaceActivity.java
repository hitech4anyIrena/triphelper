package com.hitech4any.triphelper.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hitech4any.triphelper.R;
import com.hitech4any.triphelper.model.Place;
import com.hitech4any.triphelper.model.Values;

/**
 * Created by hitech4any on 16/09/2017.
 */

public class MapPlaceActivity extends AppCompatActivity {
    private PlaceMapFragment PMFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map_place);
        Place place = getIntent().getParcelableExtra(Values.PLACE);
        //////Log.e("MAct",place.toString());
        if (savedInstanceState == null) {
            PMFrag = new PlaceMapFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragMapContainer, PMFrag)
                    .commit();
            PMFrag.setPlace(place);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
}
