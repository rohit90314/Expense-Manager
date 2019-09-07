package com.example.rohit.expensemanager;


import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohit.expensemanager.Model.Data;
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
public class Dashboard_Fragment extends Fragment {
    private FloatingActionButton ft_main_btn;
    private FloatingActionButton ft_income_btn;
    private FloatingActionButton ft_expense_btn;

    private TextView ft_income_txt;
    private TextView ft_expense_txt;
    private Animation Fade_Open;
    private Animation Fade_Close;
    private boolean isOpen = false;

    private TextView income_total_sum_dash;
    private TextView expense_total_sum_dash;


    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;


    private RecyclerView incomeRecycler,expenseRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View myview =inflater.inflate(R.layout.fragment_dashboard_, container, false);
        initialize(myview);
        animate();
        fire();


        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double amount=0;
                for(DataSnapshot snap : dataSnapshot.getChildren())
                {
                    Data d = snap.getValue(Data.class);
                    amount += d.getAmount();
                    income_total_sum_dash.setText(String.valueOf(amount));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double amount=0;
                for(DataSnapshot snap : dataSnapshot.getChildren())
                {
                    Data d = snap.getValue(Data.class);
                    amount += d.getAmount();
                    expense_total_sum_dash.setText(String.valueOf(amount));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        LinearLayoutManager incomeLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        incomeLayoutManager.setStackFromEnd(true);
        incomeLayoutManager.setReverseLayout(true);
        incomeRecycler.setHasFixedSize(true);
        incomeRecycler.setLayoutManager(incomeLayoutManager);

        LinearLayoutManager expenseLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        expenseLayoutManager.setStackFromEnd(true);
        expenseLayoutManager.setReverseLayout(true);
        expenseRecycler.setHasFixedSize(true);
        expenseRecycler.setLayoutManager(expenseLayoutManager);

        return myview;
    }

    public void ftanimate() {
        if (isOpen) {
            ft_expense_btn.startAnimation(Fade_Close);
            ft_expense_txt.startAnimation(Fade_Close);
            ft_income_btn.startAnimation(Fade_Close);
            ft_income_txt.startAnimation(Fade_Close);
            ft_income_txt.setClickable(false);
            ft_income_btn.setClickable(false);
            ft_expense_txt.setClickable(false);
            ft_expense_btn.setClickable(false);
            isOpen = false;
        } else {
            ft_expense_btn.startAnimation(Fade_Open);
            ft_expense_txt.startAnimation(Fade_Open);
            ft_income_btn.startAnimation(Fade_Open);
            ft_income_txt.startAnimation(Fade_Open);
            ft_income_txt.setClickable(true);
            ft_income_btn.setClickable(true);
            ft_expense_txt.setClickable(true);
            ft_expense_btn.setClickable(true);
            isOpen = true;
        }
    }

    public void animate()
    {
        ft_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_data();

                if(isOpen)
                {
                    ft_expense_btn.startAnimation(Fade_Close);
                    ft_expense_txt.startAnimation(Fade_Close);
                    ft_income_btn.startAnimation(Fade_Close);
                    ft_income_txt.startAnimation(Fade_Close);
                    ft_income_txt.setClickable(false);
                    ft_income_btn.setClickable(false);
                    ft_expense_txt.setClickable(false);
                    ft_expense_btn.setClickable(false);
                    isOpen = false;
                }
                else
                {
                    ft_expense_btn.startAnimation(Fade_Open);
                    ft_expense_txt.startAnimation(Fade_Open);
                    ft_income_btn.startAnimation(Fade_Open);
                    ft_income_txt.startAnimation(Fade_Open);
                    ft_income_txt.setClickable(true);
                    ft_income_btn.setClickable(true);
                    ft_expense_txt.setClickable(true);
                    ft_expense_btn.setClickable(true);
                    isOpen = true;
                }
            }
        });

    }

    public void initialize(View myview)
    {
        ft_main_btn = myview.findViewById(R.id.ft_btn);
        ft_expense_btn = myview.findViewById(R.id.ft_btn_exp);
        ft_income_btn = myview.findViewById(R.id.ft_btn_inc);

        ft_income_txt = myview.findViewById(R.id.ft_txt_income);
        ft_expense_txt = myview.findViewById(R.id.ft_txt_expense);

        Fade_Open = AnimationUtils.loadAnimation(getActivity(),R.anim.fadeopen);
        Fade_Close = AnimationUtils.loadAnimation(getActivity(),R.anim.fadeclose);

        income_total_sum_dash = myview.findViewById(R.id.dash_inc_result);
        expense_total_sum_dash =myview.findViewById(R.id.dash_exp_result);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        incomeRecycler = myview.findViewById(R.id.recycler_dash_income);
        expenseRecycler = myview.findViewById(R.id.recycler_dash_expense);

    }

    public void fire()
    {
        FirebaseRecyclerAdapter<Data,incomeViewHolder> inc_adapter = new FirebaseRecyclerAdapter<Data, incomeViewHolder>(
                Data.class,
                R.layout.dash_income_layout,
                Dashboard_Fragment.incomeViewHolder.class,
                mIncomeDatabase
        ) {
            @Override
            protected void populateViewHolder(incomeViewHolder viewHolder, Data model, int position) {
                viewHolder.setIncomeAmount(model.getAmount());
                viewHolder.setIncomeDate(model.getDate());
                viewHolder.setIncomeType(model.getTitle());

            }
        };
        FirebaseRecyclerAdapter<Data,expenseViewHolder> exp_adapter = new FirebaseRecyclerAdapter<Data, expenseViewHolder>(
                Data.class,
                R.layout.dash_expense_layout,
                Dashboard_Fragment.expenseViewHolder.class,
                mExpenseDatabase
        ) {
            @Override
            protected void populateViewHolder(expenseViewHolder viewHolder, Data model, int position) {
                viewHolder.setExpenseAmount(model.getAmount());
                viewHolder.setExpenseDate(model.getDate());
                viewHolder.setExpenseType(model.getTitle());

            }
        };
        incomeRecycler.setAdapter(inc_adapter);
        expenseRecycler.setAdapter(exp_adapter);
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    public void add_data()
    {
        ft_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert_income_data();
            }
        });
        ft_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert_expense_data();
            }
        });

    }

    public void insert_income_data()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View viewdialog = inflater.inflate(R.layout.custom_insertdata,null);
        dialog.setView(viewdialog);

        final AlertDialog DialogView = dialog.create();


        final EditText dAmount =viewdialog.findViewById(R.id.insert_amount);
        final EditText dTitle =viewdialog.findViewById(R.id.insert_title);
        final EditText dNote =viewdialog.findViewById(R.id.insert_note);

        Button dSave = viewdialog.findViewById(R.id.insert_save);
        Button dCancel = viewdialog.findViewById(R.id.insert_cancel);


        dSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = dAmount.getText().toString().trim();
                String title = dTitle.getText().toString().trim();
                String note = dNote.getText().toString().trim();

                if(TextUtils.isEmpty(amount))
                {
                    dAmount.setError("Required Field");
                    return;
                }

                if(TextUtils.isEmpty(title))
                {
                    dAmount.setError("Required Field");
                    return;
                }

                if(TextUtils.isEmpty(note))
                {
                    dAmount.setError("Required Field");
                    return;
                }

                int amount_int = Integer.parseInt(amount);

                String id = mIncomeDatabase.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(amount_int,title,note,id,mDate);
                mIncomeDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(),"Data Inserted Successfully",Toast.LENGTH_SHORT);
                DialogView.dismiss();
                ftanimate();
            }

        });

        dCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ftanimate();

                DialogView.dismiss();

            }
        });
        DialogView.show();;
    }

    public void insert_expense_data()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View viewdialog = inflater.inflate(R.layout.custom_insertdata,null);
        dialog.setView(viewdialog);

        final AlertDialog DialogView = dialog.create();


        final EditText dAmount =viewdialog.findViewById(R.id.insert_amount);
        final EditText dTitle =viewdialog.findViewById(R.id.insert_title);
        final EditText dNote =viewdialog.findViewById(R.id.insert_note);

        Button dSave = viewdialog.findViewById(R.id.insert_save);
        Button dCancel = viewdialog.findViewById(R.id.insert_cancel);


        dSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = dAmount.getText().toString().trim();
                String title = dTitle.getText().toString().trim();
                String note = dNote.getText().toString().trim();

                if(TextUtils.isEmpty(amount))
                {
                    dAmount.setError("Required Field");
                    return;
                }

                if(TextUtils.isEmpty(title))
                {
                    dAmount.setError("Required Field");
                    return;
                }

                if(TextUtils.isEmpty(note))
                {
                    dAmount.setError("Required Field");
                    return;
                }

                int amount_int = Integer.parseInt(amount);

                String id = mExpenseDatabase.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(amount_int,title,note,id,mDate);
                mExpenseDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(),"Data Inserted Successfully",Toast.LENGTH_SHORT);
                ftanimate();
                DialogView.dismiss();
            }

        });

        dCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ftanimate();

                DialogView.dismiss();

            }
        });
        DialogView.show();;
    }

    public static class incomeViewHolder extends RecyclerView.ViewHolder
    {

        View v;
        public incomeViewHolder(View itemView) {
            super(itemView);
            v = itemView;
        }

        public void setIncomeType(String type)
        {
            TextView aType = v.findViewById(R.id.dash_income_card_type);
            aType.setText(type);
        }
        public void setIncomeAmount(double amount)
        {
            TextView aAmount = v.findViewById(R.id.dash_income_card_amount);
            aAmount.setText(String.valueOf(amount));
        }
        public void setIncomeDate(String date)
        {
            TextView aDate = v.findViewById(R.id.dash_income_card_date);
            aDate.setText(date);
        }


    }

    public static class expenseViewHolder extends RecyclerView.ViewHolder
    {

        View v;
        public expenseViewHolder(View itemView) {
            super(itemView);
            v = itemView;
        }

        public void setExpenseType(String type)
        {
            TextView aType = v.findViewById(R.id.dash_expense_card_type);
            aType.setText(type);
        }
        public void setExpenseAmount(double amount)
        {
            TextView aAmount = v.findViewById(R.id.dash_expense_card_amount);
            aAmount.setText(String.valueOf(amount));
        }
        public void setExpenseDate(String date)
        {
            TextView aDate = v.findViewById(R.id.dash_expense_card_date);
            aDate.setText(date);
        }


    }

}

