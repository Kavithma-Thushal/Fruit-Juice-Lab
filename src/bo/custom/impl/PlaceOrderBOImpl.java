package bo.custom.impl;

import bo.custom.PlaceOrderBO;
import dao.DAOFactory;
import dao.custom.CustomerDAO;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailsDAO;
import db.DBConnection;
import javafx.scene.control.Alert;
import model.CustomerDTO;
import model.ItemDTO;
import model.OrderDTO;
import model.OrderDetailDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderBOImpl implements PlaceOrderBO {

    CustomerDAO customerDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    ItemDAO itemDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    OrderDAO orderDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    OrderDetailsDAO orderDetailsDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAILS);

    @Override
    public ArrayList<CustomerDTO> loadAllCustomers() throws SQLException, ClassNotFoundException {
        return customerDAO.loadAll();
    }

    @Override
    public ArrayList<ItemDTO> loadAllItems() throws SQLException, ClassNotFoundException {
        return itemDAO.loadAll();
    }

    @Override
    public CustomerDTO searchCustomer(String newValue) throws SQLException, ClassNotFoundException {
        return customerDAO.search(newValue);
    }

    @Override
    public ItemDTO searchItem(String newItemCode) throws SQLException, ClassNotFoundException {
        return itemDAO.search(newItemCode);
    }

    @Override
    public boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.exist(id);
    }

    @Override
    public boolean existItem(String id) throws SQLException, ClassNotFoundException {
        return itemDAO.exist(id);
    }

    @Override
    public boolean existOrders(String id) throws SQLException, ClassNotFoundException {
        return orderDAO.exist(id);
    }

    @Override
    public String generateNewOrderId() throws SQLException, ClassNotFoundException {
        return orderDAO.generateNextId();
    }

    @Override
    public boolean saveOrder(String orderId, String customerId, LocalDate orderDate, List<OrderDetailDTO> orderDetails) {
        /*Transaction*/
        Connection connection = null;
        try {
            if (existOrders(orderId)) {
                new Alert(Alert.AlertType.ERROR, "There is a order associated with the orderId ").show();
            }

            connection = DBConnection.getDbConnection().getConnection();
            connection.setAutoCommit(false);

            boolean orderAdded=orderDAO.save(new OrderDTO(orderId, customerId, orderDate));
            if (!orderAdded) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            for (OrderDetailDTO detail : orderDetails) {

                boolean odAdded=orderDetailsDAO.save(detail);
                if (!odAdded) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }

                ItemDTO item = findItem(detail.getItemCode());
                item.setQtyOnHand(item.getQtyOnHand() - detail.getQty());

                boolean itemUpdate = itemDAO.update(item);
                if (!itemUpdate) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
            }
            connection.commit();
            connection.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ItemDTO findItem(String code) {
        try {
            /*Connection connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Item WHERE itemCode=?");
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return new ItemDTO(code, resultSet.getString("description"), resultSet.getInt("qtyOnHand"), resultSet.getBigDecimal("unitPrice"));*/

            return itemDAO.search(code);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find the Item " + code, e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
