// ===== File: AIAgentViewModel.java (patched with Luxe Salon KB + multi-word partial match) =====
package ai.frontdesk.agent.viewmodel;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import ai.frontdesk.agent.model.KnowledgeEntry;
import ai.frontdesk.agent.repository.HelpRequestRepository;
import ai.frontdesk.agent.repository.KnowledgeBaseRepository;

public class AIAgentViewModel extends ViewModel {

    private final KnowledgeBaseRepository kbRepo = new KnowledgeBaseRepository();
    private final HelpRequestRepository requestRepo = new HelpRequestRepository();

    // ✅ Luxé Salon & Spa knowledge base
    private final List<KnowledgeEntry> defaultKB = new ArrayList<>() {{
        add(new KnowledgeEntry("What are your business hours", "Our hours are Monday-Friday 9am-8pm, Saturday 10am-6pm, and we’re closed on Sundays."));
        add(new KnowledgeEntry("Where are you located", "We are located at 123 Bliss Avenue, Cityville, CA 90210."));
        add(new KnowledgeEntry("How do I contact you", "You can reach us at +1 (555) 123-4567 or info@luxesalon.com."));
        add(new KnowledgeEntry("What services do you offer", "We offer haircuts, styling, coloring, nails, facials, massages, waxing, and bridal packages."));
        add(new KnowledgeEntry("What are your prices", "Haircuts range from $50-$150, nails $30-$100, facials $80-$200, and bridal packages start at $500."));
        add(new KnowledgeEntry("Do you offer any discounts", "Yes, we have monthly membership discounts and free consultations for new clients."));
        add(new KnowledgeEntry("Do you offer bridal packages", "Yes, we do! Our bridal packages start at $500 and include hair, makeup, and more."));
    }};

    public void processQuestion(String user, String question, Consumer<String> onAnswer, Runnable onEscalate) {
        // Fetch the live data from the KnowledgeBaseRepository
        List<KnowledgeEntry> kbList = kbRepo.getKnowledgeListLiveData().getValue();

        // If the knowledge base is empty, seed it with default entries
        if (kbList == null || kbList.isEmpty()) {
            kbList = new ArrayList<>(defaultKB);

            // Add default entries to Firebase
            for (KnowledgeEntry entry : defaultKB) {
                kbRepo.addKnowledgeEntry(new KnowledgeEntry(entry.getQuestion(), entry.getAnswer()));
            }

            // Return early to avoid attempting a match before data is loaded
            onEscalate.run();
            return;
        }

        // Normalize the user question
        String normalized = question.toLowerCase().replaceAll("[^a-z0-9 ]", "").trim();

        // Perform exact matching
        for (KnowledgeEntry entry : kbList) {
            String entryQuestion = entry.getQuestion().toLowerCase().replaceAll("[^a-z0-9 ]", "").trim();
            if (entryQuestion.equals(normalized)) {
                onAnswer.accept(entry.getAnswer());
                return;
            }
        }

        // If no match found, escalate to supervisor
        requestRepo.createHelpRequest(question, user);
        onEscalate.run();
    }


    public void listenForSupervisorAnswer(String question, Consumer<String> onAnswer) {
        requestRepo.listenForAnswer(question, answer -> {
            kbRepo.addKnowledgeEntry(new KnowledgeEntry(question, answer));
            onAnswer.accept(answer);
        });
    }
}
