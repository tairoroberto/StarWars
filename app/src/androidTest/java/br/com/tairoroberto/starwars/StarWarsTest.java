package br.com.tairoroberto.starwars;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.ActivityCompat;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.tairoroberto.starwars.principal.HomeActivity;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class StarWarsTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void testGetSharedPreferences() {
        SharedPreferences preferences = mActivityRule.getActivity().getSharedPreferences("QR_STAR_WARS", Context.MODE_PRIVATE);
        Assert.assertNotNull(preferences);
    }

    @Test
    public void testGetSharedEditor() {
        SharedPreferences preferences = mActivityRule.getActivity().getSharedPreferences("QR_STAR_WARS", Context.MODE_PRIVATE);
        Assert.assertNotNull(preferences.edit());
    }

    @Test
    public void testIsConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) mActivityRule.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = null;
        if (cm != null) {
            info = cm.getActiveNetworkInfo();
        }

        Assert.assertTrue(info != null);
    }

    @Test
    public void testGetLocationService() {
        LocationManager lm = (LocationManager) mActivityRule.getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mActivityRule.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivityRule.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Assert.assertNotNull(null);
        }
        Assert.assertNotNull(lm);
    }

    @Test
    public void testeGetUserName() {
        Cursor c = mActivityRule.getActivity().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            String name = c.getString(c.getColumnIndex("display_name"));
            c.close();
            Assert.assertNotNull(name);
        }
    }
}

