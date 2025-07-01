package studentbooking.bean;

import javafx.beans.property.SimpleBooleanProperty;

import javax.persistence.*;

/**
 * @ LiuXinran
 */

public class OrdersEntity {
    private SimpleBooleanProperty isSelected = new SimpleBooleanProperty();	//是否选中
    private String orderNum;
    private String name;
    private String time;
    private String ispaid;
    private String trainName;
    private String startPlace;
    private String endPlace;
    private String startTime;
    private String endTime;
    private String ticketType;
    private Integer remainTickets;
    private float fare;

    public boolean getIsIsSelected() {
        return isSelected.get();
    }

    public SimpleBooleanProperty isSelectedProperty() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected.set(isSelected);
    }


    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIspaid() {
        return ispaid;
    }

    public void setIspaid(String ispaid) {
        this.ispaid = ispaid;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public Integer getRemainTickets() {
        return remainTickets;
    }

    public void setRemainTickets(Integer remainTickets) {
        this.remainTickets = remainTickets;
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

        OrdersEntity that = (OrdersEntity) o;

        if (Float.compare(that.fare, fare) != 0) return false;
        if (orderNum != null ? !orderNum.equals(that.orderNum) : that.orderNum != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (ispaid != null ? !ispaid.equals(that.ispaid) : that.ispaid != null) return false;
        if (trainName != null ? !trainName.equals(that.trainName) : that.trainName != null) return false;
        if (startPlace != null ? !startPlace.equals(that.startPlace) : that.startPlace != null) return false;
        if (endPlace != null ? !endPlace.equals(that.endPlace) : that.endPlace != null) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;
        if (ticketType != null ? !ticketType.equals(that.ticketType) : that.ticketType != null) return false;
        if (remainTickets != null ? !remainTickets.equals(that.remainTickets) : that.remainTickets != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result = orderNum != null ? orderNum.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (ispaid != null ? ispaid.hashCode() : 0);
        result = 31 * result + (trainName != null ? trainName.hashCode() : 0);
        result = 31 * result + (startPlace != null ? startPlace.hashCode() : 0);
        result = 31 * result + (endPlace != null ? endPlace.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (ticketType != null ? ticketType.hashCode() : 0);
        result = 31 * result + (remainTickets != null ? remainTickets.hashCode() : 0);
        result = 31 * result + (fare != +0.0f ? Float.floatToIntBits(fare) : 0);
        return result;
    }
}
