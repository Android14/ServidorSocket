/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Miguel
 */
public class DBHelper {

    /**
     * Retorna una nueva instancia de acceso a la base de datos
     * @return Connection
     * @throws SQLException
     * @throws IOException
     * @throws Exception 
     */
    public static Connection getConnection() 
            throws ClassNotFoundException, SQLException {

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection("jdbc:sqlserver://localhost:1433;user=sa;password=mjpp;database=Localizador");

        } catch (ClassNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            throw e;
        }
    }
    
    /**
     * Ejecuta un comando(INSERT,UPDATE,DELETE) en la base de datos
     * @param connection Connection sobre el cual se ejecutara el comando
     * @param query Comando a ejecutar en formato SQL
     * @param parametros Parametros del query si los hay o null si no los hay
     * @return El id asignado en la base de datos si el comando es INSERT o la cantidad de registros afectados en
     * los demas comandos
     * @throws SQLException 
     */
    public static int executeCommand(Connection connection, String query, HashMap<String, Object> parametros) throws SQLException {
        int id = -1;
        if (connection != null && !connection.isClosed()) {
            PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            if (parametros != null) {
                for (Map.Entry<String, Object> parametro : parametros.entrySet()) {
                    ps.setObject(Integer.parseInt(parametro.getKey()), parametro.getValue());
                }
            }
            id = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int _id = rs.getInt(1);
                if (_id > 0) {
                    // ## Es una insercion
                    id = _id;
                }
            }
            rs.close();
        }
        return id;
    }

    /**
     * Ejecuta una consulta a la base de datos
     * @param connection Connection sobre la cual se ejecutara la consulta
     * @param query Consulta a ejecutar
     * @param parametros Parametros de query si los hay o null si no los hay
     * @return ResulSet con el resultado de la consulta
     * @throws SQLException 
     */
    public static ResultSet executeQuery(Connection connection, String query, HashMap<String, Object> parametros) throws SQLException {
        ResultSet resultSet = null;

        if (connection != null && !connection.isClosed()) {
            PreparedStatement ps = connection.prepareStatement(query);
            if (parametros != null) {
                for (Map.Entry<String, Object> parametro : parametros.entrySet()) {
                    ps.setObject(Integer.parseInt(parametro.getKey()), parametro.getValue());
                }
            }
            resultSet = ps.executeQuery();
        }

        return resultSet;
    }

    /**
     * Ejecuta un procedimiento en la base de datos
     * @param connection Connection sobre la cual se ejecutara el procedimiento
     * @param procedureName Nombre del procedimiento a ejecutar
     * @param parametros Parametros del procedimiento o nul si no los hay
     * @return ResulSet con el resultado del procedimiento
     * @throws SQLException 
     */
    public static ResultSet executeProcedure(Connection connection, String procedureName, HashMap<String, Object> parametros) throws SQLException {
        ResultSet resultSet = null;
        String storedProcedure = "CALL " + procedureName + "(";

        if (connection != null && !connection.isClosed()) {
            if (parametros != null) {
                for (int i = 0; i < parametros.size(); i++) {
                    storedProcedure += ((i + 1) == parametros.size()) ? "?" : "?,";
                }
            }
            CallableStatement cs = connection.prepareCall(storedProcedure + ")");
            if (parametros != null) {
                for (Map.Entry<String, Object> parametro : parametros.entrySet()) {
                    cs.setObject(parametro.getKey(), parametro.getValue());
                }
            }
            resultSet = cs.executeQuery();
        }

        return resultSet;
    }

    /**
     * Ejecuta una consulta a la base de datos paginando los resultados
     * @param connection Connection sobre la cual se ejecutara la consulta
     * @param query Consulta a ejecutar
     * @param parametros Parametros de query si los hay o null si no los hay
     * @param offset Indice a partir del cual se obtendran los resultados
     * @param rowCount Cantidad de filas a retornar
     * @return ResultSet con el resultado de la consulta
     * @throws SQLException 
     */
    public static ResultSet executeQuery(Connection connection, String query, HashMap<String, Object> parametros,
            int offset, int rowCount) throws SQLException {

        ResultSet resultSet = null;
        query += " LIMIT :offset , :rowCount";

        if (connection != null && !connection.isClosed()) {
            PreparedStatement ps = connection.prepareStatement(query);
            if (parametros != null) {
                parametros.put("offset", offset);
                parametros.put("rowCount", rowCount);
                for (Map.Entry<String, Object> parametro : parametros.entrySet()) {
                    ps.setObject(Integer.parseInt(parametro.getKey()), parametro.getValue());
                }
            }
            resultSet = ps.executeQuery();
        }

        return resultSet;

    }
}
