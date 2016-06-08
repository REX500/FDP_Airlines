package guiLayer;

import applicationLayer.Customer;
import applicationLayer.Employee;
import applicationLayer.Flight;
import applicationLayer.Plane;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.sun.org.apache.xml.internal.dtm.ref.CustomStringPool;
import dataBaseLayer.*;
import javafx.animation.PauseTransition;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

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
    Tab book, checkIn, flightStatus, myBooking;
    TabPane tabPane;
    int runCheck;
    boolean closepopupwindow = true;
    Stage popup;

    @Override
    public void start(Stage s)throws Exception{


        initialize();

        window = s;
        borderPane = new BorderPane();
        borderPane.setId("pane-border");

        Menu file = new Menu("File");
        Menu edit = new Menu("Edit");
        Menu help = new Menu("Help");

        MenuItem oscaItem = new MenuItem("OSCA");

        edit.getItems().add(oscaItem);

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(file,edit,help);
        menuBar.setId("bar-menu");

        MenuItem fileItem = new MenuItem("CostumerView");
        file.getItems().add(fileItem);
        fileItem.setOnAction(e->
        {
            customerviewmethod();
        });


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
        myBooking = new Tab("My Booking");
        checkIn = new Tab("Check in");
        flightStatus = new Tab("Flight Status");

        // making a contect for the booking tab
        // adding a search bar, search button and a calendar

        // date pickers were found on the java2s.com

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
        calendar.setPadding(new Insets(10,10,10,140));


        HBox labelBox = new HBox(180);
        Label labelFrom = new Label("Departure Date");
        Label labelReturn = new Label("Return Date");
        labelBox.getChildren().addAll(labelFrom, labelReturn);
        labelBox.setPadding(new Insets(0,0,0,155));

        HBox btnBox = new HBox();
        btnBox.setPadding(new Insets(200,20,10,610));
        btnBox.getChildren().addAll(searchButton);
        searchButton.setMinWidth(150);

        searchButton.setPadding(new Insets(15,50,15,50));
        vBox.getChildren().addAll(searchText,labelBox,calendar, btnBox);

        book.setContent(vBox);

        searchButton.setOnAction(e-> {
            String searchedDestination = searchText.getText();

            // getting date values from date pickers was done using intelliJ and nothing else
            LocalDate datePicFrom = datePickerFrom.getValue();
            LocalDate datePicTo = datePickerTo.getValue();
            // here we get the date values from the datepicker and store
            // it into a string variable divided by a slash /
            int firstDay = datePicFrom.getDayOfMonth();
            int firstMonth = datePicFrom.getMonthValue();
            int firstYear = datePicFrom.getYear();
            String  realFirstDate = firstDay+"/"+firstMonth+"/"+firstYear;

            int secondDay = datePicTo.getDayOfMonth();
            int secondMonth = datePicTo.getMonthValue();
            int secondYear = datePicTo.getYear();
            String realSecondDate = secondDay+"/"+secondMonth+"/"+secondYear;

            // now when we have dates we need to search if the actual flight in that date exists

            for(int i= 0 ; i < flights.size(); i++){
                // here we check if all three parameters match
                if(searchedDestination.equalsIgnoreCase(flights.get(i).getDestination()) && realFirstDate.equals(flights.get(i).getFromDate()) && realSecondDate.equals(flights.get(i).getReturnDate())){
                    // now that it matches we do something with it
                    // we make a view with the flight
                    flightMatchesView(flights.get(i).getDestination(), realFirstDate, realSecondDate, flights.get(i).getIdFlights());
                    break;
                }
                // if destination match
                if(searchedDestination.equalsIgnoreCase(flights.get(i).getDestination())){
                    // here only the destination will match
                    flightMatchHalf(flights.get(i).getDestination());
                    break;
                }
                // if there isnt a flight to that country at all
                if(flights.size() - i ==1){
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


        checkIntMethod();
        flightStatus();
        myBooking();

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
        window.setMaxWidth(770);
        window.setMaxHeight(600);
        window.setMaxWidth(770);
        window.setMinHeight(600);
        scene.getStylesheets().add("style.css");
        window.setScene(scene);
        window.show();

    }

    customerDataBase customerDataBase;
    employeeAddressDataBase employeeAddressDataBase;
    employeeDataBase employeeDataBase;
    planeDataBase planeDataBase;
    flightDataBase flightDataBase;

    ArrayList<Customer> customers;
    ArrayList<Plane> planes;
    ArrayList<Flight> flights;
    ArrayList<Employee> employees;

    private  void initialize(){
        customerDataBase = new customerDataBase();
        employeeDataBase = new employeeDataBase();
        employeeAddressDataBase = new employeeAddressDataBase();
        planeDataBase = new planeDataBase();
        flightDataBase = new flightDataBase();

        try {
            customers = customerDataBase.getCustomers();
            planes = planeDataBase.getPlanes();
            flights = flightDataBase.getFlights();
            employees = employeeDataBase.getEmployee();

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        for(int i = 0 ; i < flights.size(); i++){
            if(dest.equals(flights.get(i).getDestination())){
                String view =(i+1)+". "+ flights.get(i).getDestination()+","+flights.get(i).getFromDate()+","+flights.get(i).getReturnDate();
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
            int realV = value -1;
            int flightId = flights.get(realV).getIdFlights();

            registerCustomer(flightId, dest);
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

    // method that has the table with the customers

    TableView<Customer> customerTable;
    private void customerTable() throws SQLException {
        TableColumn<Customer, Integer> custoID = new TableColumn<>("Customer ID");
        custoID.setMinWidth(100);
        custoID.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Customer, String> custoName = new TableColumn<>("Customer Name");
        custoName.setMinWidth(100);
        custoName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, String> custoPass = new TableColumn<>("Passport ID");
        custoPass.setMinWidth(150);
        custoPass.setCellValueFactory(new PropertyValueFactory<>("passId"));

        TableColumn<Customer, Integer> custoFlightID = new TableColumn<>("Customer Flight ID");
        custoFlightID.setMinWidth(100);
        custoFlightID.setCellValueFactory(new PropertyValueFactory<>("flightId"));

        TableColumn<Customer, String> custoPlaneClass = new TableColumn<>("Customer Plane Class");
        custoPlaneClass.setMinWidth(100);
        custoPlaneClass.setCellValueFactory(new PropertyValueFactory<>("planeClass"));

        customerTable = new TableView<>();
        customerTable.setItems(getCustomers());
        customerTable.getColumns().addAll(custoID, custoName, custoPass, custoFlightID, custoPlaneClass);
        customerTable.setEditable(false);

        VBox vBox = new VBox(10);
        Button back = new Button("Back");

        vBox.getChildren().addAll(customerTable, back);
        borderPane.setCenter(vBox);

        back.setOnAction(e-> employeeView());
    }

    public ObservableList<Customer> getCustomers() throws SQLException {
        ObservableList<Customer> customersObserve = FXCollections.observableArrayList();
        for(int i = 0 ; i < customers.size(); i++){
            customersObserve.add(customers.get(i));
        }
        return customersObserve;
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
        Button add = new Button("Add");

        planeHbox.getChildren().addAll(planeInfoButton,back, add);
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
        add.setOnAction(e->{
            addPlaneMethod();
        });
    }

    // method in which we add a plane to the database

    private void addPlaneMethod(){
        Label nameLabel = new Label("Name: ");
        Label flabel = new Label("First Class Seats: ");
        Label elabel = new Label("Economy Class Seats: ");
        Label clabel = new Label("Coach Class Seats: ");

        TextField textField1 = new TextField();
        TextField textField2 = new TextField();
        TextField textField3 = new TextField();
        TextField textField4 = new TextField();

        textField1.setPromptText("Enter name");
        textField2.setPromptText("Enter seat number");
        textField3.setPromptText("Enter seat number");
        textField4.setPromptText("Enter seat number");

        Button save = new Button("Save");
        Button cancel = new Button("Cancel");

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10,10,10,10));

        HBox hBox = new HBox(10);

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(10);

        grid.setConstraints(nameLabel, 0,0);
        grid.setConstraints(textField1, 1,0);
        grid.setConstraints(flabel, 0,1);
        grid.setConstraints(textField2, 1,1);
        grid.setConstraints(elabel, 0,2);
        grid.setConstraints(textField3, 1,2);
        grid.setConstraints(clabel, 0,3);
        grid.setConstraints(textField4, 1,3);

        grid.getChildren().addAll(nameLabel, flabel, clabel, elabel, textField1, textField2, textField3, textField4);

        hBox.getChildren().addAll(save, cancel);

        vBox.getChildren().addAll(grid, hBox);

        borderPane.setCenter(vBox);

        save.setOnAction(e->{
            String name = textField1.getText();
            String fseats = textField2.getText();
            String eseats = textField3.getText();
            String cseats = textField4.getText();


            Random r = new Random();
            int planeId = r.nextInt(99999 - 0 + 1) + 0;

            // checking if the plane with the code exists

            for(int i = 0 ; i < planes.size() ; i++){
                if(planeId == planes.get(i).getIdPlane()){
                    planeId = r.nextInt(99999 - 0 + 1) + 0;
                    i = 0;
                    // this way we reset the for loop so that it loops again until there is a unused plane id
                }
                else
                    break;
            }

            try {
                planeDataBase.addPlane(planeId, name, Integer.parseInt(fseats), Integer.parseInt(cseats), Integer.parseInt(eseats));
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("Plane Add Successful");
            a.setContentText("Plane has been added successfully!\nTo be able to see the plane successfully\nYou need to add the picture with the same name in the project\nSource folder");
            a.setHeaderText(null);
            a.showAndWait();
            initialize();
            try {
                seePlanes();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        cancel.setOnAction(e -> {
            try {
                seePlanes();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

    }

    public void customerviewmethod()
    {
        try {
            start(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // method that displays the plane on which the user clicked on

    private void seePlane(int id, String name, int fclass, int eclass, int cclass){
        Label planeId = new Label("Plane ID: "+ id);
        Label planeName = new Label("Plane Name: "+ name);
        Label planeFclass = new Label("First Class Seats: "+ fclass);
        Label planeEclass = new Label("Economy Class Seats: "+eclass);
        Label planeCclass = new Label("Coach Class Seats: "+cclass);

        Button back = new Button("Back");
        Button delete = new Button("Delete");

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
        iv2.setFitWidth(400);
        iv2.setPreserveRatio(true);
        iv2.setSmooth(true);
        iv2.setCache(true);

        //adding action listeners to the buttons
        back.setOnAction(e-> {
            employeeView();
        });
        delete.setOnAction(e->{
            try {
                planeDataBase.deletePlane(id);
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setTitle("Plane Delete successful");
                a.setContentText("Plane has been deleted!");
                a.setHeaderText(null);
                a.showAndWait();
                seePlanes();
                initialize();

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        labelVbox.getChildren().addAll(planeId, planeName, planeFclass, planeEclass, planeCclass);
        buttonVbox.getChildren().addAll(delete, back);
        mainHbox.getChildren().addAll(iv2, labelVbox, buttonVbox);

        borderPane.setCenter(mainHbox);
    }
    // method that gives us all the plane objects to be later on stored into the table view
    private ObservableList<Plane> getPlanes()throws  SQLException{
        ObservableList<Plane> planeObservableList = FXCollections.observableArrayList();
        for(int i = 0; i < planes.size(); i++){
            planeObservableList.add(planes.get(i));
        }
        return planeObservableList;
    }

    private void employeeView(){
        /**
         It locks here the size of the stage (window) and the scaleability. The login screen is still able to resize but
         the employeeview screen is not. If you change the size of the employeeview screen, then you need to change the
         icon's place too.
         */

        // Design of the employee view made by Patrik

        window.setMaxHeight(625);
        window.setMaxWidth(650);
        window.setMinHeight(600);
        window.setMinWidth(625);
        window.setResizable(false);
        window.centerOnScreen();
        menuBar.setId("menuBar");

        GridPane grid = new GridPane();
        Button flightButton = new Button();
        Button planeButton = new Button();
        Button customerButton = new Button();
        Button employeeButton = new Button();
        Button helpButton = new Button("HELP");

        /**
         * ButtonIDs for the CSS. ( style.css )
         */

        flightButton.setId("flightButton");
        planeButton.setId("planeButton");
        customerButton.setId("customerButton");
        employeeButton.setId("employeeButton");
        helpButton.setId("helpB");
        grid.setId("gridId");

        /**
         * Resize the icons. We need to keep them on this size because we have 200x200 pictures.
         */

        flightButton.setPrefSize(200,200);
        planeButton.setPrefSize(200,200);
        customerButton.setPrefSize(200,200);
        employeeButton.setPrefSize(200,200);
        helpButton.setPrefSize(100,70);


        Label flightLabel = new Label("   Flights");
        Label planeLabel = new Label("   Planes");
        Label custoLabel = new Label("  Customers");
        Label empLabel = new Label("  Employee's");

        /**
         * ID-s to CSS. (Not used yet.)
         */

        flightLabel.setId("fl");
        planeLabel.setId("pl");
        custoLabel.setId("cl");
        empLabel.setId("el");

        grid.setConstraints(flightLabel, 3,0);
        grid.setConstraints(empLabel,19,2);
        grid.setConstraints(planeLabel,19,0);
        grid.setConstraints(custoLabel, 3,2);
        grid.setConstraints(flightButton, 3,1);
        grid.setConstraints(planeButton, 19,1);
        grid.setConstraints(customerButton, 3,4);
        grid.setConstraints(employeeButton,19,4);
        grid.setConstraints(helpButton, 10,5);
        grid.setPadding(new Insets(10,10,10,10));
        grid.setHgap(5);
        grid.setVgap(10);
        grid.getChildren().addAll(flightButton, flightLabel, planeButton, planeLabel, custoLabel, customerButton, empLabel, employeeButton, helpButton);

        //adding action listeners to the button

        flightButton.setOnAction(e->
                {
                    popup = new Stage();
                    popup.initStyle(StageStyle.UNDECORATED);
                    popup.initModality(Modality.APPLICATION_MODAL);
                    popup.initOwner(window);
                    popup.centerOnScreen();
                    HBox popupHbox = new HBox(0);
                    Scene popupScene = new Scene(popupHbox, 400, 200);
                    popup.setScene(popupScene);
                    popupScene.getStylesheets().add("style.css");
                    popup.show();

                    /**
                     * ID for the CSS.
                     */
                    popupHbox.setId("popupHbox");

                    Button seeFlights = new Button();
                    Button scheduleFlight = new Button();

                    popupHbox.getChildren().addAll(seeFlights, scheduleFlight);

                    /**
                     * It is the timer to close the popup window. We have inside the IF statment a boolean(closepopupwindow), what
                     * was created at the beginning of the object, and here we call it. If it's true then close the window.
                     */

                    PauseTransition delay = new PauseTransition(Duration.seconds(5));
                    delay.setOnFinished(event ->
                    {
                        if (closepopupwindow)
                            popup.close();


                    });
                    delay.play();

                    /**
                     * ID-s for CSS. ( style.css )
                     */

                    seeFlights.setId("seeFlights");
                    scheduleFlight.setId("scheduleFlight");

                    /**
                     * Resize the icons.
                     */

                    seeFlights.setPrefSize(200, 200);
                    scheduleFlight.setPrefSize(200, 200);


                    HBox buttonHbox = new HBox(2);
                    buttonHbox.getChildren().addAll();
                    grid.setConstraints(buttonHbox, 0, 1);
                    grid.getChildren().add(buttonHbox);

                    /**
                     *If you click to the scheduleFlight and change the closepopupwindow to false, what makes the screen keep working.
                     */

                    scheduleFlight.setOnAction(d ->
                    {
                        closepopupwindow = false;
                        scheduleFlightMethod(popupHbox);
                    });

                    seeFlights.setOnAction(event -> {
                        popup.close();
                        seeFlightsTable();
                    });
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
        customerButton.setOnAction(e -> {
            try {
                customerTable();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        Button logOut = new Button("LogOut");
        logOut.setId("logoutbutton");
        logOut.setPrefSize(100,70);
        grid.setConstraints(logOut, 10,6);
        grid.getChildren().add(logOut);

        logOut.setOnAction(e->{
            try {
                signInMethod();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        borderPane.setCenter(grid);
        borderPane.setTop(menuBar);
    }

    int realId=0;
    private void scheduleFlightMethod(Pane pane){

        /**
         * It clears the popup screen and overwrite with the new labels buttons and boxes. Works like the previous GridPane ( grid ).
         */
        pane.getChildren().clear();
        pane.getStylesheets().add("scheduleFlight.css");

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

        if(pane instanceof BorderPane )
            ((BorderPane)pane).setCenter(grid);

        else
            pane.getChildren().add(grid);

        /**
         * Action to Back button and put us back to the first screen.
         */
        backButton.setOnAction(e->
        {
            try {
                popup.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });


        checkButton.setOnAction(e->{
            String fromDate = text2.getText();
            String toDate = text3.getText();

            // now that we have all the flights we need to check if the required flight
            // can be scheduled

            //lets assume that the emp didnt enter the return date
            for(int i = 0; i< flights.size(); i++) {
                if (toDate.equals("No return date") && realId == flights.get(i).getPlane_id() && fromDate.equals(flights.get(i).getFromDate())){
                    // now we can say that the flight cannot be scheduled
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Scheduling error");
                    a.setContentText("Plane is already in use in the gives date, please pick another");
                    a.setHeaderText(null);
                    a.showAndWait();
                    break;
                }
                if(realId == flights.get(i).getPlane_id() && fromDate.equals(flights.get(i).getFromDate()) && toDate == flights.get(i).getReturnDate()){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Scheduling error");
                    a.setContentText("Plane is already in use in the gives date, please pick another");
                    a.setHeaderText(null);
                    a.showAndWait();
                    break;
                }
                if(flights.size() - i ==1){
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

        for(int i = 0; i < employees.size(); i++){
            out.println(employees.get(i).getIdEmployee()+","+employees.get(i).getFname()+","+employees.get(i).getLname()+","+employees.get(i).getPassword()+","+employees.get(i).getPosition()+","+employees.get(i).getSalary());
        }

        // now we print the plane table into the same file

        File file2 = new File("OSCAPlane.csv");
        PrintWriter out2 = new PrintWriter(new FileWriter(file2, true));
        for(int j = 0; j < planes.size(); j++){
            out2.println(planes.get(j).getIdPlane()+","+planes.get(j).getName()+","+planes.get(j).getCoachClass()+","+planes.get(j).getEconomyClass()+","+planes.get(j).getFirstClass());
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
        for(int i = 0; i< employees.size(); i++){
            employeeObservableList.add(employees.get(i));
        }
        return employeeObservableList;
    }

    int moreInfoCheck = 0;
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

        HBox mainHbox = new HBox(10);
        VBox labelVbox = new VBox(8);
        VBox btnVbox = new VBox(10);

        labelVbox.getChildren().addAll(idLabel, fLabel, lLabel, posLabel, salLabel, passLabel);
        btnVbox.getChildren().addAll(moreInfo, back);

        mainHbox.getChildren().addAll(iv2, labelVbox, btnVbox);

        borderPane.setCenter(mainHbox);

        back.setOnAction(e -> {
            try {
                employeesMethod();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        moreInfo.setOnAction(e->{
            if(moreInfoCheck==1){
                employeeInfo(id, fname, lname, pos, pass, sal);
                moreInfoCheck--;
            }
            else {
                Employee employee = null;
                try {
                    employee = dataBaseLayer.employeeAddressDataBase.getAddressInfo(id, fname, lname, pass, pos, sal);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                Label countryLabel = new Label("Country: " + employee.getCountry());
                Label hometownLabel = new Label("Hometown: " + employee.getHometown());
                Label addressLabel = new Label("Address: " + employee.getAddress());
                Label zipLabel = new Label("Zip: " + employee.getZip());

                VBox addInfoBox = new VBox(8);

                addInfoBox.getChildren().addAll(countryLabel, hometownLabel, addressLabel, zipLabel);
                addInfoBox.setPadding(new Insets(0, 0, 0, 0));

                labelVbox.getChildren().add(addInfoBox);

                moreInfo.setText("Less Info");
                moreInfoCheck++;
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

            int firstClassPlaneSeats = 0, economyClassPlaneSeats = 0, coachClassPlaneSeats = 0;

            // now that we have seat number in a plane we need to see how many of the seats
            // are taken inside a plane from a flight method

            int fC = 0, eC = 0, cC = 0, plane = 0;
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

            String name = fullnameField.getText();
            String passport = passIDField.getText();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Booking Confirmation");
            alert.setHeaderText("Book a ticket");
            alert.setContentText("Are you sure?");

            Optional<ButtonType> result = alert.showAndWait();
            int errorCheck = 0;
            if (result.get() == ButtonType.OK){
                // ... user chooses OK
                if(pick.equals("first") && firstClassPlaneSeats - fC != 0 ){
                    // now we can say that we have seats available
                    // and we can book a ticket in a first class
                    try {
                        customerDataBase.setCustomer(name, passport, String.valueOf(flightId), "first");
                        flightDataBase.setFlightFirstSeats(flightId);

                        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                        a.setTitle("Customer Registered");
                        a.setContentText("Customer has been registered successfully!\nFlight ID is: "+flightId);
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
                    errorCheck++;
                }
                else if( pick.equals("first") && errorCheck == 0){
                    Alert alba = new Alert(Alert.AlertType.INFORMATION);
                    alba.setTitle("No Space Available");
                    alba.setContentText("We are sorry to inform you \nThat no seats are available\nIn the first class");
                    alba.setHeaderText(null);
                    alba.showAndWait();
                    errorCheck++;
                }
                if (pick.equals("economy") && economyClassPlaneSeats - eC !=0){
                    try {
                        customerDataBase.setCustomer(name, passport, String.valueOf(flightId), "economy");
                        flightDataBase.setFlightEconomySeats(flightId);
                        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                        a.setTitle("Customer Registered");
                        a.setContentText("Customer has been registered successfully!\nFlight ID is: "+flightId);
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
                    errorCheck++;
                }
                else if( pick.equals("economy") && errorCheck == 0){
                    Alert alba = new Alert(Alert.AlertType.INFORMATION);
                    alba.setTitle("No Space Available");
                    alba.setContentText("We are sorry to inform you \nThat no seats are available\nIn the economy class");
                    alba.setHeaderText(null);
                    alba.showAndWait();
                    errorCheck++;
                }
                if(pick.equals("coach") && coachClassPlaneSeats - cC !=0){
                    try {
                        customerDataBase.setCustomer(name, passport, String.valueOf(flightId), "coach");
                        flightDataBase.setFlightCoachSeats(flightId);
                        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                        a.setTitle("Customer Registered");
                        a.setContentText("Customer has been registered successfully!\nFlight ID is: "+flightId);
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
                    errorCheck++;
                }
                else if(pick.equals("coach") && errorCheck == 0){
                    Alert alba = new Alert(Alert.AlertType.INFORMATION);
                    alba.setTitle("No Space Available");
                    alba.setContentText("We are sorry to inform you \nThat no seats are available\nIn the coach class");
                    alba.setHeaderText(null);
                    alba.showAndWait();
                    errorCheck++;
                }
                errorCheck--;
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

        VBox vBox = new VBox(10);

        Button backButton = new Button("Back");

        vBox.getChildren().addAll(flightTable, backButton);

        borderPane.setCenter(vBox);

        backButton.setOnAction(e-> employeeView());
    }

    public ObservableList<Flight> getFlights(){
        ObservableList<Flight> flightObservableList = FXCollections.observableArrayList();
        for(int i = 0 ; i< flights.size(); i ++){
            flightObservableList.add(flights.get(i));
        }
        return flightObservableList;
    }
    private void checkIntMethod(){

        Label passLabel = new Label("Passport ID: ");
        Label flightIdLabel = new Label("Flight ID: ");

        TextField passText = new TextField();
        passText.setPromptText("Enter Passport ID");
        TextField flightIdText = new TextField();
        flightIdText.setPromptText("Enter Flight ID");

        Button checkInButton = new Button("Check In");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setConstraints(passLabel, 0,0);
        grid.setConstraints(passText , 1,0);
        grid.setConstraints(flightIdLabel, 0,1);
        grid.setConstraints(flightIdText, 1,1);

        grid.getChildren().addAll(passLabel, passText, flightIdLabel, flightIdText);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(grid, checkInButton);
        vBox.setPadding(new Insets(10,10,10,10));

        checkInButton.setOnAction(e->{
            String passID = passText.getText();
            String flightID = flightIdText.getText();
            for(int i = 0 ; i < customers.size(); i++){
                //first we check if the customer exists
                if(passID.equals(customers.get(i).getPassId()) && flightID.equals(customers.get(i).getFlightId()) ){
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Check-In Successful");
                    String info = "Welcome "+customers.get(i).getName()+"\nYou have checked in successfully!";
                    a.setContentText(info);
                    a.setHeaderText(null);
                    a.showAndWait();
                    break;
                }
                if(customers.size() - i == 1){
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Check In Error");
                        a.setContentText("Please check your log in data and try again!");
                        a.setHeaderText(null);
                        a.showAndWait();
                }
            }
            passText.clear();
            flightIdText.clear();
        });
        checkIn.setContent(vBox);
    }

    private void flightStatus()throws SQLException{
        Label flightIdLabel = new Label("Enter Flight ID: ");
        TextField flightText = new TextField();
        flightText.setPromptText("Enter ID");
        Button checkButton = new Button("Check");

        VBox mainBox = new VBox(10);
        HBox hBox = new HBox(10);

        hBox.getChildren().addAll(flightIdLabel, flightText);

        mainBox.getChildren().addAll(hBox, checkButton);

        flightStatus.setContent(mainBox);


        Label label = new Label();
        Label label1 = new Label();
        Label label2 = new Label();

        VBox labelv = new VBox(10);
        labelv.getChildren().addAll(label, label1, label2);

        mainBox.getChildren().add(labelv);

        checkButton.setOnAction(e->{

            int flightId = Integer.parseInt(flightText.getText());

            int firstClassPlaneSeats = 0, economyClassPlaneSeats = 0, coachClassPlaneSeats = 0;

            // now that we have seat number in a plane we need to see how many of the seats
            // are taken inside a plane from a flight method

            int fC = 0, eC = 0, cC = 0, plane = 0;
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

            //

            int fClassSpaces=0, cClassSpaces = 0, eClassSpaces=0;

            fClassSpaces = firstClassPlaneSeats - fC;
            cClassSpaces = coachClassPlaneSeats - cC;
            eClassSpaces = economyClassPlaneSeats - eC;

            String text1 = "First Class has "+fClassSpaces+" spaces left";
            String text2 = "Economy Class has "+eClassSpaces+" spaces left";
            String text3 = "Coach Class has "+ cClassSpaces+" spaces left";

            label.setText(text1);
            label1.setText(text2);
            label2.setText(text3);



        });
    }

    ListView<String> bookingView;
    private void myBooking(){
        TextField bookingText = new TextField();
        Label bookingLabel = new Label("Enter your passport ID to see all of the previous bookings: ");
        Button checkButton = new Button("Check");
        Button cancelButton = new Button("Cancel Ticket");

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(bookingLabel, bookingText);
        HBox btnBox = new HBox(10);
        btnBox.getChildren().addAll(checkButton, cancelButton);

        bookingView = new ListView<>();
        VBox mainBox = new VBox(10);
        mainBox.getChildren().addAll(hBox, btnBox, bookingView);

        try {
            customers = customerDataBase.getCustomers();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        checkButton.setOnAction(e->{
            String passId = bookingText.getText();

            for(int i = 0 ; i < customers.size(); i++){
                if(passId.equals(customers.get(i).getPassId())){
                    String text ="Name: "+ customers.get(i).getName()+" Passport ID: "+customers.get(i).getPassId()+" Flight ID:  "+customers.get(i).getFlightId()+" Class: "+customers.get(i).getPlaneClass();
                    bookingView.getItems().add(text);
                }
            }
        });
        cancelButton.setOnAction(e-> cancelMethod());


        myBooking.setContent(mainBox);
    }

    private void cancelMethod(){
        Label label = new Label("Enter your passport ID: ");
        Label label1 = new Label("Enter flight ID that you want to cancel: ");

        TextField text1 = new TextField();
        TextField text2 = new TextField();

        Button cancelButton = new Button("Proceed");
        Button backButton = new Button("Back");

        text1.setPromptText("Enter pass ID");
        text2.setPromptText("Enter Flight ID");

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(10);

        grid.setConstraints(label, 0,0);
        grid.setConstraints(text1,1,0);
        grid.setConstraints(label1, 0,1);
        grid.setConstraints(text2,1,1);

        grid.getChildren().addAll(label, label1, text1, text2);

        VBox vBox = new VBox(10);
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(cancelButton, backButton);

        vBox.getChildren().addAll(grid, hBox);

        cancelButton.setOnAction(e->{
            String passId = text1.getText();
            String flightID = text2.getText();

            for(int i = 0 ; i < customers.size(); i++){
                if(passId.equals(customers.get(i).getPassId()) && customers.get(i).getFlightId().equals(flightID)){
                    String name = customers.get(i).getName();
                    String fID = customers.get(i).getFlightId();
                    String passID = customers.get(i).getPassId();
                    String planeClass = customers.get(i).getPlaneClass();

                    String dbClassName = planeClass+"Class";

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Are You Sure");
                    alert.setHeaderText("Are you sure you want to cancel your ticket?");
                    alert.setContentText("Press OK to cancel");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        if(planeClass.equals("coach")) {
                            try {
                                flightDataBase.cancelCoachFlight(Integer.parseInt(fID));
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        }
                        else if(planeClass.equals("first")){
                            try {
                                flightDataBase.cancelFirstFlight(Integer.parseInt(fID));
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        }
                        else{
                            try {
                                flightDataBase.cancelEconomyFlight(Integer.parseInt(fID));
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            // now that we canceled the ticket we need to compare the dates
                            // to see whether the customer gets a full or half refund

                            try {
                                flights = flightDataBase.getFlights();
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            String stringDate= "";
                            for(int j = 0 ; j < flights.size(); j++){
                                if(fID.equals(String.valueOf(flights.get(j).getIdFlights()))){
                                    stringDate = flights.get(j).getFromDate();
                                    break;
                                }
                            }


                            Date date2 = new Date();
                            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                date2 = format.parse(stringDate);
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }

                            Date date = new Date();

                            Calendar cal = new GregorianCalendar();
                            cal.setTime(date);
                            int weeks = 0;

                            // we found this part of the code from the internet
                            // found it on stack overflow
                            while (cal.getTime().before(date2)) {
                                cal.add(Calendar.WEEK_OF_YEAR, 1);
                                weeks++;
                            }


                            if(weeks>2){
                                Alert a = new Alert(Alert.AlertType.INFORMATION);
                                a.setTitle("Good News");
                                a.setContentText("You have "+weeks+" weeks to your flight \nWhich means that you will get a full refund!");
                                a.setHeaderText(null);
                                a.showAndWait();
                            }
                            else{
                                Alert a = new Alert(Alert.AlertType.INFORMATION);
                                a.setTitle("Bad News");
                                a.setContentText("You have "+weeks+" weeks to your flight \nWhich means that you wont get a full refund!");
                                a.setHeaderText(null);
                                a.showAndWait();
                            }
                        }
                    }
                    else {
                        // ... user chose CANCEL or closed the dialog
                    }
                    break;
                }
            }
        });
        backButton.setOnAction(e -> {
            try {
                start(window);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        borderPane.setCenter(vBox);
    }
}