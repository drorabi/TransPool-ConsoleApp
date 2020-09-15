package engine.ui;

import engine.converted.classes.*;
import engine.data.DataLoader;
import engine.exceptions.*;

import javax.xml.bind.JAXBException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Engine {
    static public Transpool data;


    //Load File------------

    public void loadData(String path) throws InvalidMap, InvalidPathDepatureDestination, InvalidRoute,
            InvalidStationsNames, InvalidStationsLocation,
            InvalidStationsCoordinates, InvalidPathNames,
            FileDoesNotExist, NotXmlFile, JAXBException, InvalidRideStartDay, InvalidRideStartHour, InvalidRouteThroughTheStationTwice {
        DataLoader loader = new DataLoader(path);
        loader.load();
    }

    //Displays-----------------------

    public String displayOfferedRides() throws InvalidDisplayNoRides {
        if (data.getPlannedTrips().getTrips().isEmpty())
            throw new InvalidDisplayNoRides();
        return data.getPlannedTrips().toString();
    }

    public String displayNotMatchedRequests() throws InvalidDisplayNoRequests, NoAvailableRequests {
        int counter = 0;
        if (data.getRequests().isEmpty())
            throw new InvalidDisplayNoRequests();
        String toString = "Unmatched requests:\n";
        for (Map.Entry<Integer, Request> entry : data.getRequests().entrySet()) {
            if (!entry.getValue().getMatched()) {
                toString = toString + entry.getValue().toString() + "\n";
                counter++;
            }
        }
        if (counter == 0)
            throw new NoAvailableRequests();
        return toString;
    }

    public String displaydRidesRequests() throws InvalidDisplayNoRequests {
        String toString = "Requests:\n";
        if (data.getRequests().isEmpty())
            throw new InvalidDisplayNoRequests();

        for (Map.Entry<Integer, Request> entry : data.getRequests().entrySet()) {
            toString = toString + entry.getValue().toString() + "\n";
        }
        return toString;
    }


    public String displayAllStations() {
        return data.getMapData().getStations().toString();
    }

    //operations functions------------------

    public void addRideRequest(String name, String from, String to,/* String recurrences, int day,*/ int hour, int minutes, boolean byDeparture) throws InvalidRideStartHour, InvalidRideStartDay, InvalidRideStartMinutes, InvalidRequestDepartureDestination {
        data.addRequest(name, from, to,/*recurrences,  day,*/ hour, minutes, byDeparture);
    }


    public Set<Trip> findMatchingTrips(int serialNumber, int numberOfRides) throws InvalidRequestName, InvalidRideStartHour, InvalidRideStartDay, NoMatchingRides, InvalidRideStartMinutes {
        if (!data.getRequests().containsKey(serialNumber))
            throw new InvalidRequestName(serialNumber);
        if (data.getRequests().get(serialNumber).isByDeparture())
            return findMatchingTripsByDepature(serialNumber, numberOfRides);
        else
            return findMatchingTripsByArrival(serialNumber, numberOfRides);

    }

    public Set<Trip> findMatchingTripsByDepature(int serialNumber, int numberOfRides) throws InvalidRequestName, InvalidRideStartDay, InvalidRideStartHour, InvalidRideStartMinutes, NoMatchingRides {
        int counter = 0;
        Set<Trip> matchingTrips = new HashSet<>();
        Trip single_trip;
        int departure, destination;
        for (Map.Entry<Integer, Trip> entry : data.getPlannedTrips().getTrips().entrySet()) {
            single_trip = entry.getValue();
            for (departure = 0; departure < single_trip.getRoute().length - 1; departure++) {
                int minutesToAdd = single_trip.getRide()[departure].getMinutes();
                int newHour = single_trip.getRide()[departure].getHour();
                if (single_trip.getRoute()[departure].equals(data.getRequests().get(serialNumber).getFrom()) && ((newHour == data.getRequests().get(serialNumber).getScheduling().getStartHour() && ((minutesToAdd) % 60) == data.getRequests().get(serialNumber).getScheduling().getStartMinutes()))) {
                    for (destination = departure + 1; destination < single_trip.getRoute().length; destination++) {
                        if (single_trip.getRoute()[destination].equals(data.getRequests().get(serialNumber).getTo()) && single_trip.getCapacity() > 0) {
                            matchingTrips.add(new Trip(single_trip, new Schedule(newHour, single_trip.getSchedule().getRecurrences(), data.getRequests().get(serialNumber).getScheduling().getStartMinutes(), single_trip.getSchedule().getEndHour(), single_trip.getSchedule().getEndMinutes())));
                            counter++;
                            if (counter == numberOfRides)
                                return matchingTrips;
                            continue;
                        }
                    }
                }
            }
        }
        if (counter == 0)
            throw new NoMatchingRides();
        return matchingTrips;
    }

    public Set<Trip> findMatchingTripsByArrival(int serialNumber, int numberOfRides) throws InvalidRequestName, InvalidRideStartDay, InvalidRideStartHour, InvalidRideStartMinutes, NoMatchingRides {
        int counter = 0;
        Set<Trip> matchingTrips = new HashSet<>();
        Trip single_trip;
        int departure, destination;
        for (Map.Entry<Integer, Trip> entry : data.getPlannedTrips().getTrips().entrySet()) {
            single_trip = entry.getValue();
            for (departure = 0; departure < single_trip.getRoute().length - 1; departure++) {
                if (single_trip.getRoute()[departure].equals(data.getRequests().get(serialNumber).getFrom()) && single_trip.getCapacity() > 0) {
                    for (destination = departure + 1; destination < single_trip.getRoute().length; destination++) {
                        int minutesToAdd = single_trip.getRide()[destination].getMinutes();
                        int newHour = single_trip.getRide()[destination].getHour();
                        if (single_trip.getRoute()[destination].equals(data.getRequests().get(serialNumber).getTo()) && ((newHour == data.getRequests().get(serialNumber).getScheduling().getEndHour() && ((minutesToAdd) % 60) == data.getRequests().get(serialNumber).getScheduling().getEndMinutes()))) {
                            matchingTrips.add(new Trip(single_trip, new Schedule(single_trip.getSchedule().getStartHour(), single_trip.getSchedule().getRecurrences(), data.getRequests().get(serialNumber).getScheduling().getStartMinutes(), newHour, data.getRequests().get(serialNumber).getScheduling().getEndMinutes())));
                            counter++;
                            if (counter == numberOfRides)
                                return matchingTrips;
                            break;
                        }
                    }
                }
            }
        }
        if (counter == 0)
            throw new NoMatchingRides();
        return matchingTrips;
    }


    public void updateTrip(int choice, int request) throws InvalidChoiceForSerialNumber {
        Trip single_trip;
        for (Map.Entry<Integer, Trip> entry : data.getPlannedTrips().getTrips().entrySet()) {
            single_trip = entry.getValue();
            if (single_trip.getSerialNumber() == choice) {
                single_trip.updateTrip(data.getRequests().get(request));
                return;
            }
        }
        throw new InvalidChoiceForSerialNumber(choice);
    }


    public void checkStation(String station) throws InvalidStationsNameRequest {
        if (!data.getMapData().getStations().getStations().containsKey(station))
            throw new InvalidStationsNameRequest(station);
    }

    public void checkTime(int hour, int minutes) throws InvalidRequestMinutes, InvalidRequestHour {
        if (hour > 23 || hour < 0)
            throw new InvalidRequestHour(hour);
        if (minutes > 59 || minutes < 0)
            throw new InvalidRequestMinutes(minutes);
    }

    public void upDateRequest(int requestNummber, int tripNumber) {
        Trip trip = data.getPlannedTrips().getTrips().get(tripNumber);
        int price = caculatePriceForRequest(data.getRequests().get(requestNummber).getFrom(), data.getRequests().get(requestNummber).getTo(), trip);
        int fuel = caculateFuelForRequest(data.getRequests().get(requestNummber).getFrom(), data.getRequests().get(requestNummber).getTo(), trip);
        data.UpdateRequest(requestNummber, price, fuel, trip);
    }

    private int caculateFuelForRequest(String from, String to, Trip trip) {
        int i, leanth = 0;
        double fuelConsumption = 0;
        for (i = 0; i < trip.getRide().length - 1; i++) {
            if (trip.getRide()[i].getName().equals(from)) {
                while (!trip.getRide()[i + 1].getName().equals(to)) {
                    if (data.getMapData().getTrails().getTrails().containsKey(trip.getRide()[i].getName() + trip.getRide()[i + 1].getName())) {
                        fuelConsumption = fuelConsumption + data.getMapData().getTrails().getTrails().get(trip.getRide()[i].getName() + trip.getRide()[i + 1].getName()).getFuelUse();
                        leanth = leanth + data.getMapData().getTrails().getTrails().get(trip.getRide()[i].getName() + trip.getRide()[i + 1].getName()).getLength();
                    } else {
                        fuelConsumption = fuelConsumption + data.getMapData().getTrails().getTrails().get(trip.getRide()[i + 1].getName() + trip.getRide()[i].getName()).getFuelUse();
                        leanth = leanth + data.getMapData().getTrails().getTrails().get(trip.getRide()[i + 1].getName() + trip.getRide()[i].getName()).getLength();
                    }
                    i++;
                }
                if (data.getMapData().getTrails().getTrails().containsKey(trip.getRide()[i].getName() + trip.getRide()[i + 1].getName())) {
                    fuelConsumption = fuelConsumption + data.getMapData().getTrails().getTrails().get(trip.getRide()[i].getName() + trip.getRide()[i + 1].getName()).getFuelUse();
                    leanth = leanth + data.getMapData().getTrails().getTrails().get(trip.getRide()[i].getName() + trip.getRide()[i + 1].getName()).getLength();
                } else {
                    fuelConsumption = fuelConsumption + data.getMapData().getTrails().getTrails().get(trip.getRide()[i + 1].getName() + trip.getRide()[i].getName()).getFuelUse();
                    leanth = leanth + data.getMapData().getTrails().getTrails().get(trip.getRide()[i + 1].getName() + trip.getRide()[i].getName()).getLength();
                }
                if (fuelConsumption == 0)
                    return 0;
                return (int) (leanth / fuelConsumption);
            }
        }
        if (fuelConsumption == 0)
            return 0;
        return (int) (leanth / fuelConsumption);
    }

    private int caculatePriceForRequest(String from, String to, Trip trip) {
        int i, price = 0;
        for (i = 0; i < trip.getRide().length - 1; i++) {
            if (trip.getRide()[i].getName().equals(from)) {
                while (!trip.getRide()[i + 1].getName().equals(to)) {
                    if (data.getMapData().getTrails().getTrails().containsKey(trip.getRide()[i].getName() + trip.getRide()[i + 1].getName()))
                        price = price + data.getMapData().getTrails().getTrails().get(trip.getRide()[i].getName() + trip.getRide()[i + 1].getName()).getLength() * trip.getPpk();
                    else
                        price = price + data.getMapData().getTrails().getTrails().get(trip.getRide()[i + 1].getName() + trip.getRide()[i].getName()).getLength() * trip.getPpk();
                    i++;
                }
                if (data.getMapData().getTrails().getTrails().containsKey(trip.getRide()[i].getName() + trip.getRide()[i + 1].getName()))
                    price = price + data.getMapData().getTrails().getTrails().get(trip.getRide()[i].getName() + trip.getRide()[i + 1].getName()).getLength() * trip.getPpk();
                else
                    price = price + data.getMapData().getTrails().getTrails().get(trip.getRide()[i + 1].getName() + trip.getRide()[i].getName()).getLength() * trip.getPpk();
                return price;
            }
        }
        return price;
    }

    public String displayOptionalStations(String from, String route) throws NoOptionalStationsToGoNext {
        Station departure = data.getMapData().getStations().getStations().get(from);
        String optionalStations = "Optional next stations are:\n";
        for (Map.Entry<String, Station> entry : data.getMapData().getStations().getStations().entrySet()) {
            if (!route.contains(entry.getValue().getName())) {
                if (data.getMapData().getTrails().getTrails().containsKey(from + entry.getValue().getName()))
                    optionalStations = optionalStations + entry.getValue().getName() + "\n";
                if (data.getMapData().getTrails().getTrails().containsKey(entry.getValue().getName() + from) &&
                        (!data.getMapData().getTrails().getTrails().get(entry.getValue().getName() + from).getOneWay()))
                    optionalStations = optionalStations + entry.getValue().getName() + "\n";
            }
        }
        if (optionalStations.equals("Optional next stations are:\n"))
            throw new NoOptionalStationsToGoNext();

        return optionalStations;
    }

    public void checkTrail(String curr, String next) throws InvalidPathForTrip {
        if (!data.getMapData().getTrails().getTrails().containsKey(curr + next) && !data.getMapData().getTrails().getTrails().containsKey(next + curr))
            throw new InvalidPathForTrip(curr + " to " + next);

        if (data.getMapData().getTrails().getTrails().containsKey(next + curr) && data.getMapData().getTrails().getTrails().get(next + curr).getOneWay())
            throw new InvalidPathForTrip(curr + " to " + next);
    }

    public void checkRoute(String next, String route) throws InvalidRouteThroughTheStationTwice {
        if (route.contains(next))
            throw new InvalidRouteThroughTheStationTwice("this", next);
    }

    public void addTrip(String name, int hour, int minutes, String route, int ppk, int capacity) {
        data.addTrip(name, hour, minutes, route, ppk, capacity);
    }

    public void checkIfEmpty() throws NoTripsInTheSystem, InvalidDisplayNoRequests {
        if (data.getRequests().isEmpty())
            throw new InvalidDisplayNoRequests();
        if (data.getPlannedTrips().getTrips().isEmpty())
            throw new NoTripsInTheSystem();
    }

    public void checkRequest(int serialNumber) throws InvalidRequestName {
        if (!data.getRequests().containsKey(serialNumber))
            throw new InvalidRequestName(serialNumber);
    }

    public String displayMtachingTrips(Set<Trip> matchingTrips, int serialNumber) {
        String display = "";
        for (Trip single_trip : matchingTrips) {
            display = display + single_trip.toString() +
                    "\nAverage Fuel Consumption for your request:" + caculateFuelForRequest((data.getRequests().get(serialNumber).getFrom()), data.getRequests().get(serialNumber).getTo(), single_trip) +
                    "\nPrice for your request:" + caculatePriceForRequest(data.getRequests().get(serialNumber).getFrom(), data.getRequests().get(serialNumber).getTo(), single_trip) + "\n";
        }
        return display;
    }

    public void checkTrip(int choiceRide, Set<Trip> matchingTrips) throws InvalidChoiceForSerialNumber {
        if(!data.getPlannedTrips().getTrips().containsKey(choiceRide))
            throw new InvalidChoiceForSerialNumber(choiceRide);
        for(Trip single_trip : matchingTrips) {
            if (single_trip.getSerialNumber() == data.getPlannedTrips().getTrips().get(choiceRide).getSerialNumber())
                return;
        }
            throw new InvalidChoiceForSerialNumber(choiceRide);
    }
}
    //

//