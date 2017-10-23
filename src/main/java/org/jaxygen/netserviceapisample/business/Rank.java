package org.jaxygen.netserviceapisample.business;

import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Rank {

    private static final String USER = "root";
    private static final String PASS = "";
    private static final String CONN = "jdbc:mysql://nexus.b.snet.ovh/ourhelp?useLegacyDatetimeCode=false&serverTimezone=America/New_York";

    @NetAPI(description = "show users rank",
            status = Status.GenerallyAvailable,
            version = "1.0")
    public List<User> rank() {

        PreparedStatement pstatement = null;
        Connection con = null;

        List<User> result = new ArrayList<>();

        int lenght = 0;

        try {
            con = DriverManager.getConnection(CONN, USER, PASS);
            Statement stmt = con.createStatement();


            pstatement = con.prepareStatement("SELECT * FROM `users` ORDER BY `exp` DESC");

            ResultSet rset = pstatement.executeQuery();
                while(rset.next()) {
                    User user = new User();
                    user.setLogin(rset.getString(6));
                    user.setExp(rset.getInt(7));
                    result.add(user);
                }



        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

}
