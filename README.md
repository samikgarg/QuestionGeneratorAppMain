# 📚 Question Tracker App 🎯

## 📌 Description
The **Question Tracker App** is a **smart study companion** designed to **track, manage, and analyze** a database of **questions across multiple subjects and topics**. This application enhances learning efficiency by **selecting questions dynamically based on past performance**, ensuring that users focus more on topics where they struggle. 

With powerful **filtering, sorting, search, and analytics features**, the app provides **actionable insights** to help users improve their accuracy and meet study goals.

---

## 🚀 Features

### ✅ **Question Selection & Testing**
- **Random Question Selection**:
  - Clicking **"New Question"** retrieves a **random question** from the database.
  - The question appears **only once per session** unless it's the **only available question**.
- **Answer Submission**:
  - Users select an answer and receive **instant feedback**.
  - If incorrect, the **correct answer is displayed**.
  - Questions that are **answered incorrectly appear more frequently**.
- **Error Handling**:
  - If the **Submit button** is clicked without selecting an answer, an **error message is shown**.

---

### 🔍 **Filtering, Sorting & Searching**
- **Subject & Topic Filtering**:
  - **Only topics relevant** to the selected subject appear in the **topics combo box**.
- **Search Functionality**:
  - Users can search for questions **by keywords**, filtering **questions by question text, topic, or subject**.
- **Sorting Options**:
  - Questions can be **sorted dynamically** by clicking on **column headers**.

---

### 🛠️ **Question Database Management**
- **Edit Questions Page**:
  - Displays **all stored questions** in a structured **table format**.
  - Questions can be **filtered, searched, and sorted**.
- **Adding New Questions**:
  - Users **enter the question, multiple-choice options, the correct answer, and the topic/subject**.
  - **Validation System** prevents:
    - Empty fields.
    - Selecting a correct answer before entering options.
- **Editing & Deleting Questions**:
  - **Edit Questions**: Click the **pencil icon** to update a question.
  - **Delete Questions**: Click the **bin icon** to remove a question.
  - **Auto-Update Topics & Subjects**:
    - If a deleted question was the **only one in a subject**, that subject is **removed from dropdown filters**.
- **Dynamic Subject & Topic Management**:
  - Users can **add new topics and subjects** when creating/editing questions.
  - If a topic is mistakenly assigned to another subject, an **error message appears**.

---

### 📊 **Performance Analysis & Goal Tracking**
- **Advanced Question Filtering**:
  - Users filter questions based on:
    - **Subject & Topic**
    - **Question String** (equals, contains, starts with, ends with)
    - **Number of Times Asked** (with **inequality filters**)
  - Filters use **AND/OR logic**, where **AND conditions have precedence** over OR.
- **SQL Query Execution**:
  - Filters dynamically **build SQL queries** for precise **question retrieval and analysis**.
- **Accuracy Analysis**:
  - The **average accuracy rate** across filtered questions is displayed.
  - Users can set a **goal accuracy rate**.
- **Goal Achievement Insights**:
  - If the **goal is higher than the average**, the **number of correct answers needed** to reach it is calculated.
  - If the **goal is lower than the average**, the **number of mistakes allowed** is shown.
  - The app provides **warnings** if the goal is:
    - **Unreachable** (no number of correct answers will help).
    - **Guaranteed** (no number of incorrect answers will drop accuracy below the goal).

---

## 🛠️ Technologies Used
- **Java** (Backend Logic)
- **Java Swing & AWT** (User Interface)
- **SQLite** (Database Management)
- **Dynamic Sorting & Filtering Mechanism**

---
