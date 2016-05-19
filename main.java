package guiLayer;

import applicationLayer.Employee;
import applicationLayer.Flight;
import applicationLayer.Plane;
import dataBaseLayer.dbInit;
import dataBaseLayer.employeeDataBase;
import dataBaseLayer.flightDataBase;
import dataBaseLayer.planeDataBase;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
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
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
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

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(file,edit,help);

        VBox topVbox = new VBox();

        Button signIn = new Button("Sign in");

        HBox topHbox = new HBox();
        topHbox.getChildren().addAll(signIn);
        signIn.setPadding(new Insets(0,0,0,250));

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
        searchText.setPromptText("Search your destination...");

        Button searchButton = new Button("Search");
        VBox vBox = new VBox(10);

        DatePicker datePickerFrom = new DatePicker();
        datePickerFrom.setPadding(new Insets(0,0,0,0));


        DatePicker datePickerTo = new DatePicker();
        datePickerTo.setPadding(new Insets(0,0,0,0));


        HBox calendar = new HBox(100);
        calendar.getChildren().addAll(datePickerFrom,datePickerTo);


        HBox labelBox = new HBox(100);
        Label labelFrom = new Label("From date");
        Label labelTo = new Label("To date");
        labelBox.getChildren().addAll(labelFrom, labelTo);
        vBox.getChildren().addAll(searchText,labelBox,calendar, searchButton);
        book.setContent(vBox);

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

        scene = new Scene(borderPane, 700,500);
        window.setScene(scene);
        window.show();

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
        iv2.setFitWidth(100);
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
        });
        planeButton.setOnAction(e -> {
            try {
                seePlanes();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        borderPane.setCenter(grid);
        borderPane.setTop(menuBar);
    }

    ArrayList<Flight> flightArrayList;
    private void scheduleFlightMethod(){
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
        grid.setConstraints(text1, 1,0);
        grid.setConstraints(label3, 0,1);
        grid.setConstraints(text2, 1,1);
        grid.setConstraints(label2, 0,2);
        grid.setConstraints(text3, 1,2);
        grid.setConstraints(backButton, 0,3);
        grid.setConstraints(checkButton,1,3);

        grid.getChildren().addAll(label1, label2, label3, text1, text2, text3, backButton, checkButton);
        borderPane.setCenter(grid);

        backButton.setOnAction(e-> employeeView());

        checkButton.setOnAction(e->{
            String fromDate = text2.getText();
            String toDate = text3.getText();
            String planeId = text1.getText();
            int realId = Integer.parseInt(planeId);

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
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Flight available");
                    alert.setContentText("Your flight can be scheduled!");
                    alert.setHeaderText(null);

                    alert.show();
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            //!!!!!!!!!!!!!!!!!!
            // i need to make a cause in which he entered the return date as well
            // after that make a if(flightArray.size() - i = 1){ you are good to go}
        });
    }
}
