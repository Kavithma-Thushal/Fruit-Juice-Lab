package entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class QueryEntity {

    private String orderId;
    private String customerId;
    private LocalDate date;
    private String itemCode;
    private int qty;
    private BigDecimal unitPrice;

    public QueryEntity() {

    }

    public QueryEntity(String orderId, String customerId, LocalDate date, String itemCode, int qty, BigDecimal unitPrice) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.date = date;
        this.itemCode = itemCode;
        this.qty = qty;
        this.unitPrice = unitPrice;
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

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "QueryEntity{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", date=" + date +
                ", itemCode='" + itemCode + '\'' +
                ", qty=" + qty +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
