package engine.converted.classes;

import engine.exceptions.*;
import engine.schema.generated.MapDescriptor;
import engine.schema.generated.TransPool;
import engine.schema.generated.TransPoolTrip;

import java.util.HashMap;
import java.util.Map;

// JAXB converted TransPool
public class Transpool {
    protected MapData mapData;
    protected Plannedtrips plannedTrips;
    protected Map<Integer, Request> requests;
    public static int serialNumberCounter =999;

    public Transpool(TransPool transPool) throws InvalidPathNames, InvalidMap, InvalidPathDepatureDestination,
            InvalidStationsNames, InvalidStationsCoordinates, InvalidRoute, InvalidStationsLocation, InvalidRideStartDay, InvalidRideStartHour, InvalidRouteThroughTheStationTwice {

        setMapData(transPool.getMapDescriptor());
        setPlannedtrips(transPool.getPlannedTrips());
        setRequests();
    }

    //setters------------

    public void setPlannedtrips(engine.schema.generated.PlannedTrips plannedTrips) throws InvalidRoute, InvalidRideStartDay, InvalidRideStartHour, InvalidRouteThroughTheStationTwice {
        Map<Integer, Trip> trips= new HashMap<>();
        for (TransPoolTrip single_trip : plannedTrips.getTransPoolTrip()) {
            int price=0;
            int fuel=0;
            Station[] route= checkRide(single_trip);
            price = priceCalculator(route,single_trip.getPPK());
            fuel =fuelConsumptionCalculator(route);
            int serialNumber=++Transpool.serialNumberCounter;
            trips.put(serialNumber,new Trip(single_trip,serialNumber,route, price,fuel));
        }
        this.plannedTrips = new Plannedtrips(trips);
    }



    private void setRequests() {
        requests = new HashMap<>();
    }

    private void setMapData(MapDescriptor oldMap) throws InvalidStationsLocation, InvalidStationsNames, InvalidMap, InvalidPathNames,
            InvalidStationsCoordinates, InvalidPathDepatureDestination {
        this.mapData = new MapData(oldMap);
    }

    //checker-------------------

    private Station[] checkRide(TransPoolTrip trip) throws InvalidRoute {
        int minutes = 0;
        int hour = trip.getScheduling().getHourStart();
        String[] temp = trip.getRoute().getPath().toUpperCase().split(",");
        String ride[]=deleteSpacesFromString(temp);
        Station[] route = new Station[ride.length];
        int station;
        for (station = 0; station < ride.length - 1; station++) {
            if (!mapData.getTrails().getTrails().containsKey(ride[station] + ride[station + 1])) {
                if (!mapData.getTrails().getTrails().containsKey(ride[station + 1] + ride[station])
                        || mapData.getTrails().getTrails().get(ride[station + 1] + ride[station]).getOneWay())
                    throw new InvalidRoute(trip.getOwner(), ride[station] + " to " + ride[station + 1]);
            }
            route[station] = new Station(mapData.getStations().getStations().get(ride[station]),hour ,minutes );
            if (mapData.getTrails().getTrails().containsKey(ride[station] + ride[station + 1]))
                minutes = minutes + mapData.getTrails().getTrails().get(ride[station] + ride[station + 1]).getHowMuchTime();
            else
                minutes = minutes + mapData.getTrails().getTrails().get(ride[station + 1] + ride[station]).getHowMuchTime();
            if(minutes>=60){
                hour=setHour(minutes,hour);
                minutes=minutes%60;
            }

        }
        route[station] = new Station(mapData.getStations().getStations().get(ride[station]),hour , minutes );
        return route;
    }

    private int setHour(int minutesToAdd,int time){
        int hours = minutesToAdd / 60;
        return ((hours+time)%24);
    }

    private void checkRideRequest(String from, String to) throws InvalidRequestDepartureDestination {
        if (!mapData.stations.getStations().containsKey(from))
            throw new InvalidRequestDepartureDestination(from);

        if (!mapData.stations.getStations().containsKey(to))
            throw new InvalidRequestDepartureDestination(to);
    }

    private String[] deleteSpacesFromString(String[] route){
        int i;
        for(i=0 ; i < route.length ; i++){
            route[i]=route[i].trim();
        }
        return route;
    }


    public void addRequest(String name, String from, String to, /*String recurrences, int day,*/ int hour, int minutes, boolean byDeparture) throws InvalidRideStartMinutes, InvalidRideStartHour, InvalidRideStartDay, InvalidRequestDepartureDestination {
        checkRideRequest(from, to);
        serialNumberCounter++;
        requests.put(serialNumberCounter,new  Request(name, from, to, "One Time",/*day,*/ hour,minutes, serialNumberCounter,byDeparture));
    }

    //getters-----------------------

    public MapData getMapData() {
        return mapData;
    }

    public Plannedtrips getPlannedTrips() {
        return plannedTrips;
    }

    public Map<Integer, Request> getRequests() {
        return requests;
    }


    public void UpdateRequest(int serialNumber, int price, int fuel, Trip trip){
        Request temp = requests.get(serialNumber);
        temp.Update(price,fuel,trip);
    }

    public void addTrip(String name, int hour, int minutes, String route, int ppk, int capacity) {
        String[] way=route.split(",");
        Station[] ride= makeStationArray(way,hour,minutes);
        int price = priceCalculator(ride,ppk);
        int fuel =fuelConsumptionCalculator(ride);
        int serialNumber=++Transpool.serialNumberCounter;
        plannedTrips.getTrips().put(serialNumber,new Trip(name,serialNumber,ride, price,fuel,ppk,way, minutes,hour, capacity));
    }

    private Station[] makeStationArray(String[] way, int hour, int minutes) {
        Station[] ride=new Station[way.length];
        int minutesToAdd=minutes;
        int newHour=hour;
        for(int i=0; i< way.length-1 ; i++) {
            ride[i] = new Station(mapData.getStations().getStations().get(way[i]), newHour,minutesToAdd );
            if (mapData.getTrails().getTrails().containsKey(way[i] + way[i + 1]))
                minutesToAdd = minutesToAdd + mapData.getTrails().getTrails().get(way[i] + way[i + 1]).getHowMuchTime();
            else
                minutesToAdd = minutesToAdd + mapData.getTrails().getTrails().get(way[i + 1] + way[i]).getHowMuchTime();

            newHour = (newHour + (minutesToAdd / 60))%24;
            minutesToAdd = minutesToAdd % 60;
        }
        ride[way.length-1] = new Station(mapData.getStations().getStations().get(way[way.length-1]), newHour,minutesToAdd );
            return ride;
    }
    private int fuelConsumptionCalculator(Station[] route) {
        int i, length=0;
        double  fuel=0;
        for(i=0 ; i< route.length -1 ; i++){
            if (mapData.getTrails().getTrails().containsKey(route[i].getName() + route[i + 1].getName())) {
                fuel+= mapData.getTrails().getTrails().get(route[i].getName() + route[i + 1].getName()).getFuelUse();
                length = length + mapData.getTrails().getTrails().get(route[i].getName() + route[i + 1].getName()).getLength();
            }
            else {
                fuel += mapData.getTrails().getTrails().get(route[i + 1].getName() + route[i].getName()).getFuelUse();
                length = length + mapData.getTrails().getTrails().get(route[i + 1].getName() + route[i].getName()).getLength();
            }
        }
        if(fuel==0)
            return 0;
        return (int)(length/fuel);
    }

    private int priceCalculator(Station[] route, int ppk) {
        int i, price=0;
        for(i=0 ; i< route.length -1 ; i++){
            if (mapData.getTrails().getTrails().containsKey(route[i].getName() + route[i + 1].getName()))
                price = price + mapData.getTrails().getTrails().get(route[i].getName() + route[i + 1].getName()).getLength()*ppk;
            else
                price = price + mapData.getTrails().getTrails().get(route[i + 1].getName() + route[i].getName()).getLength()*ppk;
        }

        return price;
    }

}

