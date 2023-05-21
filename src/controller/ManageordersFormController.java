package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.CustomerDTO;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

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
    private TableView tblOrderDetails;
    @FXML
    private Label lblId;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblTotal;

    public void initialize() {
        loadAllCustomers();
        loadAllItems();
        searchCustomer();
    }

    private void loadAllCustomers() {
        try {
            Connection connection = DBConnection.getDbConnection().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Customer");

            while (resultSet.next()) {
                cmbCustomerId.getItems().add(resultSet.getString("customerId"));
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load customer ids").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllItems() {
        try {
            Connection connection = DBConnection.getDbConnection().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Item");

            while (resultSet.next()) {
                cmbItemCode.getItems().add(resultSet.getString("itemCode"));
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load item codes").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void searchCustomer() {
        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //enableOrDisablePlaceOrderButton();

            if (newValue != null) {
                try {
                    Connection connection = DBConnection.getDbConnection().getConnection();
                    try {
                        /*if (!existCustomer(newValue + "")) {
//                            "There is no such customer associated with the id " + id
                            new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + newValue + "").show();
                        }*/

                        String sql = "SELECT * FROM Customer WHERE customerId=?";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, newValue + "");
                        ResultSet resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        CustomerDTO customerDTO = new CustomerDTO(newValue + "", resultSet.getString("name"), resultSet.getString("address"));

                        txtCustomerName.setText(customerDTO.getName());
                    } catch (SQLException e) {
                        new Alert(Alert.AlertType.ERROR, "Failed to find the customer " + newValue + "" + e).show();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                txtCustomerName.clear();
            }
        });
    }

    @FXML
    private void btnAddToCartOnAction(ActionEvent actionEvent) {

    }

    @FXML
    private void btnPlaceOrderOnAction(ActionEvent actionEvent) {

    }

    @FXML
    private void txtQtyOnAction(ActionEvent actionEvent) {

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
