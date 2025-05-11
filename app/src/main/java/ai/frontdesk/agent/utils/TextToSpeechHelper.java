package ai.frontdesk.agent.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.Locale;

public class TextToSpeechHelper {

    private TextToSpeech tts;
    private final Runnable onDoneCallback;

    public TextToSpeechHelper(Context context, Runnable onDoneCallback) {
        this.onDoneCallback = onDoneCallback;
        this.tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {}

                    @Override
                    public void onDone(String utteranceId) {
                        if (onDoneCallback != null) onDoneCallback.run();
                    }

                    @Override
                    public void onError(String utteranceId) {}
                });
            }
        });
    }

    public void speak(String text) {
        if (tts != null) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utteranceId");
        }
    }

    public void stop() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
