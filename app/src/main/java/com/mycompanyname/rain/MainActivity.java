package com.mycompanyname.rain;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.Manifest.permission.CALL_PHONE;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERFORM_CALLS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button permissionButton = (Button) findViewById(R.id.btnPermission);
        Button noPermissionButton = (Button) findViewById(R.id.btnNoPermission);

        permissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mayPerformCalls()) {
                    Toast.makeText(MainActivity.this, R.string.no_permission_granted, Toast.LENGTH_LONG).show();
                    return;
                }

                contactRAIN();
            }
        });

        noPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactRAIN();
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean mayPerformCalls() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (ActivityCompat.checkSelfPermission(this, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (shouldShowRequestPermissionRationale(CALL_PHONE)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.information_message)
                    .setMessage(R.string.permission_call_needed)
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requestPermissions(new String[]{CALL_PHONE}, REQUEST_PERFORM_CALLS);
                        }
                    }).show();
        } else {
            requestPermissions(new String[]{CALL_PHONE}, REQUEST_PERFORM_CALLS);
        }

        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERFORM_CALLS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                contactRAIN();
            }
        }
    }

    private void contactRAIN() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:+50522200596"));

        try {
            startActivity(intent);
        } catch (SecurityException ex) {
            Toast.makeText(MainActivity.this, R.string.security_call, Toast.LENGTH_LONG).show();
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, R.string.unable_perform_calls, Toast.LENGTH_LONG).show();
        }
    }
}
