package controller;

import bo.BOFactory;
import bo.custom.PlaceOrderBO;
import bo.custom.impl.PlaceOrderBOImpl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.CustomerDTO;
import model.ItemDTO;
import model.OrderDetailDTO;
import view.tdm.OrderDetailTM;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ManageordersFormController {

    @FXML
    private AnchorPane root;
    @FXML
    private JFXComboBox<String> cmbCustomerId;
    @FXML
    private JFXTextField txtCustomerName;
    @FXML
    private JFXComboBox<String> cmbItemCode;
    @FXML
    private JFXTextField txtDescription;
    @FXML
    private JFXTextField txtQtyOnHand;
    @FXML
    private JFXTextField txtUnitPrice;
    @FXML
    private JFXTextField txtQty;
    @FXML
    private JFXButton btnAddToCart;
    @FXML
    private JFXButton btnPlaceOrder;
    @FXML
    private TableView<OrderDetailTM> tblOrderDetails;
    @FXML
    private Label lblId;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblTotal;
    private String orderId;

    PlaceOrderBO placeOrderBO = BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PLACEORDERS);

    public void initialize() {
        loadAllCustomers();
        loadAllItems();
        searchCustomer();
        searchItem();
        selectRow();
        setToTable();
        initUI();
    }

    private void initUI() {
        orderId = generateNewOrderId();
        lblId.setText("Order ID: " + orderId);
        lblDate.setText(LocalDate.now().toString());
        txtCustomerName.setEditable(false);
        txtDescription.setEditable(false);
        txtQtyOnHand.setEditable(false);
        txtUnitPrice.setEditable(false);
        txtQty.setEditable(false);
        txtQty.setOnAction(event -> btnAddToCart.fire());
        btnAddToCart.setDisable(true);
        btnPlaceOrder.setDisable(true);
    }

    private void setToTable() {
        tblOrderDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblOrderDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblOrderDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblOrderDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblOrderDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));
        TableColumn<OrderDetailTM, Button> lastCol = (TableColumn<OrderDetailTM, Button>) tblOrderDetails.getColumns().get(5);

        lastCol.setCellValueFactory(param -> {
            Button btnDelete = new Button("Delete");

            btnDelete.setOnAction(event -> {
                tblOrderDetails.getItems().remove(param.getValue());
                tblOrderDetails.getSelectionModel().clearSelection();
                calculateTotal();
                enableOrDisablePlaceOrderButton();
            });
            return new ReadOnlyObjectWrapper<>(btnDelete);
        });
    }

    private void selectRow() {
        tblOrderDetails.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedOrderDetail) -> {

            if (selectedOrderDetail != null) {
                cmbItemCode.setDisable(true);
                cmbItemCode.setValue(selectedOrderDetail.getCode());
                btnAddToCart.setText("Update");
                txtQtyOnHand.setText(Integer.parseInt(txtQtyOnHand.getText()) + selectedOrderDetail.getQty() + "");
                txtQty.setText(selectedOrderDetail.getQty() + "");
            } else {
                btnAddToCart.setText("Add");
                cmbItemCode.setDisable(false);
                cmbItemCode.getSelectionModel().clearSelection();
                txtQty.clear();
            }
        });
    }

    private void loadAllCustomers() {
        try {
            /*Connection connection = DBConnection.getDbConnection().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Customer");

            while (resultSet.next()) {
                cmbCustomerId.getItems().add(resultSet.getString("customerId"));
            }*/

            //CustomerDAO customerDAO = new CustomerDAOImpl();
            //ArrayList<CustomerDTO> allCustomers = customerDAO.loadAll();

            //PlaceOrderBOImpl placeOrderBO = new PlaceOrderBOImpl();
            ArrayList<CustomerDTO> allCustomers = placeOrderBO.loadAllCustomers();
            for (CustomerDTO customers : allCustomers) {
                cmbCustomerId.getItems().add(customers.getId());
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load customer ids").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllItems() {
        try {
            /*Connection connection = DBConnection.getDbConnection().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Item");

            while (resultSet.next()) {
                cmbItemCode.getItems().add(resultSet.getString("itemCode"));
            }*/

            //ItemDAO itemDAO = new ItemDAOImpl();
            //ArrayList<ItemDTO> allItems = itemDAO.loadAll();

            //PlaceOrderBOImpl placeOrderBO = new PlaceOrderBOImpl();
            ArrayList<ItemDTO> allItems = placeOrderBO.loadAllItems();
            for (ItemDTO item : allItems) {
                cmbItemCode.getItems().add(item.getCode());
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load item codes").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void searchCustomer() {
        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            enableOrDisablePlaceOrderButton();

            if (newValue != null) {
                try {
                    //Connection connection = DBConnection.getDbConnection().getConnection();
                    try {
                        if (!existCustomer(newValue + "")) {
                            new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + newValue + "").show();
                        }

                        /*String sql = "SELECT * FROM Customer WHERE customerId=?";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, newValue + "");
                        ResultSet resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        CustomerDTO customerDTO = new CustomerDTO(newValue + "", resultSet.getString("name"), resultSet.getString("address"));*/

                        //CustomerDAO customerDAO = new CustomerDAOImpl();
                        //CustomerDTO customerDTO = customerDAO.search(newValue);

                        //PlaceOrderBOImpl placeOrderBO = new PlaceOrderBOImpl();
                        CustomerDTO customerDTO = placeOrderBO.searchCustomer(newValue);

                        txtCustomerName.setText(customerDTO.getName());
                        cmbCustomerId.setDisable(true);
                    } catch (SQLException e) {
                        new Alert(Alert.AlertType.ERROR, "Failed to find the customer " + newValue + "" + e).show();
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                txtCustomerName.clear();
            }
        });
    }

    private void searchItem() {
        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newItemCode) -> {
            txtQty.setEditable(newItemCode != null);
            btnAddToCart.setDisable(newItemCode == null);

            if (newItemCode != null) {
                try {
                    if (!existItem(newItemCode + "")) {
                        new Alert(Alert.AlertType.ERROR, "There is no such item associated with the code " + newItemCode + "").show();
                    }
                    /*Connection connection = DBConnection.getDbConnection().getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Item WHERE itemCode=?");
                    preparedStatement.setString(1, newItemCode + "");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    ItemDTO itemDTO = new ItemDTO(newItemCode + "", resultSet.getString("description"), resultSet.getInt("qtyOnHand"), resultSet.getBigDecimal("unitPrice"));*/

                    //ItemDAO itemDAO = new ItemDAOImpl();
                    //ItemDTO itemDTO = itemDAO.search(newItemCode);

                    //PlaceOrderBOImpl placeOrderBO = new PlaceOrderBOImpl();
                    ItemDTO itemDTO = placeOrderBO.searchItem(newItemCode);

                    txtDescription.setText(itemDTO.getDescription());
                    txtUnitPrice.setText(itemDTO.getUnitPrice().setScale(2).toString());

                    Optional<OrderDetailTM> optOrderDetail = tblOrderDetails.getItems().stream().filter(detail -> detail.getCode().equals(newItemCode)).findFirst();
                    txtQtyOnHand.setText((optOrderDetail.isPresent() ? itemDTO.getQtyOnHand() - optOrderDetail.get().getQty() : itemDTO.getQtyOnHand()) + "");

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                txtDescription.clear();
                txtQtyOnHand.clear();
                txtUnitPrice.clear();
                txtQty.clear();
            }
        });
    }

    private boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        /*Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT customerId FROM Customer WHERE customerId=?");
        preparedStatement.setString(1, id);
        return preparedStatement.executeQuery().next();*/

        //CustomerDAO customerDAO = new CustomerDAOImpl();
        //return customerDAO.exist(id);

        //PlaceOrderBOImpl placeOrderBO = new PlaceOrderBOImpl();
        return placeOrderBO.existCustomer(id);
    }

    private boolean existItem(String code) throws SQLException, ClassNotFoundException {
        /*Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT itemCode FROM Item WHERE itemCode=?");
        preparedStatement.setString(1, code);
        return preparedStatement.executeQuery().next();*/

        //ItemDAO itemDAO = new ItemDAOImpl();
        //return itemDAO.exist(code);

        //PlaceOrderBOImpl placeOrderBO = new PlaceOrderBOImpl();
        return placeOrderBO.existItem(code);
    }

    public boolean existOrders() throws SQLException, ClassNotFoundException {
        /*Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT orderId FROM orders WHERE orderId=?");
        preparedStatement.setString(1, orderId);
        return preparedStatement.executeQuery().next();*/

        //OrderDAO orderDAO = new OrderDAOImpl();
        //return orderDAO.exist(orderId);

        //PlaceOrderBOImpl placeOrderBO = new PlaceOrderBOImpl();
        return placeOrderBO.existOrders(orderId);
    }

    public String generateNewOrderId() {
        try {
            /*Connection connection = DBConnection.getDbConnection().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT orderId FROM orders ORDER BY orderId DESC LIMIT 1;");
            return resultSet.next() ? String.format("OID-%03d", (Integer.parseInt(resultSet.getString("orderId").replace("OID-", "")) + 1)) : "OID-001";*/

            //OrderDAO orderDAO = new OrderDAOImpl();
            //return orderDAO.generateNextId();

            //PlaceOrderBOImpl placeOrderBO = new PlaceOrderBOImpl();
            return placeOrderBO.generateNewOrderId();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate a new order id").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "OID-001";
    }

    private void enableOrDisablePlaceOrderButton() {
        btnPlaceOrder.setDisable(!(cmbCustomerId.getSelectionModel().getSelectedItem() != null && !tblOrderDetails.getItems().isEmpty()));
    }

    private void calculateTotal() {
        BigDecimal total = new BigDecimal(0);

        for (OrderDetailTM detail : tblOrderDetails.getItems()) {
            total = total.add(detail.getTotal());
        }
        lblTotal.setText("Total: " + total);
    }

    @FXML
    private void btnAddToCartOnAction(ActionEvent actionEvent) {
        if (!txtQty.getText().matches("\\d+") || Integer.parseInt(txtQty.getText()) <= 0 || Integer.parseInt(txtQty.getText()) > Integer.parseInt(txtQtyOnHand.getText())) {
            new Alert(Alert.AlertType.ERROR, "Invalid Quantity").show();
            txtQty.requestFocus();
            txtQty.selectAll();
            return;
        }

        String code = cmbItemCode.getSelectionModel().getSelectedItem();
        String description = txtDescription.getText();
        int qty = Integer.parseInt(txtQty.getText());
        BigDecimal unitPrice = new BigDecimal(txtUnitPrice.getText()).setScale(2);
        BigDecimal total = unitPrice.multiply(new BigDecimal(qty)).setScale(2);

        boolean exists = tblOrderDetails.getItems().stream().anyMatch(detail -> detail.getCode().equals(code));

        if (exists) {
            OrderDetailTM orderDetailTM = tblOrderDetails.getItems().stream().filter(detail -> detail.getCode().equals(code)).findFirst().get();

            if (btnAddToCart.getText().equalsIgnoreCase("Update")) {
                orderDetailTM.setQty(qty);
                orderDetailTM.setTotal(total);
                tblOrderDetails.getSelectionModel().clearSelection();
            } else {
                orderDetailTM.setQty(orderDetailTM.getQty() + qty);
                total = new BigDecimal(orderDetailTM.getQty()).multiply(unitPrice).setScale(2);
                orderDetailTM.setTotal(total);
            }
            tblOrderDetails.refresh();
        } else {
            tblOrderDetails.getItems().add(new OrderDetailTM(code, description, qty, unitPrice, total));
        }

        cmbItemCode.getSelectionModel().clearSelection();
        cmbItemCode.requestFocus();
        calculateTotal();
        enableOrDisablePlaceOrderButton();
    }

    @FXML
    private void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        boolean bool = saveOrder(orderId, cmbCustomerId.getValue(), LocalDate.now(), tblOrderDetails.getItems().stream().map(tm -> new OrderDetailDTO(tm.getCode(), tm.getQty(), tm.getUnitPrice())).collect(Collectors.toList()));

        if (bool) {
            new Alert(Alert.AlertType.INFORMATION, "Order has been placed successfully").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Order is not placed").show();
        }

        cmbCustomerId.setDisable(false);
        orderId = generateNewOrderId();
        lblId.setText("Order Id: " + orderId);
        cmbCustomerId.getSelectionModel().clearSelection();
        cmbItemCode.getSelectionModel().clearSelection();
        tblOrderDetails.getItems().clear();
        txtQty.clear();
        calculateTotal();
    }

    public boolean saveOrder(String orderId, String customerId, LocalDate orderDate, List<OrderDetailDTO> orderDetails) {
        //PlaceOrderBOImpl placeOrderBO = new PlaceOrderBOImpl();
        return placeOrderBO.saveOrder(orderId, customerId, orderDate, orderDetails);
    }

    @FXML
    private void navigateToHome(MouseEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/dashboard_form.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage stage = (Stage) (this.root.getScene().getWindow());
        stage.setScene(scene);
        Platform.runLater(() -> stage.sizeToScene());
    }
}
