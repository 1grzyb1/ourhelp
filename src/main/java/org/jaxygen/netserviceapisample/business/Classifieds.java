package org.jaxygen.netserviceapisample.business;

import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.SessionContext;
import org.jaxygen.annotations.Status;
import org.jaxygen.netserviceapisample.business.dto.ProblemDTO;

import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Classifieds {

    @SessionContext
    public HttpSession session;

    private static final String USER = "root";
    private static final String PASS = "";
    private static final String CONN = "jdbc:mysql://nexus.b.snet.ovh:3306/grzyb_ourhelp?useLegacyDatetimeCode=false&serverTimezone=America/New_York";
    public String ret;

    @NetAPI(description = "Add new classified",
            status = Status.GenerallyAvailable,
            version = "1.0")
    public String addClaaifieds(Add add) {
        PreparedStatement pstatement = null;
        Connection con = null;

        int lvl = 0;
        String fb = null;

        if (session.getAttribute("login") == null) return "Aby dodać problem musisz się zalogować.";

        if (add.getText().equals("") || add.getTitle().equals("") || add.getType().equals(""))
            return "żadne pole nie może być puste";

        try {
            con = DriverManager.getConnection(CONN, USER, PASS);
            System.out.println("Connected");
            Statement stmt = con.createStatement();

            pstatement = con.prepareStatement("SELECT * FROM `users` WHERE `login` = ?");
            pstatement.setString(1, (String) session.getAttribute("login"));
//            pstatement.setString(1, "1grzyb1");

            ResultSet rset = pstatement.executeQuery();
            if (rset.next()) {
                lvl = rset.getInt(7);
                fb = rset.getString(8);
            }

            pstatement = con.prepareStatement("INSERT INTO `classifieds`(`id`, `login`, `lvl`, `title`, `text`, `type`, `fb`) VALUES (?,?,?,?,?,?,?);");
            pstatement.setString(2, (String) session.getAttribute("login"));
            pstatement.setInt(1, 0);
//            pstatement.setString(2, "login");
            pstatement.setInt(3, lvl);
            pstatement.setString(4, add.getTitle());
            pstatement.setString(5, add.getText());
            pstatement.setString(6, add.getType());
            pstatement.setString(7, fb);

            pstatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "jp2gmd";
    }

    @NetAPI(description = "chow classifieds",
            status = Status.GenerallyAvailable,
            version = "1.0")
    public List<ProblemDTO> getClassifieds() {
        PreparedStatement pstatement;
        Connection con;
        List<ProblemDTO> result = new ArrayList<>();

        int lenght = 0;

        try {
            con = DriverManager.getConnection(CONN, USER, PASS);
            System.out.println("Connected");
            Statement stmt = con.createStatement();

            pstatement = con.prepareStatement("SELECT  `login`, `title`, `text`, `type`, `progress`, `id`, `fb`, `lvl` FROM `classifieds` WHERE progress = 0  ORDER BY lvl DESC");

            ResultSet rset = pstatement.executeQuery();

            while (rset.next()) {
                ProblemDTO problemDTO = new ProblemDTO();
                problemDTO.setLogin(rset.getString(1));
                problemDTO.setTitle(rset.getString(2));
                problemDTO.setDescription(rset.getString(3));
                problemDTO.setType(rset.getString(4));
                problemDTO.setId(rset.getInt(5));
                problemDTO.setFb(rset.getString(7));

                result.add(problemDTO);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
    @NetAPI(description = "chow Specific classifieds",
            status = Status.GenerallyAvailable,
            version = "1.0")
    public List<ProblemDTO> getSpecificClassifieds(ProblemDTO problem) {
        PreparedStatement pstatement;
        Connection con;
        List<ProblemDTO> result = new ArrayList<>();

        int lenght = 0;

        try {
            con = DriverManager.getConnection(CONN, USER, PASS);
            System.out.println("Connected");
            Statement stmt = con.createStatement();

            pstatement = con.prepareStatement("SELECT  `login`, `title`, `text`, `type`, `progress`, `id`, `fb`, `lvl` FROM `classifieds` WHERE type = ? AND progress = 0 ORDER BY lvl DESC");
            pstatement.setString(1, problem.getType());
            ResultSet rset = pstatement.executeQuery();

            while (rset.next()) {
                ProblemDTO problemDTO = new ProblemDTO();
                problemDTO.setLogin(rset.getString(1));
                problemDTO.setTitle(rset.getString(2));
                problemDTO.setDescription(rset.getString(3));
                problemDTO.setType(rset.getString(4));
                problemDTO.setId(rset.getInt(5));

                result.add(problemDTO);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }



}
