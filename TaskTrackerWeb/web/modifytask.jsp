<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- modify block -->

<div id="basic-modal-content-modify">

    <h3>Modify task</h3>

    <!-- get task from session -->
    <c:set var="modifyTask" value="${sessionScope.modifyTask}" />

    <!-- build and fill form whith task parameters -->
    <div>
        <form action="modifytask.perform" method="post">

            <div class="modify-block-label">ID:</div>
            <input type="text" name="modifyId" value="${modifyTask.task.id}" readonly/>

            <br/>
            <div class="modify-block-label">Name:</div>
            <input type="text" name="modifyName" value="${modifyTask.task.name}"/>

            <br/>
            <div class="modify-block-label">Parent:</div>
            <select name="modifyParent">
                <c:choose>
                    <c:when test="${modifyTask.task.id eq 0}">
                        <option value="0" selected>no</option>
                    </c:when>
                    <c:otherwise>
                        <option value="0">no</option>
                    </c:otherwise>
                </c:choose>

                <c:forEach items="${taskList}" var="parentTask">
                    <c:if test="${parentTask.task.id ne modifyTask.task.id}">
                        <c:choose>
                            <c:when test="${parentTask.task.id eq modifyTask.task.parentId}">
                                <option value="${parentTask.task.id}" selected>
                                    ${parentTask.task.id}
                                    <c:out value=" - " />
                                    ${parentTask.task.name}
                                </option>
                            </c:when>
                            <c:otherwise>
                                <option value="${parentTask.task.id}">
                                    ${parentTask.task.id}
                                    <c:out value=" - " />
                                    ${parentTask.task.name}
                                </option>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </c:forEach>
            </select>

            <br/>
            <div class="modify-block-label">User:</div>
            <select name="modifyUser">
                <c:forEach items="${userList}" var="userInfo">
                    <c:choose>
                        <c:when test="${userInfo.emp.name eq modifyTask.emp.name}">
                            <option value="${userInfo.emp.name}" selected>${userInfo.emp.name}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${userInfo.emp.name}">${userInfo.emp.name}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>

            <br/>
            <div class="modify-block-label">Begin:</div>
            <input type="text" name="modifyBegin" value="${modifyTask.task.begin}" id="calendarBeginM" readonly="true"/>

            <div id="cCallbackBeginM" class="select-free"></div>

            <br/>
            <div class="modify-block-label">End:</div>
            <input type="text" name="modifyEnd" value="${modifyTask.task.end}" id="calendarEndM" readonly="true"/>

            <div id="cCallbackEndM" class="select-free"></div>

            <br/>
            <div class="modify-block-label">Status:</div>
            <select name="modifyStatus">
                <c:choose>
                    <c:when test='${"open" eq modifyTask.task.status}'>
                        <option value="open" selected>open</option>
                    </c:when>
                    <c:otherwise>
                        <option value="open">open</option>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test='${"process" eq modifyTask.task.status}'>
                        <option value="process" selected>process</option>
                    </c:when>
                    <c:otherwise>
                        <option value="process">process</option>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test='${"close" eq modifyTask.task.status}'>
                        <option value="close" selected>close</option>
                    </c:when>
                    <c:otherwise>
                        <option value="close">close</option>
                    </c:otherwise>
                </c:choose>
            </select>

            <br style="float:none;"/>
            <div class="modify-block-label">Description:</div>
            <br style="float:none;"/>
            <textarea name="modifyDescription">${modifyTask.task.description}</textarea>

            <br/>
            <input type="submit" name="taskModify" value="Modify" id="modifyButton"/>

        </form>
    </div>
</div>

<div style='display:none;'>
    <img src='im/x.png' alt='' style="border:1px solid white;"/>
</div>
