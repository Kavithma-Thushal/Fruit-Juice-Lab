package dao.custom;

import dao.CrudDAO;
import model.OrderDTO;

import java.sql.*;
import java.time.LocalDate;

public interface OrderDAO extends CrudDAO<OrderDTO, String> {

}
