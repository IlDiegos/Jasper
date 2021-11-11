package com.mycompany.hibernatefxml;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.Pedido;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class PrimaryController implements Initializable {

    @FXML
    private TableView<Pedido> tablaPedidos;
    @FXML
    private TableColumn<Pedido, Long> colId;
    @FXML
    private TableColumn<Pedido, String> colNombre;
    @FXML
    private TableColumn<Pedido, String> colFecha;
    @FXML
    private TableColumn<Pedido, Boolean> colRecogido;
    @FXML
    private Button btnSalir;

    @FXML
    private Label labelPendiente;

    java.util.Date utilDate = new java.util.Date();
    long lnMilisegundos = utilDate.getTime();
    java.sql.Date sqlDate = new java.sql.Date(lnMilisegundos);

    Session s = HibernateUtil.getSessionFactory().openSession();
    ObservableList<Pedido> q = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        listar();
                        pendientes();

                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 5000);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre_pedido"));
        colFecha.setCellValueFactory(
                (var row) -> {
                    DateFormat formatoCorto = DateFormat.getDateInstance(DateFormat.SHORT);
                    String fechaCorta = formatoCorto.format(row.getValue().getFecha());
                    return new SimpleStringProperty(fechaCorta);
                }
        );
        colRecogido.setCellValueFactory(new PropertyValueFactory<>("recogido"));

        listar();
        pendientes();

    }

    private void pendientes() {
        Query pendienteHoy = s.createQuery("FROM Pedido where fecha =current_date() and recogido=false");
        labelPendiente.setText(pendienteHoy.list().size() + "");
        listar();

    }

    private void listar() {
        Query hoy = s.createQuery("FROM Pedido where fecha =current_date()");
        System.out.println(hoy.list());
        tablaPedidos.getItems().clear();
        tablaPedidos.getItems().addAll(hoy.list());

    }

    @FXML
    private void salir(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void marcar(MouseEvent event) {

        Query marcar = s.createQuery("UPDATE Pedido set recogido=true");

        Long i = tablaPedidos.getSelectionModel().getSelectedItem().getId();
        Transaction t = s.beginTransaction();
        marcar.executeUpdate();
        t.commit();

    }
}
