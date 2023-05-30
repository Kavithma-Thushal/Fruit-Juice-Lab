package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBO;
import lk.ijse.pos.dto.*;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PlaceOrderBO extends SuperBO {

    ArrayList<CustomerDTO> loadAllCustomers() throws SQLException, ClassNotFoundException;

    ArrayList<ItemDTO> loadAllItems() throws SQLException, ClassNotFoundException;

    CustomerDTO searchCustomer(String newValue) throws SQLException, ClassNotFoundException;

    ItemDTO searchItem(String newItemCode) throws SQLException, ClassNotFoundException;

    boolean existCustomer(String id) throws SQLException, ClassNotFoundException;

    boolean existItem(String id) throws SQLException, ClassNotFoundException;

    boolean existOrders(String id) throws SQLException, ClassNotFoundException;

    String generateNewOrderId() throws SQLException, ClassNotFoundException;

    boolean saveOrder(OrderDTO orderDTO);

    ItemDTO findItem(String code);

    ArrayList<QueryEntityDTO> searchOrderByID(String oid) throws SQLException, ClassNotFoundException;
}
