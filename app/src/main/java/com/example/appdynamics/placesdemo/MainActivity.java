package com.example.appdynamics.placesdemo;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.api.services.civicinfo.model.GeographicDivision;
import com.google.api.services.civicinfo.model.RepresentativeInfoResponse;

import java.util.Collection;
import java.util.Map;


public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private GoogleGeoInfo.Location mLocation;
        private GoogleGeoInfo.Place mPlace;
        private EditText mAddress;
        private Button mButton;
        private TextView mLatitude;
        private TextView mLongitude;
        private TextView mElevation;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            // Get address to search on and do geo-coding
            mAddress = (EditText) rootView.findViewById(R.id.editText);
            mButton = (Button) rootView.findViewById(R.id.button);
            mLatitude = (TextView) rootView.findViewById(R.id.text_lat);
            mLongitude = (TextView) rootView.findViewById(R.id.text_lng);
            mElevation = (TextView) rootView.findViewById(R.id.elevation);

            // Get Elevation and Civic Info data when user enters search address
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String searchAddress = mAddress.getText().toString();
                    Log.d(TAG, "searchAddress: " + searchAddress);
                    if (! searchAddress.isEmpty()) {
                        try {
                            GoogleGeoInfo geoInfo = new GoogleGeoInfo() {
                                @Override
                                public void onGeocodeSuccess(PlaceResult places) {
                                    mPlace = places.getResults()[0];
                                    mLocation = mPlace.getGeometry().getLocation();

                                    Log.d(TAG, "Lat/Lng: " + mLocation.getLat() + "," + mLocation.getLng());

                                    mLatitude.setText(" " + mLocation.getLat().toPlainString());
                                    mLongitude.setText(" " + mLocation.getLng().toPlainString());

                                    // Get elevation from Elevation API
                                    getElevationInfo(mLocation);

                                    // Get civic information from Civic Information API
                                    GoogleCivicInfo civicInfo = new GoogleCivicInfo() {
                                        @Override
                                        public void onSuccess(RepresentativeInfoResponse response) {
                                            Map<String, GeographicDivision> divisionMap = response.getDivisions();
                                            Collection<GeographicDivision> divisions = divisionMap.values();

                                            Log.d(TAG, "Found " + divisions.size() + " divisions");
                                            for (GeographicDivision division : divisions)
                                                Log.d(TAG, division.getName());
                                        }

                                        @Override
                                        public void onFailure() {
                                            Log.d(TAG, "civicInfo.getInfo() failed");
                                        }
                                    };
                                    civicInfo.getInfo(mPlace.getFormatted_address());
                                }

                                @Override
                                public void onGeocodeFailure(String status) {
                                    Log.e(TAG, "Error: " + status);
                                }

                                @Override
                                public void onElevationSuccess(ElevationResult elevation) {
                                    Log.d(TAG, "Elevation: " + elevation.getResults()[0].getElevation().toPlainString());
                                    mElevation.setText(" " + elevation.getResults()[0].getElevation().toPlainString());
                                }

                                @Override
                                public void onElevationFailure(String status) {
                                    Log.d(TAG, "Error: " + status);

                                }
                            };
                            geoInfo.getGeocodeInfo(searchAddress);
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            return rootView;
        }
    }
}
