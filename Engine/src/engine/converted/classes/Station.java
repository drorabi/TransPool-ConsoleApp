package engine.converted.classes;

import engine.schema.generated.Stop;

//internal of 'stop'
public class Station {


    private Point coordinate;
    private String name;
    private int hour;
    private int minutes;
    
    public Station(String name, Point coordinate){
        setCoordinate(coordinate);
        this.name = name.trim().toUpperCase();
        minutes =-1;
        hour=-1;
    }

    public Station(Stop single_station) {
        name=single_station.getName().trim().toUpperCase();
        coordinate= new Point(single_station.getX(),single_station.getX());
        minutes =-1;
        hour=-1;
    }

    public Station(Station station, int hour, int minutes) {
        name=station.name.trim().toUpperCase();
        coordinate=station.coordinate;
        this.hour =hour;
        this.minutes =minutes;
    }

    public void setCoordinate(Point coordinate){
        this.coordinate = coordinate;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public String getTimeAtStation() {
        if (minutes == -1 || hour == -1)
            return " ";

        if (minutes < 10)
            return hour + ":0" + minutes;
        else
            return hour + ":" + minutes;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getHour() {
        return hour;
    }

    public String getName() {
        return name;
    }



    @Override
    public String toString() {
        return name + "  " + getTimeAtStation();
    }
}
