package org.exercise.nations;

import java.sql.*;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/db_nations";
        String user = "root";
        String password = "root";

        System.out.println("Inserisci una stringa di ricerca per nazione");
        Scanner scan = new Scanner(System.in);
        String filter = scan.nextLine();
        scan.close();

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = """
                    select c.name as country_name , c.country_id , r.name as region_name ,c2.name as continent_name from countries c
                    join regions r on r.region_id = c.region_id 
                    join continents c2 on r.continent_id = c2.continent_id
                    where c.name like ?
                    order by c.name 
                            """;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, "%" + filter + "%");

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String countryName = rs.getString(1);
                        int countryId = rs.getInt(2);
                        String regionName = rs.getString(3);
                        String continentName = rs.getString(4);

                        System.out.println(countryName + "  " + countryId + "  " + regionName + "  " + continentName);
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
