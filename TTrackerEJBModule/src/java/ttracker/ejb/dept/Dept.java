package ttracker.ejb.dept;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;

/**
 * Employee component remote interface. 
 */
public interface Dept extends EJBObject {

    public Integer getId() throws RemoteException;

    public String getName() throws RemoteException;

}
