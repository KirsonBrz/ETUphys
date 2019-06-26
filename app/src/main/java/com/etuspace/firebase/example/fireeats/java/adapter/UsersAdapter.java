
package com.etuspace.firebase.example.fireeats.java.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.etuspace.firebase.example.fireeats.java.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.etuspace.firebase.example.fireeats.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerView adapter for a list of Restaurants.
 */
public class UsersAdapter extends FirestoreAdapter<UsersAdapter.ViewHolder> {





     int selectedLab;
    public interface OnUserSelectedListener {

        void onRestaurantSelected(DocumentSnapshot restaurant);

    }

    private OnUserSelectedListener mListener;


    public UsersAdapter(Query query, OnUserSelectedListener listener, int selectedLab) {
        super(query);
        mListener = listener;
        this.selectedLab = selectedLab;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener, selectedLab);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private FirebaseFirestore mFirestore;



        @BindView(R.id.fabMessage)
        FloatingActionButton fabMessage;

        @BindView(R.id.fabUpStatus)
        FloatingActionButton fabUpStatus;


        @BindView(R.id.userNameItem)
        TextView nameView;

        @BindView(R.id.userPositionItem)
        TextView positionItem;

        @BindView(R.id.groupItem)
        TextView groupItem;



        public ViewHolder(View itemView) {
            super(itemView);
            mFirestore = FirebaseFirestore.getInstance();
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnUserSelectedListener listener, final int selectedLab) {

            final User user = snapshot.toObject(User.class);



            // Load image


            String status = "";

            switch (selectedLab)
            {
                case 1:
                    switch (user.getFirstlab()){
                        case "0":
                            status = "Протокол не подписан\n";
                            itemView.setBackgroundColor(Color.parseColor("#b71c1c"));
                            break;
                        case "1":
                            status = "Обработка результатов\nизмерений";
                            itemView.setBackgroundColor(Color.parseColor("#FF6F00"));
                            break;
                            default:
                                status = "Работа сдана\n";
                                itemView.setBackgroundColor(Color.parseColor("#1B5E20"));
                                break;
                    }
                    break;
                case 2:
                    switch (user.getSecondlab()){
                        case "0":
                            status = "Протокол не подписан\n";
                            itemView.setBackgroundColor(Color.parseColor("#b71c1c"));
                            break;
                        case "1":
                            status = "Обработка результатов\nизмерений";
                            itemView.setBackgroundColor(Color.parseColor("#FF6F00"));
                            break;
                        default:
                            status = "Работа сдана\n";
                            itemView.setBackgroundColor(Color.parseColor("#1B5E20"));
                            break;
                    }
                    break;
                case 3:
                    switch (user.getThirdlab()){
                        case "0":
                            status = "Протокол не подписан\n";
                            itemView.setBackgroundColor(Color.parseColor("#b71c1c"));
                            break;
                        case "1":
                            status = "Обработка результатов\nизмерений";
                            itemView.setBackgroundColor(Color.parseColor("#FF6F00"));
                            break;
                        default:
                            status = "Работа сдана\n";
                            itemView.setBackgroundColor(Color.parseColor("#1B5E20"));
                            break;
                    }
                    break;
                case 4:
                    switch (user.getFourlab()){
                        case "0":
                            status = "Протокол не подписан\n";
                            itemView.setBackgroundColor(Color.parseColor("#b71c1c"));
                            break;
                        case "1":
                            status = "Обработка результатов\nизмерений";
                            itemView.setBackgroundColor(Color.parseColor("#FF6F00"));
                            break;
                        default:
                            status = "Работа сдана\n";
                            itemView.setBackgroundColor(Color.parseColor("#1B5E20"));
                            break;
                    }
                    break;
                    default:
                        break;
            }

            nameView.setText(user.getSurname() + " " + user.getName() );
            positionItem.setText(status);



            fabMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Написать сообщение пользователю: " + user.getSurname() + " " +  user.getName(),Toast.LENGTH_SHORT).show();

                }
            });

            fabUpStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(),"Удерживайте для изменения статуса",Toast.LENGTH_SHORT).show();
                }
            });

            fabUpStatus.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final DocumentReference sfDocRef = mFirestore.collection("users").document(snapshot.getId());

                    mFirestore.runTransaction(new Transaction.Function<Void>() {
                        @Override
                        public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot snapshot = transaction.get(sfDocRef);



                            String newStatus = "";
                            String lab = "";
                            switch (selectedLab)
                            {
                                case 1:
                                    lab = "firstlab";
                                    newStatus = String.valueOf(Integer.parseInt(user.getFirstlab())+1);
                                    break;
                                case 2:
                                    lab = "secondlab";
                                    newStatus = String.valueOf(Integer.parseInt(user.getSecondlab())+1);
                                    break;
                                case 3:
                                    lab = "thirdlab";
                                    newStatus = String.valueOf(Integer.parseInt(user.getThirdlab())+1);
                                    break;
                                case 4:
                                    lab = "fourlab";
                                    newStatus = String.valueOf(Integer.parseInt(user.getFourlab())+1);
                                    break;
                                default:
                                    break;
                            }


                            transaction.update(sfDocRef, lab, newStatus);

                            // Success
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "Transaction success!");
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Transaction failure.", e);
                                }
                            });

                    Toast.makeText(itemView.getContext(), "Статус успешно изменён!",Toast.LENGTH_SHORT).show();



                    return true;
                }

            });






            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onRestaurantSelected(snapshot);
                    }
                }
            });
        }

    }
}
