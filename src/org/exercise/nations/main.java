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
        System.out.println("inserisci un id country:");
        int inputId = Integer.parseInt(scan.nextLine());


        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = """
                    select l.`language`  from countries c 
                    join country_languages cl on cl.country_id = c.country_id 
                    join languages l on cl.language_id = l.language_id 
                    where c.country_id = ?
                                                """;
            String sql2 = """
                    select cs.year, cs.population ,cs.gdp  from countries c 
                    join country_stats cs on cs.country_id = c.country_id 
                    where c.country_id = ?
                    order by cs.`year` desc
                    limit 1                   
                                        """;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, inputId);

                try (ResultSet rs = ps.executeQuery()) {
                    String lenguages = "Lenguages";

                    while (rs.next()) {
                        lenguages += rs.getString(1) + ", ";
                    }
                    System.out.println(lenguages);
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sql2)) {
                ps.setInt(1, inputId);

                try (ResultSet rs = ps.executeQuery()) {
                    System.out.println("most recent stats");
                    while (rs.next()) {
                        int year = rs.getInt(1);
                        int population = rs.getInt(2);
                        long gdp = rs.getLong(3);

                        System.out.println("year: " + year);
                        System.out.println("population: " + population);
                        System.out.println("gdp: " + gdp);

                    }
                }
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        scan.close();
    }
}
