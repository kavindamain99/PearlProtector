package com.example.pearlprotectors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageProjects extends AppCompatActivity {

    Button button;
    ListView listView;
    List<Projects> user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_projects);

        button = (Button)findViewById(R.id.addProduct);
        listView = (ListView)findViewById(R.id.listview);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageProjects.this, AddProject.class);
                startActivity(intent);
            }
        });

        user = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("ProjectDetails");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()) {

                    Projects projects = studentDatasnap.getValue(Projects.class);
                    user.add(projects);
                }

                MyAdapter adapter = new MyAdapter(ManageProjects.this, R.layout.custom_project_details, (ArrayList<Projects>) user);
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
        ImageView imageView;
        Button button1;
        Button button2;
    }

    class MyAdapter extends ArrayAdapter<Projects> {
        LayoutInflater inflater;
        Context myContext;
        List<Projects> user;


        public MyAdapter(Context context, int resource, ArrayList<Projects> objects) {
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
                view = inflater.inflate(R.layout.custom_project_details, null);

                holder.COL1 = (TextView) view.findViewById(R.id.ProductCode);
                holder.COL2 = (TextView) view.findViewById(R.id.ProductName);
                holder.COL3 = (TextView) view.findViewById(R.id.ProductPrice);
                holder.COL4 = (TextView) view.findViewById(R.id.ProductDescription);
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
            holder.COL4.setText("Description:- "+user.get(position).getDescription());
            Picasso.get().load(user.get(position).getImage()).into(holder.imageView);
            System.out.println(holder);

            holder.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("Do you want to delete this Project?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    final String idd = user.get(position).getId();
                                    FirebaseDatabase.getInstance().getReference("ProjectDetails").child(idd).removeValue();
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
                    View view1 = inflater.inflate(R.layout.custom_update_project_details, null);
                    dialogBuilder.setView(view1);

                    final EditText editText1 = (EditText) view1.findViewById(R.id.ptitle);
                    final EditText editText2 = (EditText) view1.findViewById(R.id.pvenue);
                    final EditText editText3 = (EditText) view1.findViewById(R.id.pdate);
                    final EditText editText4 = (EditText) view1.findViewById(R.id.pdescription);
                    final Button buttonupdate = (Button) view1.findViewById(R.id.addbtn);
                    final ImageView imageView = (ImageView) view1.findViewById(R.id.imageView);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final String idd = user.get(position).getId();
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ProjectDetails").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String image = (String) snapshot.child("image").getValue();
                            String title = (String) snapshot.child("title").getValue();
                            String venue = (String) snapshot.child("venue").getValue();
                            String date = (String) snapshot.child("date").getValue();
                            String description = (String) snapshot.child("description").getValue();

                            editText1.setText(title);
                            editText2.setText(venue);
                            editText3.setText(date);
                            editText4.setText(description);
                            Picasso.get().load(image).into(imageView);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    buttonupdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String title = editText1.getText().toString();
                            String venue = editText2.getText().toString();
                            String date = editText3.getText().toString();
                            String description = editText4.getText().toString();

                            if (title.isEmpty()) {
                                editText1.setError("Project title is required");
                            }else if (venue.isEmpty()) {
                                editText2.setError("Project venue is required");
                            }else if (date.isEmpty()) {
                                editText3.setError("Project date is required");
                            }else if (description.isEmpty()) {
                                editText4.setError("Project Description is required");
                            }else {

                                HashMap map = new HashMap();
                                map.put("title", title);
                                map.put("venue", venue);
                                map.put("date", date);
                                map.put("description", description);
                                reference.updateChildren(map);

                                Toast.makeText(ManageProjects.this, "Updated successfully", Toast.LENGTH_SHORT).show();

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