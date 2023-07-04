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
        String sql = "select Orders.oid,Orders.customerID,Orders.date,OrderDetails.oid,OrderDetails.itemCode,OrderDetails.qty,OrderDetails.unitPrice from Orders inner join OrderDetails on Orders.oid=OrderDetails.oid where Orders.oid=?";
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
