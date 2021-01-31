package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

//for testing purposes only
public class scratch {
    public static void main(String[] args) {
        System.out.println(String.valueOf(22));
        Student st1 = new Student("felix", "11926171", 18);
        st1.setName("felix");
        LinkedList<Subject> temp = new LinkedList<Subject>();
        temp.add(new Subject("phys", 3, "09:00-10:00,MW"));
        temp.add(new Subject("subject2", 3, "07:00-9:00,MW"));
        temp.add(new Subject("subject3", 3, "07:00-9:00,TH"));

//        String dataString = saveToText(st1, temp);
//        appendStrToFile(dataString);


    }

//    //save to Text
//    static String saveToText(Student st, LinkedList<Subject> subjects) {
//        //array to store the csv string
//        //FORMAT: name,idNum,currentUnits,maxUnits,subjects...
//        ArrayList<String> data = new ArrayList<String>();
//        data.add(st.name);
//        data.add(st.idNumber);
//        data.add(String.valueOf(st.currentUnits));
//        data.add(String.valueOf(st.maxUnits));
//        //loop thorough inked list and get name and time and append
//        //subject format subject.name>subject.time
//        for (Subject subject : subjects) {
//            data.add(subject.name + ">" + subject.time);
//        }
//
//        ///convert  array to string
//        StringBuilder sb = new StringBuilder();
//        for (String s : data) {
//            sb.append(s);
//            sb.append("|");
//        }
//
//        System.out.println(sb.toString());
//        return sb.toString();
//
//
//    }
//
//
//    //append string to text
//    static void appendStrToFile(String str) {
//        try {
////           File f= new File("src/sample/test.txt");
////           FileWriter fw = new FileWriter(f);
////           fw.write(str);
////           fw.close();
//
//
//            File file = new File("src/sample/test.txt");
//            FileWriter fr = new FileWriter(file, true);
//            BufferedWriter br = new BufferedWriter(fr);
//            PrintWriter writer = new PrintWriter(br);
//            writer.println(str);
//            writer.close();
//
//
//        } catch (IOException i) {
//            i.printStackTrace();
//        }
//
//
//    }
}
