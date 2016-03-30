/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbis.hello;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


/**
 * 
 * @author t_bis_000
 */
@SpringBootApplication
public class Application {

    final static Logger logger = Logger.getLogger(Application.class);

    private final String url = "jdbc:postgresql://localhost/tideDb";
    private final String user = "tidesPostgresRole";
    private final String password = "tides";

    public static void main(String[] args) {
	ApplicationContext ctx = SpringApplication.run(Application.class, args);

	System.out.println("Let's inspect the beans provided by Spring Boot:");

	String[] beanNames = ctx.getBeanDefinitionNames();
	Arrays.sort(beanNames);
	for (String beanName : beanNames) {
	    System.out.println(beanName);
	}

	Application app = new Application();
	// OK transaction
	DateTime dt = new DateTime();
	app.addTideHeightAndAssignTime(dt, 3);

    }

    /**
     * Connect to the PostgreSQL database
     * 
     * @return a Connection object
     * @throws java.sql.SQLException
     */
    public Connection connect() throws SQLException {
	return DriverManager.getConnection(url, user, password);
    }

    /**
     * Close a AutoCloseable object
     * 
     * @param closable
     */
    private Application close(AutoCloseable closeable) {
	try {
	    if (closeable != null) {
		closeable.close();
	    }
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}
	return this;
    }

    /**
     * insert an actor and assign him to a specific film
     * 
     * @param actor
     * @param filmId
     */
    public void addTideHeightAndAssignTime(DateTime dt, int height) {

	Connection conn = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	ResultSet rs = null;

	// insert an actor into the actor table
	String SQLInsertTideHeight = "INSERT INTO tide_height(date_time, height) "
		+ "VALUES(?,?)";

	// assign actor to a film
	// String SQLAssignActor = "INSERT INTO film_actor(actor_id,film_id) "
	// + "VALUES(?,?)";

	int tideHeightId = 0;
	try {
	    // connect to the database
	    conn = connect();
	    conn.setAutoCommit(false);

	    // add actor
	    pstmt = conn.prepareStatement(SQLInsertTideHeight,
		    Statement.RETURN_GENERATED_KEYS);

	    pstmt.setString(1, dt.toString());
	    pstmt.setString(2, String.valueOf(height));

	    int affectedRows = pstmt.executeUpdate();

	    if (affectedRows > 0) {
		// get actor id
		rs = pstmt.getGeneratedKeys();

		if (rs.next()) {
		    tideHeightId = rs.getInt(1);
		    // if (tideHeightId > 0) {
		    // pstmt2 = conn.prepareStatement(SQLAssignActor);
		    // pstmt2.setInt(1, actorId);
		    // pstmt2.setInt(2, filmId);
		    // pstmt2.executeUpdate();
		    // }
		}
	    } else {
		// rollback the transaction if the insert failed
		conn.rollback();
	    }

	    // commit the transaction if everything is fine
	    conn.commit();

	    System.out.println(String.format(
		    "The tideHeight was inserted with id %d .", tideHeightId));

	} catch (SQLException ex) {
	    System.out.println(ex.getMessage());
	    // roll back the transaction
	    System.out.println("Rolling back the transaction...");
	    try {
		if (conn != null) {
		    conn.rollback();
		}
	    } catch (SQLException e) {
		System.out.println(e.getMessage());
	    }

	} finally {
	    this.close(rs).close(pstmt).close(pstmt2).close(conn);
	}
    }

}
