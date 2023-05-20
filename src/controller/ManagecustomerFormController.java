package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.tdm.CustomerTM;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

public class ManagecustomerFormController {

    @FXML
    private AnchorPane root;
    @FXML
    private JFXTextField txtCustomerId;
    @FXML
    private JFXTextField txtCustomerName;
    @FXML
    private JFXTextField txtCustomerAddress;
    @FXML
    private JFXButton btnAddNewCustomer;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private TableView<CustomerTM> tblCustomers;

    public void initialize() {
        initUI();
        loadAllCustomers();
        setToTable();
        selectRow();
    }

    private void initUI() {
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCustomerId.setDisable(true);
        txtCustomerName.setDisable(true);
        txtCustomerAddress.setDisable(true);
        //txtCustomerId.setEditable(false);
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
    }

    private void loadAllCustomers() {
        tblCustomers.getItems().clear();
        try {
            Connection connection = DBConnection.getDbConnection().getConnection();
            String sql = "SELECT * FROM Customer";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                CustomerTM customerTM = new CustomerTM(resultSet.getString("customerId"), resultSet.getString("name"), resultSet.getString("address"));
                tblCustomers.getItems().add(customerTM);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void setToTable() {
        tblCustomers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    private void selectRow() {
        tblCustomers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
            btnSave.setText(newValue != null ? "Update" : "Save");
            btnSave.setDisable(newValue == null);

            if (newValue != null) {
                txtCustomerId.setText(newValue.getId());
                txtCustomerName.setText(newValue.getName());
                txtCustomerAddress.setText(newValue.getAddress());

                txtCustomerId.setDisable(false);
                txtCustomerName.setDisable(false);
                txtCustomerAddress.setDisable(false);
            }
        });

        //txtCustomerAddress.setOnAction(event -> btnSave.fire());
        loadAllCustomers();
    }

    private boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT customerId FROM Customer WHERE customerId=?");
        preparedStatement.setString(1, id);
        return preparedStatement.executeQuery().next();
    }

    @FXML
    private void btnAddNewCustomerOnAction(ActionEvent actionEvent) {
        txtCustomerId.setDisable(false);
        txtCustomerName.setDisable(false);
        txtCustomerAddress.setDisable(false);
        txtCustomerId.clear();
        //txtCustomerId.setText(generateNewId());
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCustomerName.requestFocus();
        btnSave.setDisable(false);
        btnSave.setText("Save");
        tblCustomers.getSelectionModel().clearSelection();
    }

    @FXML
    private void btnSaveOnAction(ActionEvent actionEvent) {
        String customerId = txtCustomerId.getText();
        String customerName = txtCustomerName.getText();
        String customerAddress = txtCustomerAddress.getText();

        /*Validation*/
        if (!customerName.matches("[A-Z a-z]+")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Name").show();
            txtCustomerName.requestFocus();
            return;
        } else if (!customerAddress.matches(".{3,}")) {
            new Alert(Alert.AlertType.ERROR, "Address should be at least 3 characters long").show();
            txtCustomerAddress.requestFocus();
            return;
        }

        /*Save Customer*/
        if (btnSave.getText().equalsIgnoreCase("Save")) {
            try {
                if (existCustomer(customerId)) {
                    new Alert(Alert.AlertType.ERROR, customerId + " already exists").show();
                }
                Connection connection = DBConnection.getDbConnection().getConnection();
                String sql = "INSERT INTO customer (customerId, name, address) VALUES (?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, customerId);
                preparedStatement.setString(2, customerName);
                preparedStatement.setString(3, customerAddress);
                preparedStatement.executeUpdate();

                //tblCustomers.getItems().add(new CustomerTM(customerId, customerName, customerAddress));
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to save the customer " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {

            /*Update customer*/
            try {
                if (!existCustomer(customerId)) {
                    new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + customerId).show();
                }
                Connection connection = DBConnection.getDbConnection().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE customer SET name=?, address=? WHERE customerId=?");
                preparedStatement.setString(1, customerName);
                preparedStatement.setString(2, customerAddress);
                preparedStatement.setString(3, customerId);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to update the customer " + customerId + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            CustomerTM selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
            selectedCustomer.setName(customerName);
            selectedCustomer.setAddress(customerAddress);
            tblCustomers.refresh();
        }
        //btnAddNewCustomer.fire();
    }

    @FXML
    private void btnDeleteOnAction(ActionEvent actionEvent) {
        String id = tblCustomers.getSelectionModel().getSelectedItem().getId();
        try {
            if (!existCustomer(id)) {
                new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + id).show();
            }
            Connection connection = DBConnection.getDbConnection().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM customer WHERE customerId=?");
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();

            tblCustomers.getItems().remove(tblCustomers.getSelectionModel().getSelectedItem());
            tblCustomers.getSelectionModel().clearSelection();
            initUI();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete the customer " + id).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateToHome(MouseEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/dashboard_form.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage stage = (Stage) (this.root.getScene().getWindow());
        stage.setScene(scene);
        //Platform.runLater(() -> stage.sizeToScene());
    }
}
