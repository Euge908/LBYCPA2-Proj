package sample;

import java.io.*;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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
//        ArrayList<String> studentList = storeArray();
//        ArrayList<String> users = new ArrayList<String>();
//        ArrayList<String> pass = new ArrayList<String>();
//
//        for (int i=0; i<studentList.size();i++){
//            //temp array will be each individual line from text file
//            String[] tempArray = studentList.get(i).split("\\|");
//
//            pass.add(tempArray[2]);
//            users.add(tempArray[3]);
//        }
//        System.out.println(users);
//        System.out.println(pass);
//        String temp = "temp";
//
//        System.out.println(pass.contains("pass1"));
////        System.out.println(studentList);




    }
//    /**
//     * Generates Tuition after enrollment
//     *
//     * @param st1 current student object
//     */
//    private void generateTuition(Student st1) {
//        LocalDate dt = LocalDate.now();
//
//        // multiply current units with multiplier
////        double tuitionFee = st1.currentUnits* tuitionMultiplier;
//        double tuitionFee = 80758;
//
//        double misc = tuitionFee * ((float) 5234 / 68124);
//        double special = tuitionFee * ((float) 200 / 68124);
//        double development = tuitionFee * ((float) 2000 / 68124);
//        double idValid = tuitionFee * ((float) 46 / 68124);
//        double finalFee = tuitionFee + misc +special+development+idValid;
//
//        DecimalFormat numberFormat = new DecimalFormat("#.00");
//        System.out.println(numberFormat.format(misc));
//
//        feeText.setText("FEES\t\t\t\t\tASSESSMENT"
//                + "\n\n" + "Tuition:\t\t\t\t" + tuitionFee
//                + "\n" + "Miscellaneous:\t\t\t" + (numberFormat.format(misc))
//                + "\n" + "Special Fees:\t\t\t" + (numberFormat.format(special))
//                + "\n" + "Development Fees:\t\t" + (numberFormat.format(development))
//                + "\n" + "ID Validation:\t\t\t" + (numberFormat.format(idValid))
//                + "\n\n\n" + "Deadline of Payment w/o Surcharge..................."+ dt.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
//                + "\n" + "Deadline of Payment w/ Surcharge..................."+ dt.with(TemporalAdjusters.lastDayOfMonth())
//                +"\n" +"NOTE: Content is accurate only as of printing date and time. This is not a\n" +
//                "proof of official enrollment and final assessment of tuition and fees.\n" +
//                "Courses will be dropped automatically for unsettled payment.\n" +
//                "For DLSU internal use (payment purposes) only."
//                +"\n\n\n\n" +"ASSESSED AMOUNT:\t\t" +(numberFormat.format(finalFee))
//                +"\n" + "Other Fees:\t\t\t\t"+ "0.00"
//                +"\n" + "PLEASE PAY THIS AMOUNT:\t"+(numberFormat.format(finalFee)));
//
//
//
//    }


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