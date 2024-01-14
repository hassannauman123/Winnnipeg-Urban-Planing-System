import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.util.*;
import java.util.List;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Scanner;

public class project {
  static Connection connection;

  public static void main(String[] args) throws Exception {

    // startup sequence
    MyDatabase db = new MyDatabase();
    runConsole(db);

    System.out.println("Exiting...");
  }

  public static void runConsole(MyDatabase db) {

    Scanner console = new Scanner(System.in);
    System.out.println("===================== Trees Database =====================");
    printHelp();
    System.out.print("Welcome! Type h for help. ");
    System.out.print("db > ");
    String line = console.nextLine();
    String[] parts;
    String arg = "";

    while (line != null && !line.equals("q")) {
      parts = line.split("\\s+");
      if (line.indexOf(" ") > 0)
        arg = line.substring(line.indexOf(" ")).trim();

      if (parts[0].equals("h"))
        printHelp();

      else if (parts[0].equals("delete")) {
        try {
          db.delete();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      else if (parts[0].equals("init")) {
        try {
          db.initialize();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      // Get unique species in a park
      else if (parts[0].equals("species")) {
        if (parts.length >= 2)
          db.getUniqueSpecies(arg);
        else
          System.out.println("Require an argument for this command: species <parkID>");
      }

      // get most common assets in the parks
      else if (parts[0].equals("popParkAssets")) {
        try {
          if (parts.length >= 3) {
            if (parts[1].equals("-most")) {
              db.getMostParkAssets(parts[2], true);
            } else if (parts[1].equals("-least")) {
              db.getMostParkAssets(parts[2], false);
            } else {
              System.out.println("Please indicate -most to get the most or -least to get the least.");
            }
          } else
            System.out.println("Require an argument for this command: popParkAssets <-most/-least> <numberOfAssets>");
        } catch (Exception e) {
          System.out.println("Number of assets must be an integer");
        }
      }

      // get most common trees in the park
      else if (parts[0].equals("popParkTrees")) {
        try {
          if (parts.length >= 3) {
            if (parts[1].equals("-most")) {
              db.getMostParkTrees(parts[2], true);
            } else if (parts[1].equals("-least")) {
              db.getMostParkTrees(parts[2], false);
            } else {
              System.out.println("Please indicate -most to get the most or -least to get the least.");
            }
          } else
            System.out.println("Require an argument for this command: popParkTrees <-most/-least> <numberOfTrees>");
        } catch (Exception e) {
          System.out.println("Number of trees must be an integer");
        }
      }

      // get most common trees in the neighborhood
      else if (parts[0].equals("popNTrees")) {
        try {
          if (parts.length >= 3) {
            if (parts[1].equals("-most")) {
              db.getMostNeighborhoodTrees(parts[2], true);
            } else if (parts[1].equals("-least")) {
              db.getMostNeighborhoodTrees(parts[2], false);
            } else {
              System.out.println("Please indicate -most to get the most or -least to get the least.");
            }
          } else
            System.out.println("Require an argument for this command: popNTrees <-most/-least> <numberOfTrees>");
        } catch (Exception e) {
          System.out.println("Number of trees must be an integer");
        }
      }

      // get most popular neighborhoods (neighborhoods with most houses)
      else if (parts[0].equals("denseN")) {
        try {
          if (parts.length >= 3) {
            if (parts[1].equals("-most")) {
              db.getTopNeighborhoods(parts[2], true);
            } else if (parts[1].equals("-least")) {
              db.getTopNeighborhoods(parts[2], false);
            } else {
              System.out.println("Please indicate -most to get the most or -least to get the least.");
            }
          } else
            System.out.println("Require an argument for this command: denseN <-most/-least> <numberOfNeighborhoods>");
        } catch (Exception e) {
          System.out.println("Number of neighborhoods must be an integer");
        }
      }

      // get the houses on the given street
      else if (parts[0].equals("houses")) {
        try {
          if (parts.length >= 2)
            db.getHousesOnStreet(arg);
          else
            System.out.println("Require an argument for this command: houses <Street Name>");
        } catch (Exception e) {
          System.out.println("Street name invalid");
        }
      }

      // get parks with fewer/greater than the given number of trees
      else if (parts[0].equals("parkTrees")) {
        try {
          if (parts.length >= 3) {
            if (parts[1].equals("-fewer")) {
              db.getParksWithFewerTrees(parts[2], true);
            } else if (parts[1].equals("-greater")) {
              db.getParksWithFewerTrees(parts[2], false);
            } else {
              System.out.println("Please indicate -fewer to get fewer trees or -greater to get greater trees.");
            }
          } else
            System.out.println("Require an argument for this command: parkTrees <-fewer/-greater> <numberOfTrees>");
        } catch (Exception e) {
          System.out.println("Number of trees must be an integer");
        }
      }

      // get neighborhoods with fewer/greater than the given number of trees
      else if (parts[0].equals("nTrees")) {
        try {
          if (parts.length >= 3) {
            if (parts[1].equals("-fewer")) {
              db.getNeighborhoodsWithFewerTrees(parts[2], true);
            } else if (parts[1].equals("-greater")) {
              db.getNeighborhoodsWithFewerTrees(parts[2], false);
            } else {
              System.out.println("Please indicate -fewer to get fewer trees or -greater to get greater trees.");
            }
          } else
            System.out.println("Require an argument for this command: nTrees <-fewer/-greater> <numberOfTrees>");
        } catch (Exception e) {
          System.out.println("Number of trees must be an integer");
        }
      }

      // get list of assets which are not in any park
      else if (parts[0].equals("nullAssets")) {
        try {
          db.getAssetsNotInAnyPark();
        } catch (Exception e) {
          System.out.println("Something went wrong: " + e.getMessage());
        }
      }

      // get list of parks with fewer/greater than the given number of assets
      else if (parts[0].equals("parkAssets")) {
        try {
          if (parts.length >= 3) {
            if (parts[1].equals("-fewer")) {
              db.getParksWithFewerAssets(parts[2], true);
            } else if (parts[1].equals("-greater")) {
              db.getParksWithFewerAssets(parts[2], false);
            } else {
              System.out.println("Please indicate -fewer to get fewer assets or -greater to get greater assets.");
            }
          } else
            System.out.println("Require an argument for this command: parkAssets <-fewer/-greater> <numberOfAssets>");
        } catch (Exception e) {
          System.out.println("Number of assets must be an integer");
        }
      }

      // get the common name of the given species biotonical name
      else if (parts[0].equals("name")) {
        try {
          if (parts.length >= 2)
            db.getCommonName(arg);
          else
            System.out.println("Require an argument for this command: name <botonicalName>");
        } catch (Exception e) {
          System.out.println("Invalid input");
        }
      }

      // get the average diameter of trees
      else if (parts[0].equals("average")) {
        try {
          if (parts.length >= 2)
            db.getAverageDiameter(
                arg);
          else
            System.out.println("Require an argument for this command: average <parkName>");
        } catch (Exception e) {
          System.out.println("Invalid input");
        }
      }

      // get the tree count for the given specie
      else if (parts[0].equals("treeCount")) {
        try {
          if (parts.length >= 2)
            db.getTreeCountForSpecies(arg);
          else
            System.out.println("Require an argument for this command: " + "treeCount <botonicalName>");
        } catch (Exception e) {
          System.out.println("Error retrieving tree count. Please check your input.");
        }
      }

      // get the most common trees 
      else if (parts[0].equals("popSpecies")) {
        try {
          if (parts.length >= 3) {
            if (parts[1].equals("-most")) {
              int num = Integer.parseInt(parts[2]);
              db.getMostPopularTrees(num, true);
            } else if (parts[1].equals("-least")) {
              int num = Integer.parseInt(parts[2]);
              db.getMostPopularTrees(num, false);
            } else {
              System.out
                  .println("Please indicate -most to get most popular trees or -least to get least popular trees.");
            }
          } else
            System.out.println("Require an argument for this command: popSpecies <-fewer/-greater> <numberOfSpecies>");
        } catch (Exception e) {
          System.out.println("Number of trees must be an integer");
        }
      }

      else if (parts[0].equals("popNSpecies")) {
        try {
          if (parts.length >= 2) {
            // Join all parts except the first one to handle spaces in neighborhood name
            String tsInput = String.join(
                " ",
                Arrays.copyOfRange(parts, 1, parts.length));

            // Split the input into neighborhoodName and x
            String[] tsParts = tsInput.split(",", 2); // Limit the split to 2 parts

            if (tsParts.length == 2) {
              // Call the method with separated values
              int x = Integer.parseInt(tsParts[1].trim());
              db.getTopTreeSpecies(tsParts[0].trim(), x);
            } else {
              System.out.println("Invalid format. Use popNSpecies <neighborhoodName, x>");
            }
          } else {
            System.out.println("Require an argument for this command");
          }
        } catch (NumberFormatException e) {
          System.out.println("x must be an integer");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      // Get diameter in range or fewer/greater than the given diameter
      else if (parts[0].equals("diameter")) {
        try {
          if (parts.length >= 3) {
            if (parts[1].equals("-range")) {
              System.out.println(parts[2]);
              db.getTreesInDiameterRange("range", parts[2]);
            } else if (parts[1].equals("-greater")) {
              System.out.println(parts[2]);
              db.getTreesInDiameterRange("greater", parts[2]);
            } else if (parts[1].equals("-fewer")) {
              System.out.println(parts[2]);
              db.getTreesInDiameterRange("fewer", parts[2]);
            }
          }
          else
            System.out.println("Invalid format. Use: diameter <-range/-greater/-fewer> <diameter>");
        } catch (Exception e) {
          System.out.println("Invalid diameter range. Please provide a valid range (e.g., 5-10).");
        }
      }

      else if (parts[0].equals("parks")) {
        try {
          if (parts.length >= 2) {
            db.getParkData(Integer.parseInt(parts[1]));
          } else System.out.println(
            "Require an argument for this command: parks <numberOfParks>"
          );
        } catch (Exception e) {
          System.out.println("Number of parks must be an integer");
        }
      }

      else if (parts[0].equals("neighborhoods")) {
        try {
          if (parts.length >= 2) {
            db.getNData(Integer.parseInt(parts[1]));
          } else System.out.println(
            "Require an argument for this command: neighborhoods <numberOfNeighborhoods>"
          );
        } catch (Exception e) {
          System.out.println("Number of neighborhoods must be an integer");
        }
      }

      else if (parts[0].equals("parkID")) {
        if (parts.length >= 2) {
          StringBuilder parkNameBuilder = new StringBuilder();
          for (int i = 1; i < parts.length; i++) {
            parkNameBuilder.append(parts[i]);
            if (i < parts.length - 1) {
              parkNameBuilder.append(" ");
            }
          }
          String parkName = parkNameBuilder.toString();
          db.getParkID(parkName);
        } else {
          System.out.println(
            "Require an argument for this command: parkID <parkName>"
          );
        }
      }

      else
        System.out.println("Read the help with h, or find help somewhere else.");

      System.out.print("db > ");
      line = console.nextLine();
    }

    console.close();
  }

  private static void printHelp() {
      System.out.printf("%-3s %-50s %s%n", "", "Command", "Explanation");
      System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------");
      System.out.printf("%-3s %-50s %s%n", "1.", "h", "Get help");
      System.out.printf("%-3s %-50s %s%n", "2.", "parks <numOfParks>", "Get <numberOfParks> parks in the parks table.");
      System.out.printf("%-3s %-50s %s%n", "3.", "parkID <parkName>", "Get parkID parks of the given <parkName>. ");
      System.out.printf("%-3s %-50s %s%n", "4.", "neighborhoods <numOfNeighborhoods>", "Get <numberOfNeighborhoods> neighborhoods in the neighborhoods table.");
      System.out.printf("%-3s %-50s %s%n", "5.", "species <parkID>", "Get all unique species in the given <parkID>.");
      System.out.printf("%-3s %-50s %s%n", "6.", "popParkAssets <-most/-least> <numberOfAssets>", "Get parks with <-most/-least> assets. <numberOfAssets> is the length of the list.");
      System.out.printf("%-3s %-50s %s%n", "7.", "popParkTrees <-most/-least> <numberOfTrees>", "Get parks with <-most/-least> trees. <numberOfTrees> is the length of the list.");
      System.out.printf("%-3s %-50s %s%n", "8.", "popNTrees <-most/-least> <numberOfTrees>", "Get neighborhoods <-most/-least> trees. <numberOfTrees> is the length of the list.");
      System.out.printf("%-3s %-50s %s%n", "9.", "denseN <-most/-least> <numberOfNeighborhoods>", "Get <-most/-least> dense neighborhoods (Neighborhoods with the <-most/-least> number of houses.)");
      System.out.printf("%-3s %-50s %s%n", "10.", "houses <streetName>", "Get houses on the given street.");
      System.out.printf("%-3s %-50s %s%n", "11.", "parkTrees <-fewer/-greater> <numberOfTrees>", "Get parks with <-fewer/-greater> trees than <numberOfTrees>.");
      System.out.printf("%-3s %-50s %s%n", "12.", "nTrees <-fewer/-greater> <numberOfTrees>", "Get neighborhoods with <-fewer/-greater> trees than <numberOfTrees>.");
      System.out.printf("%-3s %-50s %s%n", "13.", "nullAssets", "Get assets that are not in any park");
      System.out.printf("%-3s %-50s %s%n", "14.", "parkAssets <-fewer/-greater> <numberOfAssets>", "Get parks with <-fewer/-greater> assets than <numberOfAssets>.");
      System.out.printf("%-3s %-50s %s%n", "15.", "name <botanicalName>", "Get the common name of <botanicalName>.");
      System.out.printf("%-3s %-50s %s%n", "16.", "average <parkName>", "Get the average diameter of trees in the given park.");
      System.out.printf("%-3s %-50s %s%n", "17.", "treeCount <botanicalName>", "Get the tree count for the given species botanical name.");
      System.out.printf("%-3s %-50s %s%n", "18.", "popSpecies <-most/-least> <numberOfSpecies>", "Get <-most/-least> popular species. <numberOfSpecies> is the length of the list.");
      System.out.printf("%-3s %-50s %s%n", "19.", "popNSpecies <neighborhoodName, x>", "Get x msot popular tree species in the <neighborhoodname>. x is the length of the list.");
      System.out.printf("%-3s %-50s %s%n", "20.", "diameter <-range/-greater/-fewer> <diameter>", "Get trees which have diameter <-greater/-fewer> than the specified <diameter>,");
      System.out.printf("%-3s %-50s %s%n", "", "", "or which have diamter in <-range> of the given <diameter>, if range the format of <diameter> has to be minRange-maxRange (eg. 5-10)");
      System.out.printf("%-3s %-50s %s%n", "21.", "delete", "Delete all the tables and the records in the database.");
      System.out.printf("%-3s %-50s %s%n", "22.", "init", "Initialize the database by creating tables and adding all the records");
      System.out.printf("%-3s %-50s %s%n", "23.", "q", "Exit the program");
      System.out.println("---- end of help -----\n");
      System.out.print("db > ");
  }
}

class MyDatabase {
  private Connection connection;

  public MyDatabase() {
    try {
      String url = "jdbc:sqlite:project1.db";
      connection = DriverManager.getConnection(url);
    } catch (SQLException e) {
      e.printStackTrace(System.out);
    }
  }

  public void delete() {
      try {
          String[] tableNames = {"neighborhoods", "addresses", "trees", "assetPark", "parks", "species", "bioname", "assets"};

          for (String tableName : tableNames) {
              String sql = "DROP TABLE IF EXISTS " + tableName + ";";
              try (PreparedStatement statement = connection.prepareStatement(sql)) {
                  statement.execute();
              }
          }
      } catch (Exception e) {
          System.err.println("Error deleting tables.");
          e.printStackTrace();
      }
  }

  public void initialize() {
    try {
        long startTime = System.currentTimeMillis();

        System.out.println("Starting initialization");

        String[] createTableStatements = {
                "CREATE TABLE assets (assetID INTEGER PRIMARY KEY AUTOINCREMENT, assetClass TEXT NOT NULL, assetType TEXT, assetSize TEXT);",
                "CREATE TABLE bioName (biotonicalName TEXT PRIMARY KEY, commonName TEXT NOT NULL);",
                "CREATE TABLE species (specieID INTEGER PRIMARY KEY AUTOINCREMENT, biotonicalName TEXT REFERENCES bioName(biotonicalName));",
                "CREATE TABLE parks ( parkID INTEGER PRIMARY KEY, parkName TEXT NOT NULL);",
                "CREATE TABLE assetPark (assetID INTEGER REFERENCES assets(assetID), parkID INTEGER REFERENCES parks(parkID), PRIMARY KEY (assetID, parkID));",
                "CREATE TABLE neighborhoods (neighborhoodID INTEGER PRIMARY KEY AUTOINCREMENT, neighborhoodName TEXT NOT NULL);",
                "CREATE TABLE addresses (addressID INTEGER PRIMARY KEY AUTOINCREMENT, streetNumber INTEGER NOT NULL, streetName TEXT NOT NULL, streetType TEXT NOT NULL, neighborhoodID TEXT REFERENCES neighborhoods(neighborhoodID));",
                "CREATE TABLE trees (treeID INTEGER PRIMARY KEY, diameter INTEGER NOT NULL, parkID INTEGER REFERENCES parks(parkID), specieID INTEGER REFERENCES species(specieID), neighborhoodID INTEGER REFERENCES neighborhoods(neighborhoodID));"
        };

        for (String createTableStatement : createTableStatements) {
            try (PreparedStatement statement = connection.prepareStatement(createTableStatement)) {
                statement.execute();
            }
        }

        insertItems();

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Initialization completed in " + totalTime + " milliseconds.");
    } catch (Exception e) {
        System.err.println("Error initializing database.");
        e.printStackTrace();
    }
  }

  public void insertItems() {
    try {
        // Read the SQL queries from the project.sql file
        String sqlFilePath = "project.sql";
        BufferedReader reader = new BufferedReader(new FileReader(sqlFilePath));
        StringBuilder sqlQueries = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sqlQueries.append(line).append(" ");
        }

        // Split the content into individual SQL queries
        String[] queries = sqlQueries.toString().split(";");

        // Execute each SQL query
        for (String query : queries) {
            if (!query.trim().isEmpty()) {
                try (Statement statement = connection.createStatement()) {
                    System.out.println(query);
                    statement.execute(query);
                } catch (Exception e) {
                    // Handle any exceptions during query execution (e.g., print or log the error)
                    System.err.println("Error executing SQL query: " + query);
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Initialization complete.");

    } catch (Exception e) {
        // Handle file reading or other exceptions
        System.err.println("Error initializing database.");
        e.printStackTrace();
    }
  }

  public void getParkID(String parkName) {
    try {
      String query = "SELECT parks.parkID FROM parks WHERE parks.parkName LIKE ?";

      PreparedStatement pstmt = connection.prepareStatement(query);
      pstmt.setString(1, parkName); // Use setString for VARCHAR/TEXT type parameters
      ResultSet rs = pstmt.executeQuery();
      boolean found = false;

      System.out.printf("%-50s%n", "Park ID");
      System.out.println("--------------------------------------------------");

      while (rs.next()) {
        System.out.printf("%-50s%n", rs.getInt("parkID"));
        found = true;
      }

      if (!found) {
        System.out.println(
          "No park found for the given park name, or no such park exists"
        );
      }
      pstmt.close();
    } catch (SQLException e) {
      System.out.println("Error executing SQL query: " + e.getMessage());
    }
  }

  public void getParkData(int topX) {
    try {
      String query = "SELECT * FROM Parks LIMIT ?";
      PreparedStatement pstmt = connection.prepareStatement(query);
      pstmt.setInt(1, topX);

      ResultSet rs = pstmt.executeQuery();

      System.out.printf("%-10s %-15s%n", "ParkID", "ParkName");
      System.out.println(
        "------------------------------------------------------------"
      );

      while (rs.next()) {
        int parkID = rs.getInt("parkID");
        String parkName = rs.getString("parkName");

        System.out.printf("%-10s %-15s%n", parkID, parkName);
      }

      pstmt.close();
    } catch (SQLException | NumberFormatException e) {
      System.out.println(
        "Error retrieving park information: " + e.getMessage()
      );
    }
  }

  public void getNData(int topX) {
    try {
      String query = "SELECT * FROM neighborhoods LIMIT ?";
      PreparedStatement pstmt = connection.prepareStatement(query);
      pstmt.setInt(1, topX);

      ResultSet rs = pstmt.executeQuery();

      System.out.printf("%-20s %-30s%n", "neighborhoodID", "neighborhoodName");
      System.out.println(
        "------------------------------------------------------------"
      );

      while (rs.next()) {
        int neighborhoodID = rs.getInt("neighborhoodID");
        String neighborhoodName = rs.getString("neighborhoodName");

        System.out.printf("%-20s %-30s%n", neighborhoodID, neighborhoodName);
      }

      pstmt.close();
    } catch (SQLException | NumberFormatException e) {
      System.out.println(
        "Error retrieving park information: " + e.getMessage()
      );
    }
  }

  public void getUniqueSpecies(String id) {
      try {
          int parkID = Integer.parseInt(id);
          String query = "SELECT DISTINCT bioName.biotonicalName FROM trees " +
                  "JOIN species ON trees.specieID = species.specieID " +
                  "JOIN bioName ON species.biotonicalName = bioName.biotonicalName " +
                  "WHERE trees.parkID = ?";
          PreparedStatement pstmt = connection.prepareStatement(query);
          pstmt.setInt(1, parkID);
          ResultSet rs = pstmt.executeQuery();
          boolean found = false;

          System.out.printf("%-50s%n", "Species");
          System.out.println("--------------------------------------------------");

          while (rs.next()) {
              System.out.printf("%-50s%n", rs.getString("biotonicalName"));
              found = true;
          }

          if (!found) {
              System.out.println("No species found for the given park ID, or no such park exists");
          }
          pstmt.close();
      } catch (SQLException | NumberFormatException e) {
          System.out.println("Invalid input.");
      }
  }

  public void getMostParkAssets(String id, boolean top) {
    try {
      int numPark = Integer.parseInt(id);
      String order = "DESC";

      if (top == false) {
        order = "ASC";
      }

      String query = "SELECT parks.parkName, COUNT(assetPark.assetID) AS assetCount " +
          "FROM parks " +
          "LEFT JOIN assetPark ON parks.parkID = assetPark.parkID " +
          "GROUP BY parks.parkID, parks.parkName " +
          "ORDER BY assetCount " + order +
          " LIMIT ?";
      PreparedStatement pstmt = connection.prepareStatement(query);
      pstmt.setInt(1, numPark);
      ResultSet rs = pstmt.executeQuery();
      boolean found = false;

      System.out.printf("%-40s %-50s%n", "Park Name", "Asset Count");
      System.out.println("------------------------------------------------------------");

      while (rs.next()) {
        String parkName = rs.getString("parkName");
        int assetCount = rs.getInt("assetCount");

        System.out.printf("%-40s %-50s%n", parkName, assetCount);
      }
      pstmt.close();
    } catch (SQLException | NumberFormatException e) {
      System.out.println("Invalid input.");
    }
  }

  public void getMostParkTrees(String id, boolean top) {
    try {
      int numPark = Integer.parseInt(id);
      String order = "DESC";

      if (top == false) {
        order = "ASC";
      }
      String query = "SELECT parks.parkName, COUNT(trees.treeID) AS treeCount " +
          "FROM parks LEFT JOIN trees ON parks.parkID = trees.parkID " +
          "GROUP BY parks.parkID, parks.parkName " +
          "ORDER BY treeCount " + order +
          " LIMIT ?";

      PreparedStatement pstmt = connection.prepareStatement(query);
      pstmt.setInt(1, numPark);
      ResultSet rs = pstmt.executeQuery();

      System.out.printf("%-30s %-15s%n", "Park Name", "Number of Trees");
      System.out.println("---------------------------------------------------");

      while (rs.next()) {
        String parkName = rs.getString("parkName");
        int treeCount = rs.getInt("treeCount");

        System.out.printf("%-30s %-15s%n", parkName, treeCount);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void getMostNeighborhoodTrees(String id, boolean top) {
    try {
      int numNeighborhood = Integer.parseInt(id);
      String order = "DESC";

      if (top == false) {
        order = "ASC";
      }
      String query = "SELECT neighborhoods.neighborhoodName, COUNT(trees.treeID) AS treeCount " +
          "FROM neighborhoods LEFT JOIN trees ON neighborhoods.neighborhoodID = trees.neighborhoodID " +
          "GROUP BY neighborhoods.neighborhoodID, neighborhoods.neighborhoodName " +
          "ORDER BY treeCount " + order +
          " LIMIT ?";

      PreparedStatement pstmt = connection.prepareStatement(query);
      pstmt.setInt(1, numNeighborhood);
      ResultSet rs = pstmt.executeQuery();

      System.out.printf("%-30s %-15s%n", "Neighborhood", "Number of Trees");
      System.out.println("---------------------------------------------------");

      while (rs.next()) {
        String neighborhoodName = rs.getString("neighborhoodName");
        int treeCount = rs.getInt("treeCount");

        System.out.printf("%-30s %-15s%n", neighborhoodName, treeCount);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void getTopNeighborhoods(String id, boolean top) {
    try {
      int numNeighborhood = Integer.parseInt(id);
      String order = "DESC";

      if (top == false) {
        order = "ASC";
      }
      String query = "SELECT neighborhoods.neighborhoodName AS \"Neighborhood Name\", " +
          "COUNT(addresses.addressID) AS \"Number of Houses\" " +
          "FROM neighborhoods " +
          "JOIN addresses ON neighborhoods.neighborhoodID = addresses.neighborhoodID " +
          "GROUP BY neighborhoods.neighborhoodName " +
          "ORDER BY COUNT(addresses.addressID) " + order +
          " LIMIT ?;";

      PreparedStatement pstmt = connection.prepareStatement(query);
      pstmt.setInt(1, numNeighborhood);
      ResultSet rs = pstmt.executeQuery();

      System.out.printf("%-20s %-20s%n", "Neighborhood Name", "Number of Houses");
      System.out.println("------------------------------------------------------");

      while (rs.next()) {
        String neighborhoodName = rs.getString("Neighborhood Name");
        int numHouses = rs.getInt("Number of Houses");

        System.out.printf("%-20s %-20s%n", neighborhoodName, numHouses);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void getHousesOnStreet(String streetName) {
    try {
      // Query to retrieve houses on the given street
      String houseQuery = "SELECT a.addressID, a.streetNumber, a.streetName, a.streetType, n.neighborhoodName " +
          "FROM addresses a " +
          "JOIN neighborhoods n ON a.neighborhoodID = n.neighborhoodID " +
          "WHERE a.streetName LIKE ?";

      // Prepare and execute the query
      PreparedStatement houseStmt = connection.prepareStatement(houseQuery);
      houseStmt.setString(1, streetName);
      ResultSet houseRs = houseStmt.executeQuery();

      // List to store house details
      List<String> houseList = new ArrayList<>();

      // Add column names to the list
      houseList.add("Address ID\tStreet Number\tStreet Name\tStreet Type\tNeighborhood Name");

      // Add house details to the list
      while (houseRs.next()) {
        String houseDetails = String.format("%-16s%-16s%-16s%-16s%-15s",
            houseRs.getInt("addressID"),
            houseRs.getInt("streetNumber"),
            houseRs.getString("streetName"),
            houseRs.getString("streetType"),
            houseRs.getString("neighborhoodName"));
        houseList.add(houseDetails);
      }

      // Display house details in a tabular format
      if (!houseList.isEmpty()) {
        System.out.println("Houses on " + streetName + ":");
        for (String details : houseList) {
          System.out.println(details);
        }
      } else {
        System.out.println("No houses on " + streetName + ".");
      }

      // Query to retrieve the total number of houses on the given street
      String totalHousesQuery = "SELECT COUNT(*) as totalHouses FROM addresses WHERE streetName LIKE ?";

      // Prepare and execute the query
      PreparedStatement totalHousesStmt = connection.prepareStatement(totalHousesQuery);
      totalHousesStmt.setString(1, streetName);
      ResultSet totalHousesRs = totalHousesStmt.executeQuery();

      // Display the total number of houses
      if (totalHousesRs.next()) {
        int totalHouses = totalHousesRs.getInt("totalHouses");
        System.out.println("---------------------------------------------------");
        System.out.println("Total number of houses on " + streetName + ": " + totalHouses);
        System.out.println("---------------------------------------------------");
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void getParksWithFewerTrees(String id, boolean fewer) {
    try {
      int numTrees = Integer.parseInt(id);
      String order = "<";

      if (fewer == false) {
        order = ">";
      }
      String query = "SELECT parks.parkName, COUNT(trees.treeID) AS treeCount " +
          "FROM parks " +
          "LEFT JOIN trees ON parks.parkID = trees.parkID " +
          "GROUP BY parks.parkID, parks.parkName " +
          "HAVING treeCount " + order + " ? " +
          "ORDER BY treeCount DESC";

      PreparedStatement pstmt = connection.prepareStatement(query);
      pstmt.setInt(1, numTrees);
      ResultSet rs = pstmt.executeQuery();
      boolean found = false;

      System.out.printf("%-50s %-15s%n", "Park Name", "Tree Count");
      System.out.println("--------------------------------------------------------------");

      while (rs.next()) {
        String parkName = rs.getString("parkName");
        int treeCount = rs.getInt("treeCount");

        System.out.printf("%-50s %-15s%n", parkName, treeCount);
        found = true;
      }

      if (!found) {
        System.out.println("No parks found with fewer/greater trees than the specified number.");
      }

      pstmt.close();
    } catch (SQLException | NumberFormatException e) {
      System.out.println("Invalid input. Number of trees must be an integer.");
    }
  }

  public void getNeighborhoodsWithFewerTrees(String id, boolean fewer) {
    try {
      int numTrees = Integer.parseInt(id);
      String order = "<";

      if (fewer == false) {
        order = ">";
      }

      String query = "SELECT neighborhoods.neighborhoodName, COUNT(trees.treeID) AS treeCount " +
          "FROM neighborhoods " +
          "LEFT JOIN trees ON neighborhoods.neighborhoodID = trees.neighborhoodID " +
          "GROUP BY neighborhoods.neighborhoodID, neighborhoods.neighborhoodName " +
          "HAVING treeCount " + order + " ? " +
          "ORDER BY treeCount DESC";

      PreparedStatement pstmt = connection.prepareStatement(query);
      pstmt.setInt(1, numTrees);
      ResultSet rs = pstmt.executeQuery();
      boolean found = false;

      System.out.printf("%-50s %-15s%n", "Neighborhood Name", "Tree Count");
      System.out.println("--------------------------------------------------------------");

      while (rs.next()) {
        String neighborhoodName = rs.getString("neighborhoodName");
        int treeCount = rs.getInt("treeCount");

        System.out.printf("%-50s %-15s%n", neighborhoodName, treeCount);
        found = true;
      }

      if (!found) {
        System.out.println("No neighborhoods found with fewer/greater trees than the specified number.");
      }

      pstmt.close();
    } catch (SQLException | NumberFormatException e) {
      System.out.println("Invalid input. Number of trees must be an integer.");
    }
  }

  public void getAssetsNotInAnyPark() {
    try {
      String query = "SELECT * " +
          "FROM assets " +
          "WHERE assetID NOT IN (SELECT assetID FROM assetPark)";

      PreparedStatement pstmt = connection.prepareStatement(query);
      ResultSet rs = pstmt.executeQuery();
      boolean found = false;

      System.out.printf("%-10s %-30s %-30s %-15s%n", "Asset ID", "Asset Class", "Asset Type", "Asset Size");
      System.out.println("---------------------------------------------------------------------------------------");

      while (rs.next()) {
        int assetID = rs.getInt("assetID");
        String assetClass = rs.getString("assetClass");
        String assetType = rs.getString("assetType");
        String assetSize = rs.getString("assetSize");

        System.out.printf("%-10s %-30s %-30s %-15s%n", assetID, assetClass, assetType, assetSize);
        found = true;
      }

      if (!found) {
        System.out.println("No assets found that are not in any park.");
      }

      pstmt.close();
    } catch (SQLException e) {
      System.out.println("Error executing the SQL query: " + e.getMessage());
    }
  }

  public void getParksWithFewerAssets(String numAssets, boolean fewer) {
    try {
      int givenNumAssets = Integer.parseInt(numAssets);
      String order = "<";

      if (fewer == false) {
        order = ">";
      }

      String query = "SELECT parks.parkName, COUNT(assetPark.assetID) AS assetCount " +
          "FROM parks " +
          "LEFT JOIN assetPark ON parks.parkID = assetPark.parkID " +
          "GROUP BY parks.parkID, parks.parkName " +
          "HAVING assetCount " + order + " ? " +
          "ORDER BY assetCount DESC";

      PreparedStatement pstmt = connection.prepareStatement(query);
      pstmt.setInt(1, givenNumAssets);
      ResultSet rs = pstmt.executeQuery();
      boolean found = false;

      System.out.printf("%-50s %-15s%n", "Park Name", "Number of Assets");
      System.out.println("--------------------------------------------------------------");

      while (rs.next()) {
        String parkName = rs.getString("parkName");
        int assetCount = rs.getInt("assetCount");

        System.out.printf("%-50s %-15s%n", parkName, assetCount);
        found = true;
      }

      if (!found) {
        System.out.println("No parks found with fewer/greater assets than the specified number.");
      }

      pstmt.close();
    } catch (SQLException | NumberFormatException e) {
      System.out.println("Invalid input. Number of assets must be an integer.");
    }
  }

  // Add grater or lass than
  public void getTreesInDiameterRange(String protocol, String diameterRange) {
    try {
      String order = "ASC";
      String sProtocol = "";
      int minDiameter = 0;
      int maxDiameter = 0;

      if (protocol.equals("range")) {
        String[] range = diameterRange.split("-");
        minDiameter = Integer.parseInt(range[0]);
        maxDiameter = Integer.parseInt(range[1]);
        sProtocol = "WHERE trees.diameter BETWEEN ? AND ? ";
      } else if (protocol.equals("greater")) {
        sProtocol = "WHERE trees.diameter > ? ";
      } else if (protocol.equals("fewer")){
        sProtocol = "WHERE trees.diameter < ? ";
        order = "DESC";
      }

      String query = "SELECT trees.treeID, trees.diameter, bioName.biotonicalName " +
          "FROM trees " +
          "JOIN species ON trees.specieID = species.specieID " +
          "JOIN bioName ON species.biotonicalName = bioName.biotonicalName " +
           sProtocol +
          "ORDER BY trees.diameter " + order;


      PreparedStatement pstmt = connection.prepareStatement(query);

      if (protocol.equals("range")) {
        pstmt.setInt(1, minDiameter);
        pstmt.setInt(2, maxDiameter);
      } else if (protocol.equals("greater")) {
        pstmt.setInt(1, Integer.parseInt(diameterRange));
      } else if (protocol.equals("fewer")) {
        pstmt.setInt(1, Integer.parseInt(diameterRange));
      }

      ResultSet rs = pstmt.executeQuery();

      // Print table header
      System.out.printf("%-10s %-10s %-20s%n",
          "Tree ID", "Diameter", "Botanical Name");

      System.out.println("--------------------------------------------------------------");

      while (rs.next()) {
        int treeID = rs.getInt("treeID");
        int diameter = rs.getInt("diameter");
        String botanicalName = rs.getString("biotonicalName");

        // Print each row in the table
        System.out.printf("%-10s %-10s %-20s%n",
            treeID, diameter, botanicalName);
      }

      pstmt.close();

    } catch (SQLException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
      System.out.println("Invalid input. Please provide a valid diameter range (e.g., 5-10)." + e.getMessage());
    }
  }

  public void getTreesWithGreaterDiameterInNeighborhood(String neighborhood, String diameter, boolean greater) {
    try {
      int diameterValue = Integer.parseInt(diameter);
      String order = ">";

      if (greater == false) {
        order = "<";
      }
      String query = "SELECT trees.treeID, bioName.biotonicalName, trees.diameter " +
          "FROM trees " +
          "JOIN species ON trees.specieID = species.specieID " +
          "JOIN neighborhoods ON trees.neighborhoodID = neighborhoods.neighborhoodID " +
          "WHERE trees.diameter " + order + " ? AND neighborhoods.neighborhoodName LIKE ?";

      PreparedStatement pstmt = connection.prepareStatement(query);
      pstmt.setInt(1, Integer.parseInt(diameter));
      pstmt.setString(2, neighborhood);

      ResultSet rs = pstmt.executeQuery();
      boolean found = false;

      System.out.printf("%-15s %-25s %-10s%n", "Tree ID", "Biotonical Name", "Diameter");
      System.out.println("--------------------------------------------------------------");

      while (rs.next()) {
        int treeID = rs.getInt("treeID");
        String biotonicalName = rs.getString("biotonicalName");
        int treeDiameter = rs.getInt("diameter");

        System.out.printf("%-15s %-25s %-10s%n", treeID, biotonicalName, treeDiameter);
        found = true;
      }

      if (!found) {
        System.out.println("No trees found with the specified criteria.");
      }

      pstmt.close();
    } catch (SQLException | NumberFormatException e) {
      System.out.println("Invalid input. Diameter must be an integer.");
    }
  }

  public void getCommonName(String id) {
    try {
      String query = "SELECT commonName FROM bioName WHERE biotonicalName like ?";
      PreparedStatement pstmt = connection.prepareStatement(query);
      pstmt.setString(1, id);

      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        System.out.println("Common Name: " + rs.getString("commonName"));
      } else {
        System.out.println("No specie with that botonical name exists.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void getAverageDiameter(String parkName) {
    try {
      String query = "SELECT " +
          "   p.parkName AS \"Park Name\", " +
          "   COUNT(t.treeID) AS \"Tree Count\", " +
          "   AVG(t.diameter) AS \"Average Diameter\" " +
          "FROM " +
          "   parks p " +
          "LEFT JOIN " +
          "   trees t ON t.parkID = p.parkID " +
          "WHERE " +
          "   p.parkName LIKE ? " +
          "GROUP BY " +
          "   p.parkName";

      PreparedStatement pstmt = connection.prepareStatement(query);
      pstmt.setString(1, parkName);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        System.out.println("Park Name: " + rs.getString("Park Name"));
        System.out.println("Tree Count: " + rs.getInt("Tree Count"));
        System.out.println(
            "Average Diameter: " + rs.getDouble("Average Diameter"));
      } else {
        System.out.println("No data available for the specified park.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void getTreeCountForSpecies(String biotonicalName) {
    try {
      System.out.println(biotonicalName);
      String query = "SELECT species.biotonicalName, COUNT(trees.treeID) AS treeCount " +
          "FROM species " +
          "LEFT JOIN trees ON species.specieID = trees.specieID " +
          "WHERE species.biotonicalName LIKE ? " +
          "GROUP BY species.biotonicalName ";

      PreparedStatement pstmt = connection.prepareStatement(query);
      pstmt.setString(1, biotonicalName);
      ResultSet rs = pstmt.executeQuery();
      boolean found = false;

      System.out.printf("%-30s %-15s%n", "Trees Bio Name", "Tree Count");
      System.out.println("--------------------------------------------------");

      while (rs.next()) {
        String speciesClass = rs.getString("biotonicalName");
        int treeCount = rs.getInt("treeCount");

        System.out.printf("%-30s %-15s%n", speciesClass, treeCount);
        found = true;
      }

      if (!found) {
        System.out.println("No trees found for the specified species class.");
      }

      pstmt.close();
    } catch (SQLException e) {
      System.out.println("Error retrieving tree count for the specified species class.");
    }
  }

  public void getMostPopularTrees(int x, boolean top) {
    try {
      String order = "DESC";

      if (top == false) {
        order = "ASC";
      }
      String query = "SELECT bioName.biotonicalName, bioName.commonName, COUNT(trees.treeID) AS treeCount " +
          "FROM trees " +
          "JOIN species ON trees.specieID = species.specieID " +
          "JOIN bioName ON species.biotonicalName = bioName.biotonicalName " +
          "GROUP BY bioName.biotonicalName, bioName.commonName " +
          "ORDER BY treeCount " + order +
          " LIMIT ?";

      PreparedStatement pstmt = connection.prepareStatement(query);
      pstmt.setInt(1, x);
      ResultSet rs = pstmt.executeQuery();

      System.out.printf("%-45s %-45s %-15s%n", "Botonical Name", "Common Name", "Tree Count");
      System.out.println("-----------------------------------------------------------------------------------------------------");

      boolean found = false;

      while (rs.next()) {
        String biotonicalName = rs.getString("biotonicalName");
        String commonName = rs.getString("commonName");
        int treeCount = rs.getInt("treeCount");

        System.out.printf("%-45s %-45s %-15s%n", biotonicalName, commonName, treeCount);
        found = true;
      }

      if (!found) {
        System.out.println("No trees found.");
      }

      pstmt.close();
    } catch (SQLException | NumberFormatException e) {
      System.out.println("Error retrieving tree information: " + e.getMessage());
    }
  }

  public void getTopTreeSpecies(String neighborhoodName, int topX) {
    try {
      String query = "SELECT bioName.biotonicalName, bioName.commonName, COUNT(trees.treeID) AS treeCount " +
          "FROM neighborhoods n " +
          "LEFT JOIN trees ON trees.neighborhoodID = n.neighborhoodID " +
          "LEFT JOIN species ON trees.specieID = species.specieID " +
          "LEFT JOIN bioName ON species.biotonicalName = bioName.biotonicalName " +
          "WHERE n.neighborhoodName = ? " +
          "GROUP BY bioName.biotonicalName, bioName.commonName " +
          "ORDER BY treeCount DESC" +
          " LIMIT ?";

      PreparedStatement pstmt = connection.prepareStatement(query);
      pstmt.setString(1, neighborhoodName);
      pstmt.setInt(2, topX);

      ResultSet rs = pstmt.executeQuery();

      System.out.printf(
          "%-45s %-30s %-15s%n",
          "Biotonical Name",
          "Common Name",
          "Tree Count");
      System.out.println(
          "--------------------------------------------------------------------------------------");

      boolean found = false;

      while (rs.next()) {
        String biotonicalName = rs.getString("biotonicalName");
        String commonName = rs.getString("commonName");
        int treeCount = rs.getInt("treeCount");

        System.out.printf(
            "%-45s %-30s %-15s%n",
            biotonicalName,
            commonName,
            treeCount);
        found = true;
      }

      if (!found) {
        System.out.println("No data found for the given neighborhood.");
      }

      pstmt.close();
    } catch (SQLException | NumberFormatException e) {
      System.out.println(
          "Error retrieving tree information: " + e.getMessage());
    }
  }
}
