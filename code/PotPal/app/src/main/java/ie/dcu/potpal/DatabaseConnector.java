package ie.dcu.potpal;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class DatabaseConnector {
    static final String connectionURL = "jdbc:sqlite:ie/dcu/potpal/sql/plant_data";
    public static List<String> connectToDatabase(String plantLatinName) {
        List<String> plantData = new ArrayList<>();
        String query = "SELECT x.* FROM \"Plant Data\" x WHERE `Latin Name` == " + "'" + plantLatinName + "'";
        try(Connection conn = DriverManager.getConnection(connectionURL);
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(query);) {
            for(int i=1; i<=5; i++) {
                plantData.add(result.getString(i));
            }
        }
        catch (SQLException e) {
            Log.e("DatabaseConnector", "Unable to connect to database. Error code: " + e.getErrorCode() + " Error: " + e.getMessage());
        }
        return plantData;
    }
}
