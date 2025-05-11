package ai.frontdesk.agent.model;

public class KnowledgeEntry {
    private String id;
    private String question;
    private String answer;

    public KnowledgeEntry() {
        // Needed for Firestore
    }

    public KnowledgeEntry(String id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public KnowledgeEntry(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }


    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}
