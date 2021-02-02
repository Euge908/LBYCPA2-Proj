package login;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import student.*;

/** File I/O */
public class FileHandler {

    public static HashMap<String, List<String>> openCourseList() {
        HashMap<String,List<String>> timeSlots = new HashMap<>();

        try
        {
            File file = new File("assets/courses.txt");
            Scanner read = new Scanner(file);

            //for each line
            while (read.hasNextLine()) {

                //split string and create the person
                //and add to graph
                List<String> slot = Arrays.asList(read.nextLine().toUpperCase().split("\\|"));
                System.out.println(slot);

                timeSlots.put(slot.get(0),new ArrayList<>());
                for(int i = 1;i<slot.size();i++){
                    timeSlots.get(slot.get(0)).add(slot.get(i).toUpperCase());
                }
            }
            read.close();
        }
        catch (Exception e) { e.printStackTrace();}

        return timeSlots;
    }

    public static List openStudentsList(){
        List<Student> students = new ArrayList<>();
        try
        {
            File file = new File("assets/students.txt");
            Scanner read = new Scanner(file);

            //for each line
            while (read.hasNextLine()) {

                //split string and create the person
                //and add to graph
                List<String> profile = Arrays.asList(read.nextLine().split("\\|"));
                HashMap<String,String> subjects = new HashMap<>();

                Student student = new Student(profile.get(0),profile.get(1),profile.get(2),profile.get(3),profile.get(4),Integer.parseInt(profile.get(5)), Integer.parseInt(profile.get(6)));
                for(int i = 7;i<profile.size();i++){
                    String[] subj = profile.get(i).split(">");
                    subjects.put(subj[0],subj[1]);
                }
                student.setSchedule(subjects);
                students.add(student);
            }
            read.close();
        }
        catch (Exception e) { e.printStackTrace();}

        return students;
    }



    /** exports*/

    public static void writeStudentList(List<Student> students) {
        try
        {
            FileWriter fileWrite = new FileWriter("assets/students.txt");

            //for each person on the graph
            for (Student student : students) {
                ArrayList<String> data = new ArrayList<String>();
                data.add(student.name);
                data.add(student.email);
                data.add(student.password);
                data.add(student.idNumber);
                data.add(student.pic);
                data.add(String.valueOf(student.currentUnits));
                data.add(String.valueOf(student.maxUnits));
                //loop thorough linked list and get name and time and append
                //subject format subject.name>subject.time
                for (Map.Entry<String,String> subject : student.getSchedule().entrySet()) {
                    data.add(subject.getKey() + ">" + subject.getValue());
                }

                ///convert  array to string and add | delimiter
                StringBuilder sb = new StringBuilder();
                for (String s : data) {
                    sb.append(s);
                    sb.append("|");
                }
                if(sb.charAt(sb.length()-1) == '|'){
                    sb.deleteCharAt(sb.length()-1);
                }

                fileWrite.write(sb.toString());
                if(!student.getName().equalsIgnoreCase(students.get(students.size()-1).getName())){
                    fileWrite.write("\n");
                }

            }
            fileWrite.close();
        }
        catch (Exception e) { e.printStackTrace();}
    }

    public static void writeCourseList(HashMap<String,List<String>> timeSlots) {
        try
        {
            FileWriter fileWrite = new FileWriter("assets/courses.txt");

            //for each person on the graph
            int i = 0;
            for(Map.Entry<String,List<String>> subject : timeSlots.entrySet()){
                StringBuilder sb = new StringBuilder();
                sb.append(subject.getKey()+"|");
                for (String s : subject.getValue()) {
                    sb.append(s);
                    sb.append("|");
                }
                if(sb.charAt(sb.length()-1) == '|'){
                    sb.deleteCharAt(sb.length()-1);
                }
                i++;

                fileWrite.write(sb.toString());
                if(i != timeSlots.size()){
                    fileWrite.write("\n");
                }

            }

            fileWrite.close();
        }
        catch (Exception e) { e.printStackTrace();}
    }
}
