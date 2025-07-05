import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ProductForm extends JFrame {
    private JTextField tfName = new JTextField();
    private JTextField tfPrice = new JTextField();
    private JTextField tfCategory = new JTextField();
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Product> productList = new ArrayList<>();

    public ProductForm() {
        setTitle("GraphQL Product Form");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Product CRUD Workflow", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new javax.swing.BoxLayout(mainPanel, javax.swing.BoxLayout.Y_AXIS));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 12, 12));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Input Produk"),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(tfName);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(tfPrice);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(tfCategory);

        mainPanel.add(inputPanel);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        JButton btnAdd = new JButton("Add");
        JButton btnFetch = new JButton("Show All");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        btnAdd.setBackground(Color.WHITE);
        btnAdd.setForeground(Color.BLACK);
        btnFetch.setBackground(Color.WHITE);
        btnFetch.setForeground(Color.BLACK);
        btnEdit.setBackground(Color.WHITE);
        btnEdit.setForeground(Color.BLACK);
        btnDelete.setBackground(Color.WHITE);
        btnDelete.setForeground(Color.BLACK);
        btnAdd.setFocusPainted(false);
        btnFetch.setFocusPainted(false);
        btnEdit.setFocusPainted(false);
        btnDelete.setFocusPainted(false);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnFetch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 14));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnFetch);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        mainPanel.add(buttonPanel);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Price", "Category"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setSelectionBackground(new Color(220, 240, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(230, 230, 230));
        table.setShowGrid(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Price
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Category

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Produk"));
        scrollPane.setPreferredSize(new Dimension(540, 320));
        mainPanel.add(scrollPane);

        add(mainPanel, BorderLayout.CENTER);

        tfPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String text = tfPrice.getText().replaceAll("[^\\d]", "");
                if (!text.isEmpty()) {
                    try {
                        long value = Long.parseLong(text);
                        NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));
                        nf.setMaximumFractionDigits(0);
                        tfPrice.setText("Rp. " + nf.format(value));
                    } catch (NumberFormatException ex) {
                        tfPrice.setText("");
                    }
                }
            }
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                String text = tfPrice.getText().replaceAll("[^\\d]", "");
                tfPrice.setText(text);
            }
        });

        btnAdd.addActionListener(e -> tambahProduk());
        btnFetch.addActionListener(e -> ambilSemuaProduk());
        btnEdit.addActionListener(e -> editProduk());
        btnDelete.addActionListener(e -> hapusProduk());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                tfName.setText(tableModel.getValueAt(row, 1).toString());
                tfPrice.setText(tableModel.getValueAt(row, 2).toString());
                tfCategory.setText(tableModel.getValueAt(row, 3).toString());
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        ambilSemuaProduk();
    }

    private void tambahProduk() {
        String name = tfName.getText();
        String price = tfPrice.getText().replaceAll("[^\\d]", "");
        String category = tfCategory.getText();
        if (name.isEmpty() || price.isEmpty() || category.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }
        NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));
        nf.setMaximumFractionDigits(0);
        String formattedPrice = "Rp. " + nf.format(Long.parseLong(price));
        Product p = new Product(name, formattedPrice, category);
        productList.add(p);

        JOptionPane.showMessageDialog(this,
            "Produk berhasil ditambahkan:\n" +
            "Nama: " + name + "\n" +
            "Harga: " + formattedPrice + "\n" +
            "Kategori: " + category);

        clearInputFields();
    }

    private void ambilSemuaProduk() {
        tableModel.setRowCount(0);
        int id = 1;
        for (Product p : productList) {
            tableModel.addRow(new Object[]{id++, p.getName(), p.getPrice(), p.getCategory()});
        }
    }

    private void editProduk() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit!");
            return;
        }
        String name = tfName.getText();
        String price = tfPrice.getText().replaceAll("[^\\d]", "");
        String category = tfCategory.getText();
        if (name.isEmpty() || price.isEmpty() || category.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }
        NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));
        nf.setMaximumFractionDigits(0);
        String formattedPrice = "Rp. " + nf.format(Long.parseLong(price));
        tableModel.setValueAt(name, selectedRow, 1);
        tableModel.setValueAt(formattedPrice, selectedRow, 2);
        tableModel.setValueAt(category, selectedRow, 3);
        clearInputFields();
    }

    private void hapusProduk() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!");
        return;
    }
    productList.remove(selectedRow);
    tableModel.removeRow(selectedRow);
    clearInputFields();
}

    private void clearInputFields() {
        tfName.setText("");
        tfPrice.setText("");
        tfCategory.setText("");
    }

    static class Product {
        private String name;
        private String price;
        private String category;

        public Product(String name, String price, String category) {
            this.name = name;
            this.price = price;
            this.category = category;
        }
        public String getName() { return name; }
        public String getPrice() { return price; }
        public String getCategory() { return category; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProductForm::new);
    }
}
