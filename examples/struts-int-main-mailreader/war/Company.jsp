<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<html:xhtml/>
<html>
<head>
    <title>Adding Company</title>
</head>

<body>
<html:errors/>

<html:form action="/SubmitCompany">
    <table border="0">

        <tr>
            <th align="right">
                Name:
            </th>
            <td align="left">
                <html:text property="name"/>
            </td>
        </tr>

        <tr>
            <th align="right">
                Address:
            </th>
            <td align="left">
                <html:text property="address"/>
            </td>
        </tr>

        <tr>
            <td align="right">
                <html:submit property="Submit" value="Submit"/>
            </td>
            <td align="left">
                <html:reset/>
            </td>
        </tr>

    </table>

</html:form>
</body>
</html>
