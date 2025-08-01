/*
  MODULE CONFIGURATION - JavaFX module dependencies
  
  INSTRUCTIONS FOR NON-CODERS:
  - This file tells Java which external libraries the application needs
  - To add new JavaFX features: Add corresponding "requires" statements
  - To use database features: Ensure java.sql is included
  - Place this file in src/main/java/ directory
*/

module college.booking {
    // JAVAFX CORE MODULES - Essential JavaFX functionality
    requires javafx.controls;          // UI controls (buttons, text fields, etc.)
    requires javafx.fxml;              // FXML support for layouts
    requires java.desktop;             // Desktop integration features
    
    // DATABASE MODULES - For connecting to databases
    requires java.sql;                 // SQL database connectivity
    requires java.naming;              // JNDI naming services
    
    // UTILITY MODULES
    requires java.prefs;               // User preferences storage
    requires java.logging;             // Application logging
    
    // OPTIONAL MODULES - Add these if needed
    // requires javafx.media;          // Audio/video playback
    // requires javafx.web;            // WebView component
    // requires javafx.swing;          // Swing integration
    
    // EXPORTS - Make packages available to other modules
    exports com.college.booking;
    exports com.college.booking.controllers;
    
    // OPENS - Allow FXML to access controller classes via reflection
    opens com.college.booking.controllers to javafx.fxml;
    opens com.college.booking to javafx.fxml;
}

/*
  SETUP INSTRUCTIONS FOR NON-CODERS:
  
  1. MAVEN DEPENDENCIES (add to pom.xml):
     
     <properties>
         <javafx.version>17.0.2</javafx.version>
         <maven.compiler.source>11</maven.compiler.source>
         <maven.compiler.target>11</maven.compiler.target>
     </properties>
     
     <dependencies>
         <dependency>
             <groupId>org.openjfx</groupId>
             <artifactId>javafx-controls</artifactId>
             <version>${javafx.version}</version>
         </dependency>
         <dependency>
             <groupId>org.openjfx</groupId>
             <artifactId>javafx-fxml</artifactId>
             <version>${javafx.version}</version>
         </dependency>
         <dependency>
             <groupId>mysql</groupId>
             <artifactId>mysql-connector-java</artifactId>
             <version>8.0.33</version>
         </dependency>
     </dependencies>
  
  2. JVM ARGUMENTS (for running the application):
     --module-path /path/to/javafx/lib 
     --add-modules javafx.controls,javafx.fxml
     --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED
  
  3. IDE SETUP:
     - IntelliJ IDEA: Go to Run > Edit Configurations > VM Options
     - Eclipse: Project Properties > Run/Debug Settings
     - Add the JVM arguments above
  
  4. DATABASE SETUP:
     - Install MySQL or your preferred database
     - Update DatabaseConnection.java with your database credentials
     - Create the required tables using the SQL scripts in the Java files
*/