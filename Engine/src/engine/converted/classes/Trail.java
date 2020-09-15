package engine.converted.classes;

import engine.schema.generated.Path;
import engine.schema.generated.TransPool;

// JAXB converted 'path'
public class Trail {
    protected int length;
    protected int fuelConsumption;
    protected int speedLimit;
    protected String to;
    protected Boolean oneWay;
    protected String from;

    public Trail(String from, String to, Boolean oneWay, int speedLimit, int fuelConsumption, int length)
    {
        this.from=from.toUpperCase();
        this.to=to.toUpperCase();
        this.oneWay=oneWay;
        this.speedLimit=speedLimit;
        this.fuelConsumption=fuelConsumption;
        this.length=length;
    }
    public Trail(Path path){
        setFrom(path.getFrom());
        setTo(path.getTo());
        setOneWay(path.isOneWay());
        setSpeedLimit(path.getSpeedLimit());
        setFuelConsumption(path.getFuelConsumption());
        setLength(path.getLength());
    }

//setters--------------------------

    private void setFrom(String from) {
        this.from = from.toUpperCase();
    }

    private void setFuelConsumption(int fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    private void setLength(int length) {
        this.length = length;
    }

    private void setOneWay(Boolean oneWay) {
        this.oneWay = oneWay;
    }

    private void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    private void setTo(String to) {
        this.to = to.toUpperCase();
    }

    //getters-----------------

    public Boolean getOneWay() { return oneWay; }

    public int getFuelConsumption() {
        return fuelConsumption;
    }

    public double getFuelUse() {
        return (double)length/fuelConsumption;
    }

    public int getLength() {
        return length;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getHowMuchTime(){ return ((length*60)/speedLimit);}

}
