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
package com.haskins.cloudtrailviewer.utils;

import com.haskins.cloudtrailviewer.CloudTrailViewer;
import com.haskins.cloudtrailviewer.core.Printable;
import com.haskins.cloudtrailviewer.model.NameValueModel;
import com.haskins.cloudtrailviewer.thirdparty.ScreenImage;
import com.haskins.cloudtrailviewer.thirdparty.XTableColumnModel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author mark
 */
public class GeneralUtils {

    private static JFileChooser fileChooser = new JFileChooser();
    
    public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
        List<T> list = new ArrayList<>(c);
        java.util.Collections.sort(list);
        return list;
    }

    public enum OS {
        WINDOWS, UNIX, POSIX_UNIX, MAC, OTHER
    }

    private static OS os = OS.OTHER;

    static {
        try {

            String osName = System.getProperty("os.name");
            if (osName == null) {
                throw new IOException("os.name not found");
            }

            osName = osName.toLowerCase(Locale.ENGLISH);
            if (osName.contains("windows")) {
                os = OS.WINDOWS;

            }
            else if (osName.contains("linux")
                || osName.contains("mpe/ix")
                || osName.contains("freebsd")
                || osName.contains("irix")
                || osName.contains("digital unix")
                || osName.contains("unix")) {
                os = OS.UNIX;

            }
            else if (osName.contains("mac os x")) {
                os = OS.MAC;

            }
            else if (osName.contains("sun os")
                || osName.contains("sunos")
                || osName.contains("solaris")) {
                os = OS.POSIX_UNIX;

            }
            else if (osName.contains("hp-ux")
                || osName.contains("aix")) {
                os = OS.POSIX_UNIX;

            }
            else {
                os = OS.OTHER;
            }

        }
        catch (Exception ex) {
            os = OS.OTHER;
        }
    }

    public static OS getOs() {
        return os;
    }

    public static boolean isMac() {
        boolean isMac = false;

        if (GeneralUtils.getOs().equals(GeneralUtils.OS.MAC)) {
            isMac = true;
        }

        return isMac;
    }
    
    public static void savePanelAsImage(Printable panel) {
        
        fileChooser.setDialogTitle("Save Image");   

        int userSelection = fileChooser.showSaveDialog(CloudTrailViewer.frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            
            File fileToSave = fileChooser.getSelectedFile();
            
            BufferedImage bi = ScreenImage.createImage((JPanel)panel);
            try {
                ScreenImage.writeImage(bi, fileToSave.getAbsolutePath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void saveTableAsCsv(JTable table) {
        
        fileChooser.setDialogTitle("Save Table");   

        int userSelection = fileChooser.showSaveDialog(CloudTrailViewer.frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            
            TableModel model = table.getModel();
            XTableColumnModel columnModel = (XTableColumnModel)table.getColumnModel();
                    
            try (FileWriter csvFile = new FileWriter(fileChooser.getSelectedFile());) {

                for(int i = 0; i < model.getColumnCount(); i++){
                    
                    TableColumn column = columnModel.getColumnByModelIndex(i);
                    if (columnModel.isColumnVisible(column)) {
                        csvFile.write("\"" + model.getColumnName(i) + "\",");
                    }
                }

                csvFile.write(System.lineSeparator());

                for(int i=0; i< model.getRowCount(); i++) {
                    for(int j=0; j < model.getColumnCount(); j++) {
                        
                        TableColumn column = columnModel.getColumnByModelIndex(j);
                        if (columnModel.isColumnVisible(column)) {
                            csvFile.write("\"" + model.getValueAt(i,j).toString()+"\",");
                        }
                    }
                    csvFile.write("\n");
                }

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }        
        }
    }
    
    public static void orderListByComparator(DefaultListModel model, Comparator comparator) {
        
        List<NameValueModel> list = Collections.list(model.elements());
        Collections.sort(list, comparator);
            
        model.clear();

        for (NameValueModel error : list) {
            model.addElement(error);
        }
    }
}
