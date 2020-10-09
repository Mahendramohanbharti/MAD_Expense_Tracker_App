package com.example.mahendramohanbharti;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import com.example.mahendramohanbharti.Model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.core.DatabaseInfo;

import java.text.DateFormat;
import java.util.Date;


public class DashBoardFragment extends Fragment {


    //Floating button

    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;

    //floating Button TextView

    private TextView fab_income_txt;
    private TextView fab_expense_txt;


    //Boolean
    private boolean isOpen = false;

    //Animation
    private Animation FadOpen, FadClose;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_dash_board, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();
        Toast.makeText(getActivity(), "USER LOGIN " + mUser.getEmail(), Toast.LENGTH_SHORT).show();
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

        //connect floating button to layout
        fab_main_btn = myView.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn = myView.findViewById(R.id.income_Ft_btn);
        fab_expense_btn = myView.findViewById(R.id.expense_Ft_btn);

        //connect floating text

        fab_income_txt = myView.findViewById(R.id.income_ft_text);
        fab_expense_txt = myView.findViewById(R.id.expense_ft_text);

        //Animation Connect

        FadOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        FadClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();

                if (isOpen) {
                    fab_income_btn.startAnimation(FadClose);
                    fab_expense_btn.startAnimation(FadClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_income_txt.startAnimation(FadClose);
                    fab_expense_txt.startAnimation(FadClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);
                    isOpen = false;

                } else {
                    fab_income_btn.startAnimation(FadOpen);
                    fab_expense_btn.startAnimation(FadOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_txt.startAnimation(FadOpen);
                    fab_expense_txt.startAnimation(FadOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);
                    isOpen = true;
                }

            }
        });

        return myView;
    }

    //Floating button animation

    private void ftAnimation() {
        if (isOpen) {
            fab_income_btn.startAnimation(FadClose);
            fab_expense_btn.startAnimation(FadClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);

            fab_income_txt.startAnimation(FadClose);
            fab_expense_txt.startAnimation(FadClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);
            isOpen = false;

        } else {
            fab_income_btn.startAnimation(FadOpen);
            fab_expense_btn.startAnimation(FadOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_txt.startAnimation(FadOpen);
            fab_expense_txt.startAnimation(FadOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);
            isOpen = true;
        }
    }

    private void addData() {
        //Fab income Button...
        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomeDataInsert();
            }
        });
        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expenseDataInsert();
            }
        });

    }

    public void incomeDataInsert() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myViewM = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        myDialog.setView(myViewM);
        final AlertDialog dialog = myDialog.create();

        dialog.setCancelable(false);

        final EditText edtAmount = myViewM.findViewById(R.id.ammount_edt);
        final EditText edtType = myViewM.findViewById(R.id.type_edt);
        final EditText edtNote = myViewM.findViewById(R.id.note_edt);

        Button btnSave = myViewM.findViewById(R.id.btnSave);
        Button btnCancel = myViewM.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = edtType.getText().toString().trim();
                String amount = edtAmount.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if (TextUtils.isEmpty(type)) {
                    edtType.setError("Required failed...");
                    return;
                }
                if (TextUtils.isEmpty(amount)) {
                    edtAmount.setError("Required failed...");
                    return;
                }
                int ourAmountInt;
                try {
                    ourAmountInt = Integer.parseInt(amount);
                } catch (Exception e) {
                    edtAmount.setError("Amount should be type Integer");
                    return;
                }
                if (TextUtils.isEmpty(note)) {
                    edtNote.setError("Required failed...");
                    return;
                }

                String id = mIncomeDatabase.push().getKey();
                Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
                String mDate = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(ourAmountInt, type, note, id, mDate);

                mIncomeDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(), "Data ADDED", Toast.LENGTH_SHORT).show();
                ftAnimation();
                dialog.dismiss();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ftAnimation();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void expenseDataInsert() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myViewM = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        myDialog.setView(myViewM);
        final AlertDialog dialog = myDialog.create();

        dialog.setCancelable(false);

        final EditText edtAmount = myViewM.findViewById(R.id.ammount_edt);
        final EditText edtType = myViewM.findViewById(R.id.type_edt);
        final EditText edtNote = myViewM.findViewById(R.id.note_edt);

        Button btnSave = myViewM.findViewById(R.id.btnSave);
        Button btnCancel = myViewM.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = edtType.getText().toString().trim();
                String amount = edtAmount.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if (TextUtils.isEmpty(type)) {
                    edtType.setError("Required failed...");
                    return;
                }
                if (TextUtils.isEmpty(amount)) {
                    edtAmount.setError("Required failed...");
                    return;
                }
                int ourAmountInt;
                try {
                    ourAmountInt = Integer.parseInt(amount);
                } catch (Exception e) {
                    edtAmount.setError("Amount must be type Integer");
                    return;
                }
                if (TextUtils.isEmpty(note)) {
                    edtNote.setError("Required failed...");
                    return;
                }
                String id = mExpenseDatabase.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(ourAmountInt, type, note, id, mDate);

                mExpenseDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(), "Data ADDED", Toast.LENGTH_SHORT).show();
                ftAnimation();
                dialog.dismiss();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ftAnimation();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}