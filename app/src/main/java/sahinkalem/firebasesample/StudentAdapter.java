package sahinkalem.firebasesample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    View view;
    Context context;
    List<Student> studentList;
    FirebaseDatabase studentDatabase;

    public StudentAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.txtStudentID.setText(String.valueOf(student.getStudentID()));
        holder.txtStudentName.setText(student.getStudentName());
        holder.txtStudentSurname.setText(student.getStudentSurname());

        FirebaseApp.initializeApp(context);
        studentDatabase = FirebaseDatabase.getInstance();

        //Delete Student
        holder.btnStudentDelete.setOnClickListener(view -> {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        DatabaseReference reference = studentDatabase.getReference().child("students");
                        reference.child(String.valueOf(student.getStudentID())).removeValue();

                        studentList.remove(student);
                        notifyItemRemoved(position);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to delete this student?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        });

        holder.btnStudentUpdate.setOnClickListener(view -> {
            context.startActivity(new Intent(context, StudentUpdate.class).putExtra("studentID",String.valueOf( student.getStudentID())));
            notifyItemChanged(position);
        });

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final MaterialTextView txtStudentID;
        private final MaterialTextView txtStudentName;
        private final MaterialTextView txtStudentSurname;
        private final MaterialButton btnStudentDelete;
        private final MaterialButton btnStudentUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStudentID = itemView.findViewById(R.id.txtStudentID);
            txtStudentName = itemView.findViewById(R.id.txtStudentName);
            txtStudentSurname = itemView.findViewById(R.id.txtStudentSurname);
            btnStudentDelete = itemView.findViewById(R.id.btnStudentDelete);
            btnStudentUpdate = itemView.findViewById(R.id.btnStudentUpdate);
        }
    }
}
