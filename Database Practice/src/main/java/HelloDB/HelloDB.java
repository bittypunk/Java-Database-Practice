package HelloDB;

import java.sql.*;

public class HelloDB {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:sqlite:hello.sqlite";

        Connection connection = DriverManager.getConnection(url);
        Statement statement = connection.createStatement();

        // creates the Cats table if it's not already there
        // first column is String name and 2nd is int age
        String createTableSQL = "CREATE TABLE IF NOT EXISTS cats (name TEXT, age INTEGER)";
        statement.execute(createTableSQL);

        // adds data
        String insertDataSQL = "INSERT INTO cats VALUES ('Maru', 10)";
        statement.execute(insertDataSQL);

        insertDataSQL = "INSERT INTO cats VALUES ('Hello Kitty', 40)";
        statement.execute(insertDataSQL);

        insertDataSQL = "INSERT INTO cats VALUES ('Garfield', 41)";
        statement.execute(insertDataSQL);

        insertDataSQL = "INSERT INTO cats VALUES ('Snowball', 12)";
        statement.execute(insertDataSQL);

        // ResultSet provides access to the rows of data returned by your query, row by row
        // if not using try-with-resources, must close ResultSet when done
        String getAllDataSQL = "SELECT * FROM cats";
        ResultSet allCats = statement.executeQuery(getAllDataSQL);

        // .next loops over the result set: returns true if there's another row to read, false if not
        while (allCats.next()) {
            // .getString reads the name column
            String name = allCats.getString("name");
            // .getInt reads the age column     .getDouble for double/float/long columns
            int age = allCats.getInt("age");
            System.out.println(name + " is " + age + " years old.");
        }
        // close result set
        allCats.close();

        String dropTableSql = "DROP TABLE cats";   // Delete / drop the cats table
        // statement.execute(dropTableSql);  // Executes

        // close SQL statements
        statement.close();

        // close database connection
        connection.close();
    }

}
