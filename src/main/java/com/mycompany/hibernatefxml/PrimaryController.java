package com.mycompany.hibernatefxml;

import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.swing.JFrame;
import models.Pedido;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.swing.JRViewer;
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
    @FXML
    private Button btnSalir1;
    @FXML
    private Button btnSalir11;

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

    @FXML
    private void btnCarta(ActionEvent event) {
        String archivo = "Carta.jrxml";
         
        try {
            var parameters = new HashMap();
            
            parameters.put("Titulo", "Carta");
            
            JasperReport informe = JasperCompileManager.compileReport(archivo);
            JasperPrint impresion = JasperFillManager.fillReport(informe, parameters, Conexion.getCon());
            
            JRViewer visor = new JRViewer(impresion);
            
            JFrame ventanaInforme = new JFrame("Carta");
            ventanaInforme.getContentPane().add(visor);
            ventanaInforme.pack();
            ventanaInforme.setVisible(true);
            
            
            JRPdfExporter exportador = new JRPdfExporter();
            exportador.setExporterInput( new SimpleExporterInput(impresion) );
            exportador.setExporterOutput( new SimpleOutputStreamExporterOutput("Carta.pdf") );
            
            var configuracion = new SimplePdfExporterConfiguration();
            exportador.setConfiguration(configuracion);
            
            exportador.exportReport();
            
                    
        } catch (JRException ex) {
            System.out.println(ex);
        }
    }

    @FXML
    private void btnPedidos(ActionEvent event) {
         String archivo = "Pedido.jrxml";
         
        try {
            var parameters = new HashMap();
            parameters.put("Titulo", "Pedidos");
            
            JasperReport informe = JasperCompileManager.compileReport(archivo);
            JasperPrint impresion = JasperFillManager.fillReport(informe, parameters, Conexion.getCon());
            
            JRViewer visor = new JRViewer(impresion);
            
            JFrame ventanaInforme = new JFrame("Pedido");
            ventanaInforme.getContentPane().add(visor);
            ventanaInforme.pack();
            ventanaInforme.setVisible(true);
            
            JRPdfExporter exportador = new JRPdfExporter();
            exportador.setExporterInput( new SimpleExporterInput(impresion) );
            exportador.setExporterOutput( new SimpleOutputStreamExporterOutput("Pedido.pdf") );
            
            var configuracion = new SimplePdfExporterConfiguration();
            exportador.setConfiguration(configuracion);
            
            exportador.exportReport();
            
                    
        } catch (JRException ex) {
            System.out.println(ex);
        }
    }
}
