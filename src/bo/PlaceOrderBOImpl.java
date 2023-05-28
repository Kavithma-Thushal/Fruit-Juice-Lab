package bo;

import controller.ManageordersFormController;
import dao.custom.CustomerDAO;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailsDAO;
import dao.custom.impl.CustomerDAOImpl;
import dao.custom.impl.ItemDAOImpl;
import dao.custom.impl.OrderDAOImpl;
import dao.custom.impl.OrderDetailsDAOImpl;
import db.DBConnection;
import javafx.scene.control.Alert;
import model.CustomerDTO;
import model.ItemDTO;
import model.OrderDetailDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderBOImpl {

    public ArrayList<CustomerDTO> loadAllCustomers() throws SQLException, ClassNotFoundException {
        CustomerDAO customerDAO = new CustomerDAOImpl();
        return customerDAO.loadAll();
    }

    public ArrayList<ItemDTO> loadAllItems() throws SQLException, ClassNotFoundException {
        ItemDAO itemDAO = new ItemDAOImpl();
        return itemDAO.loadAll();
    }

    public CustomerDTO searchCustomer(String newValue) throws SQLException, ClassNotFoundException {
        CustomerDAO customerDAO = new CustomerDAOImpl();
        return customerDAO.search(newValue);
    }

    public ItemDTO searchItem(String newItemCode) throws SQLException, ClassNotFoundException {
        ItemDAO itemDAO = new ItemDAOImpl();
        return itemDAO.search(newItemCode);
    }

    public boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        CustomerDAO customerDAO = new CustomerDAOImpl();
        return customerDAO.exist(id);
    }

    public boolean existItem(String id) throws SQLException, ClassNotFoundException {
        ItemDAO itemDAO = new ItemDAOImpl();
        return itemDAO.exist(id);
    }

    public boolean existOrders(String id) throws SQLException, ClassNotFoundException {
        OrderDAO orderDAO = new OrderDAOImpl();
        return orderDAO.exist(id);
    }

    public String generateNewOrderId() throws SQLException, ClassNotFoundException {
        OrderDAO orderDAO = new OrderDAOImpl();
        return orderDAO.generateNextId();
    }

    public boolean saveOrder(String orderId, String customerId, LocalDate orderDate, List<OrderDetailDTO> orderDetails){
        /*Transaction*/
        Connection connection = null;
        try {
            ManageordersFormController exist=new ManageordersFormController();
            if (exist.existOrders()) {
                new Alert(Alert.AlertType.ERROR, "There is a order associated with the orderId ").show();
            }

            connection = DBConnection.getDbConnection().getConnection();
            connection.setAutoCommit(false);

            /*PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO orders (orderId, customerID, date) VALUES (?,?,?)");
            preparedStatement.setString(1, orderId);
            preparedStatement.setString(2, customerId);
            preparedStatement.setDate(3, Date.valueOf(orderDate));*/

            OrderDAO orderDAO = new OrderDAOImpl();
            int affectedOrderRows = orderDAO.saveOrders(orderId, customerId, orderDate);
            if (affectedOrderRows != 1) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            //preparedStatement = connection.prepareStatement("INSERT INTO OrderDetails (orderId, itemCode, qty, unitPrice) VALUES (?,?,?,?)");

            for (OrderDetailDTO detail : orderDetails) {
                /*preparedStatement.setString(1, orderId);
                preparedStatement.setString(2, detail.getItemCode());
                preparedStatement.setInt(3, detail.getQty());
                preparedStatement.setBigDecimal(4, detail.getUnitPrice());*/

                OrderDetailDTO orderDetailDTO = new OrderDetailDTO(detail.getItemCode(), detail.getQty(), detail.getUnitPrice());
                OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAOImpl();
                int affectedOrderDetailsRow = orderDetailsDAO.saveOrderDetails(orderId, orderDetailDTO);
                if (affectedOrderDetailsRow != 1) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }

                ItemDTO item = findItem(detail.getItemCode());
                item.setQtyOnHand(item.getQtyOnHand() - detail.getQty());

                /*PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE Item SET description=?, qtyOnHand=? , unitPrice=? WHERE itemCode=?");
                preparedStatement1.setString(1, item.getDescription());
                preparedStatement1.setInt(2, item.getQtyOnHand());
                preparedStatement1.setBigDecimal(3, item.getUnitPrice());
                preparedStatement1.setString(4, item.getCode());*/

                ItemDAO itemDAO = new ItemDAOImpl();
                int updateItem = itemDAO.updateItem(item);
                if (!(updateItem > 0)) {
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

    public ItemDTO findItem(String code) {
        try {
            /*Connection connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Item WHERE itemCode=?");
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return new ItemDTO(code, resultSet.getString("description"), resultSet.getInt("qtyOnHand"), resultSet.getBigDecimal("unitPrice"));*/

            ItemDAO itemDAO = new ItemDAOImpl();
            return itemDAO.search(code);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find the Item " + code, e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
