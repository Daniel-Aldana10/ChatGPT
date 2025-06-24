# Enhanced ChatGPT Integration – Backend Project

This project provides a robust integration with the ChatGPT (OpenAI) API using clean architectural patterns like Proxy, Chain of Responsibility, and Facade, making the system more reliable, modular, and maintainable.

---

##  Key Features

###  Intelligent ChatGPT Flow

- Structured POST requests to the /v1/chat/completions endpoint.
- Cleaned, validated input before reaching the AI.
- Simplified, extracted, and formatted responses to the client.

---

## 🛠 Design Patterns Used

### Improved Proxy

- Response caching with a configurable TTL (Time to Live).
- Detailed logging of:
  - API response times
  - Cache usage
  - Fallback behavior when OpenAI is unreachable
- Friendly fallback message in case of API failure.

---

### Chain of Responsibility

- Input is processed by sequential processors:
  - `ValidateInputProcessor`
  - `CleanInputProcessor`
  - `ImproveInputProcessor`
- Modular: Easily plug in more processors (e.g. for intent detection, spellcheck, etc.)

---

###  Facade (ChatGptFacade)

- Central entry point to the ChatGPT flow.
- Orchestrates input validation, AI call, and response handling.
- Manages exceptions gracefully.

---

###  Centralized Error Handling

- Blocks:
  - Empty inputs
  - Offensive language
  - Nonsense or junk text (e.g., "asdf", "lorem ipsum")
- Returns clear, user-friendly error messages
- Prevents backend crashes due to OpenAI/API issues

---

##  Project Structure
```
com.aygo.aiintegration
│
├── analyzer # Input processing components
├── adapter # Interface and ChatGPT adapter (includes proxy)
├── controller # REST controllers
├── service # Core logic orchestration
├── ChatRequest.java # Input model
└── ChatGptFacade.java # ChatGPT logic facade
```
### Requeriments
- Java 17+

- Spring Boot 3.x

- Maven or Gradle

- Internet connection (for OpenAI access)
## Installing
Clone the repository:
```sh
git clone <https://github.com/Daniel-Aldana10/ChatGPT>
cd ChatGPT
```
Build the project using Maven:
```sh
mvn clean install
```
---

## Configuration Instructions

### Create a `.env` File in the Project Root

Create a file named `.env` and include the following:

api.chatgpt.key=xx-xxxxxxxxxxxxxxxxxxxxxxxxxxxx

api.chatgpt.URL=https://api.openai.com/v1/chat/completions

## Authors
* **Daniel Aldana** - [GitHub](https://github.com/Daniel-Aldana10)

