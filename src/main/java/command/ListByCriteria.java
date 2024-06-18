package command;

public class ListByCriteria implements Command{
    @Override
    public String executeCommand(String message) {
        try {
            DatabaseSetup dbSetup = new DatabaseSetup();

            return dbSetup.listByCriteria(message.trim());
        } catch (Exception e) {
            return "Failed to read product. Error: " + e.getMessage();
        }
    }
}
