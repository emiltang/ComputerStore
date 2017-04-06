package computerStore;

import java.sql.*;
import java.util.*;

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

        String options = "\nWhat do you want to do?\n" +
                "0)\tList all components\n" +
                "1)\tList all computer systems\n" +
                "2)\tView price list\n" +
                "3)\tGet an offer\n" +
                "4)\tSell a component or a computer system\n" +
                "5)\tView Restock List\n";
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
                    System.out.println("Select a computer system");
                    Map<Integer, String> map = computerStore.findComputerSystems();
                    map.forEach((k, v) -> {
                        System.out.println(k + ")\t" + v);
                    });
                    int id = scanner.nextInt();

                    System.out.println("Enter Ampount");
                    int amount = scanner.nextInt();

                    computerStore.getOffer(map.get(id), amount);
                    break;
                case 4:
                    System.out.println("What do you want to sell?\n" +
                            "0)\tA Component\n" +
                            "1)\tA ComputerSystem\n"
                    );
                    int choice = scanner.nextInt();
                    switch (choice) {
                        case 0:
                            System.out.println("Enter Component ID");
                            int componentId = scanner.nextInt();
                            computerStore.sellComponent(componentId);
                            break;
                        case 1:
                            System.out.println("Select a Computer System");
                            Map<Integer, String> map1 = computerStore.findComputerSystems();
                            map1.forEach((k, v) -> {
                                System.out.println(k + ")\t" + v);
                            });
                            int selection = scanner.nextInt();
                            computerStore.sellComputerSystem(map1.get(selection));
                            break;
                        default:
                            System.out.println("Please Enter a Valid number");
                    }
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

    private Map<Integer, String> findComputerSystems() {
        try {
            ResultSet resultSet = statement.executeQuery(
                    "SELECT computerSystemName " +
                            "FROM computerSystems "
            );

            Map<Integer, String> map = new HashMap<>();
            int count = 0;
            while (resultSet.next()) {
                map.put(count++, resultSet.getString(1));
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void sellComponent(int id) {
        try {
            PreparedStatement preparedStatement1 = connection.prepareStatement(
                    "SELECT currentStock " +
                            "FROM Components " +
                            "WHERE id = ?"
            );
            preparedStatement1.setInt(1, id);
            ResultSet resultSet = preparedStatement1.executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) > 0) {
                PreparedStatement preparedStatement2 = connection.prepareStatement(
                        "UPDATE Components " +
                                "SET currentStock = currentStock - 1 " +
                                "WHERE id = ?"
                );
                preparedStatement2.setInt(1, id);
                preparedStatement2.executeUpdate();
            } else {
                System.out.println("Component out of stock");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sellComputerSystem(String name) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT min(currentStock) " +
                            "FROM ComputerSystems JOIN Components " +
                            "ON cpu = id OR ram = id OR mainBoard = id OR computerCase = id OR gpu = id " +
                            "WHERE computerSystemName = ?" +
                            "GROUP BY computerSystemName"
            );
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) > 0) {
                PreparedStatement preparedStatement2 = connection.prepareStatement(
                        "UPDATE Components " +
                                "SET currentStock = currentStock - 1 " +
                                "WHERE id IN (SELECT id FROM ComputerSystems JOIN Components " +
                                "ON cpu = id OR ram = id OR mainBoard = id OR computerCase = id OR gpu = id " +
                                "WHERE computerSystemName = ?)"
                );
                preparedStatement2.setString(1, name);
                preparedStatement2.executeUpdate();
            } else {
                System.out.println("Component out of stock");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void printRestockList() {
        try {
            ResultSet resultSet = statement.executeQuery(
                    "SELECT name, prefStock - currentStock AS restockAmount " +
                            "FROM Components " +
                            "WHERE currentStock < minStock"
            );
            System.out.println("\nRestock List");
            while (resultSet.next()) {
                System.out.format("%-48s%3d\n", resultSet.getString(1), resultSet.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getOffer(String name1, int amount) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT computerSystemName, SUM(price) * 1.3 AS price " +
                            "FROM ComputerSystems JOIN Components " +
                            "ON cpu = id OR ram = id OR mainBoard = id OR computerCase = id OR gpu = id " +
                            "WHERE computerSystemName = ? " +
                            "GROUP BY computerSystemName"
            );
            preparedStatement.setString(1, name1);
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
                        "SELECT componentName, price " +
                                "FROM " + table + " JOIN Components ON " + table + ".id = Components.id"
                );
                System.out.print("\n" + table + "\n");
                while (resultSet.next()) {
                    double d = resultSet.getDouble(2);
                    System.out.format("%-48s%10.2f\n", resultSet.getString(1), d + (d * 0.3));
                }
            }

            System.out.println("\nComputerSystems");
            ResultSet resultSet1 = statement.executeQuery(
                    "SELECT computerSystemName, componentName, price * 1.3 AS price " +
                            "FROM ComputerSystems JOIN Components " +
                            "ON cpu = id OR ram = id OR mainBoard = id OR computerCase = id OR gpu = id "
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
                    "SELECT componentName, currentStock FROM Components"
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
                    "SELECT computerSystemName, min(currentStock) " +
                            "FROM ComputerSystems JOIN Components " +
                            "ON cpu = id OR ram = id OR mainBoard = id OR computerCase = id OR gpu = id " +
                            "GROUP BY computerSystemName"
            );
            while (resultSet.next()) {
                System.out.format("%-48s%16d\n", resultSet.getString(1), resultSet.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
