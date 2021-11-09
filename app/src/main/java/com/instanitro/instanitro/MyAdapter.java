package com.instanitro.instanitro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<InstaAccount> instaAccounts;
    private Context context;
    private String usernaame,mainUsername;

    public MyAdapter(Context c, ArrayList<InstaAccount>b){
        context=c;
        instaAccounts=b;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final MyViewHolder vHolder= new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_view_accounts,viewGroup,false));

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myViewHolder.username.setText(instaAccounts.get(i).getUsername());
        mainUsername=instaAccounts.get(i).getUsername();
        myViewHolder.switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainAccount mainAccount=new MainAccount(mainUsername);
                FirebaseDatabase.getInstance().getReference("MainAccount").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(mainAccount);
            }
        });
        usernaame=instaAccounts.get(myViewHolder.getAdapterPosition()).getUsername();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("MainUsername").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MainAccount mainAccount=dataSnapshot.getValue(MainAccount.class);
                mainUsername= Objects.requireNonNull(mainAccount).getMainUsername();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (usernaame.equals(mainUsername)){
            myViewHolder.switchButton.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return instaAccounts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        Button switchButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.insta_username);
            switchButton=itemView.findViewById(R.id.switch_button);
        }
    }
}
