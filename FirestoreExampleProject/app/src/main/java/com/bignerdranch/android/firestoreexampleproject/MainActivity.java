package com.bignerdranch.android.firestoreexampleproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");
//    private DocumentReference noteRef = db.collection("Notebook").document("My First Note");
    private DocumentReference noteRef = db.document("Notebook/My First Note");
//    private ListenerRegistration noteListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        textViewData = findViewById(R.id.text_view_data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                String data = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Note note = documentSnapshot.toObject((Note.class));
                    note.setDocumentId(documentSnapshot.getId());

                    String documentId = note.getDocumentId();
                    String title = note.getTitle();
                    String description = note.getDescription();

                    data+= "ID: " + documentId +
                            "\nTitle: " + title +
                            "\nDescription: " + description + "\n\n";

//                    notebookRef.document(documentId).update()
                }

                textViewData.setText(data);
            }
        });
    }

    // this stop addSnapshotListener from being updated when the app stops
//    @Override
//    protected void onStop() {
//        super.onStop();
//        noteListener.remove();
//    }

    public void addNote(View v) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

//        Map<String, Object> note = new HashMap<>();
//        note.put(KEY_TITLE, title);
//        note.put(KEY_DESCRIPTION, description);
        Note note = new Note(title, description);

        notebookRef.add(note);
    }

    public void loadNotes(View v) {
       notebookRef.get()
               .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                   @Override
                   public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                       String data = "";

                       for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                           Note note = documentSnapshot.toObject(Note.class);
                           note.setDocumentId(documentSnapshot.getId());

                           String documentId = note.getDocumentId();
                           String title = note.getTitle();
                           String description = note.getDescription();

                           data+= "ID: " + documentId +
                                   "\nTitle: " + title +
                                   "\nDescription: " + description + "\n\n";
                       }

                       textViewData.setText(data);
                   }
               });
    }
}
