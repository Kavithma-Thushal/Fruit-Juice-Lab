package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.bo.custom.PlaceOrderBO;
import lk.ijse.pos.dao.DAOFactory;
import lk.ijse.pos.dao.custom.*;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.entity.*;
import javafx.scene.control.Alert;
import lk.ijse.pos.dto.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlaceOrderBOImpl implements PlaceOrderBO {

    CustomerDAO customerDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    ItemDAO itemDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    OrderDAO orderDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    OrderDetailsDAO orderDetailsDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAILS);

    QueryDAO queryDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY);

    @Override
    public ArrayList<CustomerDTO> loadAllCustomers() throws SQLException, ClassNotFoundException {
        ArrayList<CustomerDTO> customerArrayList = new ArrayList<>();
        ArrayList<Customer> cusArr = customerDAO.loadAll();
        for (Customer customer : cusArr) {
            customerArrayList.add(new CustomerDTO(customer.getCustomerId(), customer.getName(), customer.getAddress()));
        }
        return customerArrayList;
    }

    @Override
    public ArrayList<ItemDTO> loadAllItems() throws SQLException, ClassNotFoundException {
        ArrayList<ItemDTO> itemArray = new ArrayList<>();
        ArrayList<Item> itm = itemDAO.loadAll();
        for (Item item : itm) {
            itemArray.add(new ItemDTO(item.getItemCode(), item.getDescription(), item.getQtyOnHand(), item.getUnitPrice()));
        }
        return itemArray;
    }

    @Override
    public CustomerDTO searchCustomer(String newValue) throws SQLException, ClassNotFoundException {
        Customer customer = customerDAO.search(newValue);
        return new CustomerDTO(customer.getCustomerId(), customer.getName(), customer.getAddress());
    }

    @Override
    public ItemDTO searchItem(String newItemCode) throws SQLException, ClassNotFoundException {
        Item item = itemDAO.search(newItemCode);
        return new ItemDTO(item.getItemCode(), item.getDescription(), item.getQtyOnHand(), item.getUnitPrice());
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
    public boolean saveOrder(OrderDTO orderDTO) {
        /*Transaction*/
        Connection connection = null;
        try {
            if (existOrders(orderDTO.getOrderId())) {
                new Alert(Alert.AlertType.ERROR, "There is a order associated with the orderId ").show();
            }

            connection = DBConnection.getDbConnection().getConnection();
            connection.setAutoCommit(false);

            boolean orderAdded = orderDAO.save(new Orders(orderDTO.getOrderId(), orderDTO.getCustomerId(), orderDTO.getOrderDate()));
            if (!orderAdded) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            for (OrderDetailDTO detail : orderDTO.getOrderDetailDTO()) {

                boolean odAdded = orderDetailsDAO.save(new OrderDetails(detail.getOrderID(), detail.getItemCode(), detail.getQty(), detail.getUnitPrice()));
                if (!odAdded) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }

                ItemDTO item = findItem(detail.getItemCode());
                item.setQtyOnHand(item.getQtyOnHand() - detail.getQty());

                Item im = new Item(item.getCode(), item.getDescription(), item.getQtyOnHand(), item.getUnitPrice());
                boolean itemUpdate = itemDAO.update(im);
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
            Item item = itemDAO.search(code);
            return new ItemDTO(item.getItemCode(), item.getDescription(), item.getQtyOnHand(), item.getUnitPrice());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find the Item " + code, e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<QueryEntityDTO> searchOrderByID(String orderId) throws SQLException, ClassNotFoundException {
        ArrayList<QueryEntityDTO> dtoList = new ArrayList<>();
        ArrayList<QueryEntity> entityList = queryDAO.searchOrderByOID(orderId);
        for (QueryEntity ce : entityList) {
            dtoList.add(new QueryEntityDTO(ce.getOrderId(), ce.getCustomerId(), ce.getDate(), ce.getItemCode(), ce.getQty(), ce.getUnitPrice()));
        }
        return dtoList;
    }
}
