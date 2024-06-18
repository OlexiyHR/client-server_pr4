package command;

import java.sql.*;

public class DatabaseSetup {
    private Connection connection = null;

    // Метод для підключення до бази даних
    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:warehouse.db");
        System.out.println("Connection to SQLite has been established.");
    }

    // Метод для створення таблиць (оновлює, якщо вони вже існують)
    public void createTables() throws SQLException {
        String dropProductTable = "DROP TABLE IF EXISTS product";

        String createProductGroupTable = "CREATE TABLE IF NOT EXISTS product_group (" +
                "group_name TEXT PRIMARY KEY NOT NULL, " +
                "description TEXT NOT NULL)";

        String createProductTable = "CREATE TABLE product (" +
                "product_name TEXT PRIMARY KEY NOT NULL, " +
                "description TEXT NOT NULL, " +
                "producer TEXT NOT NULL, " +
                "amount INTEGER NOT NULL, " +
                "price REAL NOT NULL, " +
                "group_id INTEGER NOT NULL, " +
                "FOREIGN KEY(group_id) REFERENCES product_group(id) NOT NULL)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(dropProductTable);
            stmt.execute(createProductGroupTable);
            stmt.execute(createProductTable);
            fillGroup();
            System.out.println("Tables 'product_group' and 'product' have been created.");
        }
    }

    public void fillGroup() throws SQLException {
        String insertProductGroup = "INSERT INTO product_group (group_name, description) VALUES " +
                "('Food', 'Includes food and beverages'), " +
                "('Non-Food', 'Household appliances, electronics, home and office furniture, accessories')";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeQuery(insertProductGroup);
        }
    }

    public void setup() {
        try {
            connect(); // Підключення до бази даних
            createTables(); // Створення таблиць (оновлює, якщо вони вже існують)
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createProduct(String [] parts) {
        try {
            // Витягаємо дані з масиву частин
            String productName = parts[0].trim();
            String description = parts[1].trim();
            String producer = parts[2].trim();
            int amount = Integer.parseInt(parts[3].trim());
            double price = Double.parseDouble(parts[4].trim());
            String group = parts[5].trim();

            String sql = "INSERT INTO product (product_name, description, producer, amount, price, group) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, productName);
                stmt.setString(2, description);
                stmt.setString(3, producer);
                stmt.setInt(4, amount);
                stmt.setDouble(5, price);
                stmt.setString(6, group);
                stmt.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteProduct(String productName) throws SQLException {
        String deleteProductQuery = "DELETE FROM product WHERE product_name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(deleteProductQuery)) {
            stmt.setString(1, productName);
            int rowsAffected = stmt.executeUpdate();
        }
    }

    public String readProduct (String productName) throws SQLException {
        String selectProductQuery = "SELECT * FROM product WHERE product_name = ?";
        String result = "";

        try (PreparedStatement stmt = connection.prepareStatement(selectProductQuery)) {
            stmt.setString(1, productName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("product_name");
                String description = rs.getString("description");
                String producer = rs.getString("producer");
                int amount = rs.getInt("amount");
                double price = rs.getDouble("price");
                String group = rs.getString("group");

                result = "Product Name: " + name + "\n" +
                        "Description: " + description + "\n" +
                        "Producer: " + producer + "\n" +
                        "Amount: " + amount + "\n" +
                        "Price: " + price + "\n" +
                        "Group: " + group;
            }
        }
        return result;
    }

    public void updateProduct(String [] parts) {
        try {
            // Витягаємо дані з масиву частин
            String productName = parts[0].trim();
            String description = parts[1].trim();
            String producer = parts[2].trim();
            int amount = Integer.parseInt(parts[3].trim());
            double price = Double.parseDouble(parts[4].trim());
            String group = parts[5].trim();

            String selectProductQuery = "SELECT * FROM product WHERE product_name = ?";
            String updateProductQuery = "UPDATE product SET product_name = ?, description = ?, producer = ?, amount = ?, price = ?, group = ? WHERE product_name = ?";

            try (PreparedStatement selectStmt = connection.prepareStatement(selectProductQuery);
                 PreparedStatement updateStmt = connection.prepareStatement(updateProductQuery)) {

                // Пошук продукту за назвою
                selectStmt.setString(1, productName);
                ResultSet rs = selectStmt.executeQuery();

                if (rs.next()) {
                    // Отримуємо поточні значення полів
                    String currentProductName = rs.getString("product_name");
                    String currentDescription = rs.getString("description");
                    String currentProducer = rs.getString("producer");
                    int currentAmount = rs.getInt("amount");
                    double currentPrice = rs.getDouble("price");
                    String currentGroup = rs.getString("group");

                    // Перевіряємо, чи нові значення відрізняються від старих
                    boolean updateNeeded = false;
                    if (!productName.equals(currentProductName)) {
                        updateStmt.setString(1, productName);
                        updateNeeded = true;
                    } else {
                        updateStmt.setString(1, currentProductName); // Залишаємо старе значення
                    }

                    if (!description.equals(currentDescription)) {
                        updateStmt.setString(2, description);
                        updateNeeded = true;
                    } else {
                        updateStmt.setString(2, currentDescription); // Залишаємо старе значення
                    }

                    if (!producer.equals(currentProducer)) {
                        updateStmt.setString(3, producer);
                        updateNeeded = true;
                    } else {
                        updateStmt.setString(3, currentProducer); // Залишаємо старе значення
                    }

                    if (amount != currentAmount) {
                        updateStmt.setInt(4, amount);
                        updateNeeded = true;
                    } else {
                        updateStmt.setInt(4, currentAmount); // Залишаємо старе значення
                    }

                    if (price != currentPrice) {
                        updateStmt.setDouble(5, price);
                        updateNeeded = true;
                    } else {
                        updateStmt.setDouble(5, currentPrice); // Залишаємо старе значення
                    }


                    if (!group.equals(currentGroup)) {
                        updateStmt.setString(6, group);
                        updateNeeded = true;
                    } else {
                        updateStmt.setString(6, currentGroup); // Залишаємо старе значення
                    }

                    updateStmt.setString(7, currentProductName); // Умова для WHERE

                    // Виконуємо оновлення, якщо є необхідність
                    if (updateNeeded) {
                        updateStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String listByCriteria (String columnName) throws SQLException {
        String query = "SELECT * FROM product ORDER BY " + columnName;

        StringBuilder result = new StringBuilder();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            result.append("Products sorted by ").append(columnName).append(":\n");

            while (rs.next()) {
                String productName = rs.getString("product_name");
                String description = rs.getString("description");
                String producer = rs.getString("producer");
                int amount = rs.getInt("amount");
                double price = rs.getDouble("price");
                String group = rs.getString("group");

                result.append(String.format("Product: %s, Description: %s, Producer: %s, Amount: %d, Price: %.2f, Group: %s\n",
                        productName, description, producer, amount, price, group));
            }
        }

        return result.toString();

    }
}
