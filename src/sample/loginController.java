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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class loginController {


    @FXML
    public PasswordField password;
    public TextField username;
    public Label status;
    public int dummyData = 10;
    public void initialize() throws IOException {

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

    /**
     * stores  test.txt file to an array and returns array
     *
     * @return Array of CSV
     */
    public static ArrayList<String> storeArray() {
        BufferedReader bufReader = null;
        try {
            bufReader = new BufferedReader(new FileReader("src/sample/test.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String> pokeList = new ArrayList<>();
        String line = null;
        try {
            line = bufReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (line != null) {
            pokeList.add(line);
            try {
                line = bufReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            bufReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pokeList;

    }





    public void signIn() throws IOException {
        //gets txt file and stores into an array
        ArrayList<String> studentList = storeArray();
        ArrayList<String> users = new ArrayList<String>();
        ArrayList<String> pass = new ArrayList<String>();

        for (int i=0; i<studentList.size();i++){
            //temp array will be each individual line from text file
            String[] tempArray = studentList.get(i).split("\\|");
            pass.add(tempArray[2]);
            users.add(tempArray[3]);
        }
        System.out.println(users);
        System.out.println(pass);


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