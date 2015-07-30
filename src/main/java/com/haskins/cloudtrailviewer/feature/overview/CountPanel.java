/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.cloudtrailviewer.feature.overview;

import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.TimeStampComparator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel that show the number of Events that have been received.
 * 
 * @author mark
 */
public class CountPanel extends JPanel {
    
    private final List<Event> events = new ArrayList<>();
    
    private final OverviewFeature parent;
    
    private final JLabel eventCount = new JLabel(String.valueOf(0));
    
    private boolean sorted = false;
        
    public CountPanel(String name, Color bgColour, OverviewFeature p) {
        
        this.parent = p;
        
        this.setBackground(bgColour);
        
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(125,45));
        this.setMaximumSize(new Dimension(125,45));
        this.setPreferredSize(new Dimension(125,45));
                
        eventCount.setToolTipText("Total Events for the Service");
        eventCount.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eventCount.addMouseListener(new MouseAdapter() {
                
            @Override
            public void mouseClicked(MouseEvent e) {

                if (!sorted) {
                    Collections.sort(events, new TimeStampComparator());
                    sorted = true;
                }

                parent.showEventsTable(events);
            }
        });
        
        Font labelFont = eventCount.getFont();
        eventCount.setForeground(Color.white);
        eventCount.setAlignmentX(CENTER_ALIGNMENT);
        eventCount.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 30));
        
        JPanel countPanel = new JPanel();
        countPanel.setLayout(new BoxLayout(countPanel,BoxLayout.PAGE_AXIS));
        countPanel.add(Box.createHorizontalGlue());
        countPanel.add(Box.createVerticalGlue());
        countPanel.add(eventCount);
        countPanel.add(Box.createHorizontalGlue());
        countPanel.add(Box.createVerticalGlue());
        countPanel.setOpaque(false);
        
        this.add(countPanel, BorderLayout.CENTER);
        
        JLabel title = new JLabel(name);
        title.setForeground(Color.white);
        title.setAlignmentX(CENTER_ALIGNMENT);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel,BoxLayout.PAGE_AXIS));
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(title, BorderLayout.CENTER);
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.setOpaque(false);
        
        this.add(titlePanel, BorderLayout.PAGE_START);
        
        this.setOpaque(true);
    }
    
    public void newEvent(Event event) {
        this.events.add(event);
        
        int count = Integer.parseInt(eventCount.getText());
        count++;
        eventCount.setText(String.valueOf(count));
        
        Font labelFont = eventCount.getFont();
        if (eventCount.getText().length() >= 6) {
            eventCount.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 20));
        }
        
        this.revalidate();
    }
}