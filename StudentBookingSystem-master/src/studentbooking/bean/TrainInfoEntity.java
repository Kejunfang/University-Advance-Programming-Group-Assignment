package studentbooking.bean;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by leiyang on 2016/11/28.
 */

public class TrainInfoEntity {
    private String trainName;
    private String stationName;
    private String arriveTime;
    private String startTime;
    private String stayTime;
    private int num;
    private float fare;


    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStayTime() {
        return stayTime;
    }

    public void setStayTime(String stayTime) {
        this.stayTime = stayTime;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public float getFare() {
        return fare;
    }

    public void setFare(float fare) {
        this.fare = fare;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrainInfoEntity that = (TrainInfoEntity) o;

        if (num != that.num) return false;
        if (Float.compare(that.fare, fare) != 0) return false;
        if (trainName != null ? !trainName.equals(that.trainName) : that.trainName != null) return false;
        if (stationName != null ? !stationName.equals(that.stationName) : that.stationName != null) return false;
        if (arriveTime != null ? !arriveTime.equals(that.arriveTime) : that.arriveTime != null) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;
        if (stayTime != null ? !stayTime.equals(that.stayTime) : that.stayTime != null) return false;

        return true;
    }

    public int hashCode() {
        int result = trainName != null ? trainName.hashCode() : 0;
        result = 31 * result + (stationName != null ? stationName.hashCode() : 0);
        result = 31 * result + (arriveTime != null ? arriveTime.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (stayTime != null ? stayTime.hashCode() : 0);
        result = 31 * result + num;
        result = 31 * result + (fare != +0.0f ? Float.floatToIntBits(fare) : 0);
        return result;
    }
}
