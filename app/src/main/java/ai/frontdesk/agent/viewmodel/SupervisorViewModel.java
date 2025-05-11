package ai.frontdesk.agent.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ai.frontdesk.agent.repository.HelpRequestRepository;

import java.util.List;

public class SupervisorViewModel extends ViewModel {

    private final HelpRequestRepository requestRepo = new HelpRequestRepository();
    private final MutableLiveData<List<String>> unresolved = new MutableLiveData<>();

    public SupervisorViewModel() {
        requestRepo.listenForUnresolved(unresolved::postValue);
    }

    public LiveData<List<String>> getUnresolvedQuestions() {
        return unresolved;
    }

    public void submitAnswer(String question, String answer) {
        requestRepo.answerHelpRequest(question, answer);
    }
}
