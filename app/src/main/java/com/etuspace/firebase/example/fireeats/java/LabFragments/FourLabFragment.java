package com.etuspace.firebase.example.fireeats.java.LabFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etuspace.firebase.example.fireeats.java.adapter.UsersAdapter;
import com.etuspace.firebase.example.fireeats.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.jaredrummler.materialspinner.MaterialSpinner;


public class FourLabFragment extends Fragment implements UsersAdapter.OnUserSelectedListener {

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private ViewGroup mEmptyView;


    private UsersAdapter mAdapter;

    RecyclerView mUsersRecycle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View rootView =
                inflater.inflate(R.layout.fragment_four_lab, container, false);

        MaterialSpinner spinnerGroup = (MaterialSpinner) getActivity().findViewById(R.id.spinnerGroup);
        mUsersRecycle = rootView.findViewById(R.id.recycler_four_lab);
        mEmptyView = rootView.findViewById(R.id.viewEmptyUsers4);
        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collection("users").whereEqualTo("group",spinnerGroup.getText().toString());




        mAdapter = new UsersAdapter(mQuery, this, 4) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mUsersRecycle.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mUsersRecycle.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors

            }
        };

        mUsersRecycle.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        mUsersRecycle.setAdapter(mAdapter);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();


        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }


    @Override
    public void onRestaurantSelected(DocumentSnapshot user) {


    }

}
