package dao.custom;

import dao.CrudDAO;
import entity.OrderDetails;
import model.OrderDTO;
import model.OrderDetailDTO;

import java.sql.SQLException;

public interface OrderDetailsDAO extends CrudDAO<OrderDetails, String> {

}
