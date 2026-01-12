package echo.core;

import echo.models.Book;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Library {

    public List<Book> getAllBooks() throws SQLException {
        String sql = "SELECT * FROM books ORDER BY id";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Book> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public List<Book> searchBooks(String term) throws SQLException {
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ? ORDER BY id";
        String like = "%" + term + "%";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                List<Book> list = new ArrayList<>();
                while (rs.next()) list.add(mapRow(rs));
                return list;
            }
        }
    }

    public boolean borrowBook(int bookId, String borrowerName, int days) throws SQLException {
        String sql = "UPDATE books SET available = FALSE, borrower = ?, due_date = ? WHERE id = ? AND available = TRUE";
        LocalDate due = LocalDate.now().plusDays(days);
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, borrowerName);
            ps.setDate(2, Date.valueOf(due));
            ps.setInt(3, bookId);
            int updated = ps.executeUpdate();
            return updated == 1;
        }
    }

    public boolean returnBook(int bookId) throws SQLException {
        String sql = "UPDATE books SET available = TRUE, borrower = NULL, due_date = NULL WHERE id = ? AND available = FALSE";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            int updated = ps.executeUpdate();
            return updated == 1;
        }
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("isbn"),
                rs.getBoolean("available"),
                rs.getString("borrower"),
                rs.getDate("due_date")
        );
    }
}
