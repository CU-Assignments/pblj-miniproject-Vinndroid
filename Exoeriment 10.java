import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {
    private static final String URL = "jdbc:sqlite:products.db";

    public static void createTable() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS Product (" +
                         "ProductID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "ProductName TEXT NOT NULL, " +
                         "Price REAL NOT NULL, " +
                         "Quantity INTEGER NOT NULL)";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void createProduct(String name, double price, int quantity) {
        String sql = "INSERT INTO Product (ProductName, Price, Quantity) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, quantity);
            pstmt.executeUpdate();
            System.out.println("Product added successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void readProducts() {
        String sql = "SELECT * FROM Product";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nProduct List:");
            while (rs.next()) {
                System.out.println(rs.getInt("ProductID") + " | " +
                                   rs.getString("ProductName") + " | " +
                                   rs.getDouble("Price") + " | " +
                                   rs.getInt("Quantity"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void updateProduct(int productId, String name, double price, int quantity) {
        String sql = "UPDATE Product SET ProductName=?, Price=?, Quantity=? WHERE ProductID=?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, productId);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Product updated successfully.");
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void deleteProduct(int productId) {
        String sql = "DELETE FROM Product WHERE ProductID=?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        createTable();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Create Product");
            System.out.println("2. Read Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter product name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    createProduct(name, price, quantity);
                    break;
                case 2:
                    readProducts();
                    break;
                case 3:
                    System.out.print("Enter ProductID to update: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new product name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new price: ");
                    double newPrice = scanner.nextDouble();
                    System.out.print("Enter new quantity: ");
                    int newQuantity = scanner.nextInt();
                    updateProduct(updateId, newName, newPrice, newQuantity);
                    break;
                case 4:
                    System.out.print("Enter ProductID to delete: ");
                    int deleteId = scanner.nextInt();
                    deleteProduct(deleteId);
                    break;
                case 5:
                    System.out.println("Exiting program.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
