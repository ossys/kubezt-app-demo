package com.example;

import java.sql.*;
import java.util.Properties;

import org.openziti.jdbc.ZitiDriver;
import org.openziti.Ziti;
import org.openziti.ZitiContext;

public class App 
{
    public static void main( String[] args )
    {
        if (args.length != 1) {
            System.out.println("Usage: App <ziti identity.json>");
            System.exit(1);
        }

        // Initialize the Ziti Java SDK with our identity
        final ZitiContext zitiContext = Ziti.newContext(args[0], "".toCharArray());

        //The zdbc driver registers with java.sql.DriverManager when the zdbc wrapper jar is included in the application.
        // The Ziti JDBC wrapper checks each database URL to see if it starts with zdbc or jdbc:ziti.
        // If it does, then the wrapper accepts the connection request, configures Ziti, configures the driver,
        // and then delegates to the driver to establish a database connection over the Ziti network fabric.
        String url = "zdbc:postgresql://pg-peo-c4i-demo-primary.env-bauf2k.svc.dev.ahq:5432/zerotrust?sslmode=require";

        Properties props = new Properties();
        props.setProperty("user", "s3xpp3yebing2caacaws");
        props.setProperty("password", "j61trIZEV3PbeLeM6b1t");
        props.setProperty("connectTimeout", "240");

        // Tell the ZDBC driver to wait for up to two minutes for a service named "postgres" to be available
        // in the local SDK before connecting.
//        props.setProperty(ZitiDriver.ZITI_WAIT_FOR_SERVICE_NAME, "postgres.oci");
//        props.setProperty(ZitiDriver.ZITI_WAIT_FOR_SERVICE_TIMEOUT, "PT120S");

        try (Connection conn = DriverManager.getConnection(url, props)) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("SELECT * FROM demo")) {
                    while (rs.next()) {
                        System.out.println("Row: " + rs.getString(1) + ":" + rs.getString(2) + ":" + rs.getString(3));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Ziti.getContexts().forEach(c -> c.destroy());
        }

        System.exit(0);
    }
}
