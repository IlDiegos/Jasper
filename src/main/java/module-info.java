module com.mycompany.hibernatefxml {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.base;
    requires java.sql;
    requires java.persistence;
    requires java.naming;
    
    
    opens com.mycompany.hibernatefxml to javafx.fxml;
    opens models;
    exports com.mycompany.hibernatefxml;
    
}
