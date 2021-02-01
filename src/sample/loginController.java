package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class loginController {

    @FXML
    public PasswordField password;
    public TextField username;
    public Label status;

    public void initialize(){

        //option to press enter to login
        //after password input
        password.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    try {
                        signIn();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void Login(MouseEvent mouseEvent) throws IOException {
        signIn();
    }

    public void signIn() throws IOException {
        if(username.getText().equals("user") && password.getText().equals("pass")){
            status.setText("Success");

            /*if(T instance of student){
                    load student view fxml (pass matched student)
              } else{
                    load admin view fxml
                    pass matched admin
                }
             */


            //open student view
            try {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../sample/studentView.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Student View");
                stage.setScene(new Scene(root1));
                stage.getIcons().add(new Image("file:assets/icon.png"));
                stage.show();
                //close login
                Stage thisStage = (Stage) status.getScene().getWindow();
                thisStage.close();

            }catch(Exception e){

                System.out.println(e);
                System.out.println("Cant load window");
            }



        }
        else{
            status.setText("Failed login");
            username.clear();
            password.clear();
        }
    }

    /** text limit option */
    public static void textLimit(final TextField tf, final int max) {
        if (tf.getText().length() > max) {
            int pos = tf.getCaretPosition();
            tf.setText(tf.getText(0, max));
            tf.positionCaret(pos);
        }
    }




}