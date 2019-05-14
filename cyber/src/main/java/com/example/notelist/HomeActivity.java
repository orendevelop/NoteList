package com.example.notelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {


    String TAG = "HomeActivity";
    ArrayList<Note> notesArray = new ArrayList();
    AdapterCustom adapterCustom = new AdapterCustom();
    ImageView addBtn;
    EditText noteContent;
    TextView logoutBtn;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    String userID;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ListView listView = findViewById(R.id.list);
        noteContent = findViewById(R.id.note_content_edit);
        addBtn = findViewById(R.id.save_note_btn);

        listView.setAdapter(adapterCustom);
        notesArray.clear();

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();

        downloadNotes();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long tsLong = System.currentTimeMillis() / 1000;
                Random r = new Random();

                String ts = String.valueOf((int)(Math.random() * 100 + 1) + tsLong.toString());
                Note note = new Note(ts, String.valueOf(new Date()), noteContent.getText().toString().trim());
                myRef.child("users").child(userID).child(ts).setValue(note);
                notesArray.add(note);
                adapterCustom.notifyDataSetChanged();

            }
        });

        logoutBtn = findViewById(R.id.btn_logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             Log.d("ssss", "mkoreeee");
                                             mAuth.signOut();
                                             startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                                         }
                                     }

        );




    }

    public void downloadNotes() {
        myRef.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String content = snapshot.child("content").getValue(String.class);
                    String date = snapshot.child("date").getValue(String.class);
                    String id = snapshot.child("id").getValue(String.class);
                    notesArray.add(new Note(id, date, content));
                }
                Log.d("ssss", notesArray.toString());
                adapterCustom.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private class AdapterCustom extends BaseAdapter {


        @Override
        public int getCount() {
            return notesArray.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(R.layout.row_notes_list, null);
            TextView noteContent, dateContent;
            ImageView removeBtn;
            noteContent = view.findViewById(R.id.note_textView);
            dateContent = view.findViewById(R.id.date_textView);
            removeBtn = view.findViewById(R.id.remove_btn);

            noteContent.setText(notesArray.get(i).getContent());
            dateContent.setText(notesArray.get(i).getDate());

            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCustom.notifyDataSetChanged();
                    Log.d("ssss", String.valueOf(i));
                    myRef.child("users").child(userID).child(notesArray.get(i).getId()).removeValue();
                    notesArray.remove(i);
                    adapterCustom.notifyDataSetChanged();
                }
            });
            return view;
        }

    }
}
