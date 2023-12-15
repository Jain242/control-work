import java.sql.*;
import java.util.Scanner;

class Animal {
    private String name;
    private int age;
    private String[] commands;

    public Animal(String name, int age, String[] commands) {
        this.name = name;
        this.age = age;
        this.commands = commands;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String[] getCommands() {
        return commands;
    }

    public void addCommand(String newCommand) {
        String[] newCommands = new String[commands.length + 1];
        System.arraycopy(commands, 0, newCommands, 0, commands.length);
        newCommands[commands.length] = newCommand;
        commands = newCommands;
    }
}

class Cat extends Animal {
    public Cat(String name, int age, String[] commands) {
        super(name, age, commands);
    }
}

class Dog extends Animal {
    public Dog(String name, int age, String[] commands) {
        super(name, age, commands);
    }
}

class Hamster extends Animal {
    public Hamster(String name, int age, String[] commands) {
        super(name, age, commands);
    }
}


public class PetRegistry {
    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:registry.db");

            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Animals (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "age INTEGER," +
                    "commands TEXT" +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, "UTF-8");

        while (true) {
         System.out.println("Меню:");
            System.out.println("1. Завести новое животное");
            System.out.println("2. Определить животное в правильный класс");
            System.out.println("3. Увидеть список команд, которое выполняет животное");
            System.out.println("4. Обучить животное новым командам");
            System.out.println("0. Выход");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addAnimal();
                    break;
                // Other menu items
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Некорректный выбор. Пожалуйста, повторите.");
            }
        }
    }

    private static void addAnimal() {
        Scanner scanner = new Scanner(System.in, "UTF-8");
   System.out.print("Введите имя животного: ");
        String name = scanner.nextLine();

   System.out.print("Введите возраст животного: ");
        int age = scanner.nextInt();
        scanner.nextLine();

       System.out.print("Введите команды для животного через запятую: ");
        String[] commands = scanner.nextLine().split(", ");

        Animal newAnimal = new Animal(name, age, commands);

        try {
            addAnimalToDB(newAnimal);
       System.out.println("Новое животное успешно добавлено.");
        } catch (SQLException e) {
             System.out.println("Ошибка при добавлении животного: " + e.getMessage());
        }
    }

    private static void addAnimalToDB(Animal animal) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO Animals (name, age, commands) VALUES (?, ?, ?)");
        preparedStatement.setString(1, animal.getName());
        preparedStatement.setInt(2, animal.getAge());
        preparedStatement.setString(3, String.join(", ", animal.getCommands()));
        preparedStatement.executeUpdate();
    }
}
