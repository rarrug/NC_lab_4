package ttracker.dao;

import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.naming.NamingException;
import java.util.Collection;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;
import ttracker.dao.exc.TrackerException;
import ttracker.ejb.dept.Dept;
import ttracker.ejb.dept.DeptHome;
import ttracker.ejb.emp.Emp;
import ttracker.ejb.emp.EmpHome;
import ttracker.ejb.task.Task;
import ttracker.ejb.task.TaskHome;
import ttracker.ejb.task.TaskRecord;

/**
 * Make operation with database
 */
public class DBModel implements IModel {

    private TaskHome taskHome;
    private EmpHome empHome;
    private DeptHome deptHome;

    public DBModel() throws NamingException {
        /* initialize task home and emp home contexts */
        Context initial = new InitialContext();
        Object objRef = initial.lookup(SQLConsts.JNDI_TASK);
        taskHome = (TaskHome) javax.rmi.PortableRemoteObject.narrow(
                objRef, TaskHome.class);
        objRef = initial.lookup(SQLConsts.JNDI_EMP);
        empHome = (EmpHome) javax.rmi.PortableRemoteObject.narrow(
                objRef, EmpHome.class);
        objRef = initial.lookup(SQLConsts.JNDI_DEPT);
        deptHome = (DeptHome) javax.rmi.PortableRemoteObject.narrow(
                objRef, DeptHome.class);
    }

    /**
     * Select task by id
     * @param taskId Task number
     * @return Task with identificator id
     */
    public Info getTaskById(Integer taskId) throws TrackerException {
        try {
            Task task = taskHome.findByPrimaryKey(taskId);
            return getInfo(task);
        } catch (Exception ex) {
            throw new TrackerException("Cannot get task by id", ex);
        }
    }

    /**
     * Get list of task keys by some string parameter 
     * @param param Some string parameter (name, user name, etc.)
     * @param sql SQL query
     * @return
     */
    private Collection<Info> getTaskByParam(String param, String sql) throws FinderException, RemoteException {
        Collection<Task> taskLists = null;
        if (sql.equals(SQLConsts.SELECT_TASK_BY_NAME)) {
            taskLists = taskHome.findByName(param);
        } else if (sql.equals(SQLConsts.SELECT_TASK_BY_EMP)) {
            taskLists = taskHome.findByEmp(param);
        }
        Collection<Info> tasks = new ArrayList<Info>();
        for (Task task : taskLists) {
            tasks.add(getInfo(task));
        }
        return tasks;
    }

    /**
     * Select task by name
     * @param tName Task name
     * @return List of tasks with name - tName
     */
    public Collection<Info> getTaskByName(String tName) throws TrackerException {
        try {
            return getTaskByParam(tName, SQLConsts.SELECT_TASK_BY_NAME);
        } catch (Exception ex) {
            throw new TrackerException("Cannot get task by name", ex);
        }
    }

    /**
     * Select task by employee name
     * @param eName Employee name
     * @return List of tasks with employee name - eName
     */
    public Collection<Info> getTaskByEmp(String eName) throws TrackerException {
        try {
            return getTaskByParam(eName, SQLConsts.SELECT_TASK_BY_EMP);
        } catch (Exception ex) {
            throw new TrackerException("Cannot get task by emp name", ex);
        }
    }

    /**
     * Get full task list
     * @param hier true if need hierarchical list or false another
     * @return Task list
     */
    public Collection<Info> getAllTasks(boolean hier) throws TrackerException {
        try {
            Collection<Info> tvc = new ArrayList<Info>();
            for (Task task : taskHome.findAll(hier)) {
                tvc.add(getInfo(task));
            }
            return tvc;
        } catch (Exception ex) {
            throw new TrackerException("Cannot get task list: " + ex.getMessage(), ex);
        }
    }

    /**
     * Get info about task uncluding employee and dept info
     * @param task Task object
     * @return General information element
     * @throws FinderException
     * @throws RemoteException
     */
    private Info getInfo(Task task) throws FinderException, RemoteException {

        Emp emp = empHome.findByPrimaryKey(task.getEmpId());
        Dept dept = deptHome.findByPrimaryKey(emp.getDeptId());

        Info taskValue = new Info();
        taskValue.setTask(task);
        taskValue.setEmp(emp);
        taskValue.setDept(dept);

        return taskValue;
    }

    /**
     * Get info about employee uncluding dept info
     * @param emp Employee object
     * @return General information element
     * @throws FinderException
     * @throws RemoteException
     */
    private Info getInfo(Emp emp) throws FinderException, RemoteException {
        Dept dept = deptHome.findByPrimaryKey(emp.getDeptId());

        Info taskValue = new Info();
        taskValue.setEmp(emp);
        taskValue.setDept(dept);

        return taskValue;
    }

    /**
     * Add task record to database
     * @param newTask New task object
     */
    public void addTask(TaskRecord newTask) throws TrackerException {
        try {
            taskHome.create(newTask);
        } catch (Exception ex) {
            throw new TrackerException("Cannot add task", ex);
        }
    }

    /**
     * Delete task from database by id
     * @param taskId Task number
     */
    public void deleteTask(Integer taskId) throws TrackerException {
        try {
            taskHome.findByPrimaryKey(taskId).remove();
        } catch (Exception ex) {
            throw new TrackerException("Cannot delete task", ex);
        }
    }

    /**
     * Update task in database
     * @param task Modify task object
     */
    public void modifyTask(TaskRecord task) throws TrackerException {
        try {
            taskHome.modify(task);
        } catch (RemoteException ex) {
            throw new TrackerException("Cannot modify task", ex);
        }
    }

    /**
     * Get employee from database by id
     * @param empId Employee number
     * @return Employee object
     */
    public Info getEmpById(Integer empId) throws TrackerException {
        try {
            Emp emp = empHome.findByPrimaryKey(empId);
            return getInfo(emp);
        } catch (Exception ex) {
            throw new TrackerException("Cannot get employee by id", ex);
        }
    }

    /**
     * Get employee list
     * @return List of employee
     */
    public Collection<Info> getAllEmps() throws TrackerException {
        try {
            Collection<Info> emps = new ArrayList<Info>();
            for (Emp emp : empHome.findAll()) {
                emps.add(getInfo(emp));
            }
            return emps;
        } catch (Exception ex) {
            throw new TrackerException("Cannot get employee list", ex);
        }
    }
}
