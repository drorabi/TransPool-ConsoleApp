package engine.converted.classes;


import engine.exceptions.InvalidStationsCoordinates;
import engine.exceptions.InvalidStationsLocation;
import engine.exceptions.InvalidStationsNames;
import engine.schema.generated.MapBoundries;
import engine.schema.generated.Stop;
import engine.schema.generated.Stops;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// internal class of 'Stops'
public class Stations {

    protected Map<String, Station> stations;
    protected Map<String, Station> stations_coordinates;


    public Stations(Stops stops) throws InvalidStationsCoordinates, InvalidStationsNames {
        stations=new HashMap<>();
        stations_coordinates= new HashMap<>();

        for(Stop single_station: stops.getStop())
            addStation(single_station);

    }

    private void addStation(Stop single_station) throws InvalidStationsNames, InvalidStationsCoordinates {

        if(stations.containsKey(single_station.getName().trim()))
            throw new InvalidStationsNames(single_station.getName());

        if(stations_coordinates.containsKey(single_station.getX() + "," + single_station.getY())) {
            Station secondStation=stations_coordinates.get(single_station.getX() + "," + single_station.getY());
            throw new InvalidStationsCoordinates(secondStation.getName(),single_station.getName(),single_station.getX() + "," + single_station.getY());
        }

        stations.put(single_station.getName().trim().toUpperCase(),new Station(single_station));
        stations_coordinates.put(single_station.getX() + "," + single_station.getY(),new Station(single_station));
    }


    public Map<String, Station> getStations() {
        return this.stations;
    }

    public void setStations(Map<String, Station> stations) {
        this.stations = stations;
    }

    @Override
    public String toString() {
        String toString = "Stations:\n";
        for (Map.Entry<String, Station> entry : stations.entrySet()) {
            toString = toString + entry.getValue().toString() + "\n";

        }
        return toString;
    }
}
