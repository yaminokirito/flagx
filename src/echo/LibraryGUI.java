package echo;

import echo.core.Library;
import echo.models.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LibraryGUI extends JFrame {
    private final Library library = new Library();
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField searchField;

    public LibraryGUI() {
        super("EchoShelf - Library");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // Top controls
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton showAllBtn = new JButton("Show All Books");
        JButton searchBtn = new JButton("Search");
        searchField = new JTextField(20);
        JButton borrowBtn = new JButton("Borrow Book");
        JButton returnBtn = new JButton("Return Book");

        top.add(showAllBtn);
        top.add(new JLabel("Search:"));
        top.add(searchField);
        top.add(searchBtn);
        top.add(borrowBtn);
        top.add(returnBtn);

        // Table
        String[] cols = {"ID", "Title", "Author", "ISBN", "Available", "Borrower", "Due Date"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Actions
        showAllBtn.addActionListener(e -> loadAll());
        searchBtn.addActionListener(e -> doSearch());
        borrowBtn.addActionListener(e -> doBorrow());
        returnBtn.addActionListener(e -> doReturn());

        // initial load
        loadAll();
    }

    private void loadAll() {
        try {
            List<Book> books = library.getAllBooks();
            refreshTable(books);
        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void doSearch() {
        String q = searchField.getText().trim();
        if (q.isEmpty()) {
            loadAll();
            return;
        }
        try {
            List<Book> books = library.searchBooks(q);
            refreshTable(books);
        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void doBorrow() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a book row first.");
            return;
        }
        int bookId = (int) tableModel.getValueAt(row, 0);
        boolean available = Boolean.parseBoolean(tableModel.getValueAt(row, 4).toString());
        if (!available) {
            JOptionPane.showMessageDialog(this, "Book is already borrowed.");
            return;
        }
        String borrower = JOptionPane.showInputDialog(this, "Borrower name:");
        if (borrower == null || borrower.trim().isEmpty()) return;
        String daysStr = JOptionPane.showInputDialog(this, "Days to borrow (default 14):", "14");
        int days = 14;
        try { days = Integer.parseInt(daysStr); } catch (Exception ignored) {}
        try {
            boolean ok = library.borrowBook(bookId, borrower.trim(), days);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Book borrowed successfully.");
                loadAll();
            } else {
                JOptionPane.showMessageDialog(this, "Could not borrow (maybe already borrowed).");
                loadAll();
            }
        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void doReturn() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a book row first.");
            return;
        }
        int bookId = (int) tableModel.getValueAt(row, 0);
        boolean available = Boolean.parseBoolean(tableModel.getValueAt(row, 4).toString());
        if (available) {
            JOptionPane.showMessageDialog(this, "Book is not currently borrowed.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Return selected book?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            boolean ok = library.returnBook(bookId);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Book returned.");
                loadAll();
            } else {
                JOptionPane.showMessageDialog(this, "Could not return book.");
                loadAll();
            }
        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void refreshTable(List<Book> books) {
        tableModel.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;
        for (Book b : books) {
            String due = b.getDueDate() == null ? "" : b.getDueDate().toLocalDate().format(fmt);
            tableModel.addRow(new Object[]{
                    b.getId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getIsbn(),
                    b.isAvailable(),
                    b.getBorrower() == null ? "" : b.getBorrower(),
                    due
            });
        }
    }

    private void showError(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
