//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.sql.*;
import java.util.Date;
import java.util.Objects;
import java.util.*;
import java.time.LocalDate;

public class Main {
    private static String userType;
    private static Connection conn;
    private static PreparedStatement ps;
    private static Scanner scanner;
    private static String HOST_NAME = "";
    private static final String PORT = "";               // mysql use this port(default)
    private static final String USER_NAME = "";          // admin name: root
    private static final String PASSWORD = "";     // admin pwd
    private static final String DB_NAME = "";           // DB name (model in mysql workbench)

    // 'Pizza' = not sensitive
    // Pizza = sensitive

    public static Connection getConnection() {

        try{
            String url = "jdbc:mysql://" + HOST_NAME + ":" + PORT + "/" + DB_NAME; // need to specify DB_NAME
            Connection connection = DriverManager.getConnection(url, USER_NAME, PASSWORD);

            return connection;

        } catch(SQLException e){
            e.printStackTrace();

            return null;

        }

    }

    public static void main(String[] args) {
        conn = getConnection();
        mainMenu();


        /*try {

            conn = getConnection(); // main fonk dışında kullanmalısın
            int CID = 1;
            String Cname = "Arda";
            String CCity = "Istanbul";
            String CStreet = "Bostancı";
            int Ctel = 2434;
            String ApartmanName = "Taşkın";
            int ApartmanNo = 31;
            String CSurname = "Güler";
            int AccountNo = 0;
            String sql = "INSERT INTO Customer (CID, Cname, CCity, CStreet, CTel, ApartmanName, ApartmanNo, CSurname, AccountNo) VALUES(?,?,?,?,?,?,?,?,?)";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, CID);
            preparedStatement.setString(2, Cname);
            preparedStatement.setString(3, CCity);
            preparedStatement.setString(4, CStreet);
            preparedStatement.setInt(5, Ctel);
            preparedStatement.setString(6, ApartmanName);
            preparedStatement.setInt(7, ApartmanNo);
            preparedStatement.setString(8, CSurname);
            preparedStatement.setInt(9, AccountNo);

            // if u wnat insert use the way in the photo, it is secured to SQL injections

            //stmt = conn.createStatement();
            boolean result = preparedStatement.execute(sql);  // u can check the stmt is executed or not

            ResultSet rs = preparedStatement.getResultSet(); // iterate over all rows

            while(rs.next()){       // iterate to rows, ath the end it retuns false = exit loop

                System.out.println("My name is " + Cname + " and my account id " + CID + " and my city is " + CCity);

            }
            !!! DONT FORGET !!! ==> preparedStatement.close();

            conn.close();
        } catch(SQLException e){
            e.printStackTrace();
        }

             */
    }

    static void mainMenu(){  // first panel
        try {
            System.out.println("---------- Welcome to the bank ----------\n");
            System.out.println("Select user type:");
            System.out.println("1.Customer\n" + "2.Administrator\n" + "3.Loan Officer\n" + "4.Investment Advisor\n"
             + "5.Exit");

            scanner = new Scanner(System.in);
            System.out.print("Enter your choice : ");
            userType = scanner.nextLine();

            if (Objects.equals(userType, "1") || Objects.equals(userType, "Customer"))
                customerView();
            else if (Objects.equals(userType, "2") || Objects.equals(userType, "Administrator"))
                administratorView();
            else if (Objects.equals(userType, "3") || Objects.equals(userType, "Loan Officer"))
                loanOfficerView();
            else if (Objects.equals(userType, "4") || Objects.equals(userType, "Investment Advisor"))
                investmentAdvisorView();
            else if (Objects.equals(userType, "5") || Objects.equals(userType, "Exit")){
                conn.close();
                System.exit(0);
            }

        } catch (Exception e){
            System.out.println("Error occured! \n" + " return main menu!");
            mainMenu();
        }
    }

    private static void investmentAdvisorView() {
        try {
            conn = getConnection();
            scanner = new Scanner(System.in);
            String sql;
            System.out.print("\n\nTo track investment transactions (buying/selling assets), select AccountNo (u can type 'Exit'" +
                    " to exit): ");
            String AccountNo = scanner.nextLine();  // AccountNo is here

            if(Objects.equals(AccountNo, "Exit"))
                mainMenu();

            sql = "SELECT * FROM transaction WHERE (Ttype LIKE 'asset%'" + " OR Ttype LIKE 'bond%')" + "AND AccountNo = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, AccountNo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int id = rs.getInt("id");
                String date = rs.getString("Tdate");
                double amount = rs.getDouble("Tamount");
                String type = rs.getString("Ttype");

                System.out.println("id = " + id + ", date = " + date + ", amount = " + amount + ", type = "
                        + type + ", AccountNo = " + AccountNo);
            }
            investmentAdvisorView();
            conn.close();

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error occured! \n" + " return main menu!");
            mainMenu();
        }


    }

    private static void loanOfficerView() {
        try {
            conn = getConnection();
            System.out.print("\n\nChoose an option: ");
            scanner = new Scanner(System.in);
            System.out.print("type'1' or '2' \n1. Retrieve account loan history.\n" + "2. Process Loans and see them\n"
                    + "3. See payments\n" + "4. See overdue loan(s)"+ "\nEnter your choice : ");
            String option = scanner.nextLine();


            if(Objects.equals(option, "Exit"))
                mainMenu();

            if (Objects.equals(option, "1") || Objects.equals(option, "Retrieve")) {
                System.out.print("Enter AccountNo: ");
                String AccountNo = scanner.nextLine();  // AccountNo is here

                String sql = "SELECT * FROM transaction WHERE Ttype LIKE 'l%' AND AccountNo = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, AccountNo);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {                    // iterate to rows, ath the end it retuns false = exit loop
                    int id = rs.getInt("id");
                    String date = rs.getString("Tdate");
                    double amount = rs.getDouble("Tamount");
                    String type = rs.getString("Ttype");

                    System.out.println("\nTID = " + id + ", date = " + date + ", amount = "
                            + amount + ", type = " + type + ", AccountNo = " + AccountNo);
                }
            }

            if (Objects.equals(option, "2") || Objects.equals(option, "Process")) {
                System.out.print("Enter AccountNo: ");
                String AccountNo = scanner.nextLine();  // AccountNo is here

                String sql = "SELECT" +
                        " E.random_employee_id," +
                        " L.loan_id," +
                        " T.transaction_id"+
                " FROM "+
                        "(SELECT EID AS random_employee_id FROM Employee WHERE Etype LIKE 'l%' ORDER BY RAND() LIMIT 1) E ,"+
                        "(SELECT LID AS loan_id FROM Loan) L,"+
                        "(SELECT id AS transaction_id FROM transaction WHERE ttype LIKE 'l%') T";
                ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                String lid  = "";
                String eid = "";
                int tid = 0;
                while (rs.next()) {
                    eid = rs.getString("random_employee_id");
                    lid = rs.getString("loan_id");
                    tid = rs.getInt("transaction_id");
                }

                sql = "INSERT INTO processes(EID, LID, id) VALUES (?, ?, ?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, eid);
                ps.setString(2, lid);
                ps.setInt(3, tid);
                ps.executeUpdate();

            }

            if (Objects.equals(option, "3") || Objects.equals(option, "Payments and Loans")) {
                try {
                    System.out.print("Enter AccountNo: ");
                    String AccountNo = scanner.nextLine();  // AccountNo is here

                    String sql = "SELECT * FROM has INNER JOIN payment ON has.LID = payment.LID;";
                    ps = conn.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    String lid  = "";
                    String cardNo = "";
                    String pNo = "";
                    String date = "";
                    double amount = 0;

                    while (rs.next()) {         // so payment done
                        AccountNo = rs.getString(1);
                        lid = rs.getString(2);
                        cardNo = rs.getString(3);
                        pNo = rs.getString(4);
                        date = rs.getString(6);
                        amount = rs.getDouble(5);
                    }
                } catch (SQLException e){
                    e.printStackTrace();
                    System.out.println("Error occured! \n" + " return main menu!");
                    mainMenu();
                }


            }

            if (Objects.equals(option, "4") || Objects.equals(option, "overdue")) {
                try {
                    String sql = "SELECT LID FROM loan WHERE Ldate < ?";
                    LocalDate currentDate = LocalDate.now();
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, currentDate.toString()); // test ekmek lazım
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        String lid = rs.getString("LID");
                        System.out.println("LID = " + lid);
                    }
                } catch (SQLException e){
                    e.printStackTrace();
                }

            }

            conn.close();

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error occured! \n" + " return main menu!");
            mainMenu();
        }
    }

    private static void administratorView() {
        try {
            conn = getConnection();
            System.out.print("\n\n" + "1. Add Customer     2. Add Account      3.Add Employee\n"
                    + "4. Remove Customer     5. Remove Account      6.Remove Employee\n "
                    + "7. List employee(s)     8. Retrieve All transactions "+ "Enter an option: ");
            scanner = new Scanner(System.in);
            String option = scanner.nextLine();
            if(Objects.equals(option, "Exit"))
                mainMenu();

            if(Objects.equals(option, "1") || Objects.equals(option, "Add Customer")){
                System.out.print("Enter CustomerNo you want to Add: ");
                String cid = scanner.nextLine();

                System.out.print("\nEnter city: ");
                String city = scanner.nextLine();

                System.out.print("\nEnter district: ");
                String district = scanner.nextLine();

                System.out.print("\nEnter Apartman Name: ");
                String apartmanName = scanner.nextLine();

                System.out.print("\nEnter ApartmanNo: ");
                String apartmanNo = scanner.nextLine();

                System.out.print("\nEnter telephone number (with 11 chars, without starting 0): ");
                String tel = scanner.nextLine();

                System.out.print("\nEnter customer name: ");
                String cName = scanner.nextLine();

                System.out.print("\nEnter customer surname: ");
                String cSurname = scanner.nextLine();

                String sql = "INSERT INTO customer(CID, Ccity, Cdistrict, ApartmanName, ApartmanNO, Ctel, Cname, Csurname) "
                        + "values (?,?,?,?,?,?,?,?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, cid);
                ps.setString(2, city);
                ps.setString(3, district);
                ps.setString(4, apartmanName);
                ps.setString(5,apartmanNo);
                ps.setString(6,tel);
                ps.setString(7,cName);
                ps.setString(8,cSurname);
                ResultSet rs = ps.executeQuery();

                System.out.println("New customer is added with customer NO: " + cid);

            }

            if(Objects.equals(option, "2") || Objects.equals(option, "Add Account")){
                System.out.print("Enter AccountNo you want to Add: ");
                String ano = scanner.nextLine();

                System.out.print("\nEnter Balance: ");
                double balance = scanner.nextDouble();

                System.out.print("\nEnter email adress: ");
                String aemail = scanner.nextLine();

                System.out.print("\nEnter currency: ");
                String currency = scanner.nextLine();

                System.out.print("\nEnter Bname: ");
                String bname = scanner.nextLine();

                System.out.print("\nEnter Account Type (Savings or Checkings): ");
                String atype = scanner.nextLine();

                String sql = "INSERT INTO account(AccountNo, Balance, Aemail, Currency, Bname, AccountType) " +
                        "values (?,?,?,?,?,?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, ano);
                ps.setDouble(2, balance);
                ps.setString(3,aemail);
                ps.setString(4,currency);
                ps.setString(5,bname);
                ps.setString(6,atype);
                ResultSet rs = ps.executeQuery();

                System.out.println("New Account is added with AccountNo: " + ano);

            }

            if(Objects.equals(option, "3") || Objects.equals(option, "Add Employee")){
                System.out.print("Enter Employee ID you want to Add: ");
                String eid = scanner.nextLine();

                System.out.print("\nEnter name: ");
                String ename = scanner.nextLine();

                System.out.print("\nEnter surnname: ");
                String eSurName = scanner.nextLine();

                System.out.print("\nEnter salary: ");
                double salary = scanner.nextDouble();

                System.out.print("\nEnter city: ");
                String city = scanner.nextLine();

                System.out.print("\nEnter district: ");
                String district = scanner.nextLine();


                String sql = "INSERT INTO employee(EID, Ename, Esurname, Esalary, Ecity, Edistrict) " +
                        "values (?,?,?,?,?,?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, eid);
                ps.setString(2, ename);
                ps.setString(3, eSurName);
                ps.setDouble(4, salary);
                ps.setString(5, city);
                ps.setString(6, district);
                ResultSet rs = ps.executeQuery();

                System.out.println("New employee is added with EID:  " + eid);

            }

            if(Objects.equals(option, "4") || Objects.equals(option, "Remove Customer")){
                System.out.print("Enter CustomerID yoou want to remove: ");
                String cid = scanner.nextLine();

                String sql = "DELETE FROM customer WHERE CID = ?;";
                ps = conn.prepareStatement(sql);
                ps.setString(1, cid);
                ps.executeUpdate();

                System.out.println("Customer (" + cid + ") is removed");

            }

            if(Objects.equals(option, "5") || Objects.equals(option, "Remove Account")){
                System.out.print("Enter AccountNo yoou want to remove: ");
                String ano = scanner.nextLine();

                String sql = "DELETE FROM account WHERE AccountNo = ?;";
                ps = conn.prepareStatement(sql);
                ps.setString(1, ano);
                ps.executeUpdate();

                System.out.println("Account (" + ano + ") is removed");

            }


            if(Objects.equals(option, "6") || Objects.equals(option, "Remove Employee")){
                System.out.print("Enter EID yoou want to remove: ");
                String eid = scanner.nextLine();

                String sql = "DELETE FROM employee WHERE EID = ?;";
                ps = conn.prepareStatement(sql);
                ps.setString(1, eid);
                ps.executeUpdate();

                System.out.println("Employee (" + eid + ") is removed");

            }


            if(Objects.equals(option, "7") || Objects.equals(option, "List")){
                System.out.print("Enter Bname (ex. Kozyatağı Şubesi): ");
                String Bname = scanner.nextLine();

                String sql = "SELECT works.EID, employee.Ename, employee.Esurname" +
                        " FROM works JOIN employee ON works.EID = employee.EID " +
                        "WHERE Bname = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, Bname);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String eid = rs.getString("EID");
                    String ename = rs.getString("Ename");
                    String esurname = rs.getString("Esurname");

                    System.out.println("\nEID = " + eid + ", Ename = " + ename + ", Esurname = " + esurname);
                }

            }

            if (Objects.equals(option, "8") || Objects.equals(option, "Retrieve")) {
                System.out.print("Enter AccountNo: ");
                String AccountNo = scanner.nextLine();  // AccountNo is here

                String sql = "SELECT * FROM transaction WHERE AccountNo = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, AccountNo);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {                    // iterate to rows, ath the end it retuns false = exit loop
                    int id = rs.getInt("id");
                    String date = rs.getString("Tdate");
                    double amount = rs.getDouble("Tamount");
                    String type = rs.getString("Ttype");

                    System.out.println("\nTID = " + id + ", date = " + date + ", amount = "
                            + amount + ", type = " + type + ", AccountNo = " + AccountNo);
                }
            }

            // !!! DONT FORGET !!! ==> preparedStatement.close();

            conn.close();
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println("\nError occured returning main menu:");
        }

    }

    private static void customerView() {
        try{
            conn = getConnection();
            System.out.print("\n\nEnter your AccountNo or u can exit by typing Exit: ");
            scanner = new Scanner(System.in);
            String AccountNo = scanner.nextLine();  // AccountNo is here
            if(Objects.equals(AccountNo, "Exit"))
                mainMenu();

            String sql;

            sql = "SELECT AccountType FROM Account WHERE AccountNo = ?";
            String AccountType = " ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, AccountNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                AccountType = rs.getString("AccountType");


            if(AccountType.equals("Savings")){
                System.out.println("\n1.Buy  " + "2.Sell  " + "3.Exit  ");

                sql = "SELECT Balance FROM Account WHERE AccountNo = ?";
                double Balance = 0;
                ps = conn.prepareStatement(sql);
                ps.setString(1, AccountNo);
                rs = ps.executeQuery();

                if (rs.next()) {
                    Balance = rs.getDouble("Balance");
                    System.out.println("Current Savings Balance: " + Balance);
                }



                sql = "SELECT Currency FROM account WHERE AccountNo = ?";
                String currency = "";
                ps = conn.prepareStatement(sql);
                ps.setString(1, AccountNo);
                rs = ps.executeQuery();

                if (rs.next())
                    currency = rs.getString("Currency");

                System.out.print("Enter ur choice: ");
                userType = scanner.nextLine();
                if (Objects.equals(userType, "1") || Objects.equals(userType, "Buy")){
                    System.out.println("\n\nWhich type of investment you buy (type Asset or Bond): \n  " + "1.Asset  " + "2.Bond  ");
                    String typeOfInvestment = scanner.nextLine();

                    if (Objects.equals(userType, "Asset")){
                        System.out.println("Enter the amount: ");
                        double amount = scanner.nextDouble();

                        sql = "SELECT PID FROM hold WHERE AccountNo = ?";
                        String pid = "";
                        ps = conn.prepareStatement(sql);
                        ps.setString(1, AccountNo);
                        rs = ps.executeQuery();

                        if (rs.next())
                            pid = rs.getString("PID");

                        sql = "UPDATE investmentportfolio SET Value = Value + ? WHERE PID = ?";
                        /* hep bole yap sql injectiona karsı (secured) */
                        ps = conn.prepareStatement(sql);
                        ps.setDouble(1, amount);
                        ps.setString(2, pid);
                        ps.executeUpdate();

                        sql = "UPDATE account SET Balance = Balance - ? WHERE AccountNo = ?";

                        /* hep bole yap sql injectiona karsı (secured) */
                        ps = conn.prepareStatement(sql);
                        ps.setDouble(1, amount);
                        ps.setString(2, AccountNo);
                        ps.executeUpdate();

                        sql = "INSERT INTO transaction (Tdate, Tamount, Ttype, AccountNo,targetNo) VALUES (?, ?, ?, ?, ?)";
                        ps = conn.prepareStatement(sql);
                        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        LocalDate currentDate = LocalDate.now();

                        ps.setDate(1, java.sql.Date.valueOf(currentDate));// (using today's date for example)
                        ps.setDouble(2, amount); // DOUBLE YAPMAYI UNUTMA FLOAT YAPMISSIN
                        ps.setString(3, "asset bought");
                        ps.setString(4, AccountNo);
                        ps.setString(5, null);

                        int affectedRows = ps.executeUpdate();
                        if (affectedRows > 0) {
                            rs = ps.getGeneratedKeys();
                            if (rs.next()) {
                                int generatedId = rs.getInt(1);
                                String TID = "T" + generatedId;  // unique transaction id, auto-incremented in mysql
                                System.out.println("Transaction recorded with TID: " + TID);
                            }
                        }

                        sql = "INSERT INTO investment (IAmount, IType, ICurrency, AccountNo) "
                                + "VALUES (?, ?, ?, ?)";
                        ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        ps.setDouble(1, amount);
                        ps.setString(2, typeOfInvestment);
                        ps.setString(3, currency);
                        ps.setString(4, AccountNo);

                        ps.executeUpdate();

                            // If you want the auto-generated 'id':
                        int generatedId = 0;
                        rs = ps.getGeneratedKeys();
                        if (rs.next()) {
                            generatedId = rs.getInt(1);   // e.g. 5
                            // The trigger will set INo = "I5"
                            System.out.println("Inserted base id: " + generatedId);
                        }


                        /************ have to link investmentholding and porrtfolio via linked_to ***************/
                        /* done by deafult assumed all linked via mysql */

                        sql = "INSERT INTO held_in (id, PID) VALUES (?, ?)";

                        /* hep bole yap sql injectiona karsı (secured) */
                        ps = conn.prepareStatement(sql);
                        ps.setDouble(1, generatedId);
                        ps.setString(2, pid);
                        ps.executeUpdate();

                    }

                    if (Objects.equals(userType, "Bond")){
                        System.out.println("Enter the amount: ");
                        double amount = scanner.nextDouble();

                        sql = "SELECT PID FROM investmentportfolio WHERE Itype = 'Bond'";
                        String pid = "";
                        ps = conn.prepareStatement(sql);
                        rs = ps.executeQuery();

                        if (rs.next())
                            pid = rs.getString("PID");

                        sql = "UPDATE investmentportfolio SET Value = Value + ? WHERE PID = ?";
                        /* hep bole yap sql injectiona karsı (secured) */
                        ps = conn.prepareStatement(sql);
                        ps.setDouble(1, amount);
                        ps.setString(2, pid);
                        ps.executeUpdate();

                        sql = "UPDATE account SET Balance = Balance - ? WHERE AccountNo = ?";

                        /* hep bole yap sql injectiona karsı (secured) */
                        ps = conn.prepareStatement(sql);
                        ps.setDouble(1, amount);
                        ps.setString(2, AccountNo);
                        ps.executeUpdate();

                        sql = "INSERT INTO transaction (Tdate, Tamount, Ttype, AccountNo,targetNo) VALUES (?, ?, ?, ?, ?)";
                        ps = conn.prepareStatement(sql);
                        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        LocalDate currentDate = LocalDate.now();

                        ps.setDate(1, java.sql.Date.valueOf(currentDate));// (using today's date for example)
                        ps.setDouble(2, amount); // DOUBLE YAPMAYI UNUTMA FLOAT YAPMISSIN
                        ps.setString(3, "bond bought");
                        ps.setString(4, AccountNo);
                        ps.setString(5, null);

                        int affectedRows = ps.executeUpdate();
                        if (affectedRows > 0) {
                            rs = ps.getGeneratedKeys();
                            if (rs.next()) {
                                int generatedId = rs.getInt(1);
                                String TID = "T" + generatedId;  // unique transaction id, auto-incremented in mysql
                                System.out.println("Transaction recorded with TID: " + TID);
                            }
                        }

                        sql = "INSERT INTO investment (IAmount, IType, ICurrency, AccountNo) "
                                + "VALUES (?, ?, ?, ?)";
                        ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        ps.setDouble(1, amount);
                        ps.setString(2, typeOfInvestment);
                        ps.setString(3, currency);
                        ps.setString(4, AccountNo);

                        ps.executeUpdate();

                        // If you want the auto-generated 'id':
                        int generatedId = 0;
                        rs = ps.getGeneratedKeys();
                        if (rs.next()) {
                            generatedId = rs.getInt(1);   // e.g. 5
                            // The trigger will set INo = "I5"
                            System.out.println("Inserted base id: " + generatedId);
                        }

                        sql = "INSERT INTO held_in (id, PID) VALUES (?, ?)";

                        /* hep bole yap sql injectiona karsı (secured) */
                        ps = conn.prepareStatement(sql);
                        ps.setDouble(1, generatedId);
                        ps.setString(2, pid);
                        ps.executeUpdate();


                    }

                    else{
                        System.out.println("Enter 'Asset' or 'Bond'");
                        customerView();
                    }

                }

                if (Objects.equals(userType, "2") || Objects.equals(userType, "Sell")){
                    System.out.println("\n\nWhich type of investment you buy: \n  " + "1.Asset  " + "2.Bond  ");
                    String typeOfInvestment = scanner.nextLine();

                    if (Objects.equals(typeOfInvestment, "1") || Objects.equals(userType, "Asset")){
                        System.out.println("Enter the amount: ");
                        double amount = scanner.nextDouble();

                        sql = "SELECT PID FROM hold WHERE AccountNo = ?";
                        String pid = "";
                        ps = conn.prepareStatement(sql);
                        ps.setString(1, AccountNo);
                        rs = ps.executeQuery();

                        if (rs.next())
                            pid = rs.getString("PID");

                        sql = "UPDATE investmentportfolio SET Value = Value - ? WHERE PID = ?";
                        /* hep bole yap sql injectiona karsı (secured) */
                        ps = conn.prepareStatement(sql);
                        ps.setDouble(1, amount);
                        ps.setString(2, pid);
                        ps.executeUpdate();

                        sql = "UPDATE account SET Balance = Balance + ? WHERE AccountNo = ?";

                        /* hep bole yap sql injectiona karsı (secured) */
                        ps = conn.prepareStatement(sql);
                        ps.setDouble(1, amount);
                        ps.setString(2, AccountNo);
                        ps.executeUpdate();

                        sql = "INSERT INTO transaction (Tdate, Tamount, Ttype, AccountNo,targetNo) VALUES (?, ?, ?, ?, ?)";
                        ps = conn.prepareStatement(sql);
                        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        LocalDate currentDate = LocalDate.now();

                        ps.setDate(1, java.sql.Date.valueOf(currentDate));// (using today's date for example)
                        ps.setDouble(2, amount); // DOUBLE YAPMAYI UNUTMA FLOAT YAPMISSIN
                        ps.setString(3, "asset sold");
                        ps.setString(4, AccountNo);
                        ps.setString(5, null);

                        int affectedRows = ps.executeUpdate();
                        if (affectedRows > 0) {
                            rs = ps.getGeneratedKeys();
                            if (rs.next()) {
                                int generatedId = rs.getInt(1);
                                String TID = "T" + generatedId;  // unique transaction id, auto-incremented in mysql
                                System.out.println("Transaction recorded with TID: " + TID);
                            }
                        }

                        /************ have to link investmentholding and porrtfolio via linked_to ***************/
                        /* done by deafult assumed all linked via mysql */
                    }

                    if (Objects.equals(typeOfInvestment, "2") || Objects.equals(userType, "Bond")){
                        System.out.println("Enter the amount: ");
                        double amount = scanner.nextDouble();

                        sql = "SELECT PID FROM hold WHERE AccountNo = ?";
                        String pid = "";
                        ps = conn.prepareStatement(sql);
                        ps.setString(1, AccountNo);
                        rs = ps.executeQuery();

                        if (rs.next())
                            pid = rs.getString("PID");

                        sql = "UPDATE investmentportfolio SET Value = Value - ? WHERE PID = ?";
                        /* hep bole yap sql injectiona karsı (secured) */
                        ps = conn.prepareStatement(sql);
                        ps.setDouble(1, amount);
                        ps.setString(2, pid);
                        ps.executeUpdate();

                        sql = "UPDATE account SET Balance = Balance + ? WHERE AccountNo = ?";

                        /* hep bole yap sql injectiona karsı (secured) */
                        ps = conn.prepareStatement(sql);
                        ps.setDouble(1, amount);
                        ps.setString(2, AccountNo);
                        ps.executeUpdate();

                        sql = "INSERT INTO transaction (Tdate, Tamount, Ttype, AccountNo,targetNo) VALUES (?, ?, ?, ?, ?)";
                        ps = conn.prepareStatement(sql);
                        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        LocalDate currentDate = LocalDate.now();

                        ps.setDate(1, java.sql.Date.valueOf(currentDate));// (using today's date for example)
                        ps.setDouble(2, amount); // DOUBLE YAPMAYI UNUTMA FLOAT YAPMISSIN
                        ps.setString(3, "bond sold");
                        ps.setString(4, AccountNo);
                        ps.setString(5, null);

                        int affectedRows = ps.executeUpdate();
                        if (affectedRows > 0) {
                            rs = ps.getGeneratedKeys();
                            if (rs.next()) {
                                int generatedId = rs.getInt(1);
                                String TID = "T" + generatedId;  // unique transaction id, auto-incremented in mysql
                                System.out.println("Transaction recorded with TID: " + TID);
                            }
                        }
                    }
                }

                if (Objects.equals(userType, "3") || Objects.equals(userType, "Exit")){
                    System.exit(0);
                }

            }

            else {
                System.out.println("\n1.Deposit  " + "2.Withdraw  " + "3.Transfer Funds  " + "4.Exit  ");

                sql = "SELECT Balance FROM Account WHERE AccountNo = ?";
                double Balance = 0;
                ps = conn.prepareStatement(sql);
                ps.setString(1, AccountNo);
                rs = ps.executeQuery();

                if (rs.next()) {
                    Balance = rs.getDouble("Balance");
                    System.out.println("Current Balance: " + Balance);
                }

                System.out.print("\nEnter an option or name of it : ");
                userType = scanner.nextLine();

                if (Objects.equals(userType, "1") || Objects.equals(userType, "Deposit")){

                    System.out.print("Enter deposit amount : ");
                    int DepositValue = scanner.nextInt();

                    sql = "UPDATE account SET Balance = Balance + ? WHERE AccountNo = ?";
                    /* hep bole yap sql injectiona karsı (secured) */
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, DepositValue);
                    ps.setString(2, AccountNo);
                    ps.executeUpdate();

                    sql = "INSERT INTO transaction (Tdate, Tamount, Ttype, AccountNo,targetNo) VALUES (?, ?, ?, ?, ?)";
                    ps = conn.prepareStatement(sql);
                    PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    LocalDate currentDate = LocalDate.now();

                    ps.setDate(1, java.sql.Date.valueOf(currentDate));// (using today's date for example)
                    ps.setFloat(2, DepositValue);
                    ps.setString(3, "deposit");
                    ps.setString(4, AccountNo);
                    ps.setString(5, null);

                    int affectedRows = ps.executeUpdate();
                    if (affectedRows > 0) {
                        rs = ps.getGeneratedKeys();
                        if (rs.next()) {
                            int generatedId = rs.getInt(1);
                            String TID = "T" + generatedId;  // unique transaction id, auto-incremented in mysql
                            System.out.println("Transaction recorded with TID: " + TID);
                        }
                    }
                    customerView();
                }
                else if (Objects.equals(userType, "2") || Objects.equals(userType, "Withdraw")){
                    System.out.print("Enter withdrawal amount : ");
                    int WithdrawValue = scanner.nextInt();

                    sql = "SELECT Balance FROM Account WHERE AccountNo = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, AccountNo);
                    rs = ps.executeQuery();

                    if (rs.next())
                        // Get balance from sql query
                        Balance = rs.getDouble("Balance");

                    if(Balance >= WithdrawValue && WithdrawValue > 0){
                        sql = "UPDATE account SET Balance = Balance - ? WHERE AccountNo = ?";

                        /* hep bole yap sql injectiona karsı (secured) */
                        ps = conn.prepareStatement(sql);
                        ps.setInt(1, WithdrawValue);
                        ps.setString(2, AccountNo);
                        ps.executeUpdate();

                        sql = "INSERT INTO transaction (Tdate, Tamount, Ttype, AccountNo,targetNo) VALUES (?, ?, ?, ?, ?)";
                        ps = conn.prepareStatement(sql);
                        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        LocalDate currentDate = LocalDate.now();

                        ps.setDate(1, java.sql.Date.valueOf(currentDate));// (using today's date for example)
                        ps.setFloat(2, WithdrawValue);
                        ps.setString(3, "withdraw");
                        ps.setString(4, AccountNo);
                        ps.setString(5, null);

                        int affectedRows = ps.executeUpdate();
                        if (affectedRows > 0) {
                            rs = ps.getGeneratedKeys();
                            if (rs.next()) {
                                int generatedId = rs.getInt(1);
                                String TID = "T" + generatedId;  // unique transaction id, auto-incremented in mysql
                                System.out.println("Transaction recorded with TID: " + TID);

                            }
                        }

                        sql = "SELECT Balance FROM Account WHERE AccountNo = ?";
                        ps = conn.prepareStatement(sql);
                        ps.setString(1, AccountNo);
                        rs = ps.executeQuery();

                        if (rs.next())
                            // Get balance from sql query
                            Balance = rs.getDouble("Balance");

                        System.out.println("\n\nNew Balance: " + Balance);
                        customerView();
                    }
                    else
                        System.out.println("You don't have enough money");


                    customerView();
                }

                else if (Objects.equals(userType, "3") || Objects.equals(userType, "Transfer Funds")){
                    System.out.print("Enter transfer funds amount : ");
                    int TransferFunds = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Account no that you want to send : ");
                    String targetAccountNo = scanner.nextLine();


                    sql = "UPDATE account SET balance = balance + ? WHERE accountNo =  ?";

                    ps = conn.prepareStatement(sql);
                    ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                    ps.setFloat(1, TransferFunds);
                    ps.setString(2, targetAccountNo);
                    ps.executeUpdate();


                    sql = "UPDATE account SET balance = balance - ? WHERE accountNo =  ?";

                    ps = conn.prepareStatement(sql);
                    ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                    ps.setFloat(1, TransferFunds);
                    ps.setString(2, AccountNo);
                    ps.executeUpdate();

                    sql = "INSERT INTO transaction (Tdate, Tamount, Ttype, AccountNo,targetNo) VALUES (?, ?, ?, ?, ?)";
                    ps = conn.prepareStatement(sql);
                    PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    LocalDate currentDate = LocalDate.now();

                    ps.setDate(1, java.sql.Date.valueOf(currentDate));// (using today's date for example)
                    ps.setFloat(2, TransferFunds);
                    ps.setString(3, "transfer funds");
                    ps.setString(4, AccountNo);
                    ps.setString(5, targetAccountNo);

                    int affectedRows = ps.executeUpdate();
                    if (affectedRows > 0) {
                        rs = ps.getGeneratedKeys();
                        if (rs.next()) {
                            int generatedId = rs.getInt(1);
                            String TID = "T" + generatedId;  // unique transaction id, auto-incremented in mysql
                            System.out.println("Transaction recorded with TID: " + TID);

                        }
                    }
                    System.out.println("Succesfull");

                    customerView();
                }

                else if (Objects.equals(userType, "4") || Objects.equals(userType, "Exit"))
                    mainMenu();
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occured! \n" + " returning back to customer panel!");
            customerView();
        }
    }
}

