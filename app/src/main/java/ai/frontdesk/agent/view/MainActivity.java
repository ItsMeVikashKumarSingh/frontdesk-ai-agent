package ai.frontdesk.agent.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ai.frontdesk.agent.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnClient = findViewById(R.id.btnClient);
        Button btnSupervisor = findViewById(R.id.btnSupervisor);
        Button btnLearnedAnswers = findViewById(R.id.btnLearnedAnswers);
        
        btnLearnedAnswers.setOnClickListener(v -> {
            startActivity(new Intent(this, LearnedAnswersActivity.class));
        });

        btnClient.setOnClickListener(v -> startActivity(new Intent(this, AIAgentActivity.class)));
        btnSupervisor.setOnClickListener(v -> startActivity(new Intent(this, SupervisorActivity.class)));
    }
}