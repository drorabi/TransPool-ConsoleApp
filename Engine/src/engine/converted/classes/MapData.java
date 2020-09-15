package engine.converted.classes;

import engine.exceptions.*;
import engine.schema.generated.*;

// JAXB converter MapDescriptor
public class MapData {
    protected MapBoundries boundaries;
    protected Stations stations;
    protected Trails trails;

    public MapData(MapDescriptor oldMap) throws InvalidStationsNames, InvalidStationsCoordinates,
            InvalidMap, InvalidPathNames, InvalidPathDepatureDestination, InvalidStationsLocation {

       setMapBoundries(oldMap.getMapBoundries());
       setStations(oldMap.getStops());
       setTrails(oldMap.getPaths());
    }

    //setters---------------------

    private void setTrails(Paths paths) throws InvalidPathDepatureDestination, InvalidPathNames {
        for(Path single_path: paths.getPath()){
            checkPath(single_path);
        }
        trails=new Trails(paths);
    }

    private void setStations(Stops stops) throws InvalidStationsLocation, InvalidStationsNames, InvalidStationsCoordinates {
        for(Stop single_station: stops.getStop()) {
            checkCoordinates(single_station);
        }
        stations= new Stations(stops);
    }

    private void setMapBoundries(MapBoundries mapBoundries) throws InvalidMap {
        checkMapSize(mapBoundries);
        boundaries=mapBoundries;
    }

    //checkers---------------------

    private void checkPath(Path single_path) throws InvalidPathDepatureDestination {
        if (!stations.getStations().containsKey(single_path.getFrom().toUpperCase()))
            throw new InvalidPathDepatureDestination(single_path.getFrom().toUpperCase());

        if (!stations.getStations().containsKey(single_path.getTo().toUpperCase()))
            throw new InvalidPathDepatureDestination(single_path.getTo().toUpperCase());
    }

    private void checkCoordinates(Stop single_station) throws InvalidStationsLocation {
        if(single_station.getX() > boundaries.getLength()
                || single_station.getX() < 0
                || single_station.getY() > boundaries.getWidth()
                || single_station.getY() < 0)
            throw new InvalidStationsLocation(single_station.getName());
    }

    private void checkMapSize(MapBoundries mapBoundries) throws InvalidMap {
        if (mapBoundries.getLength() > 100)
            throw new InvalidMap("Length is too big");
        if (mapBoundries.getLength() < 6)
            throw new InvalidMap("Length is too small");
        if (mapBoundries.getWidth() > 100)
            throw new InvalidMap("Width is too big");
        if (mapBoundries.getWidth() < 6)
            throw new InvalidMap("Width is too small");
    }

    //getters---------------------

    public MapBoundries getBoundaries() {
        return boundaries;
    }

    public Stations getStations() {
        return stations;
    }

    public Trails getTrails() {
        return trails;
    }

    public void setTrails(Trails trails) {
        this.trails = trails;
    }

    public void setStations(Stations stations) {
        this.stations = stations;
    }

    public void setBoundaries(MapBoundries boundaries) {
        this.boundaries = boundaries;
    }
}
