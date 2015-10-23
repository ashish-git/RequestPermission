package com.blogspot.ozastechnologies.requestpermission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    /**
     * mReadImeiText {@link TextView}
     */
    private TextView mReadImeiText;
    /**
     * mLayout {@link View}
     */
    private View mLayout;
    /**
     * The constant for REQUEST_PERMISSIONS_READ_PHONE_STATE
     */
    private final int REQUEST_PERMISSIONS_READ_PHONE_STATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.mLayout);

        mReadImeiText = (TextView) findViewById(R.id.imei);

        Button readImeiButton = (Button) findViewById(R.id.readImeiButton);
        readImeiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.READ_PHONE_STATE)) {

                        Snackbar.make(mLayout, getString(R.string.info_permission_text),
                                Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Request the permission
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.READ_PHONE_STATE},
                                        REQUEST_PERMISSIONS_READ_PHONE_STATE);
                            }
                        }).show();

                    } else {
                        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.READ_PHONE_STATE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    REQUEST_PERMISSIONS_READ_PHONE_STATE);

                        } else {
                            mReadImeiText.setText(getIMEI());

                        }

                    }

                } else {
                    mReadImeiText.setText(getIMEI());

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, read IMEI.
                    mReadImeiText.setText(getIMEI());

                } else {
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_permission_not_granted), Snackbar.LENGTH_LONG).show();

                }
                return;
            }
        }
    }

    /**
     * Used to get the IMEI.
     *
     * @return IMEI
     */
    public String getIMEI() {
        String imei = null;
        String serviceName = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(serviceName);
        if (telephonyManager == null) {
            return getString(R.string.telephone_module_not_available);
        }
        int deviceType = telephonyManager.getPhoneType();
        switch (deviceType) {
            case (TelephonyManager.PHONE_TYPE_GSM):
                break;
            case (TelephonyManager.PHONE_TYPE_CDMA):
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                break;
            default:
                break;
        }
        imei = telephonyManager.getDeviceId();
        return imei;

    }
}
