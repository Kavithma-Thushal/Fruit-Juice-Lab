package dao.custom;

import dao.CrudDAO;
import model.CustomerDTO;

import java.sql.*;
import java.util.ArrayList;

public interface CustomerDAO extends CrudDAO<CustomerDTO,String> {

}
