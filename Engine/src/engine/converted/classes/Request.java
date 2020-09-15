package engine.converted.classes;

import engine.exceptions.InvalidRideStartMinutes;
import engine.exceptions.InvalidRideStartDay;
import engine.exceptions.InvalidRideStartHour;

public class Request {
    protected String name;
    protected String from;
    protected String to;
    protected Schedule schedule;
    protected int serialNumber;
    protected int price;
    protected int fuelConsumption;
    protected boolean matched;
    protected Trip matchedTrip;
    protected boolean byDeparture;

    Request(String name, String from, String to, String recurrences,/*int day,*/ int hour, int minutes, int serialNumber,boolean byDeparture) throws InvalidRideStartDay, InvalidRideStartHour, InvalidRideStartMinutes {
        this.name = name;
        this.from = from;
        this.to = to;
        this.byDeparture=byDeparture;
        setSchedule(/*int day,*/ hour, recurrences, minutes);
        this.serialNumber = serialNumber;
        price = 0;
        this.fuelConsumption = 0;
        matched = false;
        matchedTrip =null;
    }

    //setter---------------------

private void setSchedule(int hour, String recurrences, int minutes) {
        if(byDeparture)
            this.schedule=new Schedule(/*int day,*/  hour, recurrences,  minutes, -1, -1);
        else
            this.schedule=new Schedule(/*int day,*/  -1, recurrences,  -1, hour, minutes);

}
    //getters---------------


    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getName() {
        return name;
    }

    public Schedule getScheduling() {
        return schedule;
    }

    public int getFuelConsumption() {
        return fuelConsumption;
    }

    public int getPrice() {
        return price;
    }

    public boolean getMatched() {
        return matched;
    }

    public boolean isByDeparture() {
        return byDeparture;
    }

    public void Update(int price, int fuel, Trip trip) {
        Station temp;
        this.price = price;
        this.fuelConsumption = fuel;
        this.matchedTrip=trip;
        if(byDeparture) {
            temp = findDestinatin();
            schedule.setEndHour(temp.getHour());
            schedule.setEndMinutes(temp.getMinutes());
        }
        else{
            temp = findDeparture();
            schedule.setStartHour(temp.getHour());
            schedule.setStartMinutes(temp.getMinutes());
        }
        matched = true;
    }




    @Override
    public String toString() {
        String toString = "Request number " + serialNumber + ":\nName:" + name
                + ", Schedule to:" + schedule.toString() + "\nfrom "
                + from + " to " + to + "\n";
        if (matched)
            toString = toString + "this request have been matched to " + matchedTrip.getOwner()
                    + "'s trip\n" + "trip number:" + matchedTrip.getSerialNumber()
                    + "\n" + "price: " + price + "\n" + "Average Fuel Consumption: " + fuelConsumption + "\n";

        return toString;
    }

    private Station findDestinatin(){
        int i;
        for(i=0; i<matchedTrip.getRide().length;i++){
            if(matchedTrip.getRide()[i].getName().equals(to))
                return matchedTrip.getRide()[i];
        }
        return null;
    }

    private Station findDeparture() {
        int i;
        for(i=0; i<matchedTrip.getRide().length;i++){
            if(matchedTrip.getRide()[i].getName().equals(from))
                return matchedTrip.getRide()[i];
        }
        return null;
    }

}
