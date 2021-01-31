package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/** pop up class if ever needed
 * (ex. successfully added/failed to add course)
 *  call PopUp.display(String message)
 */

public class PopUp {
    //set popup
    //displays a new small window
    //that will display text with "ok" button

    public static void display(String text){
        Stage popupwindow=new Stage();

        //must deal with this window first
        popupwindow.initModality(Modality.APPLICATION_MODAL);

        Label label= new Label(text); //display text
        label.setAlignment(Pos.CENTER);

        Button button= new Button("Ok"); //ok button to close window
        button.setOnAction(e -> popupwindow.close());

        VBox layout= new VBox(10); //layout of the nodes
        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);

        //set window and show
        Scene scene= new Scene(layout, 300, 100);
        popupwindow.setTitle("Warning");
        popupwindow.setScene(scene);
        popupwindow.showAndWait(); //show until action
    }
}
