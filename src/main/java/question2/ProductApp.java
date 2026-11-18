package question2;

import javax.swing.*;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Locale;

public class ProductApp extends JFrame {

    private static final Path DATA_FILE = Paths.get("data.txt");
    private static final String SEPARATOR = "********************************";

    private final JTextField customerField = new JTextField(20);
    private final JTextField priceField = new JTextField(20);
    private final JComboBox<String> productBox = new JComboBox<>(
            new String[]{
                    "Cozy Nights Monarch",
                    "Restonic Barbados",
                    "Sealy Phuket"
            }
    );

    public ProductApp() {
        super("Product App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(createMenuBar());

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        form.add(new JLabel("Customer Name:"));
        form.add(customerField);

        form.add(new JLabel("Select Product:"));
        form.add(productBox);

        form.add(new JLabel("Enter Price:"));
        form.add(priceField);

        JButton processButton = new JButton("PROCESS");
        processButton.addActionListener(event -> processSale());
        form.add(new JLabel());
        form.add(processButton);

        add(form);
        getRootPane().setDefaultButton(processButton);
        pack();
        setLocationRelativeTo(null);
    }

    private JMenuBar createMenuBar() {
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(event -> dispose());

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(exitItem);

        JMenuItem processItem = new JMenuItem("Process");
        processItem.addActionListener(event -> processSale());

        JMenuItem clearItem = new JMenuItem("Clear");
        clearItem.addActionListener(event -> clearForm());

        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.add(processItem);
        toolsMenu.add(clearItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        return menuBar;
    }

    private void clearForm() {
        customerField.setText("");
        priceField.setText("");
        productBox.setSelectedIndex(0);
        customerField.requestFocusInWindow();
    }

    private void processSale() {
        String customerName = customerField.getText().trim();
        String productName = (String) productBox.getSelectedItem();

        if (customerName.isEmpty()) {
            showError("Please enter the customer's name.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceField.getText().trim());
        } catch (NumberFormatException exception) {
            showError("Please enter a valid price, such as 3500 or 3500.50.");
            return;
        }

        if (!Double.isFinite(price) || price <= 0) {
            showError("Please enter a price greater than zero.");
            return;
        }

        IProductCalculations calculations = new ProductCalculations(price);
        double total = calculations.GetTotal();

        if (!Double.isFinite(total)) {
            showError("The price is too large to calculate the total.");
            return;
        }

        String saleDetails = String.format(
                Locale.ROOT,
                "Customer Name: %s%n"
                        + "Product: %s%n"
                        + "VAT: R %.2f%n"
                        + "COMMISSION: R %.2f%n"
                        + "TOTAL: R %.2f",
                customerName, productName, calculations.GetVat(),
                calculations.GetCommission(), total
        );

        try {
            saveSale(saleDetails);
        } catch (IOException exception) {
            showError("The sale could not be saved to data.txt.");
            return;
        }

        JOptionPane.showMessageDialog(
                this, saleDetails, "Message", JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void saveSale(String saleDetails) throws IOException {
        String newLine = System.lineSeparator();
        String record = saleDetails + newLine + SEPARATOR + newLine;

        if (Files.notExists(DATA_FILE) || Files.size(DATA_FILE) == 0) {
            record = "DATA LOG" + newLine + SEPARATOR + newLine + record;
        }

        Files.write(
                DATA_FILE,
                record.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this, message, "Product App", JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductApp().setVisible(true));
    }
}