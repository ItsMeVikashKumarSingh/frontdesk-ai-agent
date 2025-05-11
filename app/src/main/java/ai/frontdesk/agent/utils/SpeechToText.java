package ai.frontdesk.agent.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

public class SpeechToText {

    private final SpeechRecognizer recognizer;
    private final OnTextRecognizedListener listener;
    private final Context context;
    private boolean isListening = false;

    public interface OnTextRecognizedListener {
        void onTextRecognized(String text);
        void onSpeechTimeout();
    }

    public SpeechToText(Context context, OnTextRecognizedListener listener) {
        this.context = context;
        this.listener = listener;
        recognizer = SpeechRecognizer.createSpeechRecognizer(context);

        recognizer.setRecognitionListener(new RecognitionListener() {
            @Override public void onReadyForSpeech(Bundle params) {
                isListening = true;
            }

            @Override public void onBeginningOfSpeech() {}

            @Override public void onRmsChanged(float rmsdB) {}

            @Override public void onBufferReceived(byte[] buffer) {}

            @Override public void onEndOfSpeech() {
                isListening = false;
            }

            @Override public void onError(int error) {
                Log.d("SpeechToText", "Speech error: " + error);
                isListening = false;
                restart();
            }

            @Override
            public void onResults(Bundle results) {
                isListening = false;
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    listener.onTextRecognized(matches.get(0));
                } else {
                    listener.onSpeechTimeout();
                }
            }

            @Override public void onPartialResults(Bundle partialResults) {}

            @Override public void onEvent(int eventType, Bundle params) {}
        });
    }

    public void startListening() {
        if (isListening) return;

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        recognizer.startListening(intent);
    }

    public void restart() {
        stop();
        startListening();
    }

    public void stop() {
        recognizer.cancel();
        isListening = false;
    }
}
