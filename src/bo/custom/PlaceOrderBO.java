package bo.custom;

import bo.SuperBO;
import model.CustomerDTO;
import model.ItemDTO;
import model.OrderDTO;
import model.OrderDetailDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
}
