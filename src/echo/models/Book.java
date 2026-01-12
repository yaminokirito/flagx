package echo.models;

import java.sql.Date;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private boolean available;
    private String borrower;
    private Date dueDate;

    public Book(int id, String title, String author, String isbn, boolean available, String borrower, Date dueDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = available;
        this.borrower = borrower;
        this.dueDate = dueDate;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public boolean isAvailable() { return available; }
    public String getBorrower() { return borrower; }
    public Date getDueDate() { return dueDate; }
}
