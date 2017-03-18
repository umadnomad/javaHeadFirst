/**
 * Created by nomad on 16/03/17.
 */
package com.riccardofinazzi.RMI;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class ServiceServImpl extends UnicastRemoteObject implements ServiceServer {

    public ServiceServImpl() throws RemoteException {
        setUpServices();
    }

    private void setUpServices() {
        serviceList = new HashMap();
        serviceList.put("Dice Rolling Service", new DiceService());
        serviceList.put("Day of the Week Service", new DayOfTheWeekService());
        serviceList.put("Visual Music Service", new MiniMusicService());
    }

    HashMap serviceList;

    @Override
    public Object[] getServiceList() throws RemoteException {
        System.out.println("in remote");
        return serviceList.keySet().toArray();
    }

    @Override
    public Service getService(Object serviceKey) throws RemoteException {
        return (Service) serviceList.get(serviceKey);
    }

    public static void main(String[] args) {
        try {
            Naming.rebind("ServiceServer", new ServiceServImpl());
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println("Remote service is running");
    }
}
