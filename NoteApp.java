import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;

public class NoteApp {
    private JFrame frame;
    private JList<String> notesList;
    private JTextArea noteContentArea;
    private JTextField titleField, categoryField, searchField;
    private Connection conn;
    private DefaultListModel<String> notesListModel;

    public NoteApp() {
        // Initialize database
        initDatabase();

        // Main frame
        frame = new JFrame("Note Maker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout(10, 10));

        // Left panel for notes list and search
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.setPreferredSize(new Dimension(200, 0));

        searchField = new JTextField("Search notes...");
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search notes...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search notes...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchNotes();
            }
        });

        notesListModel = new DefaultListModel<>();
        notesList = new JList<>(notesListModel);
        notesList.addListSelectionListener(e -> loadSelectedNote());
        JScrollPane listScrollPane = new JScrollPane(notesList);

        leftPanel.add(new JLabel("Notes"), BorderLayout.NORTH);
        leftPanel.add(searchField, BorderLayout.CENTER);
        leftPanel.add(listScrollPane, BorderLayout.SOUTH);

        // Right panel for note editing
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        titleField = new JTextField("Note Title");
        titleField.setForeground(Color.GRAY);
        titleField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (titleField.getText().equals("Note Title")) {
                    titleField.setText("");
                    titleField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (titleField.getText().isEmpty()) {
                    titleField.setText("Note Title");
                    titleField.setForeground(Color.GRAY);
                }
            }
        });

        categoryField = new JTextField("Category");
        categoryField.setForeground(Color.GRAY);
        categoryField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (categoryField.getText().equals("Category")) {
                    categoryField.setText("");
                    categoryField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (categoryField.getText().isEmpty()) {
                    categoryField.setText("Category");
                    categoryField.setForeground(Color.GRAY);
                }
            }
        });

        inputPanel.add(new JLabel("Title"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Category"));
        inputPanel.add(categoryField);

        noteContentArea = new JTextArea("Enter your note here...");
        noteContentArea.setLineWrap(true);
        noteContentArea.setWrapStyleWord(true);
        noteContentArea.setForeground(Color.GRAY);
        noteContentArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (noteContentArea.getText().equals("Enter your note here...")) {
                    noteContentArea.setText("");
                    noteContentArea.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (noteContentArea.getText().isEmpty()) {
                    noteContentArea.setText("Enter your note here...");
                    noteContentArea.setForeground(Color.GRAY);
                }
            }
        });
        JScrollPane contentScrollPane = new JScrollPane(noteContentArea);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton saveButton = new JButton("Save Note");
        saveButton.addActionListener(e -> saveNote());

        JButton newButton = new JButton("New Note");
        newButton.addActionListener(e -> clearFields());

        JButton deleteButton = new JButton("Delete Note");
        deleteButton.addActionListener(e -> deleteNote());

        buttonPanel.add(saveButton);
        buttonPanel.add(newButton);
        buttonPanel.add(deleteButton);

        rightPanel.add(inputPanel, BorderLayout.NORTH);
        rightPanel.add(contentScrollPane, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.CENTER);

        // Load initial notes
        loadNotes();

        // Show frame
        frame.setVisible(true);
    }

    private void initDatabase() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:notes.db");
            Statement stmt = conn.createStatement();
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "category TEXT," +
                "content TEXT," +
                "created_at TIMESTAMP)"
            );
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "SQLite JDBC driver not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to initialize database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadNotes() {
        notesListModel.clear();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT title FROM notes ORDER BY created_at DESC");
            while (rs.next()) {
                notesListModel.addElement(rs.getString("title"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to load notes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedNote() {
        String selectedTitle = notesList.getSelectedValue();
        if (selectedTitle != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM notes WHERE title = ?");
                pstmt.setString(1, selectedTitle);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    titleField.setText(rs.getString("title"));
                    titleField.setForeground(Color.BLACK);
                    categoryField.setText(rs.getString("category"));
                    categoryField.setForeground(Color.BLACK);
                    noteContentArea.setText(rs.getString("content"));
                    noteContentArea.setForeground(Color.BLACK);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Failed to load note: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveNote() {
        String title = titleField.getText().trim();
        String category = categoryField.getText().trim();
        String content = noteContentArea.getText().trim();

        if (title.equals("Note Title") || content.equals("Enter your note here...") || title.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Title and content are required!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Check if note exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT id FROM notes WHERE title = ?");
            checkStmt.setString(1, title);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Update existing note
                PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE notes SET category = ?, content = ?, created_at = ? WHERE title = ?"
                );
                pstmt.setString(1, category.isEmpty() || category.equals("Category") ? "" : category);
                pstmt.setString(2, content);
                pstmt.setString(3, LocalDateTime.now().toString());
                pstmt.setString(4, title);
                pstmt.executeUpdate();
            } else {
                // Insert new note
                PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO notes (title, category, content, created_at) VALUES (?, ?, ?, ?)"
                );
                pstmt.setString(1, title);
                pstmt.setString(2, category.isEmpty() || category.equals("Category") ? "" : category);
                pstmt.setString(3, content);
                pstmt.setString(4, LocalDateTime.now().toString());
                pstmt.executeUpdate();
            }
            loadNotes();
            clearFields();
            JOptionPane.showMessageDialog(frame, "Note saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to save note: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteNote() {
        String selectedTitle = notesList.getSelectedValue();
        if (selectedTitle != null) {
            try {
                PreparedStatement pstmt = conn.prepareStatement("DELETE FROM notes WHERE title = ?");
                pstmt.setString(1, selectedTitle);
                pstmt.executeUpdate();
                loadNotes();
                clearFields();
                JOptionPane.showMessageDialog(frame, "Note deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Failed to delete note: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchNotes() {
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.equals("search notes...")) searchText = "";
        notesListModel.clear();
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                "SELECT title FROM notes WHERE title LIKE ? OR category LIKE ? OR content LIKE ?"
            );
            pstmt.setString(1, "%" + searchText + "%");
            pstmt.setString(2, "%" + searchText + "%");
            pstmt.setString(3, "%" + searchText + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                notesListModel.addElement(rs.getString("title"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to search notes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        titleField.setText("Note Title");
        titleField.setForeground(Color.GRAY);
        categoryField.setText("Category");
        categoryField.setForeground(Color.GRAY);
        noteContentArea.setText("Enter your note here...");
        noteContentArea.setForeground(Color.GRAY);
        notesList.clearSelection();
    }

    private void closeDatabase() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Run on EDT
        SwingUtilities.invokeLater(() -> {
            NoteApp app = new NoteApp();
            // Add shutdown hook to close database
            Runtime.getRuntime().addShutdownHook(new Thread(app::closeDatabase));
        });
    }
}