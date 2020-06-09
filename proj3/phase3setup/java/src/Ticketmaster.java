/*
 * Submission for CS166 Project Phase 3: Implementation
 * =============================
 * Nicholas Kory
 * ID 862-18-9331
 * nkory003@ucr.edu
 *
 * Bryan Parada
 * ID 861-02-7456
 * bpara001@ucr.edu
 *
 * Database Management Systems
 * Department of Computer Science and Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.ResolverStyle;
import java.util.regex.*;
import java.time.temporal.*;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */

public class Ticketmaster{
  //reference to physical database connection
  private Connection _connection = null;
  static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

  // Regex email validation taken from:
  // https://howtodoinjava.com/regex/java-regex-validate-email-address/
  static final String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
  static final Pattern pattern = Pattern.compile(regex);

  public Ticketmaster(String dbname, String dbport, String user, String passwd) throws SQLException {
    System.out.print("Connecting to database...");
    try{
      // constructs the connection URL
      String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
      System.out.println ("Connection URL: " + url + "\n");

      // obtain a physical connection
          this._connection = DriverManager.getConnection(url, user, passwd);
          System.out.println("Done");
    }catch(Exception e){
      System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
          System.out.println("Make sure you started postgres on this machine");
          System.exit(-1);
    }
  }

  /**
   * Method to execute an update SQL statement.  Update SQL instructions
   * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
   *
   * @param sql the input SQL string
   * @throws java.sql.SQLException when update failed
   * */
  public void executeUpdate (String sql) throws SQLException {
    // creates a statement object
    Statement stmt = this._connection.createStatement ();

    // issues the update instruction
    stmt.executeUpdate (sql);

    // close the instruction
      stmt.close ();
  }//end executeUpdate

  /**
   * Method to execute an input query SQL instruction (i.e. SELECT).  This
   * method issues the query to the DBMS and outputs the results to
   * standard out.
   *
   * @param query the input query string
   * @return the number of rows returned
   * @throws java.sql.SQLException when failed to execute the query
   */
  public int executeQueryAndPrintResult (String query) throws SQLException {
    //creates a statement object
    Statement stmt = this._connection.createStatement ();

    //issues the query instruction
    ResultSet rs = stmt.executeQuery (query);

    /*
     *  obtains the metadata object for the returned result set.  The metadata
     *  contains row and column info.
     */
    ResultSetMetaData rsmd = rs.getMetaData ();
    int numCol = rsmd.getColumnCount ();
    int rowCount = 0;

    //iterates through the result set and output them to standard out.
    boolean outputHeader = true;
    while (rs.next()){
      if(outputHeader){
        for(int i = 1; i <= numCol; i++){
          System.out.print(rsmd.getColumnName(i) + "\t");
          }
          System.out.println();
          outputHeader = false;
      }
      for (int i=1; i<=numCol; ++i)
        System.out.print (rs.getString (i) + "\t");
      System.out.println ();
      ++rowCount;
    }//end while
    stmt.close ();
    return rowCount;
  }

  /**
   * Method to execute an input query SQL instruction (i.e. SELECT).  This
   * method issues the query to the DBMS and returns the results as
   * a list of records. Each record in turn is a list of attribute values
   *
   * @param query the input query string
   * @return the query result as a list of records
   * @throws java.sql.SQLException when failed to execute the query
   */
  public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
    //creates a statement object
    Statement stmt = this._connection.createStatement ();

    //issues the query instruction
    ResultSet rs = stmt.executeQuery (query);

    /*
     * obtains the metadata object for the returned result set.  The metadata
     * contains row and column info.
    */
    ResultSetMetaData rsmd = rs.getMetaData ();
    int numCol = rsmd.getColumnCount ();
    int rowCount = 0;

    //iterates through the result set and saves the data returned by the query.
    boolean outputHeader = false;
    List<List<String>> result  = new ArrayList<List<String>>();
    while (rs.next()){
      List<String> record = new ArrayList<String>();
      for (int i=1; i<=numCol; ++i)
        record.add(rs.getString (i));
      result.add(record);
    }//end while
    stmt.close ();
    return result;
  }//end executeQueryAndReturnResult

  /**
   * Method to execute an input query SQL instruction (i.e. SELECT).  This
   * method issues the query to the DBMS and returns the number of results
   *
   * @param query the input query string
   * @return the number of rows returned
   * @throws java.sql.SQLException when failed to execute the query
   */
  public int executeQuery (String query) throws SQLException {
    //creates a statement object
    Statement stmt = this._connection.createStatement ();

    //issues the query instruction
    ResultSet rs = stmt.executeQuery (query);

    int rowCount = 0;

    //iterates through the result set and count nuber of results.
    if(rs.next()){
      rowCount++;
    }//end while
    stmt.close ();
    return rowCount;
  }

  /**
   * Method to fetch the last value from sequence. This
   * method issues the query to the DBMS and returns the current
   * value of sequence used for autogenerated keys
   *
   * @param sequence name of the DB sequence
   * @return current value of a sequence
   * @throws java.sql.SQLException when failed to execute the query
   */

  public int getCurrSeqVal(String sequence) throws SQLException {
    Statement stmt = this._connection.createStatement ();

    ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
    if (rs.next()) return rs.getInt(1);
    return -1;
  }

  /**
   * Method to fetch the next value from sequence. This
   * method issues the query to the DBMS and returns the incremented
   * value of sequence used for autogenerated keys
   *
   * @param sequence name of the DB sequence
   * @return next value of a sequence
   * @throws java.sql.SQLException when failed to execute the query
   */

  public int getNextSeqVal(String sequence) throws SQLException {
    Statement stmt = this._connection.createStatement ();

    ResultSet rs = stmt.executeQuery (String.format("Select nextval('%s')", sequence));
    if (rs.next()) return rs.getInt(1);
    return -1;
  }

  /**
   * Method to close the physical connection if it is open.
   */
  public void cleanup(){
    try{
      if (this._connection != null){
        this._connection.close ();
      }//end if
    }catch (SQLException e){
           // ignored.
    }//end try
  }//end cleanup

  /**
   * The main execution method
   *
   * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
   */
  public static void main (String[] args) {
    if (args.length != 3) {
      System.err.println (
        "Usage: " + "java [-classpath <classpath>] " + Ticketmaster.class.getName () +
                " <dbname> <port> <user>");
      return;
    }//end if

    Ticketmaster esql = null;

    try{
      System.out.println("(1)");

      try {
        Class.forName("org.postgresql.Driver");
      }catch(Exception e){

        System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
        e.printStackTrace();
        return;
      }

      System.out.println("(2)");
      String dbname = args[0];
      String dbport = args[1];
      String user = args[2];

      esql = new Ticketmaster (dbname, dbport, user, "");

      esql.executeUpdate("DROP SEQUENCE IF EXISTS bid_sequence;");
      esql.executeUpdate("DROP SEQUENCE IF EXISTS sid_sequence;");
      esql.executeUpdate("DROP SEQUENCE IF EXISTS pid_sequence;");
      esql.executeUpdate("DROP SEQUENCE IF EXISTS ssid_sequence;");
      esql.executeUpdate("DROP SEQUENCE IF EXISTS mvid_sequence;");
      esql.executeUpdate("CREATE SEQUENCE bid_sequence;");
      esql.executeUpdate("CREATE SEQUENCE sid_sequence;");
      esql.executeUpdate("CREATE SEQUENCE pid_sequence;");
      esql.executeUpdate("CREATE SEQUENCE ssid_sequence;");
      esql.executeUpdate("CREATE SEQUENCE mvid_sequence;");
      esql.executeQuery("SELECT setval('bid_sequence', (SELECT MAX(bid) FROM Bookings));");
      esql.executeQuery("SELECT setval('sid_sequence', (SELECT MAX(sid) FROM Shows));");
      esql.executeQuery("SELECT setval('pid_sequence', (SELECT MAX(pid) FROM Payments));");
      esql.executeQuery("SELECT setval('ssid_sequence', (SELECT MAX(ssid) FROM ShowSeats));");
      esql.executeQuery("SELECT setval('mvid_sequence', (SELECT MAX(mvid) FROM Movies));");

      boolean keepon = true;
      while(keepon){
        System.out.println("MAIN MENU");
        System.out.println("---------");
        System.out.println("1. Add User");
        System.out.println("2. Add Booking");
        System.out.println("3. Add Movie Showing for an Existing Theater");
        System.out.println("4. Cancel Pending Bookings");
        System.out.println("5. Change Seats Reserved for a Booking");
        System.out.println("6. Remove a Payment");
        System.out.println("7. Clear Cancelled Bookings");
        System.out.println("8. Remove Shows on a Given Date");
        System.out.println("9. List all Theaters in a Cinema Playing a Given Show");
        System.out.println("10. List all Shows that Start at a Given Time and Date");
        System.out.println("11. List Movie Titles Containing \"love\" Released After 2010");
        System.out.println("12. List the First Name, Last Name, and Email of Users with a Pending Booking");
        System.out.println("13. List the Title, Duration, Date, and Time of Shows Playing a Given Movie at a Given Cinema During a Date Range");
        System.out.println("14. List the Movie Title, Show Date & Start Time, Theater Name, and Cinema Seat Number for all Bookings of a Given User");
        System.out.println("15. EXIT");

        /*
         * FOLLOW THE SPECIFICATION IN THE PROJECT DESCRIPTION
         */
        switch (readChoice()){
          case 1: AddUser(esql); break;
          case 2: AddBooking(esql); break;
          case 3: AddMovieShowingToTheater(esql); break;
          case 4: CancelPendingBookings(esql); break;
          case 5: ChangeSeatsForBooking(esql); break;
          case 6: RemovePayment(esql); break;
          case 7: ClearCancelledBookings(esql); break;
          case 8: RemoveShowsOnDate(esql); break;
          case 9: ListTheatersPlayingShow(esql); break;
          case 10: ListShowsStartingOnTimeAndDate(esql); break;
          case 11: ListMovieTitlesContainingLoveReleasedAfter2010(esql); break;
          case 12: ListUsersWithPendingBooking(esql); break;
          case 13: ListMovieAndShowInfoAtCinemaInDateRange(esql); break;
          case 14: ListBookingInfoForUser(esql); break;
          case 15: keepon = false; break;
        }
      }
    }catch(Exception e){
      System.err.println (e.getMessage ());
    }finally{
      try{
        if(esql != null) {
          System.out.print("Disconnecting from database...");
          esql.cleanup ();
          System.out.println("Done\n\nBye !");
        }//end if
      }catch(Exception e){
        // ignored.
      }
    }
  }

  public static int readChoice() {
    int input;
    // returns only if a correct value is given.
    do {
      System.out.print("Please make your choice: ");
      try { // read the integer, parse it and break.
        input = Integer.parseInt(in.readLine());
        break;
      }catch (Exception e) {
        System.out.println("Your input is invalid!");
        continue;
      }//end try
    }while (true);
    return input;
  }//end readChoice

  public static void AddUser(Ticketmaster esql){//1
    String email;
    String lname;
    String fname;
    String phone;
    String pwd = "ef72bf374c890b969714a7e881ca3cf5d1dff44a238cce65286b039345ef1303";

    System.out.println();

    // Capture email
    while (true) {
      System.out.print("Please enter the user email: ");
      try { // read the email, parse it and break if valid.
        email = in.readLine();
        // primary key, need to see if it already exists in DB
        if (esql.executeQuery("SELECT * FROM Users WHERE email = '" + email + "';") != 0) {
          System.out.println("This user is already in the system.");
          continue;
        }
        // need to make sure length is correct
        if (email.length() > 64) {
          System.out.println("Email address is too long.");
          continue;
        }

        // check if valid email address
        // Regex email validation taken from
        // https://howtodoinjava.com/regex/java-regex-validate-email-address/
        Matcher matcher = pattern.matcher(email);

        if(matcher.matches() == false) {
          System.out.println("Invalid email address.");
          continue;
        }

        break; //break if made it through validation checks
      }catch (Exception e) {
        System.out.println("Your input is invalid!");
        continue;
      }//end try
    }  //end of email input

    // Capture names
    while (true) {
      System.out.print("Please enter user's first name: ");
      try {
        fname = in.readLine();
        if (fname.length() > 32 || fname.length() == 0) {
          System.out.println("User's first name is too long or too short.");
          continue;
        }

        System.out.print("Please enter user's last name: ");
        lname = in.readLine();
        if (lname.length() > 32 || lname.length() == 0) {
          System.out.println("User's last name is too long or too short.");
          continue;
        }

        break;
      } catch (Exception e) {
        System.out.println("Your input is invalid!");
        e.printStackTrace();
        continue;
      }//end try
    } //end of capture names

    // Capture phone Number
    while (true) {
      System.out.print("Please enter user's phone number: ");
      try { // read the integer, parse it and break.
        phone = in.readLine();
        if (phone.length() != 10 || Long.parseLong(phone) < 0) {
          System.out.println("Your input is invalid!");
          continue;
        }
        break;
      }catch (Exception e) {
        System.out.println("Your input is invalid! Enter a 10 digit number with no characters.");
        e.printStackTrace();
        continue;
      }//end try
    }  // End of capture phone Number

    // Capture user password
    while (true) {
      System.out.print("Please enter user's password: ");
      try { // read the integer, parse it and break.
        in.readLine();
        break;
      }catch (Exception e) {
        System.out.println("Your input is invalid!");
        e.printStackTrace();
        continue;
      }//end try
    }  // End of capture user password

    // Build insert user query
    String query = ("INSERT INTO Users(email, lname, fname, phone, pwd)\n" +
                     "VALUES ('"+ email + "', '"+ lname + "', '"+ fname +"', '"+ phone +"', '"+ pwd + "');");

    try {
     esql.executeUpdate(query);
    } catch (SQLException e){
     e.printStackTrace();
    }
    System.out.println();
    return;

  }  //AddUser()

  public static void AddBooking(Ticketmaster esql){//2
    String email;
    int sid;
    List<List<String>> availableSeats = null;
    ArrayList<Integer> seats = new ArrayList<Integer>();
    ArrayList<Integer> seatsToAdd = new ArrayList<Integer>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm").withResolverStyle(ResolverStyle.STRICT);
    LocalDateTime dateTime = null;
    String status = null;
    int amount = 0;
    System.out.println();

    // Capture email
    while (true) {
      System.out.print("Please enter the user email: ");
      try { // read the email, parse it and break if valid.
        email = in.readLine();
        // primary key, need to see if it already exists in DB
        if (esql.executeQuery("SELECT * FROM Users WHERE email = '" + email + "';") != 1) {
          System.out.println("This user is not in our system.");
          continue;
        }
        break;
      } catch (Exception e) {
        System.out.println("Your input is invalid!");
        continue;
      } //end try
    }// end of capture email while

    //Capture show ID
    while (true) {
      System.out.print("Please enter show sid: ");
      try { // read the integer, parse it and break.
        sid = Integer.parseInt(in.readLine());
        if (esql.executeQuery("SELECT * FROM Shows WHERE sid = " + sid + ";") != 1) {
          System.out.println("This show id is not valid in our system.");
          continue;
        }

        //Get available seats
        availableSeats = esql.executeQueryAndReturnResult("SELECT csid FROM CinemaSeats WHERE tid = (SELECT tid FROM Plays WHERE sid = " + sid + ")\n" +
                                                     "EXCEPT\n" +
                                                     "SELECT csid FROM ShowSeats WHERE sid = " + sid + ";");
        break;
      }catch (Exception e) {
        System.out.println("Your input is invalid!");
        e.printStackTrace();
        continue;
      }//end try
    }// end of capture sid

   try {
    esql.executeQueryAndPrintResult("SELECT m.title, s.sdate, s.sttime, t.tname, t.tid\n" +
                                    "FROM  Shows s, Movies M, Theaters t, Plays p\n" +
                                    "WHERE s.sid = " + sid + "\n" +
                                    "AND s.mvid = m.mvid\n" +
                                    "AND s.sid = p.sid\n" +
                                    "AND p.tid = t.tid;");

    esql.executeQueryAndPrintResult("SELECT cs.sno, cs.csid\n" +
                                    "FROM  Shows s, Movies M, Theaters t, Plays p, CinemaSeats cs\n" +
                                    "WHERE s.sid = " + sid + "\n" +
                                    "AND s.mvid = m.mvid\n" +
                                    "AND s.sid = p.sid\n" +
                                    "AND p.tid = t.tid\n" +
                                    "AND p.tid = cs.tid;");
    }catch (Exception e) {
      e.printStackTrace();
    }//end try

    // Extract available seats
    for(List<String> seatList : availableSeats) {
      for(String seat : seatList) {
        try{
          seats.add(Integer.parseInt(seat));
        } catch (Exception e) {
          e.printStackTrace();
        }//end try
      }
    }

    System.out.println("Which cinema seats for the booking. Here is what is available: " + seats);

    //Get seats from User
    while (true) {
      System.out.println("Enter -1 when done.");
      System.out.print("Enter cinema seats csid: ");
      try { // read the integer, parse it and break.
        int tempCSID = Integer.parseInt(in.readLine());
        if (tempCSID == -1)
          break;
        if (!seats.contains(tempCSID)) {
          System.out.println("That is not an available seat.");
          continue;
        }
        if (seatsToAdd.contains(tempCSID)) {
          System.out.println("That seat is already added.");
          continue;
        }
        seatsToAdd.add(tempCSID);
        System.out.println("Seat " + tempCSID + " added.");
      }catch (Exception e) {
        System.out.println("Your input is invalid!");
        e.printStackTrace();
        continue;
      }//end try
    } // end of while getting seats from user

    if (seatsToAdd.size() == 0) {
      System.out.println("No seats added.");
      return;
    }

    // Get date
    while (true) {
      System.out.print("Please enter booking date and time in 'yyyy-MM-dd HH:mm' format: ");
      try { // read the integer, parse it and break.
        dateTime = LocalDateTime.parse(in.readLine(), formatter);
        break;
      } catch (Exception e) {
        System.out.println("Your input is invalid!");
        System.out.println("Make sure the format is correct and the date and time actually exist.");
        e.printStackTrace();
        continue;
      }//end try
    } //end while

    // get last information
    while (true) {
      System.out.print("Please enter 1 for Paid booking or 2 for Pending: ");
      try { // read the integer, parse it and break.
        int statusNum = Integer.parseInt(in.readLine());
        if (statusNum == 1) {
          status = "Paid";
          System.out.print("Please amount per seat: ");
          amount = Integer.parseInt(in.readLine());
          if (amount < 1 || amount > 20000) {
            System.out.println("Your input is invalid!");
            continue;
          }
          break;
        } else if (statusNum == 2) {
          status = "Pending";
          break;
        } else {
          System.out.println("Your input is invalid!");
          continue;
        }
      }catch (Exception e) {
        System.out.println("Your input is invalid!");
        e.printStackTrace();
        continue;
      }//end try
    }

    if (amount < 1)
      amount = 0;

    String newBookingQuery = ("INSERT INTO Bookings(bid, status, bdatetime, seats, sid, email)\n" +
                              "VALUES ((SELECT nextval('bid_sequence')), '" + status + "', '" + dateTime.getYear() + "-" + String.format("%02d", dateTime.getMonthValue()) + "-" + String.format("%02d", dateTime.getDayOfMonth()) + "', " + seatsToAdd.size() + ", " + sid + ", '" + email + "');");

    try {
      esql.executeUpdate(newBookingQuery);
      for (int seatToAdd : seatsToAdd) {
       esql.executeUpdate("INSERT INTO ShowSeats(ssid, sid, csid, bid, price)\n" +
                          "VALUES ((SELECT nextval('ssid_sequence'))," + sid + ", " + seatToAdd + ",(SELECT currval('bid_sequence')), "+ amount +");");
      }
    } catch (SQLException e){
     e.printStackTrace();
    }

    System.out.println("Bookings successfully added.");

    System.out.println();
    return;
  }  //AddBooking()

  public static void AddMovieShowingToTheater(Ticketmaster esql){//3

    System.out.println();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);
    LocalDate releaseDate = null;
    LocalDate startDate = null;
    LocalTime startTime = null;
    LocalTime endTime = null;
    String title;
    String country;
    String description;
    int duration;
    String lang;
    String genre;
    int tid;

    // get dates
    while (true) {
      System.out.print("Please enter movie release date in 'yyyy-MM-dd' format: ");
      try { // read the integer, parse it and break.
        releaseDate = LocalDate.parse(in.readLine(), formatter);
        System.out.print("Please enter show start date in 'yyyy-MM-dd' format: ");
        startDate = LocalDate.parse(in.readLine(), formatter);

        if (startDate.isBefore(releaseDate)) {
          System.out.println("The start date cannot come before the release date.");
          continue;
        }
        System.out.print("Please enter show start time in 'HH:mm' format: ");
        startTime = LocalTime.parse(in.readLine(), timeFormatter);
        System.out.print("Please enter show end time in 'HH:mm' format: ");
        endTime = LocalTime.parse(in.readLine(), timeFormatter);

        if (endTime.isBefore(startTime)) {
          System.out.println("The start time cannot come before the end time.");
          continue;
        }

        duration = (int) startTime.until(endTime, ChronoUnit.SECONDS);
        System.out.println("Duration set at " + duration + ".");

        break;
      } catch (Exception e) {
        System.out.println("Your input is invalid!");
        System.out.println("Make sure the format is correct and the dates actually exist.");
        e.printStackTrace();
        continue;
      }//end try
    } //end while

    //Get movie info
    while (true) {
      System.out.print("Please enter the movie's title: ");
      try {
        title = in.readLine();
        if (title.length() < 1 || title.length() > 128) {
          System.out.println("Invalid length.");
          continue;
        }

        System.out.print("Please enter the movie's country: ");
        country = in.readLine();
        if (country.length() < 1 || country.length() > 64) {
          System.out.println("Invalid length.");
          continue;
        }

        System.out.print("Please enter the movie's description (or press enter for nothing): ");
        description = in.readLine();

        System.out.print("Please enter the movie's lang as two char (or press enter for nothing): ");
        lang = in.readLine();
        if (lang.length() > 2) {
          System.out.println("Invalid length. Time to start all over!");
          continue;
        }

        System.out.print("Please enter the movie's genre no more than 16 char (or press enter for nothing): ");
        genre = in.readLine();
        if (genre.length() > 16) {
          System.out.println("Invalid length. Time to start all over!");
          continue;
        }

        break;
      } catch (Exception e) {
        System.out.println("Your input is invalid!");
        e.printStackTrace();
        continue;
      }//end try
    } //end while

    //get theater id
    while (true) {
      System.out.print("Please enter the show's theater id: ");
      try { // read the integer, parse it and break.
        tid = Integer.parseInt(in.readLine());

        if (esql.executeQuery("SELECT * FROM Theaters WHERE tid = " + tid + ";") == 0) {
          System.out.println("No theater id matches that tid.");
          continue;
        }

        if (esql.executeQuery("SELECT *\n" +
                              "FROM Shows s, Plays p\n" +
                              "WHERE s.sdate = '" + startDate.getYear() + "-" + String.format("%02d", startDate.getMonthValue()) + "-" + String.format("%02d", startDate.getDayOfMonth()) + "'\n" +
                              "AND s.sid = p.sid\n" +
                              "AND p.tid = " + tid + "\n" +
                              "AND ((s.sttime BETWEEN '" + String.format("%02d", startTime.getHour()) + ":" + String.format("%02d", startTime.getMinute()) + ":00' AND '" + String.format("%02d", endTime.getHour()) + ":" + String.format("%02d", endTime.getMinute()) + ":00') OR (s.edtime BETWEEN '" + String.format("%02d", startTime.getHour()) + ":" + String.format("%02d", startTime.getMinute()) + ":00' AND '" + String.format("%02d", endTime.getHour()) + ":" + String.format("%02d", endTime.getMinute()) + ":00'));") != 0) {
          System.out.println("A show is already booked for that theater at that time.");
          System.out.println();
          continue;
        }
        break;
      }catch (Exception e) {
        System.out.println("Your input is invalid!");
        e.printStackTrace();
        continue;
      }//end try
    }

    System.out.println();
    return;
  }

  public static void CancelPendingBookings(Ticketmaster esql){//4
    System.out.println();

    String deleteShowSeatQuery = "DELETE FROM ShowSeats WHERE bid IN (SELECT bid FROM Bookings WHERE status = 'Pending');";
    String updateBookingQuery= "UPDATE Bookings SET status = 'Cancelled' WHERE status='Pending';";
    try {
        esql.executeUpdate(deleteShowSeatQuery);
        esql.executeUpdate(updateBookingQuery);
        System.out.println("Update made.");

    }catch(Exception e){
        System.out.println("No Pending Found");
        e.printStackTrace();
    }
    System.out.println();
    return;
  }

  public static void ChangeSeatsForBooking(Ticketmaster esql) throws Exception{//5

  }

  public static void RemovePayment(Ticketmaster esql){//6
    int pid;
    String query;

    System.out.println();

    // Read in pid
    while (true) {
        System.out.print("Please provide payment id (pid) that was refunded: ");
        try {
            pid = Integer.parseInt(in.readLine());
            if (esql.executeQuery("SELECT * FROM Payments WHERE pid = " + pid + ";") == 0) {
              System.out.println("No payment id matches that pid.");
              continue;
            }
            break;
        } catch (Exception e) {
            System.out.println("Invalid Input: Exception:" + e.getMessage());
            continue;
        }
    }  //end of read in pid while

    // Build queries
    String deleteShowSeatQuery = "DELETE FROM ShowSeats WHERE bid IN (SELECT bid FROM Payments WHERE pid = " + pid + ");";
    String updateBookingQuery= "UPDATE Bookings SET status = 'Cancelled' WHERE bid = (SELECT bid FROM Payments WHERE pid = " + pid + ");";
    String deletePayment = "DELETE FROM Payments WHERE pid = " + pid + ";";

    try {
        esql.executeUpdate(deleteShowSeatQuery);
        esql.executeUpdate(updateBookingQuery);
        esql.executeUpdate(deletePayment);
        System.out.println("Update made.");
    }catch(Exception e){
        e.printStackTrace();
    }

    System.out.println();
    return;
  }

  public static void ClearCancelledBookings(Ticketmaster esql){//7
     System.out.println();
     String query= "DELETE\n" +
                   "FROM Bookings b\n" +
                   "WHERE status = 'Cancelled';";
     try {
         esql.executeUpdate(query);
         System.out.println("Cancelled bookings cleared.");

     }catch(Exception e){
         System.out.println("No Cancelled Found.");
         e.printStackTrace();
     }
     System.out.println();
     return;
     }

  public static void RemoveShowsOnDate(Ticketmaster esql){//8

  }

  public static void ListTheatersPlayingShow(Ticketmaster esql) {//9
    //
    int sid;
    System.out.println();

    do {
      System.out.print("Please enter show sid: ");
      try { // read the integer, parse it and break.
        sid = Integer.parseInt(in.readLine());
        break;
      }catch (Exception e) {
        System.out.println("Your input is invalid!");
        e.printStackTrace();
        continue;
      }//end try
    }while (true);

    String query = ("SELECT c.*, t.*\n" +
                    "FROM plays p, cinemas c, theaters t\n" +
                    "WHERE p.sid = " + sid + "\n"+
                    "AND p.tid = t.tid\n" +
                    "AND t.tid = c.cid;");

    try {
      esql.executeQueryAndPrintResult(query);
    } catch (SQLException e){
      e.printStackTrace();
    }
    System.out.println();
    return;
  }

  public static void ListShowsStartingOnTimeAndDate(Ticketmaster esql){//10
    //
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm").withResolverStyle(ResolverStyle.STRICT);
    LocalDateTime dateTime = null;

    System.out.println();

    while (true) {
      System.out.print("Please enter show date and start time in 'yyyy-MM-dd HH:mm' format: ");
      try { // read the integer, parse it and break.
        dateTime = LocalDateTime.parse(in.readLine(), formatter);
        break;
      } catch (Exception e) {
        System.out.println("Your input is invalid!");
        System.out.println("Make sure the format is correct and the date and time actually exist.");
        e.printStackTrace();
        continue;
      }//end try
    } //end while

    String query = ("SELECT *\n" +
                    "FROM Shows\n" +
                    "WHERE sdate = '" + dateTime.getYear() + "-" + String.format("%02d", dateTime.getMonthValue()) + "-" + String.format("%02d", dateTime.getDayOfMonth()) + "'\n" +
                    "AND sttime = '" + String.format("%02d", dateTime.getHour()) + ":" + String.format("%02d", dateTime.getMinute()) + ":00';");

    try {
      esql.executeQueryAndPrintResult(query);
    } catch (SQLException e){
      e.printStackTrace();
    }
    System.out.println();
    return;

  }

  public static void ListMovieTitlesContainingLoveReleasedAfter2010(Ticketmaster esql){//11
    //
    System.out.println();
    String query = ("SELECT DISTINCT title\n" +
                    "FROM Movies\n" +
                    "WHERE rdate > '2010-12-31'\n" +
                    "AND title ILIKE '%love%';");

    try {
      esql.executeQueryAndPrintResult(query);
    } catch (SQLException e){
      e.printStackTrace();
    }
    System.out.println();
    return;
  }

  public static void ListUsersWithPendingBooking(Ticketmaster esql){//12
    //
    System.out.println();
    String query = ("SELECT DISTINCT u.fname, u.lname, u.email\n" +
                    "FROM Users u, Bookings b\n" +
                    "WHERE b.status = 'Pending'\n" +
                    "AND b.email = u.email;");

    try {
      esql.executeQueryAndPrintResult(query);
    } catch (SQLException e){
      e.printStackTrace();
    }
    System.out.println();
    return;
  }

  public static void ListMovieAndShowInfoAtCinemaInDateRange(Ticketmaster esql){//13
    //
    System.out.println();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
    LocalDate startDate = null;
    LocalDate endDate = null;
    int mid;
    int cid;

    while (true) {
      System.out.print("Please enter starting date range (inclusive) 'yyyy-MM-dd' format: ");
      try { // read the integer, parse it and break.
        startDate = LocalDate.parse(in.readLine(), formatter);
        System.out.print("Please enter ending date range (inclusive) 'yyyy-MM-dd' format: ");
        endDate = LocalDate.parse(in.readLine(), formatter);

        if (endDate.isBefore(startDate)) {
          System.out.println("The ending date cannot come before the starting date.");
          continue;
        }

        break;
      } catch (Exception e) {
        System.out.println("Your input is invalid!");
        System.out.println("Make sure the format is correct and the dates actually exist.");
        e.printStackTrace();
        continue;
      }//end try
    } //end while

    do {
      System.out.print("Please enter movie mid: ");
      try { // read the integer, parse it and break.
        mid = Integer.parseInt(in.readLine());
        System.out.print("Please enter cinema cid: ");
        cid = Integer.parseInt(in.readLine());
        break;
      }catch (Exception e) {
        System.out.println("Your input is invalid!");
        e.printStackTrace();
        continue;
      }//end try
    }while (true);

    String query = ("SELECT m.title, m.duration, s.sdate, s.sttime\n" +
                    "FROM Movies m, Shows s, Theaters t, Plays p\n" +
                    "WHERE s.sdate >= '" + startDate.getYear() + "-" + String.format("%02d", startDate.getMonthValue()) + "-" + String.format("%02d", startDate.getDayOfMonth()) + "'\n" +
                    "AND s.sdate <= '" + endDate.getYear() + "-" + String.format("%02d", endDate.getMonthValue()) + "-" + String.format("%02d", endDate.getDayOfMonth()) + "'\n" +
                    "AND m.mvid = " + mid + "\n" +
                    "AND t.cid = " + cid + "\n" +
                    "AND t.tid = p.tid\n" +
                    "AND p.sid = s.sid\n" +
                    "AND s.mvid = m.mvid;");
    try {
      esql.executeQueryAndPrintResult(query);
    } catch (SQLException e){
      e.printStackTrace();
    }
    System.out.println();
    return;
  }

  public static void ListBookingInfoForUser(Ticketmaster esql){//14
    //
    String email;
    System.out.println();

    while (true) {
      System.out.print("Please enter user's email address: ");
      try {
        email = in.readLine();
        break;
      } catch (Exception e) {
        System.out.println("Your input is invalid!");
        e.printStackTrace();
        continue;
      }//end try
    } //end while

    String query = ("SELECT DISTINCT m.title, s.sdate, s.sttime, t.tname, c.sno\n" +
                    "FROM Users u, Bookings b, Shows s, Movies m, Plays p, Theaters t, CinemaSeats c, ShowSeats ss\n" +
                    "WHERE u.email = '" + email + "'\n" +
                    "AND u.email = b.email\n" +
                    "AND b.sid = s.sid\n" +
                    "AND s.mvid = m.mvid\n" +
                    "AND b.bid = ss.bid\n" +
                    "AND ss.csid = c.csid\n" +
                    "AND c.tid = t.tid;");

    try {
      esql.executeQueryAndPrintResult(query);
    } catch (SQLException e){
      e.printStackTrace();
    }
    System.out.println();
    return;
  }

}
