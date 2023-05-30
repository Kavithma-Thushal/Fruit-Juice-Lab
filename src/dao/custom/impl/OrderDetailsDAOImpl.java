package dao.custom.impl;

import dao.custom.OrderDetailsDAO;
import dao.custom.impl.util.SQLUtil;
import model.OrderDetailDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailsDAOImpl implements OrderDetailsDAO {

    @Override
    public ArrayList<OrderDetailDTO> loadAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(OrderDetailDTO orderDetailDTO) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO OrderDetails (orderId, itemCode, qty, unitPrice) VALUES (?,?,?,?)", orderDetailDTO.getOrderID(), orderDetailDTO.getItemCode(), orderDetailDTO.getQty(), orderDetailDTO.getUnitPrice());
    }

    @Override
    public boolean update(OrderDetailDTO orderDetailDTO) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exist(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public OrderDetailDTO search(String newValue) throws SQLException, ClassNotFoundException {
        return null;
    }

    /*@Override
    public int saveOrderDetails(String orderId, OrderDetailDTO detail) throws SQLException, ClassNotFoundException {
        String sql="INSERT INTO OrderDetails (orderId, itemCode, qty, unitPrice) VALUES (?,?,?,?)";
        Boolean bool=SQLUtil.execute(sql,orderId,detail.getItemCode(),detail.getQty(),detail.getUnitPrice());
        int intValue = bool ? 1 : 0;
        return intValue;
    }*/
}
