package ttracker.dao;

import java.util.Collection;
import ttracker.dao.exc.TrackerException;
import ttracker.ejb.task.TaskRecord;

public interface IModel {

    /* Task methods */
    Info getTaskById(Integer taskId) throws TrackerException;

    Collection<Info> getTaskByName(String name) throws TrackerException;
    
    Collection<Info> getTaskByEmp(String emp) throws TrackerException;

    Collection<Info> getAllTasks(boolean hier) throws TrackerException;

    void addTask(TaskRecord newTask) throws TrackerException;

    void deleteTask(Integer taskId) throws TrackerException;

    void modifyTask(TaskRecord task) throws TrackerException;

    /* Employee methods */
    Info getEmpById(Integer empId) throws TrackerException;

    Collection<Info> getAllEmps() throws TrackerException;
}
