package sahinkalem.firebasesample;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StudentUpdate extends AppCompatActivity {
    private FirebaseDatabase studentDatabase;

    TextInputEditText txtStudentID, txtStudentName, txtStudentSurname;
    MaterialButton btnUpdateStudent;
    FloatingActionButton btnBackMain;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);
        studentDatabase = FirebaseDatabase.getInstance();

        txtStudentID = findViewById(R.id.txtStudentID);
        txtStudentName = findViewById(R.id.txtStudentName);
        txtStudentSurname = findViewById(R.id.txtStudentSurname);

        getStudentByID();

        btnUpdateStudent = findViewById(R.id.btnUpdateStudent);
        btnUpdateStudent.setOnClickListener(view -> updateStudentWithDAO());

        btnBackMain = findViewById(R.id.btnBackMain);
        btnBackMain.setOnClickListener(view -> backMainActivity());
    }

    private void getStudentByID() {
        String studentID = getIntent().getStringExtra("studentID");
        DatabaseReference reference = studentDatabase.getReference().child("students");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Student> studentList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Student student = dataSnapshot.getValue(Student.class);
                    if (student.getStudentID().equals(studentID))
                        studentList.add(student);
                }
                txtStudentID.setText(String.valueOf(studentList.get(0).getStudentID()));
                txtStudentName.setText(studentList.get(0).getStudentName());
                txtStudentSurname.setText(studentList.get(0).getStudentSurname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(findViewById(R.id.main), "Database connection error", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    private void updateStudentWithDAO() {
        if (!Objects.requireNonNull(txtStudentID.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(txtStudentName.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(txtStudentSurname.getText()).toString().isEmpty()) {

            Map<String, Object> studentMap = new HashMap<>();
            studentMap.put("studentID", txtStudentID.getText().toString());
            studentMap.put("studentName", txtStudentName.getText().toString());
            studentMap.put("studentSurname", txtStudentSurname.getText().toString());

            DatabaseReference reference = studentDatabase.getReference().child("students");
            reference.child(studentMap.get("studentID").toString()).updateChildren(studentMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Snackbar.make(findViewById(R.id.main), "Student Updated", Snackbar.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void backMainActivity() {
        finish();
    }
}