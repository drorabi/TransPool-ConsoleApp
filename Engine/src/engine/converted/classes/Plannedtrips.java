package engine.converted.classes;

import engine.exceptions.InvalidRouteThroughTheStationTwice;

import java.util.*;

public class Plannedtrips {

    protected Map<Integer, Trip> trips;

    public Plannedtrips(Map<Integer, Trip> trips) throws InvalidRouteThroughTheStationTwice{
        setTrips(trips);
    }

    //setters---------------------------------------------
    private void setTrips(Map<Integer, Trip> trips) throws InvalidRouteThroughTheStationTwice {
        for (Map.Entry<Integer, Trip> entry : trips.entrySet()) {
            checkTrip(entry.getValue());
        }
        this.trips=trips;
    }

    //getters--------------------------------------

    public Map<Integer, Trip> getTrips() {
        return trips;
    }


    //checkers--------------------------------------

    private void checkTrip(Trip single_trip) throws InvalidRouteThroughTheStationTwice {
        Set<String> checkSet = new HashSet<>();
        int single_station;
        for(single_station=0 ; single_station < single_trip.getRoute().length ; single_station++){
            if(checkSet.contains(single_trip.getRoute()[single_station]))
                throw new InvalidRouteThroughTheStationTwice(single_trip.getOwner(),single_trip.getRoute()[single_station]);
            checkSet.add(single_trip.getRoute()[single_station]);
        }
    }

//toString------------------------------------------------
    @Override
    public String toString() {
        String toString = "Planned trips:\n" ;
        for (Map.Entry<Integer, Trip> entry : trips.entrySet()) {
            toString = toString + entry.getValue().toString() + "\n";
        }
        return toString;
    }


}