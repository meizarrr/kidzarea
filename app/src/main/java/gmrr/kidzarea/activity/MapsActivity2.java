package gmrr.kidzarea.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

import gmrr.kidzarea.R;
import gmrr.kidzarea.helper.SQLiteHandler;
import gmrr.kidzarea.helper.SQLiteLocationHandler;
import gmrr.kidzarea.helper.SessionManager;

public class MapsActivity2 extends AppCompatActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private SessionManager session;
    private SQLiteHandler db;
    private Lokasi lokasiTerakhir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setContentView(R.layout.activity_maps2);
        setUpMapIfNeeded();

        ImageButton btnViewMyLocation = (ImageButton) findViewById(R.id.btn_ViewMyLocation);
        ImageButton btnAddKid = (ImageButton) findViewById(R.id.btnAddKid);
        Button btnLogout = (Button) findViewById(R.id.btnLogout);

        btnViewMyLocation.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        SetLocationActivity.class);
                startActivity(i);
                finish();
            }
        });


        //just go to register
        btnAddKid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (lokasiTerakhir != null) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteria = new Criteria();
                    Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                    final Lokasi lokasi = new Lokasi(location);
                    // Input nama lokasi
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity2.this);
                    final EditText txtNamaLokasi = new EditText(MapsActivity2.this);
                    builder.setView(txtNamaLokasi).setTitle("Nama Lokasi").setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            lokasi.setName(txtNamaLokasi.getText().toString());
                            simpan(lokasi);
                        }
                    }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create().show();
                }

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    public void simpan(final Lokasi lokasi) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteLocationHandler dbHelper = new SQLiteLocationHandler(MapsActivity2.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("nama", lokasi.getName());
                values.put("longitude", lokasi.getLongitude());
                values.put("latitude", lokasi.getLatitude());
                db.insert("lokasi", null, values);
                MapsActivity2.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MapsActivity2.this, "Lokasi berhasil disimpan!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void logoutUser() {
//        session.setLogin(false);
//
//        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MapsActivity2.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Silakan klik dua kali untuk keluar.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(lokasiTerakhir.getLatitude(), lokasiTerakhir.getLongitude())).title("Marker"));
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        lokasiTerakhir = new Lokasi(location);

        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lokasiTerakhir.getLatitude(), lokasiTerakhir.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(lokasiTerakhir.getLatitude(), lokasiTerakhir.getLongitude()))     // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            // create marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(lokasiTerakhir.getLatitude(), lokasiTerakhir.getLongitude())).title("Anak 1");
            // adding marker
            mMap.addMarker(marker);
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.logokidz_kecil));

            MarkerOptions marker2 = new MarkerOptions().position(new LatLng(lokasiTerakhir.getLatitude()+12, lokasiTerakhir.getLongitude()+20)).title("Anak 2");
            // adding marker
            mMap.addMarker(marker2);
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.logokidz_kecil));
        }

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true); // true to enable
        mMap.getUiSettings().setRotateGesturesEnabled(false);
    }
}
