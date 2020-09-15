package engine.converted.classes;

import engine.exceptions.InvalidRideStartDay;
import engine.exceptions.InvalidRideStartHour;
import engine.schema.generated.Route;
import engine.schema.generated.Scheduling;
import engine.schema.generated.TransPoolTrip;

import java.util.*;

public class Trip {
    protected int SerialNumber;
    protected String owner;
    protected int capacity;
    protected int ppk;
    protected String[] route;
    protected Schedule schedule;
    protected Map<String, String> rideInformation;
    protected Station[] ride;
    protected Set<Request> poolers;
    int price;
    int fuelConsumption;

    Trip(TransPoolTrip transPoolTrip, int serialNumber, Station[] ride, int price, int fuelConsumption) throws InvalidRideStartDay, InvalidRideStartHour {
        setRoute(transPoolTrip.getRoute());
        this.ride = ride;
        owner = transPoolTrip.getOwner();
        ppk = transPoolTrip.getPPK();
        capacity = transPoolTrip.getCapacity();
        SerialNumber = serialNumber;
        setSchedule(transPoolTrip.getScheduling(), transPoolTrip.getOwner());
        rideInformation = new HashMap<>();
        poolers = new HashSet<>();
        this.price = price;
        this.fuelConsumption=fuelConsumption;
    }

    public Trip(Trip trip, Schedule schedule) {
        this.ppk = trip.ppk;
        this.owner = trip.owner;
        this.ride = trip.ride;
        this.capacity = trip.capacity;
        this.route = trip.route;
        this.schedule = schedule;
        this.SerialNumber = trip.SerialNumber;
        this.price=trip.price;
        this.fuelConsumption=trip.fuelConsumption;
        rideInformation = new HashMap<>();
        this.poolers = trip.poolers;
    }

    public Trip(String name, int serialNumber, Station[] ride, int price, int fuel, int ppk, String[] way, int minutes, int hour, int capacity) {
        this.owner=name;
        this.SerialNumber=serialNumber;
        this.ppk=ppk;
        this.ride=ride;
        this.route=way;
        schedule=new Schedule(/*int day,*/ hour,"One Time", minutes,  ride[ride.length-1].getHour(), ride[ride.length-1].getMinutes());
        rideInformation = new HashMap<>();
        poolers = new HashSet<>();
        this.price = price;
        this.fuelConsumption=fuel;
        this.capacity=capacity;
    }

    //setter---------------------

    private void setSchedule(Scheduling scheduling, String name) throws InvalidRideStartHour, InvalidRideStartDay {
        checkTime(scheduling.getHourStart(),/*scheduling.getDayStart(),*/ name);
        schedule = new Schedule(scheduling, ride[ride.length-1].getHour(), ride[ride.length-1].getMinutes());
    }

    private void checkTime(int hourStart,/* Integer dayStart,*/ String name) throws InvalidRideStartDay, InvalidRideStartHour {
        if(hourStart>23 || hourStart<0)
            throw new InvalidRideStartHour(name);
      /*  if(dayStart>7 || dayStart<1)
            throw new InvalidRideStartDay(owner);*/

    }

    private void setRoute(Route route) {
        this.route = route.getPath().trim().toUpperCase().split(",");
    }

    //getters---------------


    public Set<Request> getPoolers() {
        return poolers;
    }

    public Map<String, String> getRideInformation() {
        return rideInformation;
    }

    public String[] getRoute() {
        return route;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getPpk() {
        return ppk;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public String getOwner() {
        return owner;
    }

    public int getSerialNumber() {
        return SerialNumber;
    }

    public Station[] getRide() {
        return ride;
    }

    @Override
    public String toString() {
        return "\nRide number " + SerialNumber + ":\nOwner:" + owner + ", Schedule to:"
                + schedule.toString() + "\n" + poolersToString() + "route:\n" + rideToString()
                + capacityToString() + "Price for the whole trip: " + price + "\n"
                + "Average Fuel Consumption: " + fuelConsumption;
    }

    private String rideToString() {
        int station;
        String toString = "Stations: \n";
        for (station = 0; station < ride.length; station++) {
            toString = toString + ride[station].toString();
            if (rideInformation.containsKey(route[station] + "pick"))
                toString = toString + rideInformation.get(route[station] + "pick");
            if (rideInformation.containsKey(route[station] + "drop"))
                toString = toString + rideInformation.get(route[station] + "drop");
            toString = toString + "\n";
        }
        return toString;
    }

    public void updateTrip(Request request) {
        if (rideInformation.containsKey(request.getFrom() + "pick")) {
            String temp = rideInformation.get(request.getFrom() + "pick");
            rideInformation.remove(request.getFrom() + "pick");
            rideInformation.put(request.getFrom() + "pick", temp + ", " + request.getName());
        } else
            rideInformation.put(request.getFrom() + "pick", " picking up " + request.getName());

        if (rideInformation.containsKey(request.getTo() + "drop")) {
            String temp = rideInformation.get(request.getTo() + "drop");
            rideInformation.remove(request.getTo() + "drop");
            rideInformation.put(request.getTo() + "drop", temp + ", " + request.getName());
        } else
            rideInformation.put(request.getTo() + "drop", " dropping " + request.getName());

        this.capacity--;
        this.poolers.add(request);
    }

    private String capacityToString() {
        if (capacity == 0)
            return "There is no room in this ride\n";
        else
            return "There is room for another " + capacity + " passengers\n";
    }

    private String poolersToString() {
        if (poolers.isEmpty())
            return "";
        String toString = "Poolers:\n";
        for (Request single_request : poolers)
            toString = toString + single_request.serialNumber + ", " + single_request.getName() + "\n";
        return toString;
    }

}
