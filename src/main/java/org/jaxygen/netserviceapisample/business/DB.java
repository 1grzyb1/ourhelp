package org.jaxygen.netserviceapisample.business;

import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.Status;

import javax.servlet.http.HttpSession;
import org.jaxygen.annotations.SessionContext;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;
import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DB {

    @SessionContext
     public HttpSession session;

    private static final String USER = "root";
    private static final String PASS = "";
    private static final String CONN = "jdbc:mysql://nexus.b.snet.ovh:3306/grzyb_ourhelp?useLegacyDatetimeCode=false&serverTimezone=America/New_York";
    public String ret;

    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    @NetAPI(description = "Login user",
            status = Status.GenerallyAvailable,
            version = "1.0")
    public String login(Login login) {
        Connection con = null;
        PreparedStatement pstatement = null;
        try {
            con = DriverManager.getConnection(CONN, USER, PASS);
            System.out.println("Connected");
            Statement stmt = con.createStatement();

            login.setPassword(MD5(login.getPassword()));

            pstatement = con.prepareStatement("SELECT * FROM `users` WHERE `login` = ? AND `password`= ?");
            pstatement.setString(1, login.getLogin());
            pstatement.setString(2, login.getPassword());

            ResultSet rset = pstatement.executeQuery();
            if (rset.next()) {
                login.setLogin(rset.getString(6));
                login.setPassword(rset.getString(5));
            }
            else {
                return "nie udało się lognąć";
            }

            session.setAttribute("login", login.getLogin());
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "" + session.getAttribute("login");
    }


    @NetAPI(description = "Register user",
            status = Status.GenerallyAvailable,
            version = "1.0")
    //String name, String surname, String email, String password
    public String register(User user) {
        PreparedStatement pstatement = null;
        Connection con = null;

        URL u = null;
        try {
            u = new URL( user.getFb());
            HttpURLConnection huc =  ( HttpURLConnection )  u.openConnection ();
            huc.setRequestMethod ("GET");  //OR  huc.setRequestMethod ("HEAD");
            huc.connect () ;
            int code = huc.getResponseCode() ;
            System.out.println(code);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Błędny link do Facebooka";
        } catch (ProtocolException e) {
            e.printStackTrace();
            return "Błędny link do Facebooka";
        } catch (IOException e) {
            e.printStackTrace();
            return "Błędny link do Facebooka";
        }

        try {

            Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
            Matcher mat = pattern.matcher(user.getEmail());

            if(mat.matches()){

            }else{
                return "Niepoprawny email";
            }

            String userName = null;
            String mail = null;

            user.setPassword(MD5(user.getPassword()));
            con = DriverManager.getConnection(CONN, USER, PASS);
            System.out.println("Connected");
            Statement stmt = con.createStatement();

            pstatement = con.prepareStatement("SELECT * FROM `users` WHERE `login` = ? OR `email`= ?");
            pstatement.setString(1, user.getLogin());
            pstatement.setString(2, user.getEmail());

            ResultSet rset = pstatement.executeQuery();
            if (rset.next()) {
                userName = rset.getString(6);
                mail = rset.getString(2);

                if (userName.equals(user.getLogin())) {
                    return "user istnieje";
                }
                if (mail.equals(user.getEmail())) {
                    return "email istnieje";
                }
            }

            if(user.getPassword().equals(MD5(user.getRepassword())) == false){
                return "hasło się nie zgadzają";
            }


            pstatement = con.prepareStatement("INSERT INTO `users`(`email`, `name`, `surname`, `password`, `login`, `fb`) VALUES (?,?,?,?,?,?)");
            pstatement.setString(1, user.getEmail());
            pstatement.setString(2, user.getName());
            pstatement.setString(3, user.getSurname());
            pstatement.setString(4, user.getPassword());
            pstatement.setString(5, user.getLogin());
            pstatement.setString(6, user.getFb());

            pstatement.executeUpdate();

            pstatement = con.prepareStatement("INSERT INTO `subjects`(`login`) VALUES (?)");
            pstatement.setString(1, user.getLogin());

            pstatement.executeUpdate();


            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @NetAPI(description = "Logout",
            status = Status.GenerallyAvailable,
            version = "1.0")
    public String logOut(){

        session.setAttribute("login", null);
        return "";
    }

}