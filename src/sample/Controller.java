package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Controller {

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
                    signIn();
                }
            }
        });
    }

    public void Login(MouseEvent mouseEvent) {
        signIn();
    }

    public void signIn(){
        if(username.getText().equals("user") && password.getText().equals("pass")){
            status.setText("Success");
            /*if(T instance of student){
                    load student view fxml (pass matched student)
              } else{
                    load admin view fxml
                    pass matched admin
                }
             */

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
