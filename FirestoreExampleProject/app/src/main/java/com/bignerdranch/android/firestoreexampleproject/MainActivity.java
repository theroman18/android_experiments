package com.bignerdranch.android.firestoreexampleproject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextPriority;
    private EditText editTextTags;
    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");
    private DocumentSnapshot lastResult;

    //    private DocumentReference noteRef = db.collection("Notebook").document("My First Note");
    private DocumentReference noteRef = db.document("Notebook/My First Note");
//    private ListenerRegistration noteListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextPriority = findViewById(R.id.edit_text_priority);
        textViewData = findViewById(R.id.text_view_data);
        editTextTags = findViewById(R.id.edit_text_tags);
        updateNestedValue();
    }

    public void addNote(View v) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

//        Map<String, Object> note = new HashMap<>();
//        note.put(KEY_TITLE, title);
//        note.put(KEY_DESCRIPTION, description);
        if (editTextPriority.length() == 0) {
            editTextPriority.setText("0");
        }
        int priority = Integer.parseInt(editTextPriority.getText().toString());

        String tagInput = editTextTags.getText().toString();
        String[] tagArray = tagInput.split("\\s*,\\s*");
        Map<String, Boolean> tags = new HashMap<>();

        for (String tag : tagArray) {
            tags.put(tag, true);
        }

        Note note = new Note(title, description, priority, tags);

        notebookRef.add(note);
    }

    public void loadNotes(View v) {
        notebookRef.whereEqualTo("tags.tag1", true).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            note.setDocumentId(documentSnapshot.getId());

                            String documentId = note.getDocumentId();

                            data += "ID: " + documentId;

                            for (String tag : note.getTags().keySet()) {
                                data += "\n-" + tag;
                            }

                            data += "\n\n";
                        }
                        textViewData.setText(data);
                    }
                });
    }

    private void updateNestedValue() {
        notebookRef.document("HME7vzrhx0lZGRkCPw6e")
//                .update("tags", FieldValue.arrayUnion("new tag"));
                // removes array element by its value's name
//        .update("tags", FieldValue.arrayRemove("new tag"));
                //updates nested object element and changes it's value to false
//        .update("tags.tag1", false);
                //deletes the nested map's field value
        .update("tags.tag1.nested1.nested2", true);
    }
}
