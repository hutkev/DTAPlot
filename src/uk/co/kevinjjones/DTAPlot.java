/**
 Copyright 2011 Kevin J. Jones (http://www.kevinjjones.co.uk)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package uk.co.kevinjjones;

import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.IAxis.AxisTitle;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.ZoomableChart;
import info.monitorenter.gui.chart.axis.AAxis;
import info.monitorenter.gui.chart.axis.AxisLinear;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.List;
import java.util.SortedSet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import net.iharder.dnd.FileDrop;

public class DTAPlot {

    private ZoomableChart _chart;
    private JComboBox _lapCombo;
    private JCheckBox _speedCheck;
    private JCheckBox _tpsCheck;
    private JCheckBox _mapCheck;
    private JCheckBox _rpmCheck;
    private JCheckBox _lambdaCheck;
    private JCheckBox _turboCheck;
    private JCheckBox _boostCheck;
    private JCheckBox _waterCheck;
    private JCheckBox _airTCheck;
    private JCheckBox _oilTCheck;
    private JCheckBox _oilPCheck;
    private JCheckBox _wheelSlipCheck;
    private JCheckBox _timeSlipCheck;
    private JButton _clearBtn;
    private JButton _resetZoomBtn;
    private JButton _optionsBtn;
    private boolean _ignoreEvents = false;

    private DTAPlot() {
        super();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            // Carry on anyway
        }
        DTAPlot p = new DTAPlot();
        p.run();
    }

    private static Color getColor(int index) {
        Color c = Color.BLACK;
        switch (index) {
            case 0:
                c = new Color(228, 26, 28);
                break;
            case 1:
                c = new Color(55, 126, 184);
                break;
            case 2:
                c = new Color(77, 175, 74);
                break;
            case 3:
                c = new Color(152, 78, 163);
                break;
            case 4:
                c = new Color(255, 127, 0);
                break;
            case 5:
                c = new Color(166, 86, 40);
                break;
        }
        return c;
    }

    private Color getNextColor() {
        
        // Find least used color
        int[] count=new int[6];
        SortedSet<ITrace2D> traces = _chart.getTraces();
        for (ITrace2D trace : traces) {
            for (int i=0; i<6; i++) {
                if (trace.getColor().equals(getColor(i))) {
                    count[i]+=1;
                    break;
                }
            }
        }
        int lowest=0;
        int lowestCount=count[lowest];
        for (int i=1; i<6; i++) {
            if (count[i]<lowestCount) {
                lowest=i;
                lowestCount=count[i];
            }
        }

        return getColor(lowest);
    }

    private void run() {

        // Create The main frame
        final JFrame frame = new JFrame("DTA Plot");
        final Container content = frame.getContentPane();

        // Sort out the layout
        SpringLayout layout = new SpringLayout();
        content.setLayout(layout);

        JPanel menuArea = new JPanel();
        GridLayout menuLayout = new GridLayout(0, 1);
        menuArea.setLayout(menuLayout);
        menuLayout.setVgap(10);
        //menuArea.add(new JLabel("Select Run"));
        menuArea.setMaximumSize(new Dimension(100, 600));
        _lapCombo = new JComboBox();
        _lapCombo.setToolTipText("Drag & Drop logfile(s) to load them");
        menuArea.add(_lapCombo);
        menuArea.add(_speedCheck = new JCheckBox("Speed"));
        menuArea.add(_timeSlipCheck = new JCheckBox("Time Lag"));
        menuArea.add(_tpsCheck = new JCheckBox("TPS"));
        menuArea.add(_mapCheck = new JCheckBox("MAP"));
        menuArea.add(_rpmCheck = new JCheckBox("RPM"));
        menuArea.add(_turboCheck = new JCheckBox("Turbo %"));
        menuArea.add(_boostCheck = new JCheckBox("Boost"));
        menuArea.add(_lambdaCheck = new JCheckBox("AFR"));
        menuArea.add(_waterCheck = new JCheckBox("Water"));
        menuArea.add(_airTCheck = new JCheckBox("Air Temp"));
        menuArea.add(_oilTCheck = new JCheckBox("Oil Temp"));
        menuArea.add(_oilPCheck = new JCheckBox("Oil Pressure"));
        menuArea.add(_wheelSlipCheck = new JCheckBox("Wheel Slip"));

        menuArea.add(_clearBtn = new JButton("Clear Traces"));
        menuArea.add(_resetZoomBtn = new JButton("Reset Zoom"));
        menuArea.add(_optionsBtn = new JButton("Options"));

        // Add the chart
        _chart = new ZoomableChart();
        _chart.setToolTipText("Drag & Drop logfile(s) to load them");
        _chart.getAxesXBottom().get(0).setTitle("Time");
        _chart.getAxesXBottom().get(0).setPaintGrid(true);
        _chart.getAxesYLeft().get(0).setTitle("");
        _chart.getAxesYLeft().get(0).setPaintGrid(true);
        _chart.setPreferredSize(new Dimension(800, 600));

        // Build up
        content.add(menuArea);
        content.add(_chart);

        // Loads of constraints :-)
        layout.putConstraint(SpringLayout.WEST, menuArea,
                5,
                SpringLayout.WEST, content);
        layout.putConstraint(SpringLayout.NORTH, menuArea,
                5,
                SpringLayout.NORTH, content);

        layout.putConstraint(SpringLayout.WEST, _chart,
                5,
                SpringLayout.EAST, menuArea);
        layout.putConstraint(SpringLayout.NORTH, _chart,
                5,
                SpringLayout.NORTH, content);

        layout.putConstraint(SpringLayout.EAST, content,
                5,
                SpringLayout.EAST, _chart);
        layout.putConstraint(SpringLayout.SOUTH, content,
                5,
                SpringLayout.SOUTH, _chart);

        // Handle drops
        FileDrop fd = new FileDrop(frame, new FileDrop.Listener() {

            @Override
            public void filesDropped(java.io.File[] files) {
                RunManager mgr = RunManager.getInstance();
                for (int i = 0; i < files.length; i++) {
                    try {
                        mgr.addLogfile(files[i]);
                        refresh();
                    } catch (RTException ex) {
                        JOptionPane.showMessageDialog(content, ex.getMessage());
                    }
                }
            }
        });

        // Handle clicks
        _lapCombo.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!_ignoreEvents) {
                            setOptions();
                        }
                    }
                });

        _clearBtn.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        removeTraces();
                    }
                });


        _resetZoomBtn.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        _chart.zoomAll();
                    }
                });
        
        _optionsBtn.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setGlobalOptions(frame);
                    }
                });

        _speedCheck.addItemListener(
                new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (!_ignoreEvents) {
                            RunManager.Run run = getSelectedRun();
                            speedTrace(run, e.getStateChange() == ItemEvent.SELECTED);
                        }
                    }
                });

        _tpsCheck.addItemListener(
                new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (!_ignoreEvents) {
                            RunManager.Run run = getSelectedRun();
                            tpsTrace(run, e.getStateChange() == ItemEvent.SELECTED);
                        }
                    }
                });

        _mapCheck.addItemListener(
                new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (!_ignoreEvents) {
                            RunManager.Run run = getSelectedRun();
                            mapTrace(run, e.getStateChange() == ItemEvent.SELECTED);
                        }
                    }
                });
        
        _rpmCheck.addItemListener(
                new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (!_ignoreEvents) {
                            RunManager.Run run = getSelectedRun();
                            rpmTrace(run, e.getStateChange() == ItemEvent.SELECTED);
                        }
                    }
                });
        
        _timeSlipCheck.addItemListener(
                new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (!_ignoreEvents) {
                            RunManager.Run run = getSelectedRun();
                            timeSlipTrace(run, e.getStateChange() == ItemEvent.SELECTED);
                        }
                    }
                });
        
        _turboCheck.addItemListener(
                new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (!_ignoreEvents) {
                            RunManager.Run run = getSelectedRun();
                            turboTrace(run, e.getStateChange() == ItemEvent.SELECTED);
                        }
                    }
                });

        _boostCheck.addItemListener(
                new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (!_ignoreEvents) {
                            RunManager.Run run = getSelectedRun();
                            boostTrace(run, e.getStateChange() == ItemEvent.SELECTED);
                        }
                    }
                });


        _lambdaCheck.addItemListener(
                new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (!_ignoreEvents) {
                            RunManager.Run run = getSelectedRun();
                            lambdaTrace(run, e.getStateChange() == ItemEvent.SELECTED);
                        }
                    }
                });

        _waterCheck.addItemListener(
                new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (!_ignoreEvents) {
                            RunManager.Run run = getSelectedRun();
                            waterTrace(run, e.getStateChange() == ItemEvent.SELECTED);
                        }
                    }
                });


        _airTCheck.addItemListener(
                new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (!_ignoreEvents) {
                            RunManager.Run run = getSelectedRun();
                            airTrace(run, e.getStateChange() == ItemEvent.SELECTED);
                        }
                    }
                });

        _oilTCheck.addItemListener(
                new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (!_ignoreEvents) {
                            RunManager.Run run = getSelectedRun();
                            oilTTrace(run, e.getStateChange() == ItemEvent.SELECTED);
                        }
                    }
                });

        _wheelSlipCheck.addItemListener(
                new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (!_ignoreEvents) {
                            RunManager.Run run = getSelectedRun();
                            wheelSlipTrace(run, e.getStateChange() == ItemEvent.SELECTED);
                        }
                    }
                });


        // Enable the termination button [cross on the upper right edge]: 
        frame.addWindowListener(
                new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });

        _chart.repaint();
        frame.pack();
        frame.setVisible(true);
        setOptions();
    }
    
    private void setGlobalOptions(JFrame frame) {
        
        // Create the dialog
        JDialog dialog=new JDialog(frame,"DTA System Units",true);
        Container content = dialog.getContentPane();
        OptionsDialog oDlg=new OptionsDialog();
        content.add(oDlg);
        
        Point l=frame.getLocation();
        Dimension d=frame.getSize();
        
        int cx=l.x+d.width/2;    
        int cy=l.y+d.height/2;    
        cx-=225/2;
        cy-=325/2;
        dialog.setBounds(cx,cy,225,325);
        dialog.pack();
        dialog.setVisible(true);
    }

    private synchronized void refresh() {

        RunManager mgr = RunManager.getInstance();
        RunManager.Run[] runs = mgr.getRuns();
        for (int l = 0; l < runs.length; l++) {
            RunManager.Run r = runs[l];

            int at = -1;
            for (int i = 0; i < _lapCombo.getItemCount(); i++) {
                if (((String) _lapCombo.getItemAt(i)).equals(r.name())) {
                    at = i;
                    break;
                }
            }

            if (at == -1) {
                _lapCombo.addItem(r.name());
                speedTrace(r,true);
            }
        }

        setOptions();
    }

    private synchronized RunManager.Run getSelectedRun() {

        String run = (String) _lapCombo.getSelectedItem();

        RunManager mgr = RunManager.getInstance();
        RunManager.Run[] runs = mgr.getRuns();
        for (int l = 0; l < runs.length; l++) {
            RunManager.Run r = runs[l];
            if (r.name().equals(run)) {
                return r;
            }
        }
        return null;
    }

    private synchronized void setOptions() {
        RunManager.Run run = getSelectedRun();
        if (run != null) {
            _ignoreEvents = true;

            _speedCheck.setEnabled(run.log().hasSpeed());
            _tpsCheck.setEnabled(run.log().hasTPS());
            _mapCheck.setEnabled(run.log().hasMAP());
            _rpmCheck.setEnabled(run.log().hasRPM());
            _timeSlipCheck.setEnabled(run.log().hasDistance());
            _turboCheck.setEnabled(run.log().hasTurbo());
            _boostCheck.setEnabled(run.log().hasBoost());
            _lambdaCheck.setEnabled(run.log().hasLambda());
            _waterCheck.setEnabled(run.log().hasWater());
            _oilTCheck.setEnabled(run.log().hasOilT());
            _oilPCheck.setEnabled(run.log().hasOilP());
            _airTCheck.setEnabled(run.log().hasAirT());
            _wheelSlipCheck.setEnabled(run.log().hasSlip());

            _speedCheck.setSelected(false);
            _tpsCheck.setSelected(false);
            _mapCheck.setSelected(false);
            _rpmCheck.setSelected(false);
            _timeSlipCheck.setSelected(false);
            _turboCheck.setSelected(false);
            _boostCheck.setSelected(false);
            _lambdaCheck.setSelected(false);
            _waterCheck.setSelected(false);
            _oilTCheck.setSelected(false);
            _oilPCheck.setSelected(false);
            _airTCheck.setSelected(false);
            _wheelSlipCheck.setSelected(false);

            ITrace2D[] traces = _chart.getTraces().toArray(new ITrace2D[0]);
            String name = run.name();
            for (int i = 0; i < traces.length; i++) {
                if (traces[i].getName().equals(name + " Speed")) {
                    _speedCheck.setSelected(true);
                }
                if (traces[i].getName().equals(name + " Throttle")) {
                    _tpsCheck.setSelected(true);
                }
                if (traces[i].getName().equals(name + " MAP")) {
                    _mapCheck.setSelected(true);
                }
                if (traces[i].getName().equals(name + " RPM")) {
                    _rpmCheck.setSelected(true);
                }
                if (traces[i].getName().equals(name + " Time Lag")) {
                    _timeSlipCheck.setSelected(true);
                }
                if (traces[i].getName().equals(name + " Turbo")) {
                    _turboCheck.setSelected(true);
                }
                if (traces[i].getName().equals(name + " Boost")) {
                    _turboCheck.setSelected(true);
                }
                if (run.isLambda() && traces[i].getName().equals(name + " Lambda")) {
                    _lambdaCheck.setSelected(true);
                } 
                if (!run.isLambda() && traces[i].getName().equals(name + " AFR")) {
                    _lambdaCheck.setSelected(true);
                } 
                if (traces[i].getName().equals(name + " Water")) {
                    _waterCheck.setSelected(true);
                }
                if (traces[i].getName().equals(name + " Oil Temp")) {
                    _oilTCheck.setSelected(true);
                }
                if (traces[i].getName().equals(name + " Oil Pressure")) {
                    _oilPCheck.setSelected(true);
                }
                if (traces[i].getName().equals(name + " Air Temp")) {
                    _airTCheck.setSelected(true);
                }
                if (traces[i].getName().equals(name + " Slip")) {
                    _wheelSlipCheck.setSelected(true);
                }
            }

            _chart.setToolTipText("Select an area with mouse to zoom in");
            _lapCombo.setToolTipText("Select run to change traces");

            _ignoreEvents = false;
        } else {
            _speedCheck.setEnabled(false);
            _tpsCheck.setEnabled(false);
            _mapCheck.setEnabled(false);
            _rpmCheck.setEnabled(false);
            _timeSlipCheck.setEnabled(false);
            _turboCheck.setEnabled(false);
            _boostCheck.setEnabled(false);
            _lambdaCheck.setEnabled(false);
            _waterCheck.setEnabled(false);
            _oilTCheck.setEnabled(false);
            _oilPCheck.setEnabled(false);
            _airTCheck.setEnabled(false);
            _wheelSlipCheck.setEnabled(false);
        }
    }

    private synchronized ITrace2D findTrace(String name) {
        ITrace2D[] traces = _chart.getTraces().toArray(new ITrace2D[0]);
        for (int i = 0; i < traces.length; i++) {
            if (traces[i].getName().equals(name)) {
                return traces[i];
            }
        }
        return null;
    }

    private synchronized ITrace2D findTraceContains(String substring) {
        ITrace2D[] traces = _chart.getTraces().toArray(new ITrace2D[0]);
        for (int i = 0; i < traces.length; i++) {
            if (traces[i].getName().contains(substring)) {
                return traces[i];
            }
        }
        return null;
    }

    private synchronized IAxis getAxis(String label) {
        List<IAxis> axisl = _chart.getAxesYLeft();
        IAxis axis = null;
        IAxis empty = null;
        for (int a = 0; a < axisl.size(); a++) {
            if (axisl.get(a).getTitle().equals(label)) {
                axis = axisl.get(a);
                break;
            }
            if (axisl.get(a).getTitle().length() == 0) {
                empty = axisl.get(a);
            }
        }
        if (axis == null) {
            axis = empty;
            if (axis == null) {
                AAxis a = new AxisLinear();
                _chart.addAxisYLeft(a);
                axis = a;
            }
            axis.setAxisTitle(new AxisTitle(label));
        }
        return axis;
    }

    private synchronized void cleanAxis(String label) {
        List<IAxis> axisl = _chart.getAxesYLeft();
        for (int a = 0; a < axisl.size(); a++) {
            if (axisl.get(a).getTitle().equals(label)) {
                if (a == 0) {
                    if (axisl.size() > 1) {
                        // Swap traces from last to first axis
                        IAxis last = axisl.get(axisl.size() - 1);
                        _chart.removeAxisYLeft(last);

                        ITrace2D[] traces = last.getTraces().toArray(new ITrace2D[0]);
                        last.removeAllTraces();
                        axisl.get(0).setTitle(last.getTitle());
                        for (int i = 0; i < traces.length; i++) {
                            axisl.get(0).addTrace(traces[i]);
                        }

                    } else {
                        axisl.get(a).setTitle("");
                    }
                } else {
                    _chart.removeAxisYLeft(axisl.get(a));
                }
            }
        }
    }

    private synchronized void removeTraces() {
        _chart.removeAllTraces();
        IAxis[] axisl = _chart.getAxesYLeft().toArray(new IAxis[0]);
        for (int a = 0; a < axisl.length; a++) {
            axisl[a].removeAllTraces();
            if (a == 0) {
                axisl[a].setTitle("");
            } else {
                _chart.removeAxisYLeft(axisl[a]);
            }
        }
        setOptions();
        _chart.zoomAll();
    }

    private synchronized void speedTrace(RunManager.Run r, boolean on) {

        String suffix=" (mph)";
        if (r.isKPH())
            suffix=" (kph)";
            
        ITrace2D trace = findTrace(r.name() + " Speed");
        if (on) {
            if (trace == null) {
                IAxis axis = getAxis("Speed"+suffix);
                trace = new Trace(r.name() + " Speed");
                trace.setColor(getNextColor());
                _chart.addTrace(trace, _chart.getAxesXBottom().get(0), axis);
                for (int i = 0; i < r.length(); i++) {
                    trace.addPoint(i * 0.1, r.speed(i));
                }
            }
        } else {
            if (trace != null) {
                IAxis axis = getAxis("Speed"+suffix);
                axis.removeTrace(trace);
                _chart.removeTrace(trace);
                if (findTraceContains("Speed") == null) {
                    cleanAxis("Speed"+suffix);
                }
            }

        }
    }

    private synchronized void tpsTrace(RunManager.Run r, boolean on) {

        ITrace2D trace = findTrace(r.name() + " Throttle");
        if (on) {
            if (trace == null) {
                IAxis axis = getAxis("Throttle %");
                trace = new Trace(r.name() + " Throttle");
                trace.setColor(getNextColor());
                _chart.addTrace(trace, _chart.getAxesXBottom().get(0), axis);
                for (int i = 0; i < r.length(); i++) {
                    trace.addPoint(i * 0.1, r.tps(i));
                }
            }
        } else {
            if (trace != null) {
                IAxis axis = getAxis("Throttle %");
                axis.removeTrace(trace);
                _chart.removeTrace(trace);
                if (findTraceContains("Throttle") == null) {
                    cleanAxis("Throttle %");
                }
            }
        }
    }

    private synchronized void turboTrace(RunManager.Run r, boolean on) {

        ITrace2D trace = findTrace(r.name() + " Turbo");
        if (on) {
            if (trace == null) {
                IAxis axis = getAxis("Turbo %");
                trace = new Trace(r.name() + " Turbo");
                trace.setColor(getNextColor());
                _chart.addTrace(trace, _chart.getAxesXBottom().get(0), axis);
                for (int i = 0; i < r.length(); i++) {
                    trace.addPoint(i * 0.1, r.turbo(i));
                }
            }
        } else {
            if (trace != null) {
                IAxis axis = getAxis("Turbo %");
                axis.removeTrace(trace);
                _chart.removeTrace(trace);
                if (findTraceContains("Turbo") == null) {
                    cleanAxis("Turbo %");
                }
            }
        }
    }

    private synchronized void boostTrace(RunManager.Run r, boolean on) {

        ITrace2D trace = findTrace(r.name() + " Boost");
        if (on) {
            if (trace == null) {
                IAxis axis = getAxis("Boost (kpa)");
                trace = new Trace(r.name() + " Boost");
                trace.setColor(getNextColor());
                _chart.addTrace(trace, _chart.getAxesXBottom().get(0), axis);
                for (int i = 0; i < r.length(); i++) {
                    trace.addPoint(i * 0.1, r.boost(i));
                }
            }
        } else {
            if (trace != null) {
                IAxis axis = getAxis("Boost (kpa)");
                axis.removeTrace(trace);
                _chart.removeTrace(trace);
                if (findTraceContains("Boost") == null) {
                    cleanAxis("Boost (kpa)");
                }
            }
        }
    }

    private synchronized void mapTrace(RunManager.Run r, boolean on) {
        
        String suffix=" (psi)";
        if (r.isKPA())
            suffix=" (kpa)";

        ITrace2D trace = findTrace(r.name() + " MAP");
        if (on) {
            if (trace == null) {
                IAxis axis = getAxis("MAP"+suffix);
                trace = new Trace(r.name() + " MAP");
                trace.setColor(getNextColor());
                _chart.addTrace(trace, _chart.getAxesXBottom().get(0), axis);
                for (int i = 0; i < r.length(); i++) {
                    trace.addPoint(i * 0.1, r.map(i));
                }
            }
        } else {
            if (trace != null) {
                IAxis axis = getAxis("MAP"+suffix);
                axis.removeTrace(trace);
                _chart.removeTrace(trace);
                if (findTraceContains("MAP") == null) {
                    cleanAxis("MAP"+suffix);
                }
            }
        }
    }
    
    private synchronized void rpmTrace(RunManager.Run r, boolean on) {
        
        ITrace2D trace = findTrace(r.name() + " RPM");
        if (on) {
            if (trace == null) {
                IAxis axis = getAxis("RPM");
                trace = new Trace(r.name() + " RPM");
                trace.setColor(getNextColor());
                _chart.addTrace(trace, _chart.getAxesXBottom().get(0), axis);
                for (int i = 0; i < r.length(); i++) {
                    trace.addPoint(i * 0.1, r.rpm(i));
                }
            }
        } else {
            if (trace != null) {
                IAxis axis = getAxis("RPM");
                axis.removeTrace(trace);
                _chart.removeTrace(trace);
                if (findTraceContains("RPM") == null) {
                    cleanAxis("RPM");
                }
            }
        }
    }
    
    private synchronized void timeSlipTrace(RunManager.Run r, boolean on) {
        ITrace2D trace = findTrace(r.name() + " Time Lag");
        if (on) {
            if (trace == null) {
                IAxis axis = getAxis("Time Lag (sec)");
                trace = new Trace(r.name() + " Time Lag");
                trace.setColor(getNextColor());
                _chart.addTrace(trace, _chart.getAxesXBottom().get(0), axis);
                for (int i = 0; i < r.length(); i++) {
                    trace.addPoint(i * 0.1, r.timeSlip(i));
                }
            }
        } else {
            if (trace != null) {
                IAxis axis = getAxis("Time Lag (sec)");
                axis.removeTrace(trace);
                _chart.removeTrace(trace);
                if (findTraceContains("Time Lag") == null) {
                    cleanAxis("Time Lag (sec)");
                }
            }
        }
    }

    private synchronized void waterTrace(RunManager.Run r, boolean on) {
        
        String suffix=" (\u00B0F)";
        if (r.isDegC())
            suffix=" (\u00B0C)";

        ITrace2D trace = findTrace(r.name() + " Water");
        if (on) {
            if (trace == null) {
                IAxis axis = getAxis("Water"+suffix);
                trace = new Trace(r.name() + " Water");
                trace.setColor(getNextColor());
                _chart.addTrace(trace, _chart.getAxesXBottom().get(0), axis);
                for (int i = 0; i < r.length(); i++) {
                    trace.addPoint(i * 0.1, r.water(i));
                }
            }
        } else {
            if (trace != null) {
                IAxis axis = getAxis("Water"+suffix);
                axis.removeTrace(trace);
                _chart.removeTrace(trace);
                if (findTraceContains("Water") == null) {
                    cleanAxis("Water"+suffix);
                }
            }
        }
    }

    private synchronized void lambdaTrace(RunManager.Run r, boolean on) {
        
        String name="AFR";
        if (r.isLambda())
            name="Lambda";

        ITrace2D trace = findTrace(r.name() + " "+name);
        if (on) {
            if (trace == null) {
                IAxis axis = getAxis(name);
                trace = new Trace(r.name() + " "+name);
                trace.setColor(getNextColor());
                _chart.addTrace(trace, _chart.getAxesXBottom().get(0), axis);
                for (int i = 0; i < r.length(); i++) {
                    trace.addPoint(i * 0.1, 14.7 * r.lambda(i));
                }
            }
        } else {
            if (trace != null) {
                _chart.removeTrace(trace);
                if (findTraceContains(name) == null) {
                    cleanAxis(name);
                }
            }
        }
    }

    private synchronized void airTrace(RunManager.Run r, boolean on) {
        
        String suffix=" (\u00B0F)";
        if (r.isDegC())
            suffix=" (\u00B0C)";

        ITrace2D trace = findTrace(r.name() + " Air Temp");
        if (on) {
            if (trace == null) {
                IAxis axis = getAxis("Air Temp"+suffix);
                trace = new Trace(r.name() + " Air Temp");
                trace.setColor(getNextColor());
                _chart.addTrace(trace, _chart.getAxesXBottom().get(0), axis);
                for (int i = 0; i < r.length(); i++) {
                    trace.addPoint(i * 0.1, r.airT(i));
                }
            }
        } else {
            if (trace != null) {
                IAxis axis = getAxis("Air Temp"+suffix);
                axis.removeTrace(trace);
                _chart.removeTrace(trace);
                if (findTraceContains("Air Temp") == null) {
                    cleanAxis("Air Temp"+suffix);
                }
            }
        }
    }

    private synchronized void oilTTrace(RunManager.Run r, boolean on) {
        
        String suffix=" (\u00B0F)";
        if (r.isDegC())
            suffix=" (\u00B0C)";

        ITrace2D trace = findTrace(r.name() + " Oil Temp");
        if (on) {
            if (trace == null) {
                IAxis axis = getAxis("Oil Temp"+suffix);
                trace = new Trace(r.name() + " Oil Temp");
                trace.setColor(getNextColor());
                _chart.addTrace(trace, _chart.getAxesXBottom().get(0), axis);
                for (int i = 0; i < r.length(); i++) {
                    trace.addPoint(i * 0.1, r.oilT(i));
                }
            }
        } else {
            if (trace != null) {
                IAxis axis = getAxis("Oil Temp"+suffix);
                axis.removeTrace(trace);
                _chart.removeTrace(trace);
                if (findTraceContains("Oil Temp") == null) {
                    cleanAxis("Oil Temp"+suffix);
                }
            }
        }
    }

    private synchronized void wheelSlipTrace(RunManager.Run r, boolean on) {

        ITrace2D trace = findTrace(r.name() + " Slip");
        if (on) {
            if (trace == null) {
                IAxis axis = getAxis("Slip %");
                trace = new Trace(r.name() + " Slip");
                trace.setColor(getNextColor());
                _chart.addTrace(trace, _chart.getAxesXBottom().get(0), axis);
                for (int i = 0; i < r.length(); i++) {
                    double slip = r.wheelSlip(i);
                    if (slip < -25) {
                        slip = -25;
                    }
                    if (slip > 25) {
                        slip = 25;
                    }
                    trace.addPoint(i * 0.1, slip);
                }
            }
        } else {
            if (trace != null) {
                _chart.removeTrace(trace);
                if (findTraceContains("Slip") == null) {
                    cleanAxis("Slip %");
                }
            }
        }
    }
}
