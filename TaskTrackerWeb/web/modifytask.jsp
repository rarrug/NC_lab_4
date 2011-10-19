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
            <input type="text" name="modifyId" value="${modifyTask.taskId}" readonly/>

            <br/>
            <div class="modify-block-label">Name:</div>
            <input type="text" name="modifyName" value="${modifyTask.taskName}"/>

            <br/>
            <div class="modify-block-label">Parent:</div>
            <select name="modifyParent">
                <c:choose>
                    <c:when test="${modifyTask.taskId eq 0}">
                        <option value="0" selected>no</option>
                    </c:when>
                    <c:otherwise>
                        <option value="0">no</option>
                    </c:otherwise>
                </c:choose>

                <c:forEach items="${taskList}" var="parentTask">
                    <c:if test="${parentTask.taskId ne modifyTask.taskId}">
                        <c:choose>
                            <c:when test="${parentTask.taskId eq modifyTask.taskParentId}">
                                <option value="${parentTask.taskId}" selected>
                                    ${parentTask.taskId}
                                    <c:out value=" - " />
                                    ${parentTask.taskName}
                                </option>
                            </c:when>
                            <c:otherwise>
                                <option value="${parentTask.taskId}">
                                    ${parentTask.taskId}
                                    <c:out value=" - " />
                                    ${parentTask.taskName}
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
                        <c:when test="${userInfo.empName eq modifyTask.empName}">
                            <option value="${userInfo.empName}" selected>${userInfo.empName}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${userInfo.empName}">${userInfo.empName}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>

            <br/>
            <div class="modify-block-label">Begin:</div>
            <input type="text" name="modifyBegin" value="${modifyTask.taskBegin}" id="calendarBeginM" readonly="true"/>

            <div id="cCallbackBeginM" class="select-free"></div>

            <br/>
            <div class="modify-block-label">End:</div>
            <input type="text" name="modifyEnd" value="${modifyTask.taskEnd}" id="calendarEndM" readonly="true"/>

            <div id="cCallbackEndM" class="select-free"></div>

            <br/>
            <div class="modify-block-label">Status:</div>
            <select name="modifyStatus">
                <c:choose>
                    <c:when test='${"open" eq modifyTask.taskStatus}'>
                        <option value="open" selected>open</option>
                    </c:when>
                    <c:otherwise>
                        <option value="open">open</option>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test='${"process" eq modifyTask.taskStatus}'>
                        <option value="process" selected>process</option>
                    </c:when>
                    <c:otherwise>
                        <option value="process">process</option>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test='${"close" eq modifyTask.taskStatus}'>
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
            <textarea name="modifyDescription">${modifyTask.taskDescription}</textarea>

            <br/>
            <input type="submit" name="taskModify" value="Modify" id="modifyButton"/>

        </form>
    </div>
</div>

<div style='display:none;'>
    <img src='im/x.png' alt='' style="border:1px solid white;"/>
</div>
