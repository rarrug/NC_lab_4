package ttracker.dao;

import ttracker.ejb.dept.Dept;
import ttracker.ejb.emp.Emp;
import ttracker.ejb.task.Task;

public class Info {

    private Task task;//Information about task
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
    }
}
