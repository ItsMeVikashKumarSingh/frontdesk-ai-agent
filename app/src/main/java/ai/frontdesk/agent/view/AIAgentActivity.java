package ai.frontdesk.agent.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.*;

import ai.frontdesk.agent.R;
import ai.frontdesk.agent.datasource.LiveKitManager;
import ai.frontdesk.agent.listener.RoomEventListener;
import ai.frontdesk.agent.utils.SpeechToText;
import ai.frontdesk.agent.utils.TextToSpeechHelper;
import ai.frontdesk.agent.viewmodel.AIAgentViewModel;
import io.livekit.android.events.RoomEvent;

public class AIAgentActivity extends AppCompatActivity implements RoomEventListener {

    private TextView tvCallStatus, tvDebug;
    private Button btnStartCall;
    private boolean isConnected = false;
    private static final long SPEECH_RESTART_DELAY = 2000;
    private final Handler handler = new Handler();
    private boolean isActivityRunning = true;

    private final ActivityResultLauncher<String[]> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                for (Map.Entry<String, Boolean> entry : result.entrySet()) {
                    if (!entry.getValue()) {
                        Toast.makeText(this, "Missing permission: " + entry.getKey(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private TextToSpeechHelper tts;
    private SpeechToText stt;
    private AIAgentViewModel viewModel;
    private boolean waitingForSupervisor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_agent);

        tvCallStatus = findViewById(R.id.tvCallStatus);
        tvDebug = findViewById(R.id.tvDebug);
        btnStartCall = findViewById(R.id.btnStartCall);

        tts = new TextToSpeechHelper(this, this::onTTSSpoken);
        viewModel = new AIAgentViewModel();

        stt = new SpeechToText(this, new SpeechToText.OnTextRecognizedListener() {
            @Override
            public void onTextRecognized(String text) {
                if (isActivityRunning) {
                    tvDebug.append("\nUser: " + text);
                    handleQuestion(text);
                }
            }

            @Override
            public void onSpeechTimeout() {
                if (isActivityRunning) {
                    tvDebug.append("\n[No voice detected. Restarting in 1 second...]");
                    handler.postDelayed(() -> stt.restart(), SPEECH_RESTART_DELAY);
                }
            }

        });

        requestPermissions();

        btnStartCall.setOnClickListener(v -> {
            if (!isConnected) {
                tvCallStatus.setText("Connecting to call...");
                LiveKitManager.connect(
                        this,
                        "wss://frontdesk-project-9m701ldo.livekit.cloud",
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiVXNlciAxMjMiLCJ2aWRlbyI6eyJyb29tSm9pbiI6dHJ1ZSwicm9vbSI6InN1cHBvcnQtcm9vbSIsImNhblB1Ymxpc2giOnRydWUsImNhblN1YnNjcmliZSI6dHJ1ZSwiY2FuUHVibGlzaERhdGEiOnRydWV9LCJzdWIiOiJ1c2VyMTIzIiwiaXNzIjoiQVBJMm93VXM5NWFLRUU1IiwibmJmIjoxNzQ2OTU5Njk4LCJleHAiOjE3NDY5ODEyOTh9.OS7L82EBurvCzL0cNQ2ELTvOzZH6OTKMPiK0Cr03yY0",
                        this
                );
                isConnected = true;
                tvCallStatus.setText("Listening...");
                stt.startListening();
            }
        });
    }

    private void handleQuestion(String text) {
        runOnUiThread(() -> tvCallStatus.setText("Processing..."));

        viewModel.processQuestion("SimUser", text, response -> {
            runOnUiThread(() -> {
                tvDebug.append("\nAgent: " + response);
                tts.speak(response);
            });
        }, () -> {
            waitingForSupervisor = true;

            String escalationMessage = "Let me check with my supervisor and get back to you.";
            tvDebug.append("\nAgent: " + escalationMessage);
            tts.speak(escalationMessage);

            viewModel.listenForSupervisorAnswer(text, answer -> {
                waitingForSupervisor = false;
                tvDebug.append("\nSupervisor answered: " + answer);
                tts.speak(answer);
            });
        });
    }

    private void onTTSSpoken() {
        runOnUiThread(() -> {
            if (isActivityRunning) {
                tvCallStatus.setText("Listening...");
                stt.restart();
            }
        });
    }

    private void requestPermissions() {
        List<String> needed = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            needed.add(Manifest.permission.RECORD_AUDIO);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            needed.add(Manifest.permission.CAMERA);
        }

        if (!needed.isEmpty()) {
            permissionLauncher.launch(needed.toArray(new String[0]));
        }
    }

    @Override
    public void onRoomEvent(RoomEvent event) {
        Log.d("LiveKit", "Room event: " + event.getClass().getSimpleName());
        isConnected = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityRunning = false;
        disconnectLiveKit();
        stopSpeechHandlers();
    }

    private void disconnectLiveKit() {
        if (isConnected) {
            LiveKitManager.disconnect();
            isConnected = false;
        }
    }

    private void stopSpeechHandlers() {
        if (tts != null) tts.stop();
        if (stt != null) stt.stop();
    }

}
