package com.example.rohit.expensemanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rohit.expensemanager.Model.Data;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private TextView incomeTotalSum;
    private RecyclerView recyclerView;

    private EditText txtAmount,txtNote,txtTitle;
    private Button btnUpdate,btnDelete;

    private String note,title;
    private double amount;

    private String post_key;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_income2, container, false);

        incomeTotalSum = mView.findViewById(R.id.income_txt_result);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

        recyclerView = mView.findViewById(R.id.recycler_id_income);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int totlatvalue = 0;

                for (DataSnapshot mysanapshot:dataSnapshot.getChildren()){

                    Data data=mysanapshot.getValue(Data.class);

                    totlatvalue+=data.getAmount();

                    String stTotalvale=String.valueOf(totlatvalue);

                    incomeTotalSum.setText(stTotalvale+".00");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Data,IncomeFragment.MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, IncomeFragment.MyViewHolder>(
                Data.class,
                R.layout.income_recycler,
                IncomeFragment.MyViewHolder.class,
                mIncomeDatabase
        ) {
            @Override
            protected void populateViewHolder(IncomeFragment.MyViewHolder viewHolder, final Data model, final int position) {
                viewHolder.setDate(model.getDate());
                viewHolder.setAmount(model.getAmount());
                viewHolder.setNote(model.getDesc());
                viewHolder.setType(model.getTitle());
                viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        post_key = getRef(position).getKey();

                        amount = model.getAmount();
                        note = model.getDesc();
                        title = model.getTitle();
                        updateData();
                    }
                });

            }

        };
        Log.i("Hello","After Recycler Adapper");
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        View myView;
        public MyViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
        }

        private void setType(String type)
        {
            TextView mType = myView.findViewById(R.id.type_income);
            mType.setText(type);
        }
        private void setDate(String Date)
        {
            TextView mDate = myView.findViewById(R.id.date_txt_income);
            mDate.setText(Date);
        }
        private void setAmount(Double amount)
        {
            TextView mIncome = myView.findViewById(R.id.amount_txt_income);
            mIncome.setText(amount.toString());
        }
        private void setNote(String note)
        {
            TextView mNote = myView.findViewById(R.id.note_income);
            mNote.setText(note);
        }
    }

    private void updateData()
    {
        final AlertDialog.Builder mDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.update_data_layout,null);
        mDialog.setView(v);
        final AlertDialog dlg = mDialog.create();

        txtAmount = v.findViewById(R.id.upd_txt_amount);
        txtNote = v.findViewById(R.id.update_txt_note);
        txtTitle = v.findViewById(R.id.upd_txt_title);

        txtAmount.setText(String.valueOf(amount));
        txtAmount.setSelection(String.valueOf(amount).length());
        txtNote.setText(note);
        txtNote.setSelection(note.length());
        txtTitle.setText(title);
        txtTitle.setSelection(title.length());

        btnDelete = v.findViewById(R.id.update_btn_delete);
        btnUpdate = v.findViewById(R.id.update_btn_upd);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = txtTitle.getText().toString().trim();
                note = txtNote.getText().toString().trim();
                amount = Double.parseDouble(txtAmount.getText().toString().trim());
                String mDate = DateFormat.getDateInstance().format(new Date());
                Data d = new Data(amount,title,note,post_key,mDate);
                mIncomeDatabase.child(post_key).setValue(d);

                dlg.dismiss();

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIncomeDatabase.child(post_key).removeValue();
                dlg.dismiss();

            }
        });
        dlg.show();
    }
}
