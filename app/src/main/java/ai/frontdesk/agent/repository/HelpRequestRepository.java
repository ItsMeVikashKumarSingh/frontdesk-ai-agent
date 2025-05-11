package ai.frontdesk.agent.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import ai.frontdesk.agent.datasource.FirebaseSource;
import ai.frontdesk.agent.model.HelpRequest;

public class HelpRequestRepository {

    private final FirebaseSource firebaseSource;
    private final MutableLiveData<List<HelpRequest>> pendingRequestsLiveData = new MutableLiveData<>();

    public HelpRequestRepository() {
        firebaseSource = FirebaseSource.getInstance();
        listenToPendingRequests();
    }

    public void createHelpRequest(String question, String customerName) {
        String message = "Hey, I need help answering \"" + question + "\".";
        firebaseSource.createHelpRequest(question, customerName, message);
    }

    public void answerHelpRequest(String question, String answer) {
        firebaseSource.answerHelpRequest(question, answer);
    }

    public void listenForUnresolved(java.util.function.Consumer<List<String>> callback) {
        FirebaseSource.getInstance()
                .listenToPendingRequests((snapshots, e) -> {
                    if (e != null || snapshots == null) return;

                    List<String> questions = new ArrayList<>();
                    for (com.google.firebase.firestore.DocumentSnapshot doc : snapshots.getDocuments()) {
                        String status = doc.getString("status");
                        if ("pending".equals(status)) {
                            String question = doc.getString("question");
                            if (question != null) questions.add(question);
                        }
                    }
                    callback.accept(questions);
                });
    }

    public void listenForAnswer(String question, java.util.function.Consumer<String> callback) {
        FirebaseSource.getInstance()
                .listenToAnswers(question, (docSnapshot) -> {
                    if (docSnapshot != null && docSnapshot.contains("answer")) {
                        String answer = docSnapshot.getString("answer");
                        callback.accept(answer);
                    }
                });
    }


    public void resolveHelpRequest(String requestId, String answer) {
        firebaseSource.resolveHelpRequest(requestId, answer);
    }

    public void markRequestUnresolved(String requestId) {
        firebaseSource.markRequestUnresolved(requestId);
    }

    private void listenToPendingRequests() {
        firebaseSource.listenToPendingRequests(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null || snapshots == null) {
                    return;
                }

                List<HelpRequest> list = new ArrayList<>();
                for (com.google.firebase.firestore.DocumentSnapshot doc : snapshots.getDocuments()) {
                    HelpRequest req = doc.toObject(HelpRequest.class);
                    if (req != null) list.add(req);
                }

                pendingRequestsLiveData.postValue(list);
            }
        });
    }

    public LiveData<List<HelpRequest>> getPendingRequestsLiveData() {
        return pendingRequestsLiveData;
    }
}
