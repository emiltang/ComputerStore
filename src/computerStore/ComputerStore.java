package computerStore;


import java.sql.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve", "ConstantConditions"})
public class ComputerStore {

    private Connection connection;
    private Statement statement;

    private ComputerStore() {
        String url = "jdbc:postgresql://localhost:5432/testDB";
        String user = "postgres";
        String password = "helloWorld";
        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        ComputerStore computerStore = new ComputerStore();

        String options = "\nWhat do you want to do?\n"
                + "0)\tList all components\n"
                + "1)\tList all computer systems\n"
                + "2)\tView price list\n"
                + "3)\tGet an offer\n"
                + "4)\tSell a component or a computer system\n"
                + "5)\tView Restock List\n";
        System.out.println(options);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {

            switch (scanner.nextInt()) {
                case 0:
                    computerStore.printComponentStockList();
                    break;
                case 1:
                    computerStore.printComputerSystemStockList();
                    break;
                case 2:
                    computerStore.printPriceList();
                    break;
                case 3:
                    System.out.println("Enter ComputerSystem ID");
                    int id = scanner.nextInt();

                    System.out.println("Enter Ampount");
                    int amount = scanner.nextInt();

                    computerStore.getOffer(id, amount);
                    break;
                case 4:
                    computerStore.sell();
                    break;
                case 5:
                    computerStore.printRestockList();
                    break;
                default:
                    break;
            }
            System.out.println(options);
        }
    }

    private void sell() {
    }

    private void printRestockList() {
        try {
            ResultSet resultSet = statement.executeQuery(
                    "SELECT name, currentStock, prefStock " +
                            "FROM Components " +
                            "WHERE currentStock < minStock"
            );
            System.out.println("\nRestock List");
            while (resultSet.next()) {
                System.out.format("%-48s%3d\n",
                        resultSet.getString(1),
                        resultSet.getInt(3) - resultSet.getInt(2)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getOffer(int computerSystemID, int amount) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT ComputerSystems.name, SUM(price) * 1.3 AS price " +
                            "FROM ComputerSystems LEFT JOIN Components ON " +
                            "ComputerSystems.cpu = Components.id OR " +
                            "ComputerSystems.ram = Components.id OR " +
                            "ComputerSystems.mainBoard = Components.id OR " +
                            "ComputerSystems.computerCase = Components.id OR " +
                            "ComputerSystems.gpu = Components.id " +
                            "WHERE ComputerSystems.id = ? " +
                            "GROUP BY ComputerSystems.id"
            );
            preparedStatement.setInt(1, computerSystemID);
            ResultSet resultSet = preparedStatement.executeQuery();

            String name = null;
            double price = 0;

            while (resultSet.next()) {
                name = resultSet.getString(1);
                price = resultSet.getDouble(2);
            }

            // Round up to nearest 99.
            int roundPrice = (int) (99.999 * Math.ceil(price / 99.999));

            // Total price for entire sale
            roundPrice *= amount;

            // Apply discount.
            if (amount < 10) {
                roundPrice -= roundPrice * (0.02 * (amount - 1));
            } else {
                roundPrice -= roundPrice * 0.2;
            }

            System.out.println("Name: " + name + " Price: " + roundPrice + " Amount: " + amount);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void printPriceList() {
        try {
            System.out.println("\nComponents");
            String[] components = {"Gpus", "Cpus", "Rams", "Mainboards", "ComputerCases"};
            for (String table : components) {

                ResultSet resultSet = statement.executeQuery(
                        "SELECT name, price " +
                                "FROM " + table + " LEFT JOIN Components ON " + table + ".id=Components.id"
                );
                System.out.print("\n" + table + "\n");
                while (resultSet.next()) {
                    double d = resultSet.getDouble(2);
                    System.out.format("%-48s%10.2f\n", resultSet.getString(1), d + (d * 0.3));
                }
            }

            System.out.println("\nComputerSystems");
            ResultSet resultSet1 = statement.executeQuery(
                    "SELECT ComputerSystems.name,Components.name,Components.price * 1.3 AS price " +
                            "FROM ComputerSystems LEFT JOIN Components ON " +
                            "ComputerSystems.cpu=Components.id OR " +
                            "ComputerSystems.ram=Components.id OR " +
                            "ComputerSystems.mainBoard=Components.id OR " +
                            "ComputerSystems.computerCase=Components.id OR " +
                            "ComputerSystems.gpu=Components.id "
            );

            Set<String> set = new HashSet<>();
            while (resultSet1.next()) {
                String computerSystemName = resultSet1.getString(1);
                String componentName = resultSet1.getString(2);
                double price = resultSet1.getDouble(3);

                if (set.contains(computerSystemName)) {
                    System.out.format("%-48s%10.2f\n", componentName, price);
                } else {
                    System.out.println("\n" + computerSystemName);
                    set.add(computerSystemName);
                    System.out.format("%-48s%10.2f\n", componentName, price);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void printComponentStockList() {
        try {
            ResultSet resultSet = statement.executeQuery(
                    "SELECT name, currentStock FROM Components"
            );
            System.out.format("%-48s%16s\n\n", "Name", "Current Stock");
            while (resultSet.next()) {
                System.out.format("%-48s%16d\n", resultSet.getString(1), resultSet.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void printComputerSystemStockList() {
        try {
            ResultSet resultSet = statement.executeQuery(
                    "SELECT ComputerSystems.name, min(currentStock) " +
                            "FROM ComputerSystems LEFT JOIN Components ON " +
                            "ComputerSystems.cpu=Components.id OR " +
                            "ComputerSystems.ram=Components.id OR " +
                            "ComputerSystems.mainBoard=Components.id OR " +
                            "ComputerSystems.computerCase=Components.id OR " +
                            "ComputerSystems.gpu=Components.id " +
                            "GROUP BY ComputerSystems.id"
            );
            while (resultSet.next()) {
                System.out.format("%-48s%16d\n", resultSet.getString(1), resultSet.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
