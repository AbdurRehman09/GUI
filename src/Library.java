import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;
 class Book{
    private String author;
    private int year;
    private int price;
    private static int nextId = 1;
    private int id;
    private String title;
    private int popularityCount;
    private String text;


    public Book(String title, String author, int year, int popularityCount, int price,String t) {
        this.id=nextId++;
        this.title=title;
        this.author = author;
        this.year = year;
        this.popularityCount = popularityCount;
        this.price = price;
        this.text=t;
    }
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public int getp_c()
    {
        return popularityCount;
    }
    public void setp_c(int p_c)
    {
        popularityCount=p_c;
    }
    void incrementPopularityCount()
    {
        popularityCount++;
    }

    public int getprice()
    {
        return price;
    }
    public void setprice(int p)
    {
        price=p;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void settext(String txt)
    {
        this.text=txt;
    }
    public String gettext()
    {
        return text;
    }

    public void display() {
        System.out.println("ID: " + id + "\nTitle: " + title);
        System.out.println("Author: " + author);
        System.out.println("Year: " + year);
        System.out.println("Popularity Count: " + popularityCount);
        System.out.println("Price: " + price);
        System.out.println("Booktext: " + text);
    }
}
public class Library {
    protected ArrayList<Book> items;//available;

    public Library() {
        items = new ArrayList<>();

    }
    public void addItem(Book newItem) {
        items.add(newItem);
    }
    public Book getitembyid(int id)
    {
        Book item=null;
        for(int i=0;i<items.size();i++)
        {
            if(items.get(i).getId()==id)
            {
                item=items.get(i);
            }
        }
        return item;
    }
    public void edititem(int id, Book it1)
    {
        Iterator<Book>it=items.iterator();
        while(it.hasNext())
        {
            Book item=it.next();
            if(item.getId()==id)
            {
                Book curr=item;
                Book New=it1;
                curr.setTitle(New.getTitle());
                curr.setAuthor(New.getAuthor());
                curr.setYear(New.getYear());
                curr.setp_c(New.getp_c());
                curr.setprice(New.getprice());
                curr.settext(New.gettext());

                System.out.println("Item with ID " + id + " has been edited.");
                return;
            }

        }

    }
    public void displayAllItems() {

        Iterator<Book>it=items.iterator();
        while(it.hasNext())
        {
            Book it1=it.next();
            it1.display();
        }
    }
    public void deleteitem(int id)
    {
        for(int i=0;i<items.size();i++)
        {
            if(items.get(i).getId()==id)
            {

                items.remove(items.get(i));
                return;
            }

        }
    }
    public void displayitem(Book item)
    {
        item.display();
    }
    public void readingfile(String filename) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("Reading line: " + line); // Add this line for debugging
                String[] bookInfo = line.split("-");
                if (bookInfo.length >= 7) { // Check if the line has enough elements
                    String title = bookInfo[1];
                    String author = bookInfo[2];
                    int publicationYear = Integer.parseInt(bookInfo[3]);
                    int p_c = Integer.parseInt(bookInfo[4]);
                    int price = Integer.parseInt(bookInfo[5]);
                    String description = bookInfo[6];

                    Book newBook = new Book(title, author, publicationYear, p_c, price, description);
                    items.add(newBook);
                } else {
                    System.out.println("Line format incorrect: " + line);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeDataToTempFile(String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename,true));
            Iterator<Book> it = items.iterator();

            while (it.hasNext()) {
                Book item = it.next();
                String bookData = "1-" + item.getTitle() + "-" + item.getAuthor() + "-" + item.getYear() + "-" + item.getp_c() + "-" + item.getprice()+"-"+item.gettext();
                writer.write(bookData);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing data to the temporary file: " + e.getMessage());
        }
    }
}
