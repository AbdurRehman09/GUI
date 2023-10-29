import java.io.IOException;
import java.nio.file.Files;
import java.io.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
public class GUI2 extends JFrame{

    private Library Lib;
    private JTable table;
    private DefaultTableModel tmodel;


    public GUI2(){
        Lib =new Library();
        Lib.readingfile("Books.txt");
        Lib.writeDataToTempFile("temp.txt");
        setTitle("LMS in GUI");
        setSize(900,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tmodel=new DefaultTableModel(new String[]{"ID","title","Author","Year","Popularitycount","Price","Read item"},0);
        table=new JTable(tmodel);
        JScrollPane jsp=new JScrollPane(table);

        JButton fordisplayall=new JButton();
        fordisplayall.setText("Display All");
        JButton viewPopularityButton = new JButton("View Popularity");
        JButton foradding=new JButton("Add book");
        JButton foredit=new JButton("Edit book");
        JButton fordeletion=new JButton("Delete book");
        fordisplayall.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e){
                displayallbooks();
            }
        });
        foradding.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e){
                addbook();
            }
        });
        foredit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e){
                editbook();
            }
        });
        fordeletion.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e){
                deletebook();
            }
        });
        viewPopularityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayPopularityChart();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Clear the temp.txt and tempbooks.txt files
                clearTempFiles();
            }
        });
        MouseAdapter highlighter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int r = table.rowAtPoint(e.getPoint());
                if (r >= 0 && r < table.getRowCount()) {
                    table.setRowSelectionInterval(r, r);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                table.clearSelection();
            }
        };

        table.addMouseMotionListener(highlighter); // Using mouseMoved instead of mouseEntered
        Highlighttherow highlight = new Highlighttherow(Color.CYAN);
        table.setDefaultRenderer(Object.class, highlight);

        JPanel buttons=new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));//ya kaam neechaybhi krsktay thai
        buttons.add(fordisplayall);
        buttons.add(foradding);
        buttons.add(foredit);
        buttons.add(fordeletion);
        buttons.add(viewPopularityButton);
        JPanel outerpanel = new JPanel(new BorderLayout());
        outerpanel.add(jsp,BorderLayout.CENTER);

//        buttons.setLayout(new GridLayout(4,1));//(kion k wo sari width cover kar raha tha)
//        add(buttons,BorderLayout.SOUTH);
        for (Component component : buttons.getComponents()) {
            if (component instanceof AbstractButton) {
                ((AbstractButton) component).setAlignmentX(Component.CENTER_ALIGNMENT);
            }
            outerpanel.add(buttons, BorderLayout.SOUTH);
            add(outerpanel);
            setVisible(true);
        }
    }
    private class Highlighttherow extends javax.swing.table.DefaultTableCellRenderer{
        Color c;
        Highlighttherow(Color c1){this.c=c1;}

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (isSelected) {
                comp.setBackground(c);
            }
            else {
                comp.setBackground(Color.WHITE);
            }

            return comp;
        }
    }

    private void displayallbooks() {
        tmodel.setRowCount(0);
        for (Book b : Lib.items) {
            tmodel.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(), b.getYear(), b.getp_c(), b.getprice(), "Read Book"});
        }

        table.getColumnModel().getColumn(6).setCellRenderer((TableCellRenderer) new ButtonRenderer());
        table.addMouseListener(new ButtonMouseListener());
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof String) {
                setText((String) value);
            }

            return this;
        }
    }

    class ButtonMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            int column = table.getColumnModel().getColumnIndex("Read item");
            if (column != -1 && table.columnAtPoint(e.getPoint()) == column) {
                int row = table.rowAtPoint(e.getPoint());
                if (row != -1) {
                    openbooktext(Lib.items.get(row));
                }
            }
        }
    }

    private void openbooktext(Book b) {
        JFrame f = new JFrame("Read - " + b.getTitle());
        JTextArea tA = new JTextArea();
        tA.setText(b.gettext());

        JScrollPane jsp = new JScrollPane(tA);
        f.add(jsp);
        f.setSize(1600, 400);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent the default close operation

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(f, "Do you want to exit reading the book?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    f.dispose();
                }
                // If user selects "No", do nothing (frame remains open)
            }
        });

    }

    private void addbook(){
        JDialog adding=new JDialog(this,"Add Book",true);
        adding.setLayout(new GridLayout(7,2));
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField popularityCountField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField Booktext = new JTextField();


        adding.add(new JLabel("Title:"));
        adding.add(titleField);
        adding.add(new JLabel("Author:"));
        adding.add(authorField);
        adding.add(new JLabel("Year:"));
        adding.add(yearField);
        adding.add(new JLabel("Popularity Count:"));
        adding.add(popularityCountField);
        adding.add(new JLabel("Price:"));
        adding.add(priceField);
        adding.add(new JLabel("Booktext:"));
        adding.add(Booktext);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String author = authorField.getText();
                int year = Integer.parseInt(yearField.getText());
                int popularityCount = Integer.parseInt(popularityCountField.getText());
                int price = Integer.parseInt(priceField.getText());
                String t=Booktext.getText();
                Book newItem = new Book(title, author, year, popularityCount, price,t);
                Lib.addItem(newItem);
                try {
                    BufferedWriter tempFileWriter = new BufferedWriter(new FileWriter("temp.txt"));
                    tempFileWriter.close(); // Closes and clears the file
                } catch (IOException e1) {
                    System.out.println("Error clearing temp files: " + e1.getMessage());
                }

                Lib.writeDataToTempFile("temp.txt");
                displayallbooks();
                adding.dispose();
            }
        });

        JPanel addingpanel=new JPanel(new FlowLayout(FlowLayout.CENTER));
        addingpanel.add(addButton);
        adding.add(addingpanel);

        adding.pack();
        adding.setVisible(true);

    }
    private void displayPopularityChart() {

        try {
            ProcessBuilder pb = new ProcessBuilder("python", "piechart.py");
            pb.directory(new File("C:\\Users\\Abdul Rehman\\IdeaProjects\\GUI")); // Set the directory where your Python script is located

            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                // Display the generated chart
                ImageIcon chartIcon = new ImageIcon("C:\\Users\\Abdul Rehman\\Documents\\NetBeansProjects\\GUI\\src\\main\\java\\com\\mycompany\\gui\\chart.png"); // Load the saved chart image
                JLabel chartLabel = new JLabel(chartIcon);

                JDialog popularityDialog = new JDialog(this, "Popularity Chart", true);
                popularityDialog.add(chartLabel);

                popularityDialog.pack();
                popularityDialog.setVisible(true);
            } else {
                System.err.println("Error executing the Python script.");
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    private void editbook(){
        // Get the selected row
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an item to edit.");
            return;
        }

        int id = (int) tmodel.getValueAt(selectedRow, 0);

        // Create a dialog for editing the selected book
        JDialog dialog = new JDialog(this, "Edit Item", true);
        dialog.setLayout(new GridLayout(7, 2));

        JTextField titleField = new JTextField((String) tmodel.getValueAt(selectedRow, 1));
        JTextField authorField = new JTextField((String) tmodel.getValueAt(selectedRow, 2));
        JTextField yearField = new JTextField(tmodel.getValueAt(selectedRow, 3).toString());
        JTextField popularityCountField = new JTextField(tmodel.getValueAt(selectedRow, 4).toString());
        JTextField priceField = new JTextField(tmodel.getValueAt(selectedRow, 5).toString());
        JTextField Booktext = new JTextField((String)tmodel.getValueAt(selectedRow, 6));

        dialog.add(new JLabel("Title:"));
        dialog.add(titleField);
        dialog.add(new JLabel("Author:"));
        dialog.add(authorField);
        dialog.add(new JLabel("Year:"));
        dialog.add(yearField);
        dialog.add(new JLabel("Popularity Count:"));
        dialog.add(popularityCountField);
        dialog.add(new JLabel("Price:"));
        dialog.add(priceField);
        dialog.add(new JLabel("Booktext:"));
        dialog.add(Booktext);

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String author = authorField.getText();
                int year = Integer.parseInt(yearField.getText());
                int popularityCount = Integer.parseInt(popularityCountField.getText());
                int price = Integer.parseInt(priceField.getText());
                String t=Booktext.getText();
                Book editedItem = new Book(title, author, year, popularityCount, price,t);
                Lib.edititem(id, editedItem);
                try {
                    BufferedWriter tempFileWriter = new BufferedWriter(new FileWriter("temp.txt"));
                    tempFileWriter.close(); // Closes and clears the file
                } catch (IOException e1) {
                    System.out.println("Error clearing temp files: " + e1.getMessage());
                }

                Lib.writeDataToTempFile("temp.txt");
                displayallbooks();
                dialog.dispose();
            }
        });

        dialog.add(editButton);

        dialog.pack();
        dialog.setVisible(true);

    }
    private void deletebook(){
        // Get the selected row
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an item to delete.");
            return;
        }

        int id = (int) tmodel.getValueAt(selectedRow, 0);

        Lib.deleteitem(id);
        try {
            BufferedWriter tempFileWriter = new BufferedWriter(new FileWriter("temp.txt"));
            tempFileWriter.close(); // Closes and clears the file
        } catch (IOException e1) {
            System.out.println("Error clearing temp files: " + e1.getMessage());
        }

        Lib.writeDataToTempFile("temp.txt");
        displayallbooks();
    }
    private void clearTempFiles() {

        // Clear the contents of temp.txt and tempbooks.txt files
        try {
            BufferedWriter tempFileWriter = new BufferedWriter(new FileWriter("temp.txt"));
            tempFileWriter.close(); // Closes and clears the file
            BufferedWriter tempFileWriter1 = new BufferedWriter(new FileWriter("Books.txt"));
            tempFileWriter1.close();
        } catch (IOException e) {
            System.out.println("Error clearing temp files: " + e.getMessage());
        }
        Lib.writeDataToTempFile("Books.txt");
    }
    public static void main(String args[]){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                new GUI2();
            }
        });
    }
}
