package com.samyak.cowin_tracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Collections;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;

    private static final String TAG = "MainActivity";
    private String mUsername;
    private String firebaseMessagingAccessToken;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference, mTrackPincodesDatabaseReference;

    private FloatingActionButton addPincodeFloatingButton;
    private PopupWindow popupWindow;
    private Context context;
    private RelativeLayout mainLayout;
    private Button addPincodeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = ANONYMOUS;

        context = getApplicationContext();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");
        mTrackPincodesDatabaseReference = mFirebaseDatabase.getReference().child("Track Pin Codes");

        addPincodeFloatingButton = findViewById(R.id.addPincode);

        mainLayout = findViewById(R.id.main_layout);


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {
                    OnSignedInInitialize(firebaseUser.getDisplayName());
                } else {
                    OnSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Collections.singletonList(
                                            new AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso("IN").build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        addPincodeFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                View popUpView = inflater.inflate(R.layout.pincode_pop_up, null);


                popupWindow = new PopupWindow(popUpView);
                popupWindow.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);

                popupWindow.setElevation(5.0f);


                addPincodeButton = popUpView.findViewById(R.id.addPincodeButton);
                ImageButton closePopUpImageButton = popUpView.findViewById(R.id.closePopupButton);

                EditText pincodeInput = popUpView.findViewById(R.id.pincodeInput);
                RadioGroup radioGroup = popUpView.findViewById(R.id.radioGroup);

                addPincodeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pincode = pincodeInput.getText().toString();
                        int selected_radio_button_id = radioGroup.getCheckedRadioButtonId();


                        if (pincode.equals("")) {
                            Toast.makeText(MainActivity.this, "Enter a valid Pin code", Toast.LENGTH_SHORT).show();
                        } else if (selected_radio_button_id == -1) {
                            Toast.makeText(MainActivity.this, "Please Select a Slot to Track", Toast.LENGTH_SHORT).show();
                        } else {
                            String userId = Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid();
                            RadioButton radioButton = popUpView.findViewById(selected_radio_button_id);
                            String radioText = radioButton.getText().toString();

                            switch (radioText) {
                                case "18+ Slots":
                                    mTrackPincodesDatabaseReference.child(pincode).child("is_18_plus").child(userId).setValue(firebaseMessagingAccessToken);
                                    mTrackPincodesDatabaseReference.child(pincode).child("is_45_plus").child(userId).removeValue();
                                    mTrackPincodesDatabaseReference.child(pincode).child("is_all").child(userId).removeValue();
                                    mUsersDatabaseReference.child(userId).child(pincode).setValue("is_18_plus");
                                    break;
                                case "45+ Slots":
                                    mTrackPincodesDatabaseReference.child(pincode).child("is_45_plus").child(userId).setValue(firebaseMessagingAccessToken);
                                    mTrackPincodesDatabaseReference.child(pincode).child("is_18_plus").child(userId).removeValue();
                                    mTrackPincodesDatabaseReference.child(pincode).child("is_all").child(userId).removeValue();
                                    mUsersDatabaseReference.child(userId).child(pincode).setValue("is_45_plus");
                                    break;
                                case "Both":
                                    mTrackPincodesDatabaseReference.child(pincode).child("is_all").child(userId).setValue(firebaseMessagingAccessToken);
                                    mTrackPincodesDatabaseReference.child(pincode).child("is_18_plus").child(userId).removeValue();
                                    mTrackPincodesDatabaseReference.child(pincode).child("is_45_plus").child(userId).removeValue();
                                    mUsersDatabaseReference.child(userId).child(pincode).setValue("is_all");
                                    break;
                            }
                            popupWindow.dismiss();
                        }
                    }
                });

                closePopUpImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });


                popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
                popupWindow.setFocusable(true);
                popupWindow.update();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.sign_out_menu) {
            AuthUI.getInstance().signOut(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
//
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                String userId = Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid();
                String phoneNumber = mFirebaseAuth.getCurrentUser().getPhoneNumber();
                mUsersDatabaseReference.child(userId).child("Phone Number").setValue(phoneNumber);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Sign in Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void OnSignedInInitialize(String username) {
        mUsername = username;
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                    return;
                }
                firebaseMessagingAccessToken = task.getResult();
            }
        });
    }

    private void OnSignedOutCleanup() {
        mUsername = ANONYMOUS;
        firebaseMessagingAccessToken = "";
    }

}