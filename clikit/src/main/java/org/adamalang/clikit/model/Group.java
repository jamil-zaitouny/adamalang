/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.clikit.model;

import org.adamalang.clikit.exceptions.XMLFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

/** A group is a representation of a collection of commands in the XML **/
public class Group {
    public final String documentation;
    public final String name;
    public final String altName;
    public final String capName;
    public final Command[] commandList;

    public Group(String name, String altName, String documentation, Command[] commandList) {
        this.name = name.toLowerCase(Locale.ROOT);
        this.altName = altName != null ? altName.toLowerCase(Locale.ROOT) : null;
        this.capName = Common.camelize(name);
        this.documentation = documentation;
        this.commandList = commandList;
    }

    public static Group[] createGroupList(NodeList nodeList, XMLFormatException givenException, Map<String, ArgumentDefinition> arguments) throws Exception {
        ArrayList<Group> groupArray = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node groupNode = nodeList.item(i);
            String filePos = "line " + groupNode.getUserData("lineNumber") + " column " + groupNode.getUserData("colNumber");
            Element groupElem = (Element) groupNode;
            String groupName = groupElem.getAttribute("name");
            String altName = null;
            if (groupElem.hasAttribute("alt-name")) {
                altName = groupElem.getAttribute("alt-name");
            }
            if (groupName.trim().length() == 0)
                givenException.addToExceptionStack("The 'group' node at " + filePos + " is missing name attribute");
            Command[] commandList = Command.createCommandList(groupElem.getElementsByTagName("command"), givenException, arguments);
            String groupDocumentation = Common.getDocumentation(groupElem, givenException);
            Group group = new Group(groupName, altName, groupDocumentation ,commandList);
            groupArray.add(group);
        }
        return groupArray.toArray(new Group[0]);
    }
}
