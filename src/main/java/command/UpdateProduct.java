package command;

public class UpdateProduct implements Command{
    @Override
    public String executeCommand(String message) {
        try {
            // Розбиваємо стрічку на частини за допомогою роздільника "/"
            String[] parts = message.split("/");

            // Перевіряємо, чи маємо всі необхідні частини
            if (parts.length < 6) {
                return "Invalid message format. Expected format: productName/description/producer/amount/price/group";
            }

            DatabaseSetup dbSetup = new DatabaseSetup();
            dbSetup.updateProduct(parts);

            return "Product has been successfully updated: " + parts[0].trim();
        } catch (Exception e) {
            return "Failed to update product. Error: " + e.getMessage();
        }
    }
}
