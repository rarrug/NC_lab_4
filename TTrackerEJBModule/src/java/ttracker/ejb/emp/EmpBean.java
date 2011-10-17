package ttracker.ejb.emp;

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
import ttracker.ejb.dept.DeptRecord;

/**
 * Employee bean implementation.
 */
public class EmpBean implements EntityBean {

    /* Bean attributes */
    private EntityContext context;
    private Integer id;
    private String name;
    private String job;
    private DeptRecord dept;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public Integer getId() {
        return id;
    }

    public String getDeptName() {
        return dept.getDeptName();
    }

    /**
     * Find all employees
     * @return List of employee keys
     * @throws FinderException 
     */
    public Collection ejbFindAll() throws FinderException {
//        System.out.println("ejbFindAll()");
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            st = con.prepareStatement(SQLConsts.GET_EMP_KEYS);
            rs = st.executeQuery();
            Collection list = new ArrayList();
            while (rs.next()) {
                list.add(new Integer(rs.getInt("id_emp")));
            }
            return list;
        } catch (SQLException ex) {
            throw new FinderException("No employee was found: " + ex.getMessage());
        } finally {
            //free connection resources
            try {
                releaseConnection(rs, st, con);
            } catch (SQLException ex) {
                throw new FinderException("No employee was found. Cannot free resources: " + ex.getMessage());
            }
        }
    }

    /**
     * Find employee by id
     * @param id Employee id
     * @return Employee primary key
     * @throws FinderException Cannot find employee
     */
    public Integer ejbFindByPrimaryKey(Integer id) throws FinderException {
//        System.out.println("ejbFindByPrimaryKey()");
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            st = con.prepareStatement(SQLConsts.EXISTS_EMP);
            st.setInt(1, id.intValue());
            rs = st.executeQuery();
            if (!rs.next()) {
                throw new FinderException("No task was found. Id = " + id);
            }
            this.id = id;
            return id;
        } catch (SQLException e) {
            throw new FinderException("SQL error while getting task: " + e);
        } finally {
            //free connection resources
            try {
                releaseConnection(rs, st, con);
            } catch (SQLException ex) {
                throw new FinderException("No employee was found. Cannot free resources: " + ex.getMessage());
            }
        }

    }

    public void ejbStore() throws EJBException, RemoteException {
//        System.out.println("ejbStore()");
        //TODO ejbStore EmpBean
    }

    public void ejbLoad() throws EJBException, RemoteException {
//        System.out.println("ejbLoad()");
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            st = con.prepareStatement(SQLConsts.EMP_INFO_BY_ID);
            st.setInt(1, this.id.intValue());
            rs = st.executeQuery();
            if (!rs.next()) {
                throw new NoSuchEntityException("In selectRow: Row does not exist");
            }
            this.name = rs.getString("emp_fio");
            this.job = rs.getString("job");
            this.dept = new DeptRecord(rs.getInt("id_dept"), rs.getString("dept_name"));
        } catch (SQLException ex) {
            throw new RemoteException("Cannot load employee: " + ex.getMessage());
        } finally {
            //free connection resources
            try {
                releaseConnection(rs, st, con);
            } catch (SQLException ex) {
                throw new RemoteException("Cannot load employee. Cannot free resources: " + ex.getMessage());
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
