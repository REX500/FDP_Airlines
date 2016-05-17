package guiLayer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

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

    @Override
    public void start(Stage s){
        window = s;
        borderPane = new BorderPane();

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
        datePickerTo.setPadding(new Insets(0,0,0,200));


        HBox calendar = new HBox();
        calendar.getChildren().addAll(datePickerFrom,datePickerTo);


        HBox labelBox = new HBox(100);
        Label labelFrom = new Label("From date");
        Label labelTo = new Label("To date");
        labelBox.getChildren().addAll(labelFrom, labelTo);
        vBox.getChildren().addAll(searchText,labelBox,calendar, searchButton);
        book.setContent(vBox);

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(book, myBooking, checkIn, flightStatus);
        //disabling the closing property
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);



        borderPane.setCenter(tabPane);

        // adding listeners to the buttons
        signIn.setOnAction(e-> signInMethod());

        scene = new Scene(borderPane, 500,350);
        window.setScene(scene);
        window.show();

    }

    //method for the employees to log in

    private void signInMethod(){
        //this will open a window for the employee to log in trough it
        signIn sign = new signIn();
        sign.display();
    }
}
