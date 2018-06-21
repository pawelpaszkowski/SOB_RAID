package application;

import io.Raport;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(this.getClass().getResource("mainContainer.fxml"));
			AnchorPane anchorPane= loader.load();
			Scene scene = new Scene(anchorPane);
			
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("RAID 5");
			primaryStage.getIcons().add(new Image("file:resources/raid.png"));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}

    @Override
    public void stop(){
        Raport.closeFile();
    }
}
