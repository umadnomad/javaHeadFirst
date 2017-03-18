/**
 * Created by nomad on 16/03/17.
 */
package com.riccardofinazzi.RMI;

import java.awt.*;
import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.*;
import java.awt.event.*;

public class ServiceBrowser {

    JPanel        mainPanel;
    JComboBox     serviceList;
    ServiceServer server;

    public void buildGUI() {
        JFrame frame = new JFrame("RMI Browser");
        mainPanel = new JPanel();
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);

        Object[] services = getServiceList();

        serviceList = new JComboBox(services);

        frame.getContentPane().add(BorderLayout.NORTH, serviceList);
        serviceList.addActionListener(new MyListListener());

        frame.setSize(500, 500);
        frame.setVisible(true);

    }

    void loadService(Object serviceSelection) {
        try {
            Service svc = server.getService(serviceSelection);

            mainPanel.removeAll();
            mainPanel.add(svc.getGuiPanel());
            mainPanel.validate();
            mainPanel.repaint();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    // investigate better this recursive function
    Object[] getServiceList() {
        Object   obj      = null;
        Object[] services = null;

        try {
            obj = Naming.lookup("rmi://127.0.0.1/ServiceServer");
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }

        server = (ServiceServer) obj;

        try {
            services = server.getServiceList();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return services;
    }

    class MyListListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object selection = serviceList.getSelectedItem();
            loadService(selection);
        }
    }

    public static void main(String[] args) {
        new ServiceBrowser().buildGUI();
    }

}
