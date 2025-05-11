package ai.frontdesk.agent.model;

import com.google.firebase.Timestamp;

public class HelpRequest {
    private String id;
    private String question;
    private String customerName;
    private String status; // "pending", "resolved", "unresolved"
    private String answer;
    private String message; // ✅ added for supervisor prompt message
    private Timestamp createdAt;
    private Timestamp resolvedAt;

    public HelpRequest() {
        // Needed for Firestore deserialization
    }

    public HelpRequest(String id, String question, String customerName, String status, Timestamp createdAt, String message) {
        this.id = id;
        this.question = question;
        this.customerName = customerName;
        this.status = status;
        this.createdAt = createdAt;
        this.message = message;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Timestamp resolvedAt) { this.resolvedAt = resolvedAt; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
