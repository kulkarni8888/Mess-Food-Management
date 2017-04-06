package com.example.vasuchand.messfood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vasuchand.messfood.fcmnotification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import static android.R.attr.value;

/**
 * Created by Vasu Chand on 3/30/2017.
 */

public class loginActivity extends AppCompatActivity implements View.OnClickListener {

    Button b1;
    EditText e1, e2;
    TextView t1;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    Context context = loginActivity.this;
    Session session = new Session(loginActivity.this);
    DatabaseReference databaseReference;
    String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        b1 = (Button) findViewById(R.id.b1);
        e1 = (EditText) findViewById(R.id.email);
        e2 = (EditText) findViewById(R.id.password);
        t1 = (TextView) findViewById(R.id.t1);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        t1.setOnClickListener(this);
        b1.setOnClickListener(this);

    }

    private void Register() {
        final String email = e1.getText().toString();
        String password = e2.getText().toString();

        if (TextUtils.isEmpty(email)) {
            // display toast
            Toast.makeText(this, "Please Enter your email", Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter your Password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();


            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                //user

                                Toast.makeText(loginActivity.this, "You are register successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                session.setBreakFastT(context, config.btrue, false);
                                session.setDinnerT(context, config.dtrue, false);
                                session.setSnackT(context, config.strue, false);
                                session.setLunchT(context, config.ltrue, false);
                                addcoupon(config.coupon,email);

                                //overridePendingTransition(R.anim.animate_left_to_right, R.anim.animate_right_to_left);
                            } else {
                                Toast.makeText(context, "User Already Exist , Try with different Email address !", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });


    }

    @Override
    public void onClick(View view) {

        if (view == t1) {
            finish();
            Intent myIntent = new Intent(loginActivity.this, Signin.class);
            loginActivity.this.startActivity(myIntent);
            //  overridePendingTransition(R.anim.animate_left_to_right, R.anim.animate_right_to_left);
        } else if (view == b1) {
            Register();
        }
        progressDialog.setMessage("Registering ....");
    }

    public void addcoupon(String menu,String email) {

        Log.d("MainActivity","First Time Log in");

        databaseReference = FirebaseDatabase.getInstance().getReference(menu);
         id = databaseReference.push().getKey();
        session.setuserid(context,config.userid,id);
        session.setemail(context,config.email,email);
        couponleft couponleft = new couponleft(email,30, 30, 30, 30);
        databaseReference.child(id).setValue(couponleft);

        adduser(email);
    }

    public void adduser(String email)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference(config.users);

       // couponleft couponleft = new couponleft(email,30, 30, 30, 30);
        Users user = new Users(id,email);
        databaseReference.child(id).setValue(user);

        token(id,email);
        progressDialog.dismiss();


    }

    public void token(String id, String email)
    {
        String token = session.getPreferences(context,config.token);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(config.tokencolum);


        Token tkn= new Token(id,token,email);

        //System.out.println(id + " "  +email);
        databaseReference.child(id).setValue(tkn);
        finish();
        startActivity(new Intent(loginActivity.this,MainActivity.class));
    }

}
