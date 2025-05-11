package ai.frontdesk.agent.view;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import java.util.List;
import java.util.stream.Collectors;

import ai.frontdesk.agent.R;
import ai.frontdesk.agent.model.KnowledgeEntry;
import ai.frontdesk.agent.viewmodel.LearnedAnswersViewModel;

public class LearnedAnswersActivity extends AppCompatActivity {
    private ListView listLearned;
    private LearnedAnswersViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learned_answers);

        listLearned = findViewById(R.id.listLearned);
        viewModel = new ViewModelProvider(this).get(LearnedAnswersViewModel.class);

        viewModel.getLearnedAnswers().observe(this, entries -> {
            List<String> display = entries.stream()
                    .map(e -> "Q: " + e.getQuestion() + "\nA: " + e.getAnswer())
                    .collect(Collectors.toList());
            listLearned.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, display));
        });
    }
}