/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author branp
 */
public class DB {
    protected Conexion Con;
    protected ResultSet Rs;
    protected Connection Conn;
    protected PreparedStatement stmt = null;

    public DB() {
    }

    public Conexion getCon() {
        return Con;
    }

    public void setCon(Conexion Con) {
        this.Con = Con;
    }

    public ResultSet getRs() {
        return Rs;
    }

    public void setRs(ResultSet Rs) {
        this.Rs = Rs;
    }

    public Connection getConn() {
        return Conn;
    }

    public void setConn(Connection Conn) {
        this.Conn = Conn;
    }

    public PreparedStatement getStmt() {
        return stmt;
    }

    public void setStmt(PreparedStatement stmt) {
        this.stmt = stmt;
    }
    protected void CerrarRecursos() throws SQLException {
        if (Rs != null) {
            Rs.close();
        }
        if (stmt != null) {
            stmt.close();
        }
        if (Conn != null) {
            Conn.close();
        }
        Con.CerrarConexiones();
    }
}
