package lk.ijse.pos.controller;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class DashboardFormController {

    @FXML
    private AnchorPane root;
    @FXML
    private Label lblMenu;
    @FXML
    private Label lblDescription;
    @FXML
    private ImageView imgCustomer;
    @FXML
    private ImageView imgItem;
    @FXML
    private ImageView imgOrder;
    @FXML
    private ImageView imgViewOrders;

    @FXML
    private void playMouseEnterAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();
        }
    }

    @FXML
    private void playMouseExitAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            icon.setEffect(null);
        }
    }

    @FXML
    private void navigate(MouseEvent event) throws IOException {
        Parent root = null;

        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            switch (icon.getId()) {
                case "imgCustomer":
                    root = FXMLLoader.load(this.getClass().getResource("/lk/ijse/pos/view/managecustomer_form.fxml"));
                    break;
                case "imgItem":
                    root = FXMLLoader.load(this.getClass().getResource("/lk/ijse/pos/view/manageitems_form.fxml"));
                    break;
                case "imgOrder":
                    root = FXMLLoader.load(this.getClass().getResource("/lk/ijse/pos/view/manageorders_form.fxml"));
                    break;
                case "imgViewOrders":
                    root = null;
                    break;
            }

            if (root != null) {
                Scene scene = new Scene(root);
                Stage stage = (Stage) this.root.getScene().getWindow();
                stage.setScene(scene);

                TranslateTransition transition = new TranslateTransition(Duration.millis(350), scene.getRoot());
                transition.setFromX(-scene.getWidth());
                transition.setToX(0);
                transition.play();
            }
        }
    }

    @FXML
    private void powerOffOnAction(MouseEvent event) {
        System.exit(0);
    }
}
