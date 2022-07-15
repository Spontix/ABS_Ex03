package desktop;

import absController.ABSController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

//
public class DesktopApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        primaryStage.setTitle("Players Manager");
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("MyGeneralView.fxml");
        fxmlLoader.setLocation(url);
        Parent banksRoot = fxmlLoader.load(url.openStream());

        ABSController absController=fxmlLoader.getController();
        Scene scene = new Scene(banksRoot);
        absController.getSkinMenuButton().setOnAction(e->{
         scene.getStylesheets().setAll(getClass().getResource("FullPackStyling.css").toExternalForm());
         absController.getSkinsMenuButton().setText(absController.getSkinMenuButton().getText());
        });
        absController.getDefaultSkinMenuButton().setOnAction(e->{
            scene.getStylesheets().setAll(getClass().getResource("Default.css").toExternalForm());
            absController.getSkinsMenuButton().setText(absController.getDefaultSkinMenuButton().getText());
        });
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
