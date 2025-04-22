🧠 Study Buddy – Backend Architecture
This document outlines the backend architecture for the Study Buddy application. It follows a microservices approach using GraphQL (primarily), RabbitMQ for communication, and is designed to be run entirely in Docker (no cloud, no auth, no moderation, no external integration).

📦 Services Overview
1. Question Service
Purpose: Accepts GraphQL mutations to create/store questions.

Data Storage: MongoDB (optimized for tag-based indexing).

Event Publisher: Sends messages via RabbitMQ on data changes.

Fields Stored:

json
Copy
Edit
{
  "question": "...",
  "answer": "...",
  "description": "...",
  "sideNote": "...",
  "mainTag": "...",
  "tags": ["..."],
  "type": "QUESTION | DEPENDENCY | WIKI | MIGRATE",
  "notes": ["..."],
  "createdAt": "..."
}
2. Indexing Service
Purpose:

Subscribes to RabbitMQ events.

Indexes question data into Elasticsearch.

If type=DEPENDENCY, stores graph relation in Neo4j.

3. Analytics Service
Purpose:

Subscribes to the same events.

Processes data for analytics (TBD: suggest Apache Flink, Kafka Streams, or simple Java with Mongo aggregations).

4. Search Service
Purpose:

Exposes GraphQL queries for search.

Fetches from MongoDB or Elasticsearch (depending on performance needs).

5. Export Service
Purpose:

Exposes GET endpoint to fetch all data from MongoDB.

Saves/export it as file.json to be downloaded in the browser.

🔁 Data Flow
mermaid
Copy
Edit
graph LR
    FE[Frontend (GraphQL Client)] -->|Mutation| QS[Question Service]
    QS -->|Save| MDB[(MongoDB)]
    QS -->|Event| MQ[(RabbitMQ)]
    MQ --> IDX[Indexing Service]
    MQ --> ANA[Analytics Service]
    IDX -->|To Elasticsearch| ES[(Elasticsearch)]
    IDX -->|If DEPENDENCY → Neo4j| NJ[(Neo4j)]
    FE -->|Search Query| SS[Search Service]
    SS -->|Query| MDB
    FE -->|GET| EXP[Export Service]
    EXP -->|Dump All| MDB
🗃️ MongoDB Indexing Strategy
🔍 To optimize search on:
question, mainTag, tags

✅ Index Suggestions:
js
Copy
Edit
db.questions.createIndex({ question: "text", mainTag: "text", tags: "text" })
🧩 From Java Side (Spring Data MongoDB):
java
Copy
Edit
@TextIndexed
private String question;

@TextIndexed
private String mainTag;

@TextIndexed
private List<String> tags;
Use @CompoundIndexes or @TextIndexed annotations to configure search-ready indexes.

🌐 WebSocket Use Case
A tiny scenario to use WebSocket:

Notify frontend instantly after a new question is added and indexed (success message or real-time UI update).

Example:
QuestionService emits WebSocket event on POST completion.

Frontend listens via WebSocket to update UI with the newly added entry.

