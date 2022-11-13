package com.example.pearlprotectors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pearlprotectors.Models.Projects;
import com.example.pearlprotectors.Models.SponsorshipDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SponsorSavedProjects extends AppCompatActivity {

    ListView listView;
    List<SponsorshipDetails> user;
    DatabaseReference ref;

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_saved_projects);

        listView = (ListView)findViewById(R.id.listview);

        user = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("SponsorshipsDetails");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()) {

                    SponsorshipDetails sponsorshipDetails = studentDatasnap.getValue(SponsorshipDetails.class);
                    user.add(sponsorshipDetails);
                }

                MyAdapter adapter = new MyAdapter(SponsorSavedProjects.this, R.layout.custom_sponsership_details, (ArrayList<SponsorshipDetails>) user);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder {

        TextView COL1;
        TextView COL2;
        TextView COL3;
        TextView COL4;
        TextView COL5;
        TextView COL6;
        TextView COL7;
        ImageView imageView;
        Button button1;
        Button button2;
    }

    class MyAdapter extends ArrayAdapter<SponsorshipDetails> {
        LayoutInflater inflater;
        Context myContext;
        List<SponsorshipDetails> user;


        public MyAdapter(Context context, int resource, ArrayList<SponsorshipDetails> objects) {
            super(context, resource, objects);
            myContext = context;
            user = objects;
            inflater = LayoutInflater.from(context);
            int y;
            String barcode;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.custom_sponsership_details, null);

                holder.COL1 = (TextView) view.findViewById(R.id.title);
                holder.COL2 = (TextView) view.findViewById(R.id.date);
                holder.COL3 = (TextView) view.findViewById(R.id.venue);
                holder.COL4 = (TextView) view.findViewById(R.id.name);
                holder.COL5 = (TextView) view.findViewById(R.id.type);
                holder.COL6 = (TextView) view.findViewById(R.id.email);
                holder.COL7 = (TextView) view.findViewById(R.id.contact);
                holder.imageView = (ImageView) view.findViewById(R.id.imageView2);
                holder.button1 = (Button) view.findViewById(R.id.edit);
                holder.button2 = (Button) view.findViewById(R.id.delete);


                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.COL1.setText("Title:- "+user.get(position).getTitle());
            holder.COL2.setText("Venue:- "+user.get(position).getVenue());
            holder.COL3.setText("Date:- "+user.get(position).getDate());
            holder.COL4.setText("Name:- "+user.get(position).getName());
            holder.COL5.setText("Sponsor Type:- "+user.get(position).getType());
            holder.COL6.setText("Email:- "+user.get(position).getEmail());
            holder.COL7.setText("Contact No:- "+user.get(position).getContact());
            Picasso.get().load(user.get(position).getImage()).into(holder.imageView);
            System.out.println(holder);

            holder.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("Do you want to delete this Sponsorship?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    final String idd = user.get(position).getId();
                                    FirebaseDatabase.getInstance().getReference("SponsorshipsDetails").child(idd).removeValue();
                                    //remove function not written
                                    Toast.makeText(myContext, "Item deleted successfully", Toast.LENGTH_SHORT).show();

                                }
                            })

                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();
                }
            });

            holder.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view1 = inflater.inflate(R.layout.custom_update_sponsorship_details, null);
                    dialogBuilder.setView(view1);

                    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                    final EditText editText1 = (EditText) view1.findViewById(R.id.name);
                    final EditText editText2 = (EditText) view1.findViewById(R.id.sponsertype);
                    final EditText editText3 = (EditText) view1.findViewById(R.id.email);
                    final EditText editText4 = (EditText) view1.findViewById(R.id.number);
                    final Button buttonupdate = (Button) view1.findViewById(R.id.submit);
                    final ImageView imageView = (ImageView) view1.findViewById(R.id.imageView);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final String idd = user.get(position).getId();
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SponsorshipsDetails").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = (String) snapshot.child("name").getValue();
                            String type = (String) snapshot.child("type").getValue();
                            String email = (String) snapshot.child("email").getValue();
                            String contact = (String) snapshot.child("contact").getValue();
                            String image = (String) snapshot.child("image").getValue();

                            editText1.setText(name);
                            editText2.setText(type);
                            editText3.setText(email);
                            editText4.setText(contact);
                            Picasso.get().load(image).into(imageView);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    buttonupdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final String name = editText1.getText().toString();
                            final String type = editText2.getText().toString();
                            final String email = editText3.getText().toString();
                            final String contact = editText4.getText().toString();

                            if (name.isEmpty()) {
                                editText1.setError("Name is required");
                            }else if (type.isEmpty()) {
                                editText2.setError("Sponsor type is required");
                            }else if (email.isEmpty()) {
                                editText3.setError("Email is required");
                            }else if (contact.isEmpty()) {
                                editText4.setError("Contact number is required");
                            }else {

                                HashMap map = new HashMap();
                                map.put("name", name);
                                map.put("type", type);
                                map.put("email", email);
                                map.put("contact", contact);
                                reference.updateChildren(map);

                                Toast.makeText(SponsorSavedProjects.this, "Updated successfully", Toast.LENGTH_SHORT).show();

                                alertDialog.dismiss();
                            }
                        }
                    });
                }
            });

            return view;

        }
    }
}