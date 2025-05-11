# FrontDesk AI Agent

This is a demo AI-powered virtual front desk agent app for businesses like salons, spas, or support centers. It uses LiveKit for real-time communication, integrates text-to-speech (TTS) and speech-to-text (STT) capabilities, and features a basic supervisor escalation mechanism for handling complex inquiries.

## Key Features

* **Live Two-Way Audio Communication**: Real-time voice interaction using LiveKit.
* **Automated Responses**: AI agent responds to common customer inquiries using a pre-defined knowledge base.
* **Supervisor Escalation**: Escalates complex questions to a human supervisor with context tracking.
* **Learning System**: Automatically adds supervisor-provided answers to the knowledge base for future responses.
* **Offline Mode with Default KB**: Provides fallback responses even when the dynamic knowledge base is unavailable.

---

## Project Structure

```
app/
├── view/
│   ├── MainActivity.java         # Main entry point for role selection (Client, Supervisor)
│   ├── AIAgentActivity.java      # Main AI agent call handling
│   ├── SupervisorActivity.java   # Supervisor UI for handling escalated questions
│   └── LearnedAnswersActivity.java # View learned answers (supervisor-provided)
├── viewmodel/
│   ├── AIAgentViewModel.java     # AI agent question processing and escalation logic
│   ├── SupervisorViewModel.java  # Handles unresolved question submissions
│   └── LearnedAnswersViewModel.java # Manages learned answers
├── repository/
│   ├── HelpRequestRepository.java # Handles supervisor interactions
│   └── KnowledgeBaseRepository.java # Manages the knowledge base
├── model/
│   ├── KnowledgeEntry.java       # Data model for KB entries
│   ├── Customer.java             # Data model for customer details
│   └── HelpRequest.java          # Data model for supervisor help requests
├── datasource/
│   ├── FirebaseSource.java       # Firebase database interactions
│   └── LiveKitManager.kt         # LiveKit connection handling (Kotlin)
├── listener/
│   └── RoomEventListener.java    # Event handling for LiveKit room events
└── app                           # For firebase and other app initialization
```

---

## Setup Instructions

1. **Clone the Repository**:

```bash
git clone https://github.com/ItsMeVikashKumarSingh/frontdesk-ai-agent.git
cd frontdesk-ai-agent
```

2. **Configure Firebase**:

    * Add your Firebase project credentials to `google-services.json`.
3. **Configure LiveKit**:

    * Replace the placeholder token and server URL in `AIAgentActivity.java`.
4. **Run the App**:

    * Build and run the app on an Android device or emulator.

---

## Key Components

### 1. AI Agent (AIAgentActivity.java)

* Manages real-time audio communication.
* Uses TTS for responses and STT for user input.
* Escalates complex questions to supervisors when necessary.

### 2. Supervisor Panel (SupervisorActivity.java)

* Lists unresolved customer questions.
* Allows supervisors to respond and update the knowledge base.

### 3. Knowledge Base (AIAgentViewModel.java)

* Handles AI response generation using a predefined knowledge base.
* Adds supervisor responses to the knowledge base for future reference.

### 4. Learning System (LearnedAnswersViewModel.java)

* Dynamically updates the AI agent’s knowledge without requiring app updates.

---

## Future Improvements

* Context tracking for better response accuracy.
* Improved multi-word partial matching algorithm.
* Enhanced UI for better user experience.
* Integration with external CRM systems.

---

## Known Issues

* Limited error handling for network disruptions.
* Basic UI without extensive styling.
* **Mic Not Stopping on Activity Exit** - Current version may continue recording after activity exit (needs fix).

---

## Contributing

Feel free to contribute by opening issues or submitting pull requests. Please follow the contribution guidelines.

---

## License

This project is licensed under the MIT License.
