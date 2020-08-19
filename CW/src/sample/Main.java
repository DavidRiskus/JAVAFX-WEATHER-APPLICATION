package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{

        //Retrieve sample.fxml
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Simple Weather Data Analyser"); //set Apps title

        Scene scene = new Scene(root, 1500, 1000); //Set Windows size once app is run.
        primaryStage.setScene(scene);
        scene.getStylesheets().add
                (Main.class.getResource("style.css").toExternalForm()); //set CSS File.
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}


//CSS https://moytime-blog.tumblr.com/post/58358930290/have-some-cloudy-backgrounds-message-me-if-you
//https://www.vectorstock.com/royalty-free-vector/seamless-sky-vector-20012000
