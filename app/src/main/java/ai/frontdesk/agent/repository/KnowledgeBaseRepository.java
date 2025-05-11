package ai.frontdesk.agent.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import ai.frontdesk.agent.datasource.FirebaseSource;
import ai.frontdesk.agent.model.KnowledgeEntry;

public class KnowledgeBaseRepository {

    private final FirebaseSource firebaseSource;
    private final MutableLiveData<List<KnowledgeEntry>> knowledgeListLiveData = new MutableLiveData<>();

    public KnowledgeBaseRepository() {
        firebaseSource = FirebaseSource.getInstance();
        observeKnowledgeBase();
    }

    public void addToKnowledgeBase(String question, String answer) {
        firebaseSource.addToKnowledgeBase(question, answer);
    }

    public LiveData<List<KnowledgeEntry>> getKnowledgeListLiveData() {
        return knowledgeListLiveData;
    }

    public void addKnowledgeEntry(KnowledgeEntry entry) {
        FirebaseFirestore.getInstance()
                .collection("knowledge_base")
                .add(entry);
    }


    private void observeKnowledgeBase() {
        firebaseSource.fetchKnowledgeEntries(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null || snapshots == null) {
                    return;
                }

                List<KnowledgeEntry> list = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshots) {
                    KnowledgeEntry entry = doc.toObject(KnowledgeEntry.class);
                    if (entry != null) list.add(entry);
                }

                knowledgeListLiveData.postValue(list);
            }
        });
    }
}
