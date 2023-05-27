package bo;

import dao.custom.CustomerDAO;
import dao.custom.impl.CustomerDAOImpl;
import model.CustomerDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBOImpl {

    public ArrayList<CustomerDTO> loadAll() throws SQLException, ClassNotFoundException {
        CustomerDAO customerDAO = new CustomerDAOImpl();
        return customerDAO.loadAll();
    }

    public boolean save(CustomerDTO customerDTO) throws SQLException, ClassNotFoundException {
        CustomerDAO customerDAO = new CustomerDAOImpl();
        return customerDAO.save(customerDTO);
    }
}
