package org.exercise.nations;

import java.sql.*;

public class main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/db_nat ions";
        String user = "root";
        String password = "root";


        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = """
                    select c.name as country_name , c.country_id , r.name as region_name ,c2.name as continent_name from countries c
                    join regions r on r.region_id = c.region_id 
                    join continents c2 on r.continent_id = r.region_id 
                    order by c.name 
                            """;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
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
