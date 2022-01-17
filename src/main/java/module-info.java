module com.mycompany.hibernatefxml {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.base;
    requires java.sql;
    requires java.persistence;
    requires java.naming;
    requires javafx.swing;
    requires jasperreports;
    
    
    opens com.mycompany.hibernatefxml to javafx.fxml, javafx.swing, java.sql;
    opens models;
    exports com.mycompany.hibernatefxml;
    
}
