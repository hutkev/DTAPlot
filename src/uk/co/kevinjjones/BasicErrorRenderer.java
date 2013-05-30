/*
 Copyright 2012 Kevin J. Jones (http://www.kevinjjones.co.uk)

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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import uk.co.kevinjjones.model.BasicError;

/**
 * A render for a BasicError, suitable for use with a JList.
 */
class BasicErrorRenderer extends JButton implements ListCellRenderer, ListSelectionListener {
    
    JFrame _frame;

    public BasicErrorRenderer(JFrame frame) {
        _frame=frame;
        setHorizontalAlignment(SwingConstants.LEFT);
    }
    
    private static ImageIcon[] _icons = new ImageIcon[3];
    
    public static ImageIcon getIcon(int index) {
        if (index>=0 && index <3) {
            if (_icons[index]==null) {
                URL imageURL = BasicErrorRenderer.class.getResource("images/icon"+index+".png");
                if (imageURL != null) {
                    _icons[index] = new ImageIcon(imageURL);
                    Image image = _icons[index].getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                    _icons[index].setImage(image);
                }        
            }
            return _icons[index];
        }
        return null;
    }

    /*
    * This method finds the image and text corresponding
    * to the selected value and returns the label, set up
    * to display the text and image.
    */
    @Override
    public Component getListCellRendererComponent(
                                    JList list,
                                    Object value,
                                    int index,
                                    boolean isSelected,
                                    boolean cellHasFocus) {
        
        final BasicError de=(BasicError)value;
        if (de._msg==null)
            de._msg="Internal Error";
        if (de._ex!=null)
            setText(de._msg+" - click for details...");
        else
            setText(de._msg);
        setIcon(getIcon(de._type));
        setBorderPainted(false);
        setMargin(new Insets(0,0,0,0));
        return this;
    }

    @Override
    public void valueChanged(ListSelectionEvent lse) {

        // Scan for right index
        JList list=(JList)lse.getSource();
        int index=-1;
        for (int i=lse.getFirstIndex(); i<=lse.getLastIndex(); i++) {
            if (lse.getValueIsAdjusting() && list.isSelectedIndex(i)) {
                index=i;
                break;
            }
        }
        if (index==-1)
            return;
               
        BasicError de=(BasicError)list.getSelectedValue();
        list.removeSelectionInterval(index, index);
        
        final JDialog dialog=new JDialog(_frame,"Message Information",true);
        
        JPanel displayArea = new JPanel();
        displayArea.setLayout(new BoxLayout(displayArea, BoxLayout.PAGE_AXIS));        
        
        if (de._msg!=null) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
            displayArea.add(panel);
            panel.add(new JLabel("Message:"));
            panel.add(new JLabel(de._msg,getIcon(de._type),SwingConstants.LEFT));
        }
        if (de._log!=null) {
            {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
                displayArea.add(panel);
                panel.add(new JLabel("Logfile:"));
                panel.add(new JLabel(de._log.toString(),SwingConstants.LEFT));
            }
            if (de._stream!=null) {
                {
                    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
                    displayArea.add(panel);
                    panel.add(new JLabel("Stream:"));
                    panel.add(new JLabel(de._stream.toString(),SwingConstants.LEFT));
                }
                {
                    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
                    displayArea.add(panel);
                    panel.add(new JLabel("Location:"));
                    panel.add(new JLabel(de._start+" to "+de._end,SwingConstants.LEFT));
                }
            }
        }

        if (de._ex!=null) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
            displayArea.add(panel);
            
            panel.add(new JLabel("Exception:"));
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            de._ex.printStackTrace(pw);
            pw.flush();
            sw.flush();
            panel.add(new JLabel("<html>" + sw.toString().replaceAll("\n", "<br>")));
        }
        
        JPanel buttonArea = new JPanel();
        FlowLayout buttonLayout = new FlowLayout(FlowLayout.RIGHT);
        buttonArea.setLayout(buttonLayout);
        JButton okBtn=new JButton("OK");
        buttonArea.add(okBtn);
        
        okBtn.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.setVisible(false);
                    }
                });
        
        JPanel dialogPanel = new JPanel();
        BorderLayout dialogLayout = new BorderLayout();
        dialogPanel.setLayout(dialogLayout);
        
        dialogPanel.add(displayArea,BorderLayout.CENTER);
        dialogPanel.add(buttonArea,BorderLayout.PAGE_END);

        Container content = dialog.getContentPane();
        content.add(dialogPanel);

        Point l=_frame.getLocation();
        Dimension d=_frame.getSize();
        
        int cx=l.x+d.width/2;    
        int cy=l.y+d.height/2;    
        cx-=400/2;
        cy-=360/2;
        dialog.setBounds(cx,cy,400,360);
        dialog.pack();
        dialog.setVisible(true);
    }
}
