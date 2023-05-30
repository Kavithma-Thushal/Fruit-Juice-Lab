package dao.custom;

import dao.CrudDAO;
import entity.Orders;
import model.OrderDTO;

import java.sql.*;
import java.time.LocalDate;

public interface OrderDAO extends CrudDAO<Orders, String> {

}
