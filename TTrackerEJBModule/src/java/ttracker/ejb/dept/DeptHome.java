package ttracker.ejb.dept;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * Department home interface.
 */
public interface DeptHome extends EJBHome {

    /* Find single dept by unique id.  */
    public Dept findByPrimaryKey(Integer id) throws FinderException, RemoteException;
    
    /* Find all depts */
    public Collection<Dept> findAll() throws FinderException, RemoteException;

}
