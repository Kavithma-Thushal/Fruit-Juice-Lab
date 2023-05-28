package dao.custom.impl;

import dao.custom.OrderDetailsDAO;
import dao.custom.impl.util.SQLUtil;
import model.OrderDetailDTO;

import java.sql.SQLException;

public class OrderDetailsDAOImpl implements OrderDetailsDAO {

    @Override
    public int saveOrderDetails(String orderId, OrderDetailDTO detail) throws SQLException, ClassNotFoundException {
        String sql="INSERT INTO OrderDetails (orderId, itemCode, qty, unitPrice) VALUES (?,?,?,?)";
        Boolean bool=SQLUtil.execute(sql,orderId,detail.getItemCode(),detail.getQty(),detail.getUnitPrice());
        int intValue = bool ? 1 : 0;
        return intValue;
    }
}
