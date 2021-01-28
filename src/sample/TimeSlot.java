package sample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeSlot {
    SimpleDateFormat format = new SimpleDateFormat("HH:mm");

    /**
     * letters to days: MTWRFS.
     */
    private String days;

    private Date time1;
    private Date time2;



    /** sample format : "MW" , "8:00-10:00" */
    public TimeSlot(String days, String slot) {
        String[] range = slot.split("-");
        this.days = days;
        try {
            this.time1 = format.parse(range[0]);
            this.time2 = format.parse(range[1]);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage(),e);
        }

    }

    /** @return starting time of time slot */
    public Date getStart() {
        return time1;
    }

    /** @return ending time of time slot */
    public Date getEnd() {
        return time2;
    }

    /** @return days */
    public String getDays(){
        return days;
    }



    /**
     * Checks if there is an overlap between this time slot and another time slot
     *
     * @return True if the time slots overlap.
     */
    public boolean conflicts(TimeSlot other) {

        Boolean dateConflict = false;
        for(int i=0;i<this.days.length();i++){
            for(int j=0;j<other.days.length();j++){
                if(this.days.charAt(i) == other.days.charAt(j)){
                    dateConflict = true;
                }
            }
        }
        if(!dateConflict){
            return false;
        }

        if (this.getStart().before(other.getEnd()) && this.getEnd().after(other.getStart())) {
            return true;
        }

        return false;
    }

    /** compare two slots in string format:
     *  Ex. "MW-8:00-10:00"
     *
     * @return
     */

    //format: "MW-8:00-10:00"
    public boolean isOverlap(String slot1, String slot2) {
        String[] sched1 = slot1.split("-");
        String[] sched2 = slot2.split("-");
        Date[] time1 = new Date[2];
        Date[] time2 = new Date[2];

        try {
            time1[0] = format.parse(sched1[1]);
            time1[1] = format.parse(sched1[2]);
            time2[0] = format.parse(sched2[1]);
            time2[1] = format.parse(sched2[2]);

        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage(),e);
        }
        Boolean dateConflict = false;
        for(int i=0;i<sched1[0].length();i++){
            for(int j=0;j<sched2[0].length();j++){
                if(sched1[0].charAt(i) == sched2[0].charAt(j)){
                    dateConflict = true;
                }
            }
        }
        if(!dateConflict){
            return false;
        }
        if (time1[0].before(time2[1]) && time1[1].after(time2[0])) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return format.format(this.time1) + "-" + format.format(this.time2);
    }

    /** test */
    public static void main(String[] args) {
        TimeSlot t1 = new TimeSlot("MW","8:00-10:00");
        TimeSlot t2 = new TimeSlot("T","7:00-8:01");

        if(t1.isOverlap("MW-8:00-10:00","MW-7:00-8:01")){
            System.out.println("there is time conflict");

        }
        else{
            System.out.println("no time conflict");
        }

        System.out.println(t1.getDays()+" "+t1.toString());
        System.out.println(t1.getDays()+" "+t2.toString());
    }
}
