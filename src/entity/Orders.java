package entity;

import java.time.LocalDate;

public class Orders {

    private String orderId;
    private String customerId;
    private LocalDate date;

    public Orders() {

    }

    public Orders(String orderId, String customerId, LocalDate date) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", date=" + date +
                '}';
    }
}
