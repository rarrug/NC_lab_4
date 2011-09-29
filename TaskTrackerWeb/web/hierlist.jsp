<%@page import="java.io.IOException"%>
<%@page import="ttracker.ejb.task.Task"%>
<%@page import="java.util.List"%>
<%!
    /* Current index in user list  */
    private static int index;
    /* Save string with hierarchical list */
    private static StringBuffer listBuilder;

    /**
     * Print hierarchical structure of task list to JSP page
     * @param out Out page stream
     * @param taskList User list
     */
    public static String buildHirerachicalStructure(List<Task> taskList) {
        String listAfterReplace = null;
        index = -1;
        listBuilder = new StringBuffer();

        try {
            listBuilder.append("<ul class=\"simpleTree\">\n").
                    append("<li class=\"root\" id='").append(index + 2).
                    append("'>ROOT\n").append("<ul>\n");

            while (index < taskList.size() - 1) {
                index++;
                printHierarchyNode((Task) taskList.get(index), taskList, "");
            }

            listBuilder.append("</ul>\n</li>\n</ul>\n");
            listAfterReplace = listBuilder.toString().replaceAll("</ul>\n<ul>", "");
        } finally {
            return listAfterReplace;
        }
    }

    /**
     * Print parent node and its child nodes
     * @param out Out page stream
     * @param task Parent task
     * @param list User list
     * @param s Some charaters
     */
    public static void printHierarchyNode(Task task, List<Task> list, String s) throws IOException {
        boolean flag = true;
        if (index != 0) {
            listBuilder.append((s + "<ul>\n"));
        }
        while (index < list.size()) {
            if (flag) {
                String classOpen = "";
                if (index == 0) {
                    classOpen = " class=\"open\"";
                }
                String descr = task.getDescription();
                if (descr == null) {
                    descr = "&lt;empty desrc&gt;";
                } else if (descr.length() > 30) {
                    descr = descr.substring(0, 30) + "...";
                }
                String item = (s + "<li id=\'" + (index + 2) + "\'" + classOpen + "><span>" + task.getId() + " - " + task.getName()
                        + " - " + descr + "</span>");
                listBuilder.append(item).append("\n");
                flag = false;
            }
            if (index >= list.size() - 1) {
                break;
            }
            Task child = (Task) list.get(index + 1);
            if (child != null && task.getId().compareTo(child.getParentId()) == 0) {
                index++;
                printHierarchyNode(child, list, "");
            } else {
                break;
            }
        }
        listBuilder.append((s + "</li>\n"));
        if (index != 0) {
            listBuilder.append((s + "</ul>\n"));
        }
    }
%>

<%
            out.println(buildHirerachicalStructure((List<Task>) session.getAttribute("hierResultList")));
            session.setAttribute("hierarchy", null);
%>