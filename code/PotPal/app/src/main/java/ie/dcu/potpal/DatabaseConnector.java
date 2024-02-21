package ie.dcu.potpal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class DatabaseConnector {

    // Path where the database will be stored
    private static final String DB_PATH = "/data/data/ie.dcu.potpal/databases/";
    // Name of the database file
    private static final String DB_NAME = "plant_data.db";
    // JDBC connection URL for SQLite
    private static final String connectionURL = "jdbc:sqlite:" + DB_PATH + DB_NAME;

    public static void copyDataBaseFromAssets(Context context) {
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            // Check if the database file already exists
            File dbFile = context.getDatabasePath(DB_NAME);
            if (dbFile.exists()) {
                return; // Database file already exists, no need to copy
            }

            // Create the parent directory if it doesn't exist
            File dbDir = dbFile.getParentFile();
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }

            // Open the database file from assets folder
            myInput = context.getAssets().open("databases/" + DB_NAME);

            // Path to the new database file
            String outFileName = dbFile.getAbsolutePath();

            // Open the empty database file as the output stream
            myOutput = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Close the streams
            myOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (myOutput != null) {
                    myOutput.close();
                }
                if (myInput != null) {
                    myInput.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> fetchPlantData(String latinName) {
        List<String> plantData = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            // Open the database
            db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);

            // Query the database to fetch data based on Latin Name
            cursor = db.query("PlantData", null, "LatinName=?", new String[]{latinName}, null, null, null);

            // If cursor is not null and has data, move to the first row
            if (cursor != null && cursor.moveToFirst()) {
                // Retrieve data from cursor and append to list
                plantData.add(cursor.getString(cursor.getColumnIndex("Type")));
                plantData.add(cursor.getString(cursor.getColumnIndex("Sunlight")));
                plantData.add(cursor.getString(cursor.getColumnIndex("Water")));
                plantData.add(cursor.getString(cursor.getColumnIndex("CommonNames")));
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // Close the cursor and database
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
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
