package com.example.assignment2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CsvServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("org.relique.jdbc.csv.CsvDriver");
            PrintWriter out = resp.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html lang=\"en\">");
            out.println("  <head>");
            out.println("    <meta charset=\"UTF-8\" />");
            out.println("    <title>CSV File Upload</title>");
            out.println("  </head>");
            out.println("  <body>");
            out.println("    <h1>Enter the CSV file name</h1>");
            out.println("    <form method=\"GET\">");
            out.println("      <label for=\"csvFileName\">CSV File Name:</label>");
            out.println("      <input type=\"text\" id=\"csvFileName\" name=\"csvFileName\" />");
            out.println("      <input type=\"submit\" value=\"Submit\" />");
            out.println("    </form>");

            String fileName = req.getParameter("csvFileName");
            if(fileName!=null) {
                String jdbcUrl = "jdbc:relique:csv:" + getServletContext().getRealPath("/") + "WEB-INF/";
                out.println("<table border='1'><tr>" +
                        "<th>Facility Type</th>" +
                        "<th>License Number</th>" +
                        "<th>Entity Name</th>" +
                        "<th>Camis Trade Name</th>" +
                        "<th>Address Bldg</th>" +
                        "<th>Address Street Name</th>" +
                        "<th>Address Location</th>" +
                        "<th>Address State</th>" +
                        "<th>Address Zip Code</th>" +
                        "<th>Telephone Number</th>" +
                        "<th>Number of Spaces</th>" +
                        "</tr>");

                try (Connection conn = DriverManager.getConnection(jdbcUrl);
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM " + fileName)) {
                    while (rs.next()) {
                        out.println("<tr><td>" + rs.getString("Facility Type") + "</td>");
                        out.println("<td>" + rs.getString("License Number") + "</td>");
                        out.println("<td>" + rs.getString("Entity Name") + "</td>");
                        out.println("<td>" + rs.getString("Camis Trade Name") + "</td>");
                        out.println("<td>" + rs.getString("Address Bldg") + "</td>");
                        out.println("<td>" + rs.getString("Address Street Name") + "</td>");
                        out.println("<td>" + rs.getString("Address Location") + "</td>");
                        out.println("<td>" + rs.getString("Address State") + "</td>");
                        out.println("<td>" + rs.getString("Address Zip Code") + "</td>");
                        out.println("<td>" + rs.getString("Telephone Number") + "</td>");
                        out.println("<td>" + rs.getString("Number of Spaces") + "</td></tr>");
                    }

                    // Close resources
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (Exception e) {
                    System.out.println("Exception thrown" + e);
                }
            }
            out.println("  </body>");
            out.println("</html>");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
