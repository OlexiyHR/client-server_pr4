package command;

public class DeleteProduct implements Command{
    @Override
    public String executeCommand(String message) {
        try {
            DatabaseSetup dbSetup = new DatabaseSetup();
            dbSetup.deleteProduct(message.trim());

            return "Product has been successfully deleted: " + message.trim();
        } catch (Exception e) {
            return "Failed to delete product. Error: " + e.getMessage();
        }
    }
}
