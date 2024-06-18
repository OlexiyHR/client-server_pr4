package command;

public class ReadProduct implements Command{
    @Override
    public String executeCommand(String message) {
        try {
            DatabaseSetup dbSetup = new DatabaseSetup();

            return dbSetup.readProduct(message.trim());
        } catch (Exception e) {
            return "Failed to read product. Error: " + e.getMessage();
        }
    }
}
