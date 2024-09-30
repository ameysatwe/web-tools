package com.example.assignment2;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

@WebServlet(name="shoppingCartServlet",urlPatterns = "/shoppingCart.html")
public class ShoppingCart extends HttpServlet {
    private boolean areEqualReversed(String s1, String s2) {
        s2 = (new StringBuffer(s2)).reverse().toString();
        return((!s1.isEmpty()) && s1.equals(s2));
    }

    private void askForPassword(HttpServletResponse response) {
        response.setStatus(response.SC_UNAUTHORIZED); // I.e., 401
        response.setHeader("WWW-Authenticate",
                "BASIC realm=\"shoppingCart\"");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        String authorization = req.getHeader("Authorization");
        if(authorization==null){
            askForPassword(resp);
        }else {
            String userInfo = authorization.substring(6).trim();
//        BASE64Decoder decoder = new BASE64Decoder();
            String nameAndPassword =
                    new String(Base64.getDecoder().decode(userInfo));
            // Decoded part looks like "username:password".
            int index = nameAndPassword.indexOf(":");
            String user = nameAndPassword.substring(0, index);
            String password = nameAndPassword.substring(index + 1);
            // High security: username must be reverse of password.
            if (areEqualReversed(user, password)) {
                PrintWriter out = resp.getWriter();
                showHomePage(out);
                out.println("    <div class='cart-container'>");
                out.println("      <h2>Your Cart</h2>");
                out.println("      <p>Your cart is currently empty.</p>");
                out.println("      <!-- Future items will be added dynamically here -->");
                out.println("    </div>");
                out.println("  </body>");
                out.println("</html>");
                out.println("");
            } else {
                askForPassword(resp);
            }
        }

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
        String authorization = req.getHeader("Authorization");
        if(authorization==null){
            askForPassword(resp);
        }else {
            String userInfo = authorization.substring(6).trim();
//        BASE64Decoder decoder = new BASE64Decoder();
            String nameAndPassword =
                    new String(Base64.getDecoder().decode(userInfo));
            // Decoded part looks like "username:password".
            int index = nameAndPassword.indexOf(":");
            String user = nameAndPassword.substring(0, index);
            String password = nameAndPassword.substring(index + 1);
            // High security: username must be reverse of password.
            if (areEqualReversed(user, password)) {
                PrintWriter out = resp.getWriter();
                showHomePage(out);
                String[] products = req.getParameterValues("product");
                String action = req.getParameter("action");
                if(action.equals("add")){
                    if(products!=null){

                        HttpSession sess = req.getSession();
                        sess.setAttribute("products",products);
                        System.out.println(sess.getId());
                    }
                }
                else if(action.equals("remove")){
                    if (products!=null){
                        System.out.println("here");
                        HttpSession remSess = req.getSession();
                        String[] prods = (String[]) remSess.getAttribute("products");
                        if(prods!=null){
                            System.out.println(remSess.getId()+prods.length);
                            ArrayList<String> list = new ArrayList<>(Arrays.asList(prods));
                            list.removeAll(Arrays.asList(products));
                            remSess.setAttribute("products",list.toArray(new String[0]));
                            System.out.println("remove len"+((String[]) remSess.getAttribute("products")).length);
                        }
                    }
                }
                HttpSession getSess = req.getSession();

                String[] getProds = (String[])getSess.getAttribute("products");
                System.out.println(getSess.getId());
                System.out.println(getProds.length);
                out.println("    <div class='cart-container'>");
                out.println("      <h2>Your Cart</h2>");
                if (getProds.length > 0) {
                    out.println("      <p>Your cart currently has " + getProds.length + " items.</p>");
                } else {
                    out.println("      <p>Your cart currently has is currently empty.</p>");
                }
                out.println("<ul>");
                for(String p:getProds){
                    out.println("<li> " + p + "</l1>");
                }
                out.println("</ul>");
                out.println("    </div>");
                out.println("  </body>");
                out.println("</html>");
                out.println("");



            } else {
                askForPassword(resp);
            }
        }
    }

    public void showHomePage(PrintWriter out){
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("  <head>");
        out.println("    <meta charset='UTF-8' />");
        out.println("    <title>Shopping Cart</title>");
        out.println("    <style>");
        out.println("      /* Style for the body */");
        out.println("      body {");
        out.println("        display: flex;");
        out.println("        flex-direction: column;");
        out.println("        align-items: center;");
        out.println("        margin: 0;");
        out.println("        font-family: Arial, sans-serif;");
        out.println("        background-color: #f5f5f5;");
        out.println("      }");
        out.println("");
        out.println("      /* Sticky form at the top */");
        out.println("      .form-container {");
        out.println("        background-color: white;");
        out.println("        padding: 20px;");
        out.println("        border-radius: 10px;");
        out.println("        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);");
        out.println("        text-align: center;");
        out.println("        width: 400px;");
        out.println("        position: sticky;");
        out.println("        top: 0;");
        out.println("        z-index: 1000;");
        out.println("      }");
        out.println("");
        out.println("      /* Styling the form elements */");
        out.println("      form {");
        out.println("        display: flex;");
        out.println("        flex-direction: column;");
        out.println("        align-items: flex-start;");
        out.println("      }");
        out.println("");
        out.println("      label {");
        out.println("        margin-left: 8px;");
        out.println("        margin-bottom: 10px;");
        out.println("      }");
        out.println("");
        out.println("      div {");
        out.println("        margin-bottom: 15px;");
        out.println("      }");
        out.println("");
        out.println("      button {");
        out.println("        margin-top: 20px;");
        out.println("        padding: 10px 20px;");
        out.println("        border: none;");
        out.println("        background-color: #007bff;");
        out.println("        color: white;");
        out.println("        border-radius: 5px;");
        out.println("        cursor: pointer;");
        out.println("        width: 100%;");
        out.println("      }");
        out.println("");
        out.println("      button:hover {");
        out.println("        background-color: #0056b3;");
        out.println("      }");
        out.println("");
        out.println("      /* Cart container in the middle */");
        out.println("      .cart-container {");
        out.println("        margin-top: 50px;");
        out.println("        padding: 30px;");
        out.println("        width: 600px;");
        out.println("        background-color: white;");
        out.println("        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);");
        out.println("        border-radius: 10px;");
        out.println("        text-align: center;");
        out.println("      }");
        out.println("");
        out.println("      /* Heading for the cart */");
        out.println("      .cart-container h2 {");
        out.println("        margin-bottom: 20px;");
        out.println("      }");
        out.println("");
        out.println("      /* Empty cart message */");
        out.println("      .cart-container p {");
        out.println("        color: gray;");
        out.println("      }");
        out.println("    </style>");
        out.println("  </head>");
        out.println("  <body>");
        out.println("    <div class='form-container'>");
        out.println("      <h1>Shopping Cart</h1>");
        out.println("");
        out.println("      <form method='post'>");
        out.println("        <div>");
        out.println("          <input type='checkbox' id='Laptop' name='product' value='Laptop' />");
        out.println("          <label for='Laptop'>Laptop</label>");
        out.println("        </div>");
        out.println("");
        out.println("        <div>");
        out.println("          <input type='checkbox' id='tv' name='product' value='Television' />");
        out.println("          <label for='tv'>Television</label>");
        out.println("        </div>");
        out.println("");
        out.println("        <div>");
        out.println("          <input type='checkbox' id='CPU' name='product' value='cpu' />");
        out.println("          <label for='CPU'>CPU</label>");
        out.println("        </div>");
        out.println("");
        out.println("        <div>");
        out.println("          <input type='checkbox' id='phone' name='product' value='phone' />");
        out.println("          <label for='phone'>Phone</label>");
        out.println("        </div>");
        out.println("");
        out.println("        <div>");
        out.println("          <input type='checkbox' id='charger' name='product' value='charger'/>");
        out.println("          <label for='charger'>Charger</label>");
        out.println("        </div>");
        out.println("");
        out.println("        <div>");
        out.println("          <input type='checkbox' id='lamp' name='product' value='lamp' />");
        out.println("          <label for='lamp'>Lamp</label>");
        out.println("        </div>");
        out.println("");
        out.println("        <div>");
        out.println("          <input type='checkbox' id='fan' name='product' value='fan' />");
        out.println("          <label for='fan'>Fan</label>");
        out.println("        </div>");
        out.println("");
        out.println("        <!-- First Submit Button -->");
        out.println("        <button type='submit' name='action' value='add'>Add to Cart</button>");
        out.println("");
        out.println("        <!-- Second Submit Button -->");
        out.println("        <button type='submit' name='action' value='remove'>");
        out.println("          Remove from cart");
        out.println("        </button>");
        out.println("      </form>");
        out.println("    </div>");

    }

}
