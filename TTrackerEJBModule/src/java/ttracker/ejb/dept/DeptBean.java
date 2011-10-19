package ttracker.ejb.dept;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.NoSuchEntityException;
import javax.ejb.RemoveException;
import javax.sql.DataSource;
import ttracker.dao.SQLConsts;

/**
 * Dept bean implementation.
 */
public class DeptBean implements EntityBean {

    /* Bean attributes */
    private EntityContext context;
    private Integer id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Find all depts
     * @return List of depts keys
     * @throws FinderException 
     */
    public Collection<Integer> ejbFindAll() throws FinderException {
//        System.out.println("ejbFindAll()");
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            st = con.prepareStatement(SQLConsts.GET_DEPT_KEYS);
            rs = st.executeQuery();
            Collection list = new ArrayList();
            while (rs.next()) {
                list.add(new Integer(rs.getInt("id_dept")));
            }
            return list;
        } catch (SQLException ex) {
            throw new FinderException("No dept was found: " + ex.getMessage());
        } finally {
            //free connection resources
            try {
                releaseConnection(rs, st, con);
            } catch (SQLException ex) {
                throw new FinderException("No dept was found. Cannot free resources: " + ex.getMessage());
            }
        }
    }

    /**
     * Find dept by id
     * @param id Dept id
     * @return Dept primary key
     * @throws FinderException Cannot find dept
     */
    public Integer ejbFindByPrimaryKey(Integer id) throws FinderException {
//        System.out.println("ejbFindByPrimaryKey()");
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            st = con.prepareStatement(SQLConsts.EXISTS_DEPT);
            st.setInt(1, id.intValue());
            rs = st.executeQuery();
            if (!rs.next()) {
                throw new FinderException("No dept was found. Id = " + id);
            }
            this.id = id;
            return id;
        } catch (SQLException e) {
            throw new FinderException("SQL error while getting dept: " + e);
        } finally {
            //free connection resources
            try {
                releaseConnection(rs, st, con);
            } catch (SQLException ex) {
                throw new FinderException("No dept was found. Cannot free resources: " + ex.getMessage());
            }
        }

    }

    public void ejbStore() throws EJBException, RemoteException {
//        System.out.println("ejbStore()");
        //TODO ejbStore DeptBean
    }

    public void ejbLoad() throws EJBException, RemoteException {
//        System.out.println("ejbLoad()");
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            st = con.prepareStatement(SQLConsts.DEPT_INFO_BY_ID);
            st.setInt(1, this.id.intValue());
            rs = st.executeQuery();
            if (!rs.next()) {
                throw new NoSuchEntityException("In selectRow: Row does not exist");
            }
            this.name = rs.getString("DEPT_NAME");
        } catch (SQLException ex) {
            throw new RemoteException("Cannot load dept", ex);
        } finally {
            //free connection resources
            try {
                releaseConnection(rs, st, con);
            } catch (SQLException ex) {
                throw new RemoteException("Cannot load dept. Cannot free resources", ex);
            }
        }
    }

    public void ejbActivate() throws EJBException, RemoteException {
//        System.out.println("ejbActivate()");
        this.id = (Integer) context.getPrimaryKey();

    }

    public void ejbPassivate() throws EJBException, RemoteException {
//        System.out.println("ejbPassivate()");
        this.id = null;
    }

    public void setEntityContext(EntityContext context) throws EJBException,
            RemoteException {
        this.context = context;
    }

    public void unsetEntityContext() throws EJBException, RemoteException {
        context = null;
    }

    public void ejbRemove() throws RemoveException, EJBException, RemoteException {
        //Deleting of employeers not available
    }

    /**
     * Create connection with database
     * @return Connection
     */
    private Connection getConnection() throws SQLException {
        return ((DataSource) context.lookup(SQLConsts.DB_NAME)).getConnection();
    }

    /**
     * Free resources
     * @param con Connection for free
     * @throws SQLException 
     */
    private void releaseConnection(ResultSet rs, PreparedStatement ps, Connection con) throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (con != null) {
            con.close();
        }
    }

}
