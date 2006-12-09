package app.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

/**
 * The <strong>Action</strong> class for our "Hello" application.
 * <p>
 * This is the "Controller" class in the Struts MVC architecture.
 * 
 * @author Kevin Bedell
 */

public final class HelloAction extends Action {

  /**
   * Process the specified HTTP request, and create the corresponding HTTP response (or forward to another web component
   * that will create it). Return an <code>ActionForward</code> instance describing where and how control should be
   * forwarded, or <code>null</code> if the response has already been completed.
   * 
   * @param mapping
   *          The ActionMapping used to select this instance
   * @param actionForm
   *          The optional ActionForm bean for this request (if any)
   * @param request
   *          The HTTP request we are processing
   * @param response
   *          The HTTP response we are creating
   * 
   * @exception Exception
   *              if business logic throws an exception
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    // These "messages" come from the ApplicationResources.properties file
    MessageResources messages = getResources(request);

    /*
     * Validate the request parameters specified by the user Note: Basic field validation done in HelloForm.java
     * Business logic validation done in HelloAction.java
     */
    ActionErrors errors = new ActionErrors();
    String person = (String) PropertyUtils.getSimpleProperty(form, "person");

    String badPerson = "Atilla the Hun";

    if (person.equals(badPerson)) {
      errors.add("person", new ActionMessage("examples.hello.dont.talk.to.atilla", badPerson));
      saveErrors(request, errors);
      return (new ActionForward(mapping.getInput()));
    }

    /*
     * Having received and validated the data submitted from the View, we now update the model
     */
    HelloModel hm = new HelloModel();
    hm.setPerson(person);
    hm.saveToPersistentStore();

    /*
     * If there was a choice of View components that depended on the model (or some other) status, we'd make the
     * decision here as to which to display. In this case, there is only one View component.
     * 
     * We pass data to the View components by setting them as attributes in the page, request, session or servlet
     * context. In this case, the most appropriate scoping is the "request" context since the data will not be nedded
     * after the View is generated.
     * 
     * Constants.HELLO_KEY provides a key accessible by both the Controller component (i.e. this class) and the View
     * component (i.e. the jsp file we forward to).
     */

    request.setAttribute(Constants.HELLO_KEY, hm);

    // Remove the Form Bean - don't need to carry values forward
    request.removeAttribute(mapping.getAttribute());

    // Forward control to the specified success URI
    return (mapping.findForward("SayHello"));

  }
}
