//package io.evolution.downtohang;
//
//import android.app.Activity;
//import android.content.Context;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.widget.TextView;
//
///**
// * Created by michael on 4/9/2016.
// */
//public class gpsActivity extends Activity {
//
//
//    TextView textLat;
//    TextView textLong;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_gps);
//
//        textLat = (TextView)findViewById(R.id.textLat);
//        textLong = (TextView)findViewById(R.id.textLong);
//
//        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//
//        LocationListener ll = new mylocationlistener();
//
//        lm.requestLocationUpdates(lm.GPS_PROVIDER, 0, 0, ll);
//
//
//    }
//
//        class mylocationlistener implements LocationListener{
//
//
//            @Override
//            public void onLocationChanged(Location location) {
//                if(location != null)
//                {
//                    double pLong = location.getLongitude();
//                    double pLat = location.getLatitude();
//
//                    textLat.setText(Double.toString(pLat));
//                    textLong.setText(Double.toString(pLong));
//
//
//                }
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        }
//
//    }
//
//
//}
