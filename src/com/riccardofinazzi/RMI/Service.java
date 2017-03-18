/**
 * Created by nomad on 16/03/17.
 */
package com.riccardofinazzi.RMI;

import javax.swing.*;
import java.io.*;

public interface Service extends Serializable {
    JPanel getGuiPanel();
}
