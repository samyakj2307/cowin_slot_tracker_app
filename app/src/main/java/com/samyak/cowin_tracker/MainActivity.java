package com.samyak.cowin_tracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements PincodeDataAdapter.PincodeDataRecyclerAdapterOnClickHandler {

    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;

    private static final String TAG = "MainActivity";
    private String mUsername;
    private String firebaseMessagingAccessToken;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference, mTrackPincodesDatabaseReference;
    private ChildEventListener mChildUserPincodeChangeEventListener;

    private FloatingActionButton addPincodeFloatingButton;
    private PopupWindow popupWindow;
    private Context context;
    private RelativeLayout mainLayout;
    private Button addPincodeButton;

    private RecyclerView pincodeRecyclerView;
    private LinearLayoutManager layoutManager;
    private PincodeDataAdapter pincodeDataAdapter;

    private TextView removableAddPincodeText, removableNoPincodeText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = ANONYMOUS;

        context = getApplicationContext();
        //Firebase Instances
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        // Database References
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");
        mTrackPincodesDatabaseReference = mFirebaseDatabase.getReference().child("Track Pin Codes");

        removableAddPincodeText = findViewById(R.id.add_pincodeText_removable);
        removableNoPincodeText = findViewById(R.id.no_pincodeText_removable);


        addPincodeFloatingButton = findViewById(R.id.addPincode);

        mainLayout = findViewById(R.id.main_layout);

        pincodeRecyclerView = findViewById(R.id.pincodeRecyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        pincodeRecyclerView.setLayoutManager(layoutManager);
        pincodeRecyclerView.setHasFixedSize(true);
        pincodeDataAdapter = new PincodeDataAdapter(this);
        pincodeDataAdapter.setPincodeData(new ArrayList<PincodeData>());
        pincodeRecyclerView.setAdapter(pincodeDataAdapter);


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
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso("IN").build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()
                                    ))
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
                popupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
                popupWindow.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);

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


                        if (pincode.equals("") || pincode.length() != 6) {
                            Toast.makeText(MainActivity.this, "Enter a valid Pin code", Toast.LENGTH_SHORT).show();
                        } else if (selected_radio_button_id == -1) {
                            Toast.makeText(MainActivity.this, "Please Select a Slot to Track", Toast.LENGTH_SHORT).show();
                        } else {
                            String userId = Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid();
                            RadioButton radioButton = popUpView.findViewById(selected_radio_button_id);
                            String radioText = radioButton.getText().toString();

                            //TODO change access tokens of a userid whenever a user reinstalls the app

                            switch (radioText) {
                                case "18+ Slots":
                                    mTrackPincodesDatabaseReference.child(pincode).child("users").child("is_18_plus").child(userId).setValue(firebaseMessagingAccessToken);
                                    mTrackPincodesDatabaseReference.child(pincode).child("users").child("is_45_plus").child(userId).removeValue();
                                    mTrackPincodesDatabaseReference.child(pincode).child("users").child("is_all").child(userId).removeValue();
                                    mUsersDatabaseReference.child(userId).child("Pincodes").child(pincode).setValue("is_18_plus");
                                    break;
                                case "45+ Slots":
                                    mTrackPincodesDatabaseReference.child(pincode).child("users").child("is_45_plus").child(userId).setValue(firebaseMessagingAccessToken);
                                    mTrackPincodesDatabaseReference.child(pincode).child("users").child("is_18_plus").child(userId).removeValue();
                                    mTrackPincodesDatabaseReference.child(pincode).child("users").child("is_all").child(userId).removeValue();
                                    mUsersDatabaseReference.child(userId).child("Pincodes").child(pincode).setValue("is_45_plus");
                                    break;
                                case "Both":
                                    mTrackPincodesDatabaseReference.child(pincode).child("users").child("is_all").child(userId).setValue(firebaseMessagingAccessToken);
                                    mTrackPincodesDatabaseReference.child(pincode).child("users").child("is_18_plus").child(userId).removeValue();
                                    mTrackPincodesDatabaseReference.child(pincode).child("users").child("is_45_plus").child(userId).removeValue();
                                    mUsersDatabaseReference.child(userId).child("Pincodes").child(pincode).setValue("is_all");
                                    break;
                            }
                            popupWindow.dismiss();
                            Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            String text = "You will be notified as soon as Slot is available";
                            View main = findViewById(R.id.main_layout);
                            Snackbar.make(main, text, Snackbar.LENGTH_INDEFINITE).setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            }).setActionTextColor(Color.parseColor("#445CD9"))

                                    .show();
                        }
                    }
                });

                closePopUpImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.setFocusable(true);
                popupWindow.update();
                popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

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
//                mUsersDatabaseReference.child(userId).child("Phone Number").setValue(phoneNumber);
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

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().get("date") != null) {
                String date = getIntent().getExtras().get("date").toString();
                String pincode = getIntent().getExtras().get("pincode").toString();
                String slottype = getIntent().getExtras().get("slot_type").toString();
                makeNotificationPopUp(date, pincode, slottype);
                Log.d(TAG, date + pincode + slottype);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void attachDatabaseReadListener() {
        if (mChildUserPincodeChangeEventListener == null) {
            mChildUserPincodeChangeEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Log.d("Hello", "OnChild AddedCalled");
                    String pincode = snapshot.getKey();
                    String slot_tracking = Objects.requireNonNull(snapshot.getValue()).toString();
                    PincodeData pincodeData = new PincodeData(pincode, slot_tracking);
                    ArrayList<PincodeData> pincodeDataArrayList = pincodeDataAdapter.getPincodeData();
                    pincodeDataArrayList.add(pincodeData);
                    if (pincodeDataArrayList.size() == 0) {
                        removableAddPincodeText.setVisibility(View.VISIBLE);
                        removableNoPincodeText.setVisibility(View.VISIBLE);
                    } else {
                        removableAddPincodeText.setVisibility(View.INVISIBLE);
                        removableNoPincodeText.setVisibility(View.INVISIBLE);
                    }
                    pincodeDataAdapter.setPincodeData(pincodeDataArrayList);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Log.d("Hello", "OnChild ChangedCalled");

                    String pincode = snapshot.getKey();
                    Log.d("Hello", pincode);
                    String slot_tracking = Objects.requireNonNull(snapshot.getValue()).toString();
                    Log.d("Hello", slot_tracking);

                    ArrayList<PincodeData> pincodeDataArrayList = pincodeDataAdapter.getPincodeData();

                    for (PincodeData tempPincodeData : pincodeDataArrayList) {
                        if (tempPincodeData.getPincode().equals(pincode)) {
                            int index = pincodeDataArrayList.indexOf(tempPincodeData);
                            PincodeData pincodeData = new PincodeData(pincode, slot_tracking);
                            pincodeDataArrayList.remove(index);
                            pincodeDataArrayList.add(index, pincodeData);
                            break;
                        }
                    }

                    pincodeDataAdapter.setPincodeData(pincodeDataArrayList);

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Log.d("Hello", "OnChild RemovedCalled");

                    String pincode = snapshot.getKey();

                    ArrayList<PincodeData> pincodeDataArrayList = pincodeDataAdapter.getPincodeData();

                    for (PincodeData tempPincodeData : pincodeDataArrayList) {
                        if (tempPincodeData.getPincode().equals(pincode)) {
                            pincodeDataArrayList.remove(tempPincodeData);
                            break;
                        }
                    }

                    if (pincodeDataArrayList.size() == 0) {
                        removableAddPincodeText.setVisibility(View.VISIBLE);
                        removableNoPincodeText.setVisibility(View.VISIBLE);
                    } else {
                        removableAddPincodeText.setVisibility(View.INVISIBLE);
                        removableNoPincodeText.setVisibility(View.INVISIBLE);
                    }

                    pincodeDataAdapter.setPincodeData(pincodeDataArrayList);

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Log.d("Hello", "OnChild MovedCalled");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Hello", "OnCancelled Called");

                }
            };
            if (mFirebaseAuth.getCurrentUser() != null) {
                String uid = mFirebaseAuth.getCurrentUser().getUid();
                mFirebaseDatabase.getReference().child("users").child(uid).child("Pincodes").addChildEventListener(mChildUserPincodeChangeEventListener);
            }
        }

    }

    private void detachDatabaseReadListener() {
        if (mChildUserPincodeChangeEventListener != null) {
            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                String uid = firebaseUser.getUid();
                mFirebaseDatabase.getReference().child("users").child(uid).child("Pincodes").removeEventListener(mChildUserPincodeChangeEventListener);
            }
            mChildUserPincodeChangeEventListener = null;
        }
    }


    public void makeNotificationPopUp(String date, String pincode, String slottype) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        View popUpView = inflater.inflate(R.layout.notification_popup, null);


        popupWindow = new PopupWindow(popUpView);
        popupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);

        popupWindow.setElevation(5.0f);

        ImageButton closePopUpImageButton = popUpView.findViewById(R.id.closeNotificationPopupButton);

        Button bookSlotButton = popUpView.findViewById(R.id.book_slot_button);

        TextView slottypeicon_text_view = popUpView.findViewById(R.id.notification_tracking_slot_text);
        TextView pincode_text_view = popUpView.findViewById(R.id.notification_pincode_text);
        TextView date_text_view = popUpView.findViewById(R.id.on_date_available_text);
        TextView slottype_text_view = popUpView.findViewById(R.id.slot_type_available_text);

        pincode_text_view.setText(pincode);
        date_text_view.setText(date);
        slottype_text_view.setText(slottype);
        slottypeicon_text_view.setText(slottype);

        bookSlotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri cowinWebsite = Uri.parse("https://selfregistration.cowin.gov.in/");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, cowinWebsite);
                startActivity(browserIntent);
                popupWindow.dismiss();

            }
        });


        closePopUpImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


        popupWindow.setFocusable(true);
        popupWindow.update();
        findViewById(R.id.main_layout).post(new Runnable() {
            public void run() {
                popupWindow.showAtLocation(findViewById(R.id.main_layout), Gravity.CENTER, 0, 0);
            }
        });

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
        attachDatabaseReadListener();
    }

    private void OnSignedOutCleanup() {
        mUsername = ANONYMOUS;
        firebaseMessagingAccessToken = "";
        detachDatabaseReadListener();
    }


    @Override
    public void OnClick(String pincode) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        View popUpView = inflater.inflate(R.layout.remove_pincode_popup, null);


        popupWindow = new PopupWindow(popUpView);
        popupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);

        popupWindow.setElevation(5.0f);

        ImageButton closePopUpImageButton = popUpView.findViewById(R.id.closeRemovePopUpPopupButton);

        Button removePincodeButton = popUpView.findViewById(R.id.remove_pincode_button);

        TextView pincode_text_view = popUpView.findViewById(R.id.stop_pincode_number);

        pincode_text_view.setText(pincode);

        String userId = Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid();

        removePincodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTrackPincodesDatabaseReference.child(pincode).child("users").child("is_18_plus").child(userId).removeValue();
                mTrackPincodesDatabaseReference.child(pincode).child("users").child("is_45_plus").child(userId).removeValue();
                mTrackPincodesDatabaseReference.child(pincode).child("users").child("is_all").child(userId).removeValue();
                mUsersDatabaseReference.child(userId).child("Pincodes").child(pincode).removeValue();

                popupWindow.dismiss();

            }
        });


        closePopUpImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.showAtLocation(findViewById(R.id.main_layout), Gravity.CENTER, 0, 0);

    }
}