package sample;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class FileHandler {

    private void openList(String path) {
        try
        {
            File file = new File(path);
            Scanner read = new Scanner(file);

            //for each line
            while (read.hasNextLine()) {

                /**suggested format: name|id|maxunits|currentunits|tuiton|subj1:slot/subj2:slot/subj3:slot.../subjn:slot */

                //separate each info (name,id,maxunits,currentunits,tuition,subjects
                String profile[] = read.nextLine().split("\\|");

                for(int i =0;i<profile.length;i++){
                    //edit this
                    //Student student = new Student(profile[i]);
                }

            }
            read.close();
        }
        catch (Exception e) { e.printStackTrace();}
    }

    //edit parameter : path, list of students
    private void writeList(String path) {
        try
        {
            FileWriter fileWrite = new FileWriter(path);

            /*
            for each student in list of students {

                //separate each info with delimeter
                for(int i = 0; i< student.info().size(); i++)
                {
                    fileWrite.write(student.info().get(i));
                    if(i!=student.info().size()-1){
                        fileWrite.write("|");
                    }
                }

                //avoid extra "\n" after last student
                if(student != list of student.getLast()) {
                    fileWrite.write("\n");
                }
            }
            */
            fileWrite.close();
        }
        catch (Exception e) { e.printStackTrace();}
    }

}
