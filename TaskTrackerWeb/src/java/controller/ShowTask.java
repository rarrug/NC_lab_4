package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import ttracker.dao.DAOFactory;
import ttracker.dao.Info;
import ttracker.dao.exc.TrackerException;

/**
 * Generate task list by request
 */
public class ShowTask extends SomeAction {

    /* Logger */
    private static final Logger logger = Logger.getLogger(ShowTask.class);

    public String perform(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        try {

            /* modify task by id */
            String taskid = request.getParameter(AppProperties.getProperty("modify_req_param"));
            if (taskid != null) {
                logger.info("MODIFY REQUEST");
                Integer id = new Integer(taskid);
                session.setAttribute("modifyTask", DAOFactory.getInstance().getTaskById(id));
            }

            /* find by id, name or user or hierarchical view */
            String findParameter = request.getParameter(AppProperties.getProperty("find_param"));
            if (findParameter != null) {
                String findValue = request.getParameter(AppProperties.getProperty("find_value"));

                if (AppProperties.getProperty("hierarchical_value").equals(findParameter)) {/* generate hierarchical list */
                    logger.info("HIERARCHICAL SEARCH");
                    Collection<Info> hierResultList = DAOFactory.getInstance().getAllTasks(true);
                    session.setAttribute("hierResultList", hierResultList);
                    session.setAttribute("hierarchy", "1");
                } else { /* search */
                    Collection<Info> taskList = null;
                    if ("by_id".equals(findParameter)) {/* by id */
                        logger.info("FIND BY ID");
                        taskList = new ArrayList<Info>();
                        try {
                            Integer id = new Integer(findValue);
                            taskList.add(DAOFactory.getInstance().getTaskById(id));
                        } catch (NumberFormatException nfe) {
                            throw new TrackerException("Wrong search criteries", nfe);
                        }
                    } else if ("by_name".equals(findParameter)) {/* by name */
                        logger.info("FIND BY NAME");
                        taskList = DAOFactory.getInstance().getTaskByName(findValue);
                    } else if ("by_user".equals(findParameter)) {/* by user */
                        logger.info("FIND BY User");
                        taskList = DAOFactory.getInstance().getTaskByEmp(findValue);
                    }
                    session.setAttribute("taskList", taskList);
                }
            } else {/* build full task list and employee list */
                logger.info("UPDATE TASK LIST");
                session.setAttribute("taskList", DAOFactory.getInstance().getAllTasks(false));

                /* write data to session */
                session.setAttribute("userList", DAOFactory.getInstance().getAllEmps());
                session.setAttribute("today", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            }

        } catch (NamingException ne) {
            setNotifyMessage(session, logger, FAIL_MESS, "Show exception", ne.getMessage(), ne);
        } catch (TrackerException ex) {
            setNotifyMessage(session, logger, FAIL_MESS, "Show exception", ex.getMessage(), ex);
        }
        return "/showtask.jsp"; //redirected page path
    }
}
