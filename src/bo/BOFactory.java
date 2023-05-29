package bo;

import bo.custom.impl.CustomerBOImpl;
import bo.custom.impl.ItemBOImpl;
import bo.custom.impl.PlaceOrderBOImpl;

public class BOFactory {

    private static BOFactory boFactory;

    public BOFactory() {

    }

    public static BOFactory getBoFactory() {
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes {
        CUSTOMER, ITEM, PLACEORDERS
    }

    public <T extends SuperBO> T getBO(BOFactory.BOTypes boTypes) {
        switch (boTypes) {
            case CUSTOMER:
                return (T) new CustomerBOImpl();
            case ITEM:
                return (T) new ItemBOImpl();
            case PLACEORDERS:
                return (T) new PlaceOrderBOImpl();
            default:
                return null;
        }
    }
}
