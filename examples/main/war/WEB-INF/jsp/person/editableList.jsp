<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2"
>
  <!-- Main example's person editable list JSP -
       examples/main/war/WEB-INF/jsp/editableList.jsp
  -->
  <ui:widgetContext>

    <!-- Start the list context ... -->
    <ui:list id="list">
      <!-- and continue with announcement that this list is editable -->
      <ui:formList>
		
        <ui:componentHeader>
          <ui:componentName>Editable person list</ui:componentName>
        </ui:componentHeader>
		
        <ui:component>

          <ui:componentList>

            <ui:componentListHeader/>

              <!-- List filter for editable list is exactly 
                   the same as filter for ordinary lists -->
              <ui:listFilter>
                <ui:row styleClass="filter">
                  <ui:cell/>

                  <ui:cell>
                    <ui:textInput id="name"/>
                  </ui:cell>

                  <ui:cell>
                    <ui:textInput id="surname"/>
                  </ui:cell>

                  <ui:cell>
                    <ui:textInput id="phone"/>
                  </ui:cell>

                  <ui:cell>
                    <ui:dateInput id="birthdate_start"/>
                    <br/>
                    <ui:dateInput id="birthdate_end"/>
                  </ui:cell>							

                  <ui:cell>
                    <ui:floatInput id="salary_start" styleClass="min"/>
                    <br/>
                    <ui:floatInput id="salary_end" styleClass="min"/>
                  </ui:cell>		
                  
                  <ui:cell>
                    <ui:filterButton/>
                  </ui:cell>
                </ui:row>
              </ui:listFilter>					

              <!-- Editable list rows. This tag usage is similar to ui:listRows;
                   but it makes available some extra variables -->
              <ui:formListRows>
                <ui:row>
                  <ui:cell>
                    <!-- Default variable name for accessing the row object is "row" as in
                         ordinary lists. This could have been changed by specifying "var" 
                         attribute for ui:formListRows tag. -->
                    <c:out value="${row.id}"/>
                  </ui:cell>

                  <c:choose>
                    <!-- formRow variable is of class FormRow.ViewModel.
                         It holds the properties of form used to present current row object. -->
                    <c:when test="${formRow.open}">
                      <!-- When formRow is "open", render the fields as inputs. -->
                      <ui:cell>
                        <ui:textInput id="name"/>
                      </ui:cell>

                      <ui:cell>
                        <ui:textInput id="surname"/>
                      </ui:cell>

                      <ui:cell>
                        <ui:textInput id="phone"/>
                      </ui:cell>

                      <ui:cell>
                        <ui:dateInput id="birthdate"/>
                      </ui:cell>																																			

                      <ui:cell>
                        <ui:floatInput id="salary" styleClass="min"/>
                      </ui:cell>																																			
                    </c:when>
                    <c:otherwise>
                      <!-- When formRow is "closed", render the fields as displays. -->
                      <ui:cell>
                        <c:out value="${row.name}"/>
                      </ui:cell>

                      <ui:cell>
                        <c:out value="${row.surname}"/>
                      </ui:cell>

                      <ui:cell>
                        <c:out value="${row.phone}"/>
                      </ui:cell>

                      <ui:cell>
                        <fmt:formatDate value="${row.birthdate}" pattern="dd.MM.yyyy"/>
                      </ui:cell>

                      <ui:cell>
                        <c:out value="${row.salary}"/>
                      </ui:cell>
                    </c:otherwise>						
                  </c:choose>

                  <ui:cell>
                    <ui:attribute name="width" value="0"/>
                    <c:choose>
                      <!-- Depending on formRow's current status, change button 
                           opens the formRow for editing or saves the formRow 
                           already opened. Buttons title should also be set 
                           accordingly. -->
                      <c:when test="${formRow.open}">
                        <c:set var="altEditText" value="Save person"/>
                      </c:when>
                      <c:otherwise>
                        <c:set var="altEditText" value="Edit person"/>
                      </c:otherwise>
                    </c:choose>

                    <!-- EditSave event is produced by buttons created with 
                         FormListUtil.addEditSaveButtonToRowForm() -->
                    <ui:linkButton id="editSave" showLabel="false">
                      <ui:image code="buttonChange" alt="${altEditText}" title="${altEditText}"/>
                    </ui:linkButton>
                    <!-- Delete event is produced by buttons created with 
                         FormListUtil.addDeleteButtonToRowForm() -->
                    <ui:linkButton id="delete" showLabel="false">
                      <ui:image code="buttonDelete" alt="Remove person" title="Remove person"/>
                    </ui:linkButton>
                  </ui:cell>
                </ui:row>
              </ui:formListRows>
			
            <!-- Finally the empty form for addition of new objects. -->
            <ui:formListAddForm>
              <ui:row>
                <ui:cell/>

                <ui:cell styleClass="center">
                  <ui:textInput id="name"/>
                </ui:cell>

                <ui:cell>
                  <ui:textInput id="surname"/>
                </ui:cell>

                <ui:cell>
                  <ui:textInput id="phone"/>
                </ui:cell>

                <ui:cell>
                  <ui:dateInput id="birthdate"/>
                </ui:cell>							

                <ui:cell>
                  <ui:floatInput id="salary" styleClass="w40"/>
                </ui:cell>							

                <ui:cell>
                  <ui:attribute name="width" value="0"/>
                  <ui:linkButton id="add" showLabel="false"><ui:image code="buttonAdd"/></ui:linkButton>
                </ui:cell>
              </ui:row>
            </ui:formListAddForm>
            
          </ui:componentList>

          <!-- Sequence -->
          <ui:componentListFooter/>

        </ui:component>
		
      </ui:formList>
    </ui:list>

  </ui:widgetContext>		
</jsp:root>