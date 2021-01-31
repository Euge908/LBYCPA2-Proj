

package sample;

import java.util.*;


class superNode {
    String name;
}


public class studentTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Student st1 = new Student("felix", "11926171", 18);
//        System.out.println("Enter your name");
//        st1.setName(sc.nextLine());
//        System.out.println(st1.name);
//        System.out.println("Enter your ID number");
//        st1.setName(sc.nextLine());
//        System.out.println("Enter max units");
//        st1.setMaxUnits(sc.nextInt());
//        System.out.println("max units is "+ st1.maxUnits);

        st1.addSchedule("fndckt", "08:00-10:00,M", checkUnits("fndckt"));
        st1.addSchedule("caleng2", "08:00-10:00,T", checkUnits("caleng2"));
        st1.addSchedule("engphy", "08:00-10:00,W", checkUnits("engphy"));
        st1.showSchedule();
        System.out.println("Subject List");
        st1.DisplaySubjectList();
        st1.delete("fndckt");


        //after entering all details create the graph.

//        graph g= new graph(st1);


    }

    private static void assignHashMap() {
        HashMap<String, String[]> scheduleAssignment = new HashMap<String, String[]>();

    }

    /**
     * Check how many units in a subject
     *
     * @param subject string subject
     * @return number of unit
     */
    private static int checkUnits(String subject) {
        if (subject.toLowerCase().contains("ge")) {
            return 3;
        } else if (subject.toLowerCase().contains("eng")) {
            return 3;
        } else if (subject.toLowerCase().contains("fnd")) {
            return 3;
        } else if (subject.toLowerCase().contains("pro")) {
            return 2;

        } else if (subject.toLowerCase().contains("diff")) {
            return 3;

        } else if (subject.toLowerCase().contains("dat")) {
            return 1;

        } else if (subject.toLowerCase().contains("dis")) {
            return 3;

        } else if (subject.toLowerCase().contains("fund")) {
            return 3;

        } else if (subject.toLowerCase().contains("num")) {
            return 3;

        } else if (subject.toLowerCase().contains("sof")) {
            return 3;

        } else if (subject.toLowerCase().contains("dig")) {
            return 3;

        } else if (subject.toLowerCase().contains("log")) {
            return 3;

        } else if (subject.toLowerCase().contains("oper")) {
            return 3;

        } else if (subject.toLowerCase().contains("mic")) {
            return 3;

        } else if (subject.toLowerCase().contains("re")) {
            return 3;

        } else if (subject.toLowerCase().contains("fdc")) {
            return 3;

        } else if (subject.toLowerCase().contains("com")) {
            return 3;

        } else if (subject.toLowerCase().contains("emb")) {
            return 3;

        } else if (subject.toLowerCase().contains("lca")) {
            return 3;

        } else if (subject.toLowerCase().contains("mx")) {
            return 3;

        } else if (subject.toLowerCase().contains("cpe")) {
            return 2;

        } else if (subject.toLowerCase().contains("och")) {
            return 3;

        } else if (subject.toLowerCase().contains("lce")) {
            return 3;

        } else if (subject.toLowerCase().contains("emer")) {
            return 3;

        } else if (subject.toLowerCase().contains("con")) {
            return 3;

        } else if (subject.toLowerCase().contains("ths")) {
            return 1;

        } else if (subject.toLowerCase().contains("ecn")) {
            return 3;

        } else if (subject.toLowerCase().contains("lby")) {
            return 1;
        } else if (subject.toLowerCase().contains("lby")) {
            return 1;
        }
        return -1;
    }


}

