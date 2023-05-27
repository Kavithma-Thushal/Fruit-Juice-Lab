package dao.custom;

import dao.CrudDAO;
import model.OrderDTO;

import java.sql.*;
import java.time.LocalDate;

public interface OrderDAO extends CrudDAO<OrderDTO, String> {


    /*boolean exist(String orderId) throws SQLException, ClassNotFoundException;

    String generateNextId() throws SQLException, ClassNotFoundException;*/

    int saveOrders(String orderId, String customerId, LocalDate orderDate) throws SQLException, ClassNotFoundException;
}
