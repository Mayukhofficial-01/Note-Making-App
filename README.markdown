```markdown
# 📝 Note Maker

**Note Maker** is a user-friendly, single-file Java application designed to streamline digital note-taking, promoting productivity and organization. Built with Swing for a lightweight GUI and SQLite for persistent storage, it allows users to create, edit, delete, and search notes with ease. Whether you're jotting down ideas, organizing tasks, or categorizing thoughts, Note Maker is your go-to tool for efficient note management.

---

## ✨ Features

- **Create and Edit Notes**: Write and update notes with titles, optional categories, and content.
- **Search Functionality**: Quickly find notes by searching titles, categories, or content.
- **Delete Notes**: Remove unwanted notes with a single click.
- **Persistent Storage**: Notes are saved in a local SQLite database (`notes.db`).
- **User-Friendly Interface**: Intuitive Swing-based GUI with placeholder text and responsive layout.
- **Lightweight and Single-File**: Entire application is contained in `NoteApp.java`, minimizing setup complexity.

---

## 📂 Project Structure

```
NoteMaker/
├── NoteApp.java
├── sqlite-jdbc-3.46.1.jar
├── slf4j-api-2.0.13.jar
├── slf4j-simple-2.0.13.jar
├── notes.db (generated after first run)
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

- **Java 17 or later**: Verify with `java --version`.
- **SQLite JDBC Driver**: Download `sqlite-jdbc-3.46.1.jar` from [GitHub](https://github.com/xerial/sqlite-jdbc).
- **SLF4J Libraries**: Download `slf4j-api-2.0.13.jar` and `slf4j-simple-2.0.13.jar` from [SLF4J](https://www.slf4j.org/download.html).

### Installation

1. **Clone or Download**:
   - Clone this repository or download the project files.
   - Ensure `NoteApp.java`, `sqlite-jdbc-3.46.1.jar`, `slf4j-api-2.0.13.jar`, and `slf4j-simple-2.0.13.jar` are in the same directory.

2. **Compile the Application**:
   In PowerShell or Command Prompt:
   ```bash
   javac -cp ".;sqlite-jdbc-3.46.1.jar;slf4j-api-2.0.13.jar;slf4j-simple-2.0.13.jar" NoteApp.java
   ```

3. **Run the Application**:
   ```bash
   java -cp ".;sqlite-jdbc-3.46.1.jar;slf4j-api-2.0.13.jar;slf4j-simple-2.0.13.jar" NoteApp
   ```

   - On Linux/macOS, use `:` instead of `;`:
     ```bash
     java -cp ".:sqlite-jdbc-3.46.1.jar:slf4j-api-2.0.13.jar:slf4j-simple-2.0.13.jar" NoteApp
     ```

4. **Alternative: Use Maven**:
   - Create a `pom.xml` (see [Dependencies](#dependencies)) and place `NoteApp.java` in `src/main/java/com/example/`.
   - Run:
     ```bash
     mvn clean compile
     mvn exec:java -Dexec.mainClass="com.example.NoteApp"
     ```

---

## 🖥️ Usage

1. **Launch the Application**: Run the command above to open the Note Maker GUI.
2. **Create a Note**:
   - Enter a title, optional category, and content in the right panel.
   - Click **Save Note** to store the note in the database.
3. **Edit a Note**:
   - Select a note from the left panel list.
   - Modify the title, category, or content, then click **Save Note**.
4. **Delete a Note**:
   - Select a note and click **Delete Note**.
5. **Search Notes**:
   - Type in the search bar to filter notes by title, category, or content.
6. **Clear Fields**:
   - Click **New Note** to reset the input fields.

---

## 📈 Data Flow Diagram

Below is a Level-0 Data Flow Diagram (DFD) illustrating the flow of data in Note Maker:

```mermaid
graph TD
    A[User] -->|Input: Create/Edit/Delete/Search| B[Note Maker GUI]
    B -->|User Actions| C[Application Logic]
    C -->|CRUD Operations| D[SQLite Database]
    D -->|Query Results| C
    C -->|Display Notes| B
    B -->|Show Notes/Feedback| A
```

- **User**: Interacts with the GUI to input notes or search queries.
- **Note Maker GUI**: Swing interface displaying note list and input fields.
- **Application Logic**: Handles note creation, editing, deletion, and searching.
- **SQLite Database**: Stores notes in `notes.db` with fields for title, category, content, and timestamp.

---

## 🛠️ Dependencies

- **SQLite JDBC**: `sqlite-jdbc-3.46.1.jar` for database operations.
- **SLF4J**: `slf4j-api-2.0.13.jar` and `slf4j-simple-2.0.13.jar` for logging.
- **Java 17+**: Provides Swing and core libraries.

For Maven, use this `pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>note-app</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.46.1</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.13</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>2.0.13</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## 🐞 Troubleshooting

- **"SQLite JDBC driver not found"**:
  - Ensure `sqlite-jdbc-3.46.1.jar` is in the project directory and included in the classpath.
  - Verify the JAR version matches the command.
- **"NoClassDefFoundError: org/slf4j/LoggerFactory"**:
  - Add `slf4j-api-2.0.13.jar` and `slf4j-simple-2.0.13.jar` to the classpath.
- **Java Version Issues**:
  - Confirm Java 17+ with `java --version`.
  - Install from [Adoptium](https://adoptium.net/) if needed.
- **PowerShell Classpath Issues**:
  - Use quotes around the classpath: `".;sqlite-jdbc-3.46.1.jar;slf4j-api-2.0.13.jar;slf4j-simple-2.0.13.jar"`.
  - Alternatively, use Command Prompt.

---

## 📚 Notes

- The application creates a `notes.db` file in the project directory for note storage.
- Built with Swing for zero external GUI dependencies (part of JDK).
- Single-file design (`NoteApp.java`) for simplicity and ease of maintenance.

---

## 🤝 Contributing

Contributions are welcome! Fork the repository, make improvements, and submit a pull request.

---

## 📜 License

This project is licensed under the MIT License.

---

**Note Maker** - Organize your thoughts, boost your productivity! 🚀
```