package com.instanitro.instanitro;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mDatabase;
    Dialog dialog,dialog_accounts,dialog_instaaccounts,dialog_myinstaaccounts;
    AutoCompleteTextView username,password;
    TextView confirm,cancel,accounts;
    String Username,Password;
    ArrayList<InstaAccount> instaAccounts;
    Button addAccount,viewAccounts;
    MyAdapter adapter;
    private RecyclerView mMyAccountsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        dialog = new Dialog(DashboardActivity.this);
        dialog.setContentView(R.layout.insta_login_dialog);
        dialog_accounts = new Dialog(DashboardActivity.this);
        dialog_accounts.setContentView(R.layout.dialog_accounts);
        dialog_instaaccounts = new Dialog(DashboardActivity.this);
        dialog_instaaccounts.setContentView(R.layout.insta_login_dialog);
        dialog_myinstaaccounts = new Dialog(DashboardActivity.this);
        dialog_myinstaaccounts.setContentView(R.layout.dialog_myaccounts);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        username=dialog.findViewById(R.id.insta_uname);
        password=dialog.findViewById(R.id.insta_password);
        confirm=dialog.findViewById(R.id.confirm_button);
        cancel=dialog.findViewById(R.id.cancel_button);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

        accounts=findViewById(R.id.accounts);
        accounts.setOnClickListener(this);
        addAccount=dialog_accounts.findViewById(R.id.add_account_button);
        viewAccounts=dialog_accounts.findViewById(R.id.view_accounts_button);
        addAccount.setOnClickListener(this);
        viewAccounts.setOnClickListener(this);
        Query accountQuery = FirebaseDatabase.getInstance().getReference().child("Accounts").orderByChild(FirebaseAuth.getInstance().getUid());
        accountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() <= 0) {
                    dialog.show();
                }
                else{
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    instaAccounts=new ArrayList<InstaAccount>();
                    mMyAccountsList=dialog_myinstaaccounts.findViewById(R.id.myAccountsList);
                    mMyAccountsList.setHasFixedSize(true);
                    mMyAccountsList.setLayoutManager(new LinearLayoutManager(DashboardActivity.this,LinearLayoutManager.HORIZONTAL,false));
                    mDatabase= FirebaseDatabase.getInstance().getReference().child("Accounts").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                mDatabase = FirebaseDatabase.getInstance().getReference("Accounts").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                                mDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.getChildrenCount()==0){
                                            dialog.show();
                                        }
                                        else{
                                            //NoBooks.setVisibility(View.GONE);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                                InstaAccount i=dataSnapshot1.getValue(InstaAccount.class);
                                instaAccounts.add(i);
                            }

                            adapter=new MyAdapter(DashboardActivity.this,instaAccounts);
                            mMyAccountsList.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(DashboardActivity.this,"Opsss...Something went wrong.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Query accountQuery = FirebaseDatabase.getInstance().getReference().child("Accounts").orderByChild(FirebaseAuth.getInstance().getUid());
        accountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() <= 0) {
                    dialog.show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.accounts:{
                dialog_accounts.show();
                break;}
            case R.id.auto_follow:
                break;
            case R.id.auto_like:
                break;
            case R.id.auto_unfollow:
                break;
            case R.id.auto_comment:
                break;
            case R.id.dm:
                break;
            case R.id.stats:
                break;
            case R.id.filters:
                break;
            case R.id.cancel_button:
                Query accountQuery = FirebaseDatabase.getInstance().getReference().child("Accounts").orderByChild(FirebaseAuth.getInstance().getUid());
                accountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() <= 0) {
                            finish();
                        }
                        else{
                            dialog.dismiss();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case R.id.confirm_button:
                Username=username.getText().toString();
                Password=password.getText().toString();
                Query accountsQuery = FirebaseDatabase.getInstance().getReference().child("Accounts").orderByChild(FirebaseAuth.getInstance().getUid());
                accountsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() <= 0) {
                            InstaAccount instaAccount = new InstaAccount(Username,Password,FirebaseAuth.getInstance().getUid());
                            FirebaseDatabase.getInstance().getReference("Accounts").child(FirebaseAuth.getInstance().getUid()).child(Username).setValue(instaAccount);
                            MainAccount mainAccount = new MainAccount(Username);
                            FirebaseDatabase.getInstance().getReference("MainUsername").child(FirebaseAuth.getInstance().getUid()).child(Username).setValue(mainAccount);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                    dialog.dismiss();

                break;
            case R.id.add_account_button:
                dialog_accounts.dismiss();
                dialog.show();
                break;
            case R.id.view_accounts_button:
                dialog_accounts.dismiss();
                dialog_myinstaaccounts.show();
                break;

        }
    }
}
