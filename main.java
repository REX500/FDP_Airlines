package guiLayer;

import applicationLayer.Employee;
import applicationLayer.Flight;
import applicationLayer.Plane;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import dataBaseLayer.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

/**
 * Created by Filip on 16-05-2016.
 */
public class main extends Application {
    public static void main(String[]args){
        launch(args);
    }

    Stage window;
    BorderPane borderPane;
    MenuBar menuBar;
    Scene scene;
    Tab book;
    TabPane tabPane;
    int runCheck;
    @Override
    public void start(Stage s)throws Exception{


        window = s;
        borderPane = new BorderPane();
        borderPane.setId("pane-border");


        // checking whether the app has been run for the first time

        /*Scanner read = new Scanner(new File("Settings.txt"));
        while(read.hasNext()){
            String line = read.nextLine();
            Scanner token = new Scanner(line);
            while(token.hasNext()){
                String run = token.next();
                String equals = token.next();
                int check = token.nextInt();
                if (run.equals("Run") && check == 0) {
                    // now we initialize the databases and tables
                    // because the user has ran the program for the first time
                    dbInit dataInit = new dbInit();
                    dataInit.initialize();
                    // when we are done with making all the databases we will overwrite the file so that the
                    // run = 1
                }
            }
        }
        File output = new File("Settings.txt");
        PrintWriter out = new PrintWriter(output);
        out.print("Run = 1");
        out.close();*/

        Menu file = new Menu("File");
        Menu edit = new Menu("Edit");
        Menu help = new Menu("Help");

        MenuItem oscaItem = new MenuItem("OSCA");

        edit.getItems().add(oscaItem);

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(file,edit,help);
        menuBar.setId("bar-menu");

        oscaItem.setOnAction(e -> {
            try {
                oscaMethod();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        String nameExt = "logo.jpg";
        Image logo = new Image(nameExt);

        ImageView iv3 = new ImageView();
        iv3.setImage(logo);
        iv3.setFitWidth(130);
        iv3.setPreserveRatio(true);
        iv3.setSmooth(true);
        iv3.setCache(true);

        VBox topVbox = new VBox();

        Button signIn = new Button("Sign in");
        signIn.setId("in-sign");


        HBox topHbox = new HBox();

        Region region = new Region();
        region.setMinWidth(480);

        topHbox.getChildren().addAll(iv3,region,signIn);
        signIn.setPadding(new Insets(15,50,15,50));


        topVbox.getChildren().addAll(menuBar, topHbox);

        borderPane.setTop(topVbox);

        //making a tabpane bellow

        book = new Tab("Book A Ticket");
        Tab myBooking = new Tab("My Booking");
        Tab checkIn = new Tab("Check in");
        Tab flightStatus = new Tab("Fight Status");

        // making a contect for the booking tab
        // adding a search bar, search button and a calendar
        DatePicker datePicker = new DatePicker();
        TextField searchText = new TextField();
        searchText.setPadding(new Insets(40,80,15,80));
        searchText.setPromptText("Search your destination...");
        searchText.setId("text-search");
        searchText.setAlignment(Pos.CENTER);

        Button searchButton = new Button("Search");
        VBox vBox = new VBox(10);
        searchButton.setId("button-search");

        DatePicker datePickerFrom = new DatePicker();
        datePickerFrom.setPadding(new Insets(0,0,0,0));


        DatePicker datePickerTo = new DatePicker();
        datePickerTo.setPadding(new Insets(0,0,0,0));


        HBox calendar = new HBox(100);
        calendar.getChildren().addAll(datePickerFrom, datePickerTo);


        HBox labelBox = new HBox(180);
        Label labelFrom = new Label("Departure Date");
        Label labelReturn = new Label("Return Date");
        labelBox.getChildren().addAll(labelFrom, labelReturn);

        HBox btnBox = new HBox();
        btnBox.setPadding(new Insets(230,10,10,610));
        btnBox.getChildren().addAll(searchButton);

        searchButton.setPadding(new Insets(15,50,15,50));
        vBox.getChildren().addAll(searchText,labelBox,calendar, btnBox);




        book.setContent(vBox);
        searchButton.setOnAction(e-> {
            String searchedDestination = searchText.getText();
            LocalDate datePicFrom = datePickerFrom.getValue();
            LocalDate datePicTo = datePickerTo.getValue();
            // here we get the date values from the datepicker and store
            // it into a string variable divided by a slash /
            int firstDay = datePicFrom.getDayOfMonth();
            int firstMonth = datePicFrom.getMonthValue();
            int firstYear = datePicFrom.getYear();
            String  realFirstDate = firstDay+"/"+firstMonth+"/"+firstYear;
            System.out.println(realFirstDate);

            int secondDay = datePicTo.getDayOfMonth();
            int secondMonth = datePicTo.getMonthValue();
            int secondYear = datePicTo.getYear();
            String realSecondDate = secondDay+"/"+secondMonth+"/"+secondYear;

            ArrayList<Flight> flightList = new ArrayList<Flight>();
            // now when we have dates we need to search if the actual flight in that date exists
            flightDataBase flightDataBase = new flightDataBase();
            try {
                flightList = flightDataBase.getFlights();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            for(int i= 0 ; i < flightList.size(); i++){
                // here we check if all three parameters match
                if(searchedDestination.equals(flightList.get(i).getDestination()) && realFirstDate.equals(flightList.get(i).getFromDate()) && realSecondDate.equals(flightList.get(i).getReturnDate())){
                    // now that it matches we do something with it
                    // we make a view with the flight
                    flightMatchesView(searchedDestination, realFirstDate, realSecondDate, flightList.get(i).getIdFlights());
                    break;
                }
                // if destination match
                if(searchedDestination.equals(flightList.get(i).getDestination())){
                    // here only the destination will match
                    flightMatchHalf(searchedDestination);
                    break;
                }
                // if there isnt a flight to that country at all
                if(flightList.size() - i ==1){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("No Flights Found");
                    alert.setContentText("We are sorry but we didnt manage to find any flight to the desired country");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                }
            }
        });

        tabPane = new TabPane();
        tabPane.getTabs().addAll(book, myBooking, checkIn, flightStatus);
        //disabling the closing property
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        borderPane.setCenter(tabPane);

        // adding listeners to the buttons
        signIn.setOnAction(e -> {
            try {
                signInMethod();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        scene = new Scene(borderPane, 770,600);
        scene.getStylesheets().add("Airline.css");
        window.setScene(scene);
        window.show();

    }

    //method that displays the flight

    private void flightMatchesView(String dest, String departDate, String returnDate, int id){
        Label destLabel = new Label("Destination: "+dest);
        Label departLabel = new Label("Departure Date: "+departDate);
        Label returnLabel = new Label("Return Date: "+ returnDate);

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(20,20,20,20));

        Button bookButton = new Button("Book");
        Button backButton = new Button("Back");

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(bookButton, backButton);


        vBox.getChildren().addAll(destLabel, departLabel, returnLabel, hBox);

        borderPane.setCenter(vBox);

        backButton.setOnAction(e -> {
            try {
                start(window);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        bookButton.setOnAction(e->{
            registerCustomer(id, dest);
        });
    }

    ListView<String> listView;
    private void flightMatchHalf(String dest){
        listView = new ListView<>();
        flightDataBase fdb = new flightDataBase();
        try {
            flightArrayList = fdb.getFlights();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0 ; i < flightArrayList.size(); i++){
            if(dest.equals(flightArrayList.get(i).getDestination())){
                String view =(i+1)+". "+ flightArrayList.get(i).getDestination()+","+flightArrayList.get(i).getFromDate()+","+flightArrayList.get(i).getReturnDate();
                listView.getItems().add(view);
            }
        }
        Button back = new Button("Back");
        Button book = new Button("Book A Ticket");

        HBox hBox = new HBox(8);
        hBox.getChildren().addAll(book, back);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(listView, hBox);
        vBox.setPadding(new Insets(20,20,20,20));

        book.setOnAction(e-> {

            String pick = listView.getSelectionModel().getSelectedItem();
            char v = pick.charAt(0);
            int value = Integer.parseInt(String.valueOf(v));
            System.out.println(value);
            int realV = value -1;
            int flightId = flightArrayList.get(realV).getIdFlights();

            registerCustomer(flightId, dest);
        });

        borderPane.setCenter(vBox);
    }


    //method for the employees to log in

    private void signInMethod() throws SQLException {
        //this will open a window for the employee to log in trough it
        VBox vBox = new VBox(10);
        GridPane grid = new GridPane();
        TextField userText = new TextField();
        userText.setPromptText("Username");

        PasswordField pass = new PasswordField();

        Label userNameLabel = new Label("Username: ");
        Label passLabel = new Label("Password: ");

        grid.setConstraints(userNameLabel, 0,0);
        grid.setConstraints(userText, 1,0);
        grid.setConstraints(passLabel, 0,1);
        grid.setConstraints(pass, 1,1);

        grid.getChildren().addAll(userNameLabel, userText, pass, passLabel);

        Button submit = new Button("Log In");
        Button back = new Button("Back");

        HBox hBox = new HBox(6);
        hBox.getChildren().addAll(submit, back);

        vBox.getChildren().addAll(grid, hBox);

        employeeDataBase employeeDataBase = new employeeDataBase();
        ArrayList<Employee> employeeArrayList = employeeDataBase.getEmployee();
        submit.setOnAction(e->{
            String name = userText.getText();
            String password = pass.getText();
            // comparing the entered data with the actual database data
            for(int i = 0; i < employeeArrayList.size(); i++){
                if(name.equals(employeeArrayList.get(i).getFname()) && password.equals(employeeArrayList.get(i).getPassword())){
                    // if they match we do a whole bunch of things
                    employeeView();
                    window.setTitle("Hello "+name);
                    break;
                }
                if(employeeArrayList.size()-i ==1){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Login Error");
                    a.setContentText("Wrong credentials!");
                    a.setHeaderText(null);
                    a.showAndWait();
                    break;
                }
            }
        });
        back.setOnAction(e -> {
            try {
                start(window);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        borderPane.setCenter(vBox);
        }

    // see planes method bellow
    TableView<Plane> planeTable;
    private void seePlanes() throws SQLException {
        // we will put a plane table inside that will based upon a click show a plane
        // with detailed info and a picture
        //making a table bellow
        TableColumn<Plane, Integer> planeId = new TableColumn<>("Plane ID");
        planeId.setMinWidth(100);
        planeId.setCellValueFactory(new PropertyValueFactory<>("idPlane"));

        TableColumn<Plane, String> planeName = new TableColumn<>("Name");
        planeName.setMinWidth(100);
        planeName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Plane, Integer> firstClass = new TableColumn<>("First Class Seats");
        firstClass.setMinWidth(100);
        firstClass.setCellValueFactory(new PropertyValueFactory<>("firstClass"));

        TableColumn<Plane, Integer> economyClass = new TableColumn<>("Economy class seats");
        economyClass.setMinWidth(150);
        economyClass.setCellValueFactory(new PropertyValueFactory<>("economyClass"));

        TableColumn<Plane, Integer> coachClass = new TableColumn<>("Coach Class Seats");
        coachClass.setMinWidth(150);
        coachClass.setCellValueFactory(new PropertyValueFactory<>("coachClass"));

        planeTable = new TableView<>();
        planeTable.setItems(getPlanes());
        planeTable.getColumns().addAll(planeId, planeName, firstClass, economyClass, coachClass);
        planeTable.setEditable(true);

        // making a info about a plane
        VBox planeVbox = new VBox(10);
        HBox planeHbox = new HBox(8);

        Button planeInfoButton = new Button("Info");
        Button back = new Button("Back");
        planeHbox.getChildren().addAll(planeInfoButton,back);
        planeVbox.getChildren().addAll(planeTable, planeHbox);

        borderPane.setCenter(planeVbox);

        planeInfoButton.setOnAction(e->{
            //here we will find out on which plane did the user or an employee click
            // and we will display the plane with the picture
            Plane p = planeTable.getSelectionModel().getSelectedItem();
            int idPlane = p.getIdPlane();
            String name = p.getName();
            int fClass = p.getFirstClass();
            int eClass = p.getEconomyClass();
            int cClass = p.getCoachClass();
            seePlane(idPlane, name, fClass, eClass, cClass);
        });
        back.setOnAction(e-> employeeView());
    }
    // method that displays the plane on which the user clicked on

    private void seePlane(int id, String name, int fclass, int eclass, int cclass){
        Label planeId = new Label("Plane ID: "+ id);
        Label planeName = new Label("Plane Name: "+ name);
        Label planeFclass = new Label("First Class Seats: "+ fclass);
        Label planeEclass = new Label("Economy Class Seats: "+eclass);
        Label planeCclass = new Label("Coach Class Seats: "+cclass);

        Button back = new Button("Back");
        Button change = new Button("Change Some Parameters");

        HBox mainHbox = new HBox(10);
        VBox labelVbox = new VBox(8);
        VBox buttonVbox = new VBox(10);

        //setting the image from to src folder
        String nameExt = name+".jpg";
        Image image = new Image(nameExt);

        // resizes the image to have width of 100 while preserving the ratio and using
        // higher quality filtering method; this ImageView is also cached to
        // improve performance
        ImageView iv2 = new ImageView();
        iv2.setImage(image);
        iv2.setFitWidth(200);
        iv2.setPreserveRatio(true);
        iv2.setSmooth(true);
        iv2.setCache(true);

        //adding action listeners to the buttons
        back.setOnAction(e-> {
            try {
                start(window);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        labelVbox.getChildren().addAll(planeId, planeName, planeFclass, planeEclass, planeCclass);
        buttonVbox.getChildren().addAll(change, back);
        mainHbox.getChildren().addAll(iv2, labelVbox, buttonVbox);

        borderPane.setCenter(mainHbox);
    }
    // method that gives us all the plane objects to be later on stored into the table view
    private ObservableList<Plane> getPlanes()throws  SQLException{
        ObservableList<Plane> planeObservableList = FXCollections.observableArrayList();
        planeDataBase planeDb = new planeDataBase();
        ArrayList<Plane> planeArrayList = planeDb.getPlanes();
        for(int i = 0; i < planeArrayList.size(); i++){
            planeObservableList.add(planeArrayList.get(i));
        }
        return planeObservableList;
    }

    private void employeeView(){
        GridPane grid = new GridPane();
        Button flightButton = new Button("Flights");
        Button planeButton = new Button("Planes");
        Button customerButton = new Button("Customers");
        Button employeeButton = new Button("Employee's");
        Button helpButton = new Button("Help");

        Label flightLabel = new Label("Flights");
        Label planeLabel = new Label("Planes");
        Label custoLabel = new Label("Customers");
        Label empLabel = new Label("Employee's");

        grid.setConstraints(flightLabel, 0,0);
        grid.setConstraints(planeLabel,1,0);
        grid.setConstraints(flightButton, 0,1);
        grid.setConstraints(planeButton, 1,1);
        grid.setConstraints(custoLabel, 0,2);
        grid.setConstraints(empLabel,1,2);
        grid.setConstraints(customerButton, 0,3);
        grid.setConstraints(employeeButton,1,3);
        grid.setConstraints(helpButton, 1,4);

        grid.setHgap(8);
        grid.setVgap(10);
        grid.getChildren().addAll(flightButton, flightLabel, planeButton, planeLabel, custoLabel, customerButton, empLabel, employeeButton, helpButton);

        //adding action listeners to the button
        flightButton.setOnAction(e->{
            Button seeFlights = new Button("Show");
            Button scheduleFlight = new Button("Schedule");

            HBox buttonHbox = new HBox(2);
            buttonHbox.getChildren().addAll(seeFlights, scheduleFlight);
            grid.setConstraints(buttonHbox, 0,1);
            grid.getChildren().add(buttonHbox);

            scheduleFlight.setOnAction(d-> scheduleFlightMethod());
            seeFlights.setOnAction(a -> seeFlightsTable());
        });
        planeButton.setOnAction(e -> {
            try {
                seePlanes();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        employeeButton.setOnAction(e->{
            try {
                employeesMethod();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        borderPane.setCenter(grid);
        borderPane.setTop(menuBar);
    }

    ArrayList<Flight> flightArrayList;
    int realId=0;
    private void scheduleFlightMethod(){
        ComboBox<String> planesComboBox = new ComboBox<String>();
        planesComboBox.getItems().addAll("Airbus 319","Airbus 320","Boeing 747","Boeing 777","Dash 8-Q400");
        planesComboBox.setEditable(false);




        planesComboBox.setOnAction(event -> {
            if(planesComboBox.getValue().equals("Airbus 319"))
                realId=23232;
            if(planesComboBox.getValue().equals("Airbus 320"))
                realId=90757;
            if(planesComboBox.getValue().equals("Boeing 747"))
                realId=32939;
            if(planesComboBox.getValue().equals("Boeing 777"))
                realId=21281;
            if(planesComboBox.getValue().equals("Dash 8-Q400"))
                realId=32222;
        });



        Label label3 = new Label("From Date: ");
        Label label2 = new Label("Return Date: ");
        Label label1 = new Label("Plane ID: ");
        Label label4 = new Label("");
        Label label5 = new Label("");
        Label label6 = new Label("");

        TextField text1 = new TextField();
        TextField text2 = new TextField();
        TextField text3 = new TextField();
        text1.setPromptText("Enter Plane ID");
        text2.setPromptText("Enter From Date");
        text3.setText("No return date");

        Button checkButton = new Button("Check Availability");
        Button backButton = new Button("Back");

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(10);

        grid.setConstraints(label1, 0,0);
        grid.setConstraints(planesComboBox, 1,0);
        grid.setConstraints(label3, 0,1);
        grid.setConstraints(text2, 1,1);
        grid.setConstraints(label2, 0,2);
        grid.setConstraints(text3, 1,2);
        grid.setConstraints(backButton, 0,3);
        grid.setConstraints(checkButton,1,3);


        grid.getChildren().addAll(label1, label2, label3,text2, text3, backButton, checkButton,planesComboBox);
        borderPane.setCenter(grid);

        backButton.setOnAction(e-> employeeView());

        checkButton.setOnAction(e->{
            String fromDate = text2.getText();
            String toDate = text3.getText();

            flightDataBase flightDataBase = new flightDataBase();
            try {
                flightArrayList = flightDataBase.getFlights();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            // now that we have all the flights we need to check if the required flight
            // can be scheduled

            //lets assume that the emp didnt enter the return date
            for(int i = 0; i< flightArrayList.size(); i++) {
                if (toDate.equals("No return date") && realId == flightArrayList.get(i).getPlane_id() && fromDate.equals(flightArrayList.get(i).getFromDate())){
                    // now we can say that the flight cannot be scheduled
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Scheduling error");
                    a.setContentText("Plane is already in use in the gives date, please pick another");
                    a.setHeaderText(null);
                    a.showAndWait();
                    break;
                }
                if(realId == flightArrayList.get(i).getPlane_id() && fromDate.equals(flightArrayList.get(i).getFromDate()) && toDate == flightArrayList.get(i).getReturnDate()){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Scheduling error");
                    a.setContentText("Plane is already in use in the gives date, please pick another");
                    a.setHeaderText(null);
                    a.showAndWait();
                    break;
                }
                if(flightArrayList.size() - i ==1){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Flight available");
                    alert.setContentText("Your flight can be scheduled!");
                    alert.setHeaderText(null);

                    alert.show();
                    try {
                        Thread.sleep(2000);
                        alert.close();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    // now we have to enable other buttons and schedule a flight

                    Label passExpectedLabel = new Label("Passengers expected: ");
                    Label destLabel = new Label("Destination: ");
                    TextField passText = new TextField();
                    passText.setPromptText("Enter numerical number of passengers");
                    TextField destText = new TextField();
                    destText.setPromptText("Enter the destination");

                    Button scheduleButton = new Button("Schedule a flight!");

                    grid.setConstraints(passExpectedLabel, 0,3);
                    grid.setConstraints(passText, 1,3);
                    grid.setConstraints(destLabel, 0,4);
                    grid.setConstraints(destText, 1,4);
                    grid.setConstraints(backButton, 0,5);
                    grid.setConstraints(scheduleButton,1,5);

                    grid.getChildren().addAll(passExpectedLabel, passText, destLabel, destText, scheduleButton);

                    scheduleButton.setOnAction(b->{
                        // here we will schedule a flight and input it into a database
                        String passExpectext = passText.getText();
                        int passE = Integer.parseInt(passExpectext);
                        String destination = destText.getText();
                        try {
                            flightDataBase.scheduleFlight(realId, destination, fromDate, toDate, passE);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                        alert1.setTitle("Flight Scheduling Successful!");
                        alert1.setContentText("Your flight has been scheduled!");
                        alert1.setHeaderText(null);
                        alert1.showAndWait();

                        employeeView();
                    });
                }
            }
        });
    }

    private void oscaMethod()throws Exception{
        // this method will take two database tables and put all the data into a comma separated file

        File file = new File("OSCAEmployee.csv");
        PrintWriter out = new PrintWriter(new FileWriter(file, true));

        // first table is the employee table
        employeeDataBase emp = new employeeDataBase();
        ArrayList<Employee> employeeList = new ArrayList<>();
        employeeList = emp.getEmployee();

        for(int i = 0; i < employeeList.size(); i++){
            out.println(employeeList.get(i).getIdEmployee()+","+employeeList.get(i).getFname()+","+employeeList.get(i).getLname()+","+employeeList.get(i).getPassword()+","+employeeList.get(i).getPosition()+","+employeeList.get(i).getSalary());
        }

        // now we print the plane table into the same file
        planeDataBase pln = new planeDataBase();
        ArrayList<Plane> planeArrayList = new ArrayList<>();
        planeArrayList = pln.getPlanes();

        File file2 = new File("OSCAPlane.csv");
        PrintWriter out2 = new PrintWriter(new FileWriter(file2, true));
        for(int j = 0; j < planeArrayList.size(); j++){
            out2.println(planeArrayList.get(j).getIdPlane()+","+planeArrayList.get(j).getName()+","+planeArrayList.get(j).getCoachClass()+","+planeArrayList.get(j).getEconomyClass()+","+planeArrayList.get(j).getFirstClass());
        }
        out.close();
        out2.close();
    }

    TableView<Employee> employeeTable;
    private void employeesMethod() throws SQLException {
        // in this method we will be able to see all the employee's
        // and manage them as well as add aditional data

        TableColumn<Employee, Integer> empId = new TableColumn<>("ID");
        empId.setMinWidth(100);
        empId.setCellValueFactory(new PropertyValueFactory<>("idEmployee"));

        TableColumn<Employee, String> empfname = new TableColumn<>("First Name");
        empfname.setMinWidth(100);
        empfname.setCellValueFactory(new PropertyValueFactory<>("fname"));

        TableColumn<Employee, String> emplname = new TableColumn<>("Last Name");
        emplname.setMinWidth(100);
        emplname.setCellValueFactory(new PropertyValueFactory<>("lname"));

        TableColumn<Employee, String> emppass = new TableColumn<>("Password");
        emppass.setMinWidth(100);
        emppass.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<Employee, String> emppos = new TableColumn<>("Position");
        emppos.setMinWidth(100);
        emppos.setCellValueFactory(new PropertyValueFactory<>("position"));

        TableColumn<Employee, Integer> empsal = new TableColumn<>("Salary");
        empsal.setMinWidth(100);
        empsal.setCellValueFactory(new PropertyValueFactory<>("salary"));

        employeeTable = new TableView<>();
        employeeTable.getColumns().addAll(empId, empfname, emplname, emppass, emppos, empsal);
        employeeTable.setItems(getEmployees());

        // now we make a few buttons that can give us the info about the emp or just go
        // back to the employee view

        Button empInfo = new Button("Info");
        Button back = new Button("Back");

        // now we put all of it into a vbox

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(empInfo, back);
        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(employeeTable, hBox);

        borderPane.setCenter(vBox);

        // now lets make a easy action listener for a back button

        back.setOnAction(e-> employeeView());

        // and now for the info
        empInfo.setOnAction(e->{
            //first we need to see what the emp clicked on
            Employee newE = employeeTable.getSelectionModel().getSelectedItem();
            // with this we just constructed a employee object from the table
            // we can easily use all the getters and setters now
            int id = newE.getIdEmployee();
            String fname = newE.getFname();
            String lname = newE.getLname();
            String pos = newE.getPosition();
            String pass = newE.getPassword();
            int sal = newE.getSalary();

            // now we call a method that will show us all the info about the employee

            employeeInfo(id, fname, lname, pos, pass, sal);
        });
    }

    private ObservableList<Employee> getEmployees()throws  SQLException{
        ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();
        employeeDataBase emp = new employeeDataBase();
        ArrayList<Employee> employeeArrayList = emp.getEmployee();
        for(int i = 0; i < employeeArrayList.size(); i++){
            employeeObservableList.add(employeeArrayList.get(i));
        }
        return employeeObservableList;
    }

    private void employeeInfo(int id, String fname, String lname, String pos, String pass, int sal){
        // in this method we will display all the employee info
        Label idLabel = new Label("Employee ID: "+ id);
        Label fLabel = new Label("First Name: "+ fname);
        Label lLabel = new Label("Last Name: "+lname);
        Label posLabel = new Label("Position: "+pos);
        Label salLabel = new Label("Salary: "+sal);
        Label passLabel = new Label("Password: "+pass);

        //setting the image from to src folder
        String nameExt = fname+".jpg";
        Image image = new Image(nameExt);

        // resizes the image to have width of 100 while preserving the ratio and using
        // higher quality filtering method; this ImageView is also cached to
        // improve performance
        ImageView iv2 = new ImageView();
        iv2.setImage(image);
        iv2.setFitWidth(200);
        iv2.setPreserveRatio(true);
        iv2.setSmooth(true);
        iv2.setCache(true);

        Button back = new Button("Back");
        Button moreInfo = new Button("More Info");
        Button changeInfo = new Button("Change Info");

        HBox mainHbox = new HBox(10);
        VBox labelVbox = new VBox(8);
        VBox btnVbox = new VBox(10);

        labelVbox.getChildren().addAll(idLabel, fLabel, lLabel, posLabel, salLabel, passLabel);
        btnVbox.getChildren().addAll(moreInfo, changeInfo, back);

        mainHbox.getChildren().addAll(iv2, labelVbox, btnVbox);

        borderPane.setCenter(mainHbox);

        back.setOnAction(e -> {
            try {
                employeesMethod();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
    }

    String pick = "";
    private void registerCustomer(int flightId, String dest){
        TextField fullnameField = new TextField();
        TextField passIDField = new TextField();

        Label nameLabel = new Label("Customer's Full Name: ");
        Label passIDLabel = new Label("Customer's Passport Number: ");
        Label classLabel = new Label("Pick A Class: ");


        ComboBox<String> box = new ComboBox<String>();
        box.getItems().addAll("First Class", "Economy Class", "Coach Class");
        box.setOnAction(e -> {
            if(box.getValue().equals("First Class"))
                pick = "first";
            if(box.getValue().equals("Economy Class"))
                pick = "economy";
            if(box.getValue().equals("Coach Class"))
                pick = "coach";
        });
        box.setEditable(false);

        Button bookButton = new Button("Book");
        Button backButton = new Button("Back");

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(bookButton, backButton);

        VBox vBox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(10);
        grid.setPadding(new Insets(10,10,10,10));

        grid.setConstraints(nameLabel, 0,0);
        grid.setConstraints(fullnameField, 1,0);
        grid.setConstraints(passIDLabel,0,1);
        grid.setConstraints(passIDField, 1,1);
        grid.setConstraints(classLabel, 0,2);
        grid.setConstraints(box, 1,2);

        grid.getChildren().addAll(nameLabel, fullnameField, passIDField, passIDLabel, box, classLabel);

        vBox.getChildren().addAll(grid, hBox);

        borderPane.setCenter(vBox);

        // listeners 4 d buttons

        backButton.setOnAction(e-> flightMatchHalf(dest));
        bookButton.setOnAction(e->{

            planeDataBase planeDataBase = new planeDataBase();

            int firstClassPlaneSeats = 0, economyClassPlaneSeats = 0, coachClassPlaneSeats = 0;

            // now that we have seat number in a plane we need to see how many of the seats
            // are taken inside a plane from a flight method

            int fC = 0, eC = 0, cC = 0, plane = 0;
            flightDataBase flightDataBase = new flightDataBase();
            try {
                Flight flight = flightDataBase.selectAFlight(flightId);
                plane = flight.getPlane_id();
                fC = flight.getFirstClass();
                eC = flight.getEconomyClass();
                cC = flight.getCoachClass();


            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            try {
                Plane planeObject = planeDataBase.getPlane(plane);
                firstClassPlaneSeats = planeObject.getFirstClass();
                economyClassPlaneSeats = planeObject.getEconomyClass();
                coachClassPlaneSeats = planeObject.getCoachClass();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.out.println(firstClassPlaneSeats+" "+economyClassPlaneSeats+" "+coachClassPlaneSeats);
            System.out.println(fC + " " + eC + " "+ cC);

            String name = fullnameField.getText();
            String passport = passIDField.getText();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Booking Confirmation");
            alert.setHeaderText("Book a ticket");
            alert.setContentText("Are you sure?");
            customerDataBase cdb = new customerDataBase();

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // ... user chooses OK
                if(pick.equals("first") && firstClassPlaneSeats - fC != 0 ){
                    // now we can say that we have seats available
                    // and we can book a ticket in a first class
                    try {
                        cdb.setCustomer(name, passport, String.valueOf(flightId));
                        flightDataBase.setFlightFirstSeats(flightId);
                        System.out.println("First Class works!");
                        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                        a.setTitle("Customer Registered");
                        a.setContentText("Customer has been registered successfully!");
                        a.setHeaderText(null);
                        a.show();

                        Thread.sleep(1500);
                        a.close();

                        flightMatchHalf(dest);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }


                }
                if (pick.equals("economy") && economyClassPlaneSeats - eC !=0){
                    try {
                        cdb.setCustomer(name, passport, String.valueOf(flightId));
                        flightDataBase.setFlightEconomySeats(flightId);
                        System.out.println("First Class works!");
                        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                        a.setTitle("Customer Registered");
                        a.setContentText("Customer has been registered successfully!");
                        a.setHeaderText(null);
                        a.show();

                        Thread.sleep(1500);
                        a.close();

                        flightMatchHalf(dest);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                if(pick.equals("coach") && coachClassPlaneSeats - cC !=0){
                    try {
                        cdb.setCustomer(name, passport, String.valueOf(flightId));
                        flightDataBase.setFlightCoachSeats(flightId);
                        System.out.println("First Class works!");
                        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                        a.setTitle("Customer Registered");
                        a.setContentText("Customer has been registered successfully!");
                        a.setHeaderText(null);
                        a.show();

                        Thread.sleep(1500);
                        a.close();

                        flightMatchHalf(dest);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            } else {
                // employee choses cancel

            }
        });
    }

    TableView<Flight> flightTable;
    private void seeFlightsTable(){
        TableColumn<Flight, Integer> flightID = new TableColumn<>("Flight ID");
        flightID.setMinWidth(100);
        flightID.setCellValueFactory(new PropertyValueFactory<>("idFlights"));

        TableColumn<Flight, Integer> flightPlaneId = new TableColumn<>("Plane ID");
        flightPlaneId.setMinWidth(100);
        flightPlaneId.setCellValueFactory(new PropertyValueFactory<>("plane_id"));

        TableColumn<Flight, String> flightDestination = new TableColumn<>("Destination");
        flightDestination.setMinWidth(100);
        flightDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));

        TableColumn<Flight, String> flightExpected = new TableColumn<>("Passengers Expected");
        flightExpected.setMinWidth(100);
        flightExpected.setCellValueFactory(new PropertyValueFactory<>("passExpected"));

        TableColumn<Flight, String> flightOnBoard = new TableColumn<>("Passengers On Board");
        flightOnBoard.setMinWidth(100);
        flightOnBoard.setCellValueFactory(new PropertyValueFactory<>("passOnBoard"));

        TableColumn<Flight, String> flightReturn = new TableColumn<>("Return Date");
        flightReturn.setMinWidth(100);
        flightReturn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        TableColumn<Flight, String> flightFrom = new TableColumn<>("From Date");
        flightFrom.setMinWidth(100);
        flightFrom.setCellValueFactory(new PropertyValueFactory<>("fromDate"));

        flightTable = new TableView<>();
        flightTable.getColumns().addAll(flightID, flightPlaneId, flightDestination, flightExpected, flightOnBoard, flightReturn, flightFrom);
        flightTable.setItems(getFlights());

        borderPane.setCenter(flightTable);
    }

    public ObservableList<Flight> getFlights(){
        ObservableList<Flight> flightObservableList = FXCollections.observableArrayList();
        flightDataBase flightDataBase = new flightDataBase();
        ArrayList<Flight> flightList = new ArrayList<>();
        try {
            flightList = flightDataBase.getFlights();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(int i = 0 ; i< flightList.size(); i ++){
            flightObservableList.add(flightList.get(i));
        }
        return flightObservableList;
    }
}