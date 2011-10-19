package ttracker.ejb.task;

import ttracker.dao.SQLConsts;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.NoSuchEntityException;
import javax.ejb.RemoveException;
import javax.sql.DataSource;

/**
 * Task bean implementation.
 */
public class TaskBean implements EntityBean {

    /* Bean attributes */
    private EntityContext context;
    private Integer id;
    private String name;
    private Integer parentId;
    private Date begin;
    private Date end;
    private String status;
    private String description;
    private Integer empId;

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * Find all tasks
     * @param hier true if need hierarchical list and false another
     * @return List of task keys
     * @throws FinderException Cannot find tasks
     */
    public Collection<Integer> ejbFindAll(boolean hier) throws FinderException {
//        System.out.println("ejbFindAll()");
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            if (hier) {
                st = con.prepareStatement(SQLConsts.GET_TASK_HIERARCHY);
            } else {
                st = con.prepareStatement(SQLConsts.GET_TASK_KEYS);
            }
            rs = st.executeQuery();
            Collection list = new ArrayList();
            while (rs.next()) {
                list.add(new Integer(rs.getInt("id_task")));
            }
            return list;
        } catch (SQLException ex) {
            throw new FinderException("No task was found: " + ex.getMessage());
        } finally {
            //free connection resources
            try {
                releaseConnection(rs, st, con);
            } catch (SQLException ex) {
                throw new FinderException("No task was found. Cannot free resources: " + ex.getMessage());
            }
        }
    }

    /**
     * Find task by id
     * @param id Task primary key
     * @return Task primary key
     * @throws FinderException Cannot find task
     */
    public Integer ejbFindByPrimaryKey(Integer id) throws FinderException {
//        System.out.println("ejbFindByPrimaryKey()");
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            st = con.prepareStatement(SQLConsts.EXISTS_TASK);
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
                throw new FinderException("No task was found. Cannot free resources: " + ex.getMessage());
            }
        }
    }

    /**
     * Find tasks by some string parameter
     * @param param Find parameter
     * @param sql SQL query
     * @return List of tasks keys
     * @throws FinderException Cannot find tasks
     */
    private Collection findByParameter(String param, String sql) throws FinderException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            st = con.prepareStatement(sql);
            st.setString(1, param);
            rs = st.executeQuery();
            Collection list = new ArrayList();
            while (rs.next()) {
                list.add(new Integer(rs.getInt("id_task")));
            }
            return list;
        } catch (SQLException ex) {
            throw new FinderException("No task was found: " + ex.getMessage());
        } finally {
            //free connection resources
            try {
                releaseConnection(rs, st, con);
            } catch (SQLException ex) {
                throw new FinderException("No task was found. Cannot free resources: " + ex.getMessage());
            }
        }
    }

    /**
     * Find task by name
     * @param name Task name
     * @return Task primary key
     * @throws FinderException Cannot find tasks
     */
    public Collection ejbFindByName(String name) throws FinderException {
//        System.out.println("ejbFindByName()");
        return findByParameter(name, SQLConsts.SELECT_TASK_BY_NAME);
    }

    /**
     * Find task by employee name
     * @param emp Employee name
     * @return Task primary key
     * @throws FinderException Cannot find tasks
     */
    public Collection ejbFindByEmp(String emp) throws FinderException {
//        System.out.println("ejbFindByName()");
        return findByParameter(emp, SQLConsts.SELECT_TASK_BY_EMP);
    }

    /**
     * Create new task
     * @param task New task object
     * @return Primary key of new task
     * @throws CreateException
     * @throws RemoteException 
     */
    public Integer ejbCreate(TaskRecord newTask) throws CreateException, RemoteException {
//        System.out.println("ejbCreate");
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            st = con.prepareStatement(SQLConsts.INSERT_TASK);
            st.setString(1, newTask.getName());
            if (newTask.getParentId().compareTo(Integer.valueOf(0)) == 0) {
                st.setNull(2, java.sql.Types.INTEGER);
            } else {
                st.setInt(2, newTask.getParentId());
            }
            st.setString(3, newTask.getEmp().getEmpName());
            st.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(newTask.getBegin()));
            st.setString(5, new SimpleDateFormat("yyyy-MM-dd").format(newTask.getEnd()));
            st.setString(6, newTask.getStatus());
            st.setString(7, newTask.getDescription());
            st.executeQuery();
            st = con.prepareStatement(SQLConsts.GET_TASK_ID_BY_NAME);
            st.setString(1, newTask.getName());
            rs = st.executeQuery();
            if (rs.next()) {
                id = Integer.valueOf(rs.getInt("id_task"));
            }
            return id;
        } catch (SQLException ex) {
            throw new CreateException("Cannot create task: " + ex.getMessage());
        } finally {
            //free connection resources
            try {
                releaseConnection(rs, st, con);
            } catch (SQLException ex) {
                throw new CreateException("Cannot create task. Cannot free resources: " + ex.getMessage());
            }
        }
    }

    public void ejbPostCreate(TaskRecord task)
            throws CreateException {
//        System.out.println("ejbPostCreate");
    }

    /**
     * Modify task
     * @param task New task object
     * @throws RemoteException 
     */
    public void ejbHomeModify(TaskRecord task) throws RemoteException {
//        System.out.println("ejbHomeModify()");
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = getConnection();
            st = con.prepareStatement(SQLConsts.UPDATE_TASK);
            st.setString(1, task.getName());
            if (task.getParentId() == 0) {
                st.setNull(2, java.sql.Types.INTEGER);
            } else {
                st.setInt(2, task.getParentId());
            }
            st.setString(3, task.getEmp().getEmpName());
            st.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(task.getBegin()));
            st.setString(5, new SimpleDateFormat("yyyy-MM-dd").format(task.getEnd()));
            st.setString(6, task.getStatus());
            st.setString(7, task.getDescription());
            st.setInt(8, task.getId());

            //st.executeQuery();
            st.execute();
        } catch (SQLException ex) {
            throw new RemoteException("Cannot modify task", ex);
        } finally {
            //free connection resources
            try {
                releaseConnection(null, st, con);
            } catch (SQLException ex) {
                throw new RemoteException("Cannot modify task. Cannot free resources", ex);
            }
        }
    }

    public void ejbStore() throws EJBException, RemoteException {
//        System.out.println("ejbStore()");
        //TODO ejbStore TaskBen
        //do not use. nothing update 
    }

    public void ejbLoad() throws EJBException, RemoteException {
//        System.out.println("ejbLoad()");
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            st = con.prepareStatement(SQLConsts.SELECT_TASK_BY_ID);
            st.setInt(1, this.id.intValue());
            rs = st.executeQuery();
            if (!rs.next()) {
                throw new NoSuchEntityException("Task does not exist");
            }
            this.name = rs.getString("task_name");
            this.parentId = new Integer(rs.getInt("parent_task"));
            this.begin = rs.getDate("date_begin");
            this.end = rs.getDate("date_end");
            this.status = rs.getString("status");
            this.empId = rs.getInt("id_emp");
            this.description = rs.getString("descr");
        } catch (SQLException ex) {
            throw new RemoteException("Cannot load task", ex);
        } finally {
            //free connection resources
            try {
                releaseConnection(rs, st, con);
            } catch (SQLException ex) {
                throw new RemoteException("Cannot load task. Cannot free resources", ex);
            }
        }
    }

    public void ejbActivate() throws EJBException, RemoteException {
//        System.out.println("ejbActivate()");
        this.id = (Integer) context.getPrimaryKey();
    }

    public void ejbPassivate() throws EJBException, RemoteException {
//        System.out.println("ejbPassivate()");
        id = null;
    }

    /**
     * Remove task from database
     * @throws RemoveException
     * @throws EJBException
     * @throws RemoteException 
     */
    public void ejbRemove() throws RemoveException, EJBException, RemoteException {
//        System.out.println("ejbRemove");
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = getConnection();
            st = con.prepareStatement(SQLConsts.DELETE_TASK);
            st.setInt(1, this.id.intValue());
            st.execute();
        } catch (SQLException ex) {
            throw new RemoveException("Cannot remove task: " + ex.getMessage());
        } finally {
            //free connection resources
            try {
                releaseConnection(null, st, con);
            } catch (SQLException ex) {
                throw new RemoteException("Cannot remove task. Cannot free resources", ex);
            }
        }
    }

    public void setEntityContext(EntityContext context) throws EJBException,
            RemoteException {
//        System.out.println("setEntityContext");
        this.context = context;
    }

    public void unsetEntityContext() throws EJBException, RemoteException {
//        System.out.println("unsetEntityContext");
        context = null;
    }

    /**
     * Create connection with database
     * @return Created connection
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
