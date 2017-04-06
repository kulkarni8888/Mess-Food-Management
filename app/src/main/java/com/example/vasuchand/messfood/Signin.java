package com.example.vasuchand.messfood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vasuchand.messfood.fcmnotification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Signin extends AppCompatActivity implements View.OnClickListener {

    EditText e1,e2;
    Button b1;
    TextView t1;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    Context context = Signin.this;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_signup);
        e1  = (EditText)findViewById(R.id.email);
        e2 =  (EditText)findViewById(R.id.password);
        b1 = (Button)findViewById(R.id.b1);
        t1 = (TextView)findViewById(R.id.t1);
        b1.setOnClickListener(this);
        t1.setOnClickListener(this);
        session = new Session(Signin.this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sign In ....");


    }

    private  void userlogin()
    {
        final String email = e1.getText().toString();
        String password = e2.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            // display toast
            Toast.makeText(this,"Please Enter your email",Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please Enter your Password",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {

                            session.setBreakFastT(context,"breakfast",false);
                            session.setDinnerT(context,"dinner",false);
                            session.setSnackT(context,"snack",false);
                            session.setLunchT(context,"lunch",false);

                            savedata(email);


                            //System.out.println("vasu ." +session.isFirstTimeLaunch(context,"breakfast"));
                            //overridePendingTransition(R.anim.animate_left_to_right, R.anim.animate_right_to_left);
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(context, "User Not exist , Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    @Override
    public void onClick(View view) {

        if(view==b1)
        {
            userlogin();
        }
        else if(view ==t1)
        {
            Intent myIntent = new Intent(Signin.this, loginActivity.class);
            Signin.this.startActivity(myIntent);
        }

    }

    public void savedata(final String email)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(config.users);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                progressDialog.dismiss();

                int flag = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    System.out.println("Im here" + child.getValue());
                    Map<String, String> model = (Map<String, String>) child.getValue();
                    if (model.get("email").equals(email)) {
                        String id = model.get("id");
                        session.setuserid(context,config.userid,id);
                        session.setemail(context,config.email,email);
                        token(id,email);

                    }

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void token(String id, String email)
    {
         String token = session.getPreferences(context,config.token);
         DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(config.tokencolum);


        Token tkn= new Token(id,token,email);

        //System.out.println(id + " "  +email);
        databaseReference.child(id).setValue(tkn);
        finish();
        startActivity(new Intent(Signin.this,MainActivity.class));
    }

}
