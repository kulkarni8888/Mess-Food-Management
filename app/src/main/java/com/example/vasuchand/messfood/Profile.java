package com.example.vasuchand.messfood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Toolbar toolbar;
    Context context = Profile.this;
    TextView t1, t2, t3, t4;
    EditText e1, e2;
    Button b1, b2;
    private ProgressDialog progrssDialog;
    DatabaseReference databaseReference;
    Session session;
    String id = "";
    RadioGroup radioGroup;
    String menu = "";
    couponleft post;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initToolbar();
        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        t3 = (TextView) findViewById(R.id.t3);
        t4 = (TextView) findViewById(R.id.t4);
        e1 = (EditText) findViewById(R.id.e1);
        e2 = (EditText) findViewById(R.id.e2);
        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        radioGroup = (RadioGroup) findViewById(R.id.sharecoupon);
        session = new Session(this);
        id = session.getPreferences(this, config.userid);
        System.out.println(id);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference(config.coupon).child(id);

        progrssDialog = new ProgressDialog(this);
        progrssDialog.setMessage("Please Wait....");
        loaddata();


    }

    public void loaddata() {
        progrssDialog.show();
        loadallcoupon();


    }

    public void loadallcoupon() {
        DatabaseReference ref = databaseReference;

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());

                post = snapshot.getValue(couponleft.class);
                Log.e("Get Data", String.valueOf(post.getBreakFast()));
                t1.setText("BreakFast    " + String.valueOf(post.getBreakFast()));
                t2.setText("Lunch    " + String.valueOf(post.getLunch()));
                t3.setText("Snack    " + String.valueOf(post.getSnack()));
                t4.setText("Dinner    " + String.valueOf(post.getDinner()));
                progrssDialog.dismiss();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
    }


    public void Signout() {
        firebaseAuth.signOut();
        progrssDialog.dismiss();
        finish();
        Intent myIntent = new Intent(Profile.this, Signin.class);
        Profile.this.startActivity(myIntent);
    }

    public boolean checkboolean() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            // no radio buttons are checked
            return false;
        } else {
            return true;
            // one of the radio buttons is checked
        }
    }

    public void increasedcoupons(final String id, final int number)
    {

        DatabaseReference refernce = FirebaseDatabase.getInstance().getReference(config.coupon);


        DatabaseReference ref = refernce.child(id).child(menu);

        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                if (currentData.getValue() == null || (Long) currentData.getValue() == 0) {

                    currentData.setValue(number);
                } else {
                    currentData.setValue((Long) currentData.getValue() + number);
                }
                // Toast.makeText(context, "Successfully Added Coupons to your friend !", Toast.LENGTH_SHORT).show();
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                //sendNotification(id);
                gettoken(id);

                Toast.makeText(context, "Successfully Added Coupons to your friend !", Toast.LENGTH_SHORT).show();

            }


        });

    }

    public void decreasecoupons(String menu, final String ids, final int number) {


        DatabaseReference Reference = FirebaseDatabase.getInstance().getReference(config.coupon);

        String id = session.getPreferences(context, config.userid);

        DatabaseReference ref = Reference.child(id).child(menu);


        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                if (currentData.getValue() == null || (Long) currentData.getValue() == 0) {
                    Toast.makeText(context, "You don't Have enough coupons to share", Toast.LENGTH_SHORT).show();
                } else {
                    currentData.setValue((Long) currentData.getValue() - number);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                increasedcoupons(ids, number);
            }


        });

    }


    public int leftcoupon() {
        if (menu.equals("breakFast")) {
            return (int) post.getBreakFast();
        } else if (menu.equals("lunch")) {
            return (int) post.getLunch();
        } else if (menu.equals("snack")) {
            return (int) post.getSnack();
        } else
            return (int) post.getDinner();

    }


    public void gettoken(final String senderid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(config.tokencolum);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                int flag = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    System.out.println("Im here" + child.getValue());
                    Map<String, String> model = (Map<String, String>) child.getValue();

                    if (model.get("id").equals(senderid)) {

                        String id = model.get("token");
                        String email = model.get("email");
                        sendNotification(id,email);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void sendNotification(final String reg_token, final String email) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("body", "IIIT-DELHI MESS");
                    dataJson.put("title", "You have been received Extra " + menu + " Coupons From " +email);
                    json.put("data", dataJson);
                    json.put("to", reg_token);
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=" + config.server_auth)
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                    System.out.println(" " +finalResponse + " AASd");
                } catch (Exception e) {
                    // Log.d(T,e+"");
                }
                return null;
            }
        }.execute();

    }

    @Override
    public void onClick(View view) {
        if (view == b1) {
            final String email = e1.getText().toString();
            String val = e2.getText().toString();

            if (!checkboolean()) {
                Toast.makeText(context, "Select Meal Options First ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(context, "Please Fill the Email Address", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(val)) {
                Toast.makeText(context, "Please Fill the Number of Coupons", Toast.LENGTH_SHORT).show();
                return;
            }
            final int a = Integer.parseInt(val);
            progrssDialog.show();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(config.users);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    int flag = 0;
                    for (DataSnapshot child : snapshot.getChildren()) {
                        System.out.println("Im here" + child.getValue());
                        Map<String, String> model = (Map<String, String>) child.getValue();

                        if (model.get("email").equals(email)) {
                            int b = leftcoupon();
                            String id = model.get("id");
                            System.out.println(a + "  " + b);
                            flag = 1;
                            if (a >= b) {

                                Toast.makeText(context, "Coupons number must be less than your available coupons", Toast.LENGTH_SHORT).show();
                                progrssDialog.dismiss();
                            } else
                                decreasecoupons(menu, id, a);
                            break;
                        }
                    }
                    if (flag == 0) {
                        Toast.makeText(context, "User does not exist Please Use different email address", Toast.LENGTH_SHORT).show();
                        progrssDialog.dismiss();
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Toast.makeText(context, "Something Wrong Try Later !", Toast.LENGTH_SHORT).show();
                    progrssDialog.dismiss();
                }


            });


//            if(userexist(email))
//                System.out.println(" !!!!! ****User Exist**** !!!!!");
//            else
//            {
//                Toast.makeText(context, "User does not exist Please Use different email address", Toast.LENGTH_SHORT).show();
//            }

        } else if (view == b2) {
            //progrssDialog.show();
            Signout();
        }


    }

    public void radioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // This check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioButton1:
                if (checked)
                    //Do something when radio button is clicked
                    Toast.makeText(getApplicationContext(), "BreakFast is Selected", Toast.LENGTH_SHORT).show();
                menu = "breakFast";
                break;

            case R.id.radioButton2:
                //Do something when radio button is clicked
                Toast.makeText(getApplicationContext(), "Lunch is Selected", Toast.LENGTH_SHORT).show();
                menu = "lunch";
                break;

            case R.id.radioButton3:
                //Do something when radio button is clicked
                Toast.makeText(getApplicationContext(), "Snack is Selected", Toast.LENGTH_SHORT).show();
                menu = "snack";
                break;

            case R.id.radioButton4:
                //Do something when radio button is clicked
                Toast.makeText(getApplicationContext(), "Dinner is Selected", Toast.LENGTH_SHORT).show();
                menu = "dinner";
                break;


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
