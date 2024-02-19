package ie.dcu.potpal;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class DatabaseConnector {
    /**connectionURL Static URL address of the database.*/
    static final String connectionURL = "jdbc:sqlite:ie/dcu/potpal/sql/plant_data";

    /**
     * Fetches data of specified plant from database using plant's latin name.
     * @param plantLatinName Plant's latin name.
     * @return List of string with plant's latin name, type, sunlight needs, water needs and common names.
     */
    public static List<String> fetchFromDatabase(String plantLatinName) {
        //List to store fetched data about plant
        List<String> plantData = new ArrayList<>();
        //SQL query to select plant data.
        String query = "SELECT x.* FROM \"Plant Data\" x WHERE `Latin Name` == " + "'" + plantLatinName + "'";
        //Attempt to connect to database and execute statement.
        try(Connection conn = DriverManager.getConnection(connectionURL);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);) {
            for(int i=1; i<=5; i++) {
                plantData.add(result.getString(i));
            }
        }
        catch (SQLException e) {
            Log.e("DatabaseConnectorFetch", "Unable to connect to database. Error code: " + e.getErrorCode() + " Error: " + e.getMessage());
        }
        return plantData;
    }

    /**
     * Inserts plant data into the database.
     * @param latinName Plant's latin name. Primary key. Cannot be duplicate in the database.
     * @param type String of plant's type (ex. Succulent, Cactus, Vine).
     * @param sunlight String of plant's sunlight needs (ex. Full Sunlight, Partial Shade).
     * @param water String of plant's water needs (ex. Mesic, Dry Mesic).
     * @param commonNames String of other common names plant is known by. If unknown, should be N/A.
     */
    public static void insertToDatabase(String latinName, String type, String sunlight, String water, String commonNames) {
        String query = "INSERT INTO \"Plant Data\" VALUES ('" + latinName + "', '" + type + "', '" + sunlight + "', '" + water + "', '" + commonNames + "')";
        try{
            Connection conn = DriverManager.getConnection(connectionURL);
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
            conn.close();
        }
        catch (SQLException e) {
            Log.e( "DatabseConnectorInsert", "Unable to connect to dabase. Error code: " + e.getErrorCode() + " " + e.getMessage());
        }
    }
}
