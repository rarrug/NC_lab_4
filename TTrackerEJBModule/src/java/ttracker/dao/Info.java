package ttracker.dao;

import java.util.Date;
import ttracker.ejb.dept.Dept;
import ttracker.ejb.emp.Emp;
import ttracker.ejb.task.Task;

public class Info {

    /* task info */
    private Integer taskId;
    private String taskName;
    private Integer taskParentId;
    private Date taskBegin;
    private Date taskEnd;
    private String taskStatus;
    private String taskDescription;

    /* emp info */
    private Integer empId;
    private String empName;
    private String empJob;

    /*dept info */
    private Integer deptId;
    private String deptName;

    /* getters and setters */
    
    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpJob() {
        return empJob;
    }

    public void setEmpJob(String empJob) {
        this.empJob = empJob;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Date getTaskBegin() {
        return taskBegin;
    }

    public void setTaskBegin(Date taskBegin) {
        this.taskBegin = taskBegin;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Date getTaskEnd() {
        return taskEnd;
    }

    public void setTaskEnd(Date taskEnd) {
        this.taskEnd = taskEnd;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getTaskParentId() {
        return taskParentId;
    }

    public void setTaskParentId(Integer taskParentId) {
        this.taskParentId = taskParentId;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    /*private Task task;//Information about task
    private Emp emp;//Information about employee
    private Dept dept;//Information about department

    public Dept getDept() {
    return dept;
    }

    public void setDept(Dept dept) {
    this.dept = dept;
    }

    public Emp getEmp() {
    return emp;
    }

    public void setEmp(Emp emp) {
    this.emp = emp;
    }

    public Task getTask() {
    return task;
    }

    public void setTask(Task task) {
    this.task = task;
    }*/
}
