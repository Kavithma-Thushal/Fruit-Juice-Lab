package dao.custom;

import model.OrderDetailDTO;

import java.sql.SQLException;

public interface OrderDetailsDAO {

    int saveOrderDetails(String orderId, OrderDetailDTO detail) throws SQLException, ClassNotFoundException;
}
