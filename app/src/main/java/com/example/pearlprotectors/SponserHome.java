package com.example.pearlprotectors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SponserHome extends AppCompatActivity {

    ListView listView;
    List<Projects> user;
    DatabaseReference ref;
    Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponser_home);

        listView = (ListView)findViewById(R.id.listview);
        button = (Button)findViewById(R.id.submitsponsor);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SponserHome.this, SponsorSavedProjects.class);
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

                MyAdapter adapter = new MyAdapter(SponserHome.this, R.layout.custom_project_details_sponser, (ArrayList<Projects>) user);
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
                view = inflater.inflate(R.layout.custom_project_details_sponser, null);

                holder.COL1 = (TextView) view.findViewById(R.id.ProductCode);
                holder.COL2 = (TextView) view.findViewById(R.id.ProductName);
                holder.COL3 = (TextView) view.findViewById(R.id.ProductPrice);
                holder.COL4 = (TextView) view.findViewById(R.id.ProductDescription);
                holder.imageView = (ImageView) view.findViewById(R.id.imageView2);
                holder.button1 = (Button) view.findViewById(R.id.sponser);
                holder.button2 = (Button) view.findViewById(R.id.inquiry);


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

            holder.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SponserHome.this, AddProjectSponsorShipDetails.class);
                    intent.putExtra("id", user.get(position).getId());
                    intent.putExtra("image", user.get(position).getImage());
                    intent.putExtra("title", user.get(position).getTitle());
                    intent.putExtra("venue", user.get(position).getVenue());
                    intent.putExtra("date", user.get(position).getDate());
                    startActivity(intent);
                }
            });

            holder.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SponserHome.this, SponsorAddInquoryDetails.class);
                    intent.putExtra("id", user.get(position).getId());
                    intent.putExtra("image", user.get(position).getImage());
                    intent.putExtra("title", user.get(position).getTitle());
                    intent.putExtra("venue", user.get(position).getVenue());
                    intent.putExtra("date", user.get(position).getDate());
                    startActivity(intent);
                }
            });

            return view;

        }
    }
}