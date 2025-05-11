package ai.frontdesk.agent.datasource;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import ai.frontdesk.agent.model.HelpRequest;
import ai.frontdesk.agent.model.KnowledgeEntry;

public class FirebaseSource {

    private static final String TAG = "FirebaseSource";
    private static FirebaseSource instance;
    private final FirebaseFirestore db;

    private FirebaseSource() {
        db = FirebaseFirestore.getInstance();
    }

    public static FirebaseSource getInstance() {
        if (instance == null) {
            instance = new FirebaseSource();
        }
        return instance;
    }

    public CollectionReference getHelpRequestsCollection() {
        return db.collection("help_requests");
    }

    public CollectionReference getKnowledgeBaseCollection() {
        return db.collection("knowledge_base");
    }

    public void createHelpRequest(String question, String customerName, String message) {
        String id = UUID.randomUUID().toString();

        HelpRequest request = new HelpRequest(
                id, question, customerName, "pending", Timestamp.now(), message
        );

        getHelpRequestsCollection().document(id).set(request)
                .addOnSuccessListener(unused -> Log.d(TAG, "Help request created"))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to create help request", e));
    }


    public void resolveHelpRequest(String requestId, String answer) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "resolved");
        updates.put("answer", answer);
        updates.put("resolvedAt", Timestamp.now());

        getHelpRequestsCollection().document(requestId)
                .update(updates)
                .addOnSuccessListener(unused -> Log.d(TAG, "Request resolved"))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to resolve request", e));
    }

    public void markRequestUnresolved(String requestId) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "unresolved");
        updates.put("resolvedAt", Timestamp.now());

        getHelpRequestsCollection().document(requestId)
                .update(updates)
                .addOnSuccessListener(unused -> Log.d(TAG, "Marked as unresolved"))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to mark as unresolved", e));
    }

    public void listenToAnswers(String question, java.util.function.Consumer<DocumentSnapshot> callback) {
        FirebaseFirestore.getInstance()
                .collection("help_requests")
                .whereEqualTo("question", question)
                .whereEqualTo("status", "resolved")
                .limit(1)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null || snapshots.isEmpty()) return;
                    callback.accept(snapshots.getDocuments().get(0));
                });
    }


    public void addToKnowledgeBase(String question, String answer) {
        String id = UUID.randomUUID().toString();
        KnowledgeEntry entry = new KnowledgeEntry(id, question, answer);

        getKnowledgeBaseCollection().document(id).set(entry)
                .addOnSuccessListener(unused -> Log.d(TAG, "Knowledge base updated"))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to update knowledge base", e));
    }

    public void listenToPendingRequests(EventListener<QuerySnapshot> listener) {
        getHelpRequestsCollection()
                .whereEqualTo("status", "pending")
                .orderBy("createdAt", Query.Direction.ASCENDING)
                .addSnapshotListener(listener);
    }

    public void fetchKnowledgeEntries(EventListener<QuerySnapshot> listener) {
        getKnowledgeBaseCollection()
                .orderBy("question", Query.Direction.ASCENDING)
                .addSnapshotListener(listener);
    }
    public void answerHelpRequest(String question, String answer) {
        FirebaseFirestore.getInstance()
                .collection("help_requests")
                .whereEqualTo("question", question)
                .whereEqualTo("status", "pending")
                .limit(1)
                .get()
                .addOnSuccessListener(query -> {
                    if (!query.isEmpty()) {
                        DocumentSnapshot doc = query.getDocuments().get(0);
                        doc.getReference().update("answer", answer, "status", "resolved");
                    }
                });
    }

}
