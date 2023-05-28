package dao;

import dao.custom.impl.*;

public class DAOFactory {

    private static DAOFactory daoFactory;

    public DAOFactory() {

    }

    public static DAOFactory getDaoFactory() {
        return (daoFactory == null) ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOTypes {
        CUSTOMER, ITEM, ORDER, ORDERDETAILS, QUERY
    }

    public SuperDAO getDAO(DAOTypes res) {
        switch (res) {
            case CUSTOMER:
                return new CustomerDAOImpl();
            case ITEM:
                return new ItemDAOImpl();
            case ORDER:
                return new OrderDAOImpl();
            case ORDERDETAILS:
                return new OrderDetailsDAOImpl();
            case QUERY:
                return new QueryDAOImpl();
            default:
                return null;
        }
    }
}
