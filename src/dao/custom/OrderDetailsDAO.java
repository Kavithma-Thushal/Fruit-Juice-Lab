package dao.custom;

import dao.CrudDAO;
import model.OrderDTO;
import model.OrderDetailDTO;

import java.sql.SQLException;

public interface OrderDetailsDAO extends CrudDAO<OrderDetailDTO, String> {

    int saveOrderDetails(String orderId, OrderDetailDTO detail) throws SQLException, ClassNotFoundException;
}
