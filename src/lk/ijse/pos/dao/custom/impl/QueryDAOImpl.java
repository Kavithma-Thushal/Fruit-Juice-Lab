package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.QueryDAO;
import lk.ijse.pos.dao.custom.impl.util.SQLUtil;
import lk.ijse.pos.entity.QueryEntity;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class QueryDAOImpl implements QueryDAO {

    @Override
    public ArrayList<QueryEntity> searchOrderByOID(String orderId) throws SQLException, ClassNotFoundException {
        ArrayList<QueryEntity> allRecords = new ArrayList<>();
        String sql = "select Orders.orderId,Orders.customerID,Orders.date,order_details.orderId,order_details.itemCode,order_details.qty,order_details.unitPrice from Orders inner join order_details on Orders.orderId=order_details.orderId where Orders.orderId=?";
        ResultSet rst = SQLUtil.execute(sql, orderId);
        while (rst.next()) {
            String orderID = rst.getString(1);
            String customerID = rst.getString(2);
            LocalDate orderDate = LocalDate.parse(rst.getString(3));
            String itemCode = rst.getString(5);
            int itemQty = rst.getInt(6);
            BigDecimal unitPrice = rst.getBigDecimal(7);
            QueryEntity customEntity = new QueryEntity(orderID, customerID, orderDate, itemCode, itemQty, unitPrice);
            allRecords.add(customEntity);
        }
        return allRecords;
    }
}
