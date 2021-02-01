package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

//for testing purposes only
public class scratch {
    public static void main(String[] args) {
//        System.out.println(String.valueOf(22));
//        Student st1 = new Student("felix", "felix@dlsu.edu.ph", "pass1", "11926171", 18);
//        st1.setName("felix");
//        LinkedList<Subject> temp = new LinkedList<Subject>();
//        temp.add(new Subject("phys", "09:00-10:00,MW"));
//        temp.add(new Subject("subject2", "07:00-9:00,MW"));
//        temp.add(new Subject("subject3", "07:00-9:00,TH"));

//        saveToText(st1, temp);



//        System.out.println(studentList);
//        String[] typeArray = studentList.get(3).split(" ");

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
//        System.out.println(users);
//        System.out.println(pass);
        System.out.println(studentList);




    }

    static void saveToText(Student st, LinkedList<Subject> subjects) {
        //array to store the csv string
        //FORMAT: name,idNum,currentUnits,maxUnits,subjects...
        ArrayList<String> data = new ArrayList<String>();
        data.add(st.name);
        data.add(st.email);
        data.add(st.password);
        data.add(st.idNumber);
        data.add(String.valueOf(st.currentUnits));
        data.add(String.valueOf(st.maxUnits));
        //loop thorough linked list and get name and time and append
        //subject format subject.name>subject.time
        for (Subject subject : subjects) {
            data.add(subject.name + ">" + subject.time);
        }

        ///convert  array to string and add | delimiter
        StringBuilder sb = new StringBuilder();
        for (String s : data) {
            sb.append(s);
            sb.append("|");
        }

        System.out.println(sb.toString());
        appendStrToFile("src/sample/test.txt", sb.toString());


    }

    static void appendStrToFile(String path, String str) {
        try {

            File file = new File(path);
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            PrintWriter writer = new PrintWriter(br);
            writer.println(str);
            writer.close();


        } catch (IOException i) {
            i.printStackTrace();
        }


    }
    public static ArrayList<String> storeArray() {
        BufferedReader bufReader = null;
        try {
            bufReader = new BufferedReader(new FileReader("src/sample/students.txt"));
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




}