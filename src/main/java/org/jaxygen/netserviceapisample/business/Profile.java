package org.jaxygen.netserviceapisample.business;

import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.SessionContext;
import org.jaxygen.annotations.Status;
import org.jaxygen.netserviceapisample.business.dto.ProblemDTO;

import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Profile {

    @SessionContext
    public HttpSession session;

    private static final String USER = "root";
    private static final String PASS = "";
    private static final String CONN = "jdbc:mysql://localhost/ourhelp?useLegacyDatetimeCode=false&serverTimezone=America/New_York";
    public String ret;


    @NetAPI(description = "chow my classifieds",
            status = Status.GenerallyAvailable,
            version = "1.0")
    public List<ProblemDTO> showMyClassifieds() {
        PreparedStatement pstatement = null;
        Connection con = null;
        List<ProblemDTO> result = new ArrayList<>();

        int lenght = 0;

        try {
            con = DriverManager.getConnection(CONN, USER, PASS);
            System.out.println("Connected");
            Statement stmt = con.createStatement();

            if (session.getAttribute("login") == null) return null;


            pstatement = con.prepareStatement("SELECT * FROM `classifieds` WHERE `login` = ? AND `progress` = 0" );
            pstatement.setString(1, (String) session.getAttribute("login"));

            ResultSet rset = pstatement.executeQuery();
                while (rset.next()) {
                    ProblemDTO problemDTO = new ProblemDTO();
                    problemDTO.setTitle(rset.getString(4));
                    problemDTO.setId(rset.getInt(1));
                    problemDTO.setType(rset.getString(6));
                    result.add(problemDTO);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    @NetAPI(description = "When user help another user",
            status = Status.GenerallyAvailable,
            version = "1.0")
    public String helped(Helped helped) {
        PreparedStatement pstatement = null;
        Connection con = null;
        int expAdd = 0;
        double x = 0;

        try {
            con = DriverManager.getConnection(CONN, USER, PASS);
            pstatement = con.prepareStatement("UPDATE `classifieds` SET `progress`= 1 WHERE id = ?");
            pstatement.setInt(1, helped.getId());

            pstatement.executeUpdate();

            pstatement = con.prepareStatement("SELECT "+helped.getSubject()+" FROM `subjects` WHERE `login` = ? AND progress = 0");
            pstatement.setString(1, helped.getUser());

            ResultSet rset = pstatement.executeQuery();
            if (rset.next()) {
                x = rset.getInt(1);
            }

            expAdd = (int) (x + 20);

            pstatement = con.prepareStatement("UPDATE `subjects` SET `"+helped.getSubject()+"`= ? WHERE login = ?");
            pstatement.setInt(1, expAdd);
            pstatement.setString(2, helped.getUser());
            pstatement.executeUpdate();

            pstatement = con.prepareStatement("UPDATE `users` SET `exp`= ? WHERE login = ?");
            pstatement.setInt(1, expAdd);
            pstatement.setString(2, helped.getUser());
            pstatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }


    @NetAPI(description = "return user profile",
            status = Status.GenerallyAvailable,
            version = "1.0")
    public User getMyProfile(){
        PreparedStatement pstatement = null;
        Connection con = null;
        ResultSet rset = null;

        User user = new User();
       if ( session.getAttribute("login") == null) {
           return null;
       }

        try {
            con = DriverManager.getConnection(CONN, USER, PASS);

            pstatement = con.prepareStatement("SELECT * FROM `users` WHERE `login` = ?" );
            pstatement.setString(1, (String) session.getAttribute("login"));

            rset = pstatement.executeQuery();
                if (rset.next()) {
                    user.setEmail(rset.getString(2));
                    user.setName(rset.getString(3));
                    user.setSurname(rset.getString(4));
                    user.setLogin(rset.getString(6));
                    user.setExp(rset.getInt(7));
                }

            pstatement = con.prepareStatement("SELECT * FROM `subjects` WHERE `login` = ?" );
            pstatement.setString(1, (String) session.getAttribute("login"));

            rset = pstatement.executeQuery();
            if (rset.next()) {
                user.setBuilding(rset.getInt(3));
                user.setWriting(rset.getInt(4));
                user.setLearning(rset.getInt(5));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

}
