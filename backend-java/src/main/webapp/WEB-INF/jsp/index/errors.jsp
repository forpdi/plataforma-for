<%@ page pageEncoding="UTF-8" contentType="application/json; charset=UTF-8" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
{
	status: false,
	message: 'Existem erros de validação.',
	validation: true,
	data: {
		fields: [<c:forEach var="error" items="${errors}" varStatus="loopStats">
			<c:if test="${not loopStats.first}">,</c:if>'${error.message}'
		</c:forEach>]
	}
}