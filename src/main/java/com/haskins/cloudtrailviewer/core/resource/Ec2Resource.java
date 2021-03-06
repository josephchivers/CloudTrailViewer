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

package com.haskins.cloudtrailviewer.core.resource;


import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mark
 */
public class Ec2Resource implements Resource {

    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @return either the resource name or an empty string if the EventName is not handled.
     */
    @Override
    public String getResource(Event event) {
        
        String resource = "";
        
        if (event.getEventName().equalsIgnoreCase("DescribeTags")) {
            resource = describeTags(event);
        } else if (event.getEventName().equalsIgnoreCase("DescribeInstances")) {
            resource = describeInstances(event);
        }
        
        return resource;
    }
    
    private String describeTags(Event event) {
        
        String resource = "";
        
        return resource;
    }
    
    private String describeInstances(Event event) {
        
        StringBuilder resource = new StringBuilder();

        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("instancesSet")) {
            
            Map<String, List> items = (Map)requestParameters.get("instancesSet");
            for (Map.Entry<String, List> entry : items.entrySet()) {
                
                List<Map> instances = entry.getValue();
                for (Map instance : instances) {   
                    resource.append(instance.get("instanceId")).append(",");
                }
            }
        }
        
        return resource.toString();
    }
}
