/**
 * Created by nomad on 16/03/17.
 */
package com.riccardofinazzi.RMI;

import java.rmi.*;

public interface ServiceServer extends Remote {
    Object[] getServiceList() throws RemoteException;

    Service getService(Object serviceKey) throws RemoteException;
}
