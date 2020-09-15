package engine.converted.classes;

import engine.exceptions.InvalidRideStartMinutes;
import engine.exceptions.InvalidRideStartDay;
import engine.exceptions.InvalidRideStartHour;
import engine.schema.generated.Scheduling;

public class Schedule {

    //protected int startDay;
    protected int startHour;
    protected int startMinutes;
    //protected int endDay;
    protected int endHour;
    protected int endMinutes;
    protected String recurrences;


    Schedule(Scheduling scheduling,/* int endDay,*/ int endHour, int endMinutes)  {

        // setDay(scheduling.getDayStart());
        setStartHour(scheduling.getHourStart());
        recurrences = scheduling.getRecurrences();
        startMinutes = 0;
        //this.endDay=endDay;
        this.endHour = endHour;
        this.endMinutes = endMinutes;

    }

    public Schedule(/*int day,*/ int hour, String recurrences, int minutes, int endHour, int endMinutes)  {
        /* setDay(day);*/
        setStartHour(hour);
        this.recurrences = recurrences;
        setStartMinutes(minutes);
        this.endHour = endHour;
        this.endMinutes = endMinutes;
    }

    //setters---------------

    public void setStartMinutes(int startMinutes){
        this.startMinutes = startMinutes;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public void setEndMinutes(int endMinutes) {
        this.endMinutes = endMinutes;
    }
    /*  private void setDay(Integer dayStart) throws InvalidRideStartDay {
        if (dayStart > 7 || dayStart < 1)
            throw new InvalidRideStartDay();
        startDay = dayStart;
    }*/

    public void setStartHour(Integer hourStart)  {
        startHour = hourStart;
    }

    //getters----------------

    /*public int getStartDay() {
        return startDay;
    }*/

    public int getStartHour() {
        return startHour;
    }

    public String getRecurrences() {
        return recurrences;
    }

    public int getStartMinutes() {
        return startMinutes;
    }

   /* public int getEndDay() {
        return endDay;
    }*/

    public int getEndHour() {
        return endHour;
    }

    public int getEndMinutes() {
        return endMinutes;
    }

    @Override
    public String toString() {
        String toString = "";
        if(startHour>-1 && startMinutes>-1) {
            if (startMinutes < 10)
                toString = /*" " + dayToSring() +*/  "Heading at " + startHour + ":0" + startMinutes + ", ";
            else
                toString =/*" " + dayToSring() +*/  "Heading at " + startHour + ":" + startMinutes + ", ";
        }

        if (endHour > -1 && endMinutes > -1) {
            if (endMinutes < 10)
                toString = toString + "arrival to last station at " + endHour + ":0" + endMinutes;
            else
                toString = toString + "arrival to last station at " + endHour + ":" + endMinutes;
        }
        return toString + ", " +recurrences;
    }

   /* private String dayToSring() {
        String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return weekDays[startDay - 1];
    }*/
}
