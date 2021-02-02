package login;

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

import java.io.*;
import java.util.*;
import student.*;

public class loginController {

    @FXML
    public PasswordField password;
    public TextField username;
    public Label status;

    public List<Student> students = new ArrayList<>();
    public FileHandler data = new FileHandler();

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

        students = data.openStudentsList();
    }

    public void Login(MouseEvent mouseEvent) throws IOException {
        signIn();
    }

    public void signIn() throws IOException {
        Boolean success = false;

        if (username.getText().equals("admin") && password.getText().equals("pass1")) {
            status.setText("Success");
            success = true;

            //open admin view
            try {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../admin/adminView.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Admin View");
                stage.setScene(new Scene(root1));
                stage.getIcons().add(new Image("file:assets/icon.png"));
                stage.show();
                //close login
                Stage thisStage = (Stage) status.getScene().getWindow();
                thisStage.close();

            } catch (Exception e) {

                System.out.println(e);
                System.out.println("Cant load window");
            }
        } else{

            Student active = new Student();

            //loop and get all passwords and users
            for (Student s : students) {
                if (username.getText().equals(s.getIdNumber()) && password.getText().equals(s.getPassword())) {
                    active = s;
                    success = true;
                    break;
                }
            }

            if(success){
                //open student view
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../student/studentView.fxml"));
                try {
                    Parent root1 = (Parent) fxmlLoader.load();

                    studentController studentView = fxmlLoader.getController();
                    System.out.println(active.getName());
                    studentView.setActive(active);

                    Stage stage = new Stage();
                    stage.setTitle("Student View");
                    stage.setScene(new Scene(root1));
                    stage.getIcons().add(new Image("file:assets/icon.png"));
                    stage.show();

                    //close login
                    Stage thisStage = (Stage) status.getScene().getWindow();
                    thisStage.close();

                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("Cant load window");
                }
            }

        }

        if(!success){
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