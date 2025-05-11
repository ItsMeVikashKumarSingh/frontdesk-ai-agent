package ai.frontdesk.agent.view;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import java.util.List;

import ai.frontdesk.agent.R;
import ai.frontdesk.agent.viewmodel.SupervisorViewModel;

public class SupervisorActivity extends AppCompatActivity {

    private ListView listRequests;
    private EditText etAnswer;
    private Button btnSubmit;
    private SupervisorViewModel viewModel;
    private String selectedQuestion = null;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor);

        listRequests = findViewById(R.id.listRequests);
        etAnswer = findViewById(R.id.etAnswer);
        btnSubmit = findViewById(R.id.btnSubmit);

        viewModel = new ViewModelProvider(this).get(SupervisorViewModel.class);

        viewModel.getUnresolvedQuestions().observe(this, questions -> {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, questions);
            listRequests.setAdapter(adapter);
        });

        listRequests.setOnItemClickListener((parent, view, position, id) -> {
            selectedQuestion = adapter.getItem(position);
            Toast.makeText(this, "Selected: " + selectedQuestion, Toast.LENGTH_SHORT).show();
        });

        btnSubmit.setOnClickListener(v -> {
            String answer = etAnswer.getText().toString().trim();
            if (selectedQuestion == null || answer.isEmpty()) {
                Toast.makeText(this, "Select a question and write an answer", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.submitAnswer(selectedQuestion, answer);
            Toast.makeText(this, "Answer submitted!", Toast.LENGTH_SHORT).show();
            etAnswer.setText("");
        });
    }
}