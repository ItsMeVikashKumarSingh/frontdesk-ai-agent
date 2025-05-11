package ai.frontdesk.agent.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import ai.frontdesk.agent.repository.KnowledgeBaseRepository;
import ai.frontdesk.agent.model.KnowledgeEntry;
import java.util.List;

public class LearnedAnswersViewModel extends ViewModel {
    private final KnowledgeBaseRepository repo = new KnowledgeBaseRepository();

    public LiveData<List<KnowledgeEntry>> getLearnedAnswers() {
        return repo.getKnowledgeListLiveData();
    }
}