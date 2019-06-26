package com.etuspace.firebase.example.fireeats.java;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.etuspace.firebase.example.fireeats.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class KeyDialogFragment extends DialogFragment {
    public static final String TAG = "RatingDialog";
    public int checkTrue = 0;


    @BindView(R.id.key_edit)
    EditText mKeyText;



    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_key_dialog, container, false);
        unbinder = ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @OnClick(R.id.keyFormSend)
    public void onSubmitClicked(View view) {


        if(mKeyText.getText().toString().equals("4321")){
            Toast.makeText(getActivity().getApplicationContext(), "Ключ успешно введён",Toast.LENGTH_SHORT).show();
            checkTrue = 1;
            dismiss();
        }else{
            checkTrue = 0;
            Toast.makeText(getActivity().getApplicationContext(), "Введите правильный ключ доступа",Toast.LENGTH_SHORT).show();

        }




    }

    @OnClick(R.id.keyFormCancel)
    public void onCancelClicked(View view) {
        dismiss();
    }


}
