# Enhanced ChatGPT Integration – Backend Project

This project provides a robust integration with the ChatGPT (OpenAI) API using clean architectural patterns like Proxy and Facade, making the system more reliable, modular, and maintainable.

---

##  Key Features

###  Intelligent ChatGPT Flow

- Structured POST requests to the /v1/chat/completions endpoint.
- Input is validated, cleaned, and improved before reaching the AI.
- Simplified, extracted, and formatted responses to the client.

---

## Design Patterns Used

### Improved Proxy

- Response caching with a configurable TTL (Time to Live).
- Detailed logging of:
  - API response times
  - Cache usage
  - Fallback behavior when OpenAI is unreachable
- Friendly fallback message in case of API failure.

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

## REST API

This project exposes a single REST endpoint:

### POST `/api/ai/generate`

- **Description:**
  - Endpoint to interact with ChatGPT, including input validation, cleaning, and improvement before sending the request to OpenAI.
- **Request Body:**
  - JSON object with a field `input` (string) containing the user's prompt.
- **Response:**
  - Returns the AI's response as a plain string, or a user-friendly error message if validation fails.
- **Example:**
  ```json
  {
    "input": "How do I implement a proxy pattern in Java?"
  }
  ```

---
### Deployed App
https://labchatgptimplementation134-h0h2c0hyb5bnbnat.canadacentral-01.azurewebsites.net/
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
### Requirements
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

## Design 

![diseño](assets/diagrama.png)

## Authors
* **Daniel Aldana** - [GitHub](https://github.com/Daniel-Aldana10)

