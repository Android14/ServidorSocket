/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel
 */
public class PuntosDAO {

    private SimpleDateFormat dateFormat;

    public PuntosDAO() {

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public Puntos getPunto(int idPunto) {

        Puntos empresa = null;

        try {
            Connection connection = DBHelper.getConnection();

            String consulta = "SELECT * "
                    + "FROM Puntos "
                    + "WHERE Id = ?";
            HashMap<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("1", idPunto);

            ResultSet rs = DBHelper.executeQuery(connection, consulta, parametros);
            if (rs.next()) {
                empresa = resultSetToPuntos(rs);
            }
            rs.close();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PuntosDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PuntosDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PuntosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return empresa;
    }

    public List<Puntos> getPuntos(String fecha) {

        List<Puntos> listEmpresas = new ArrayList<Puntos>();

        try {

            Connection connection = DBHelper.getConnection();

            String consulta = "SELECT * "
                    + "FROM Puntos "
                    + "WHERE FechaActualizacion > CAST(? AS DATETIME)";
            HashMap<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("1", fecha);

            ResultSet rs = DBHelper.executeQuery(connection, consulta, parametros);
            while (rs.next()) {
                Puntos empresa = resultSetToPuntos(rs);

                listEmpresas.add(empresa);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PuntosDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PuntosDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PuntosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listEmpresas;
    }

    private Puntos resultSetToPuntos(ResultSet rs) throws SQLException, ParseException {

        Puntos punto = new Puntos();
        punto.setId(rs.getInt("Id"));
        punto.setNombre(rs.getString("Nombre"));
        punto.setDescripcion(rs.getString("Descripcion"));
        punto.setLatitud(rs.getDouble("Latitud"));
        punto.setLongitud(rs.getDouble("Longitud"));
        punto.setFechaActualizacion(rs.getString("FechaActualizacion"));
        

        return punto;
    }
}
