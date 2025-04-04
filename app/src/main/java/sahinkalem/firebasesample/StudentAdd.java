package sahinkalem.firebasesample;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class StudentAdd extends AppCompatActivity {

    private DatabaseReference reference;

    TextInputEditText txtStudentID, txtStudentName, txtStudentSurname;
    MaterialButton btnAddStudentWith;
    FloatingActionButton btnBackMain;
    Student student;
    String recordCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recordCount = getIntent().getStringExtra("recordCount");

        FirebaseApp.initializeApp(this);
        reference = FirebaseDatabase.getInstance().getReference();

        txtStudentID = findViewById(R.id.txtStudentID);
        txtStudentName = findViewById(R.id.txtStudentName);
        txtStudentSurname = findViewById(R.id.txtStudentSurname);

        txtStudentID.setText(String.valueOf(Integer.parseInt(recordCount) + 1));

        btnAddStudentWith = findViewById(R.id.btnAddStudent);
        btnAddStudentWith.setOnClickListener(view -> addStudent());

        btnBackMain = findViewById(R.id.btnBackMain);
        btnBackMain.setOnClickListener(view -> backMainActivity());
    }

    private void addStudent() {
        if (!Objects.requireNonNull(txtStudentID.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(txtStudentName.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(txtStudentSurname.getText()).toString().isEmpty()) {
            student = new Student();
            student.setStudentID(txtStudentID.getText().toString());
            student.setStudentName(txtStudentName.getText().toString());
            student.setStudentSurname(txtStudentSurname.getText().toString());

            reference.child("students").child(student.getStudentID()).setValue(student).addOnSuccessListener(unused -> {
                Snackbar.make(findViewById(R.id.main), "Student Added", Snackbar.LENGTH_SHORT).show();
                txtStudentID.setText("");
                txtStudentName.setText("");
                txtStudentSurname.setText("");
            });
        } else {
            Snackbar.make(findViewById(R.id.main), "Please fill all fields", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void backMainActivity() {
        finish();
    }

}