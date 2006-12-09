package app.struts;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 * Form bean for sample application, "Hello World!"
 * 
 */
public final class HelloForm extends ActionForm {

  // --------------------------------------------------- Instance Variables

  /**
   * The person we want to say "Hello!" to
   */
  private String person = null;

  // ----------------------------------------------------------- Properties

  /**
   * Return the person to say "Hello!" to
   * 
   * @return String person the person to say "Hello!" to
   */
  public String getPerson() {

    return (this.person);

  }

  /**
   * Set the person.
   * 
   * @param person
   *          The person to say "Hello!" to
   */
  public void setPerson(String person) {

    this.person = person;

  }

  // --------------------------------------------------------- Public Methods

  /**
   * Reset all properties to their default values.
   * 
   * @param mapping
   *          The mapping used to select this instance
   * @param request
   *          The servlet request we are processing
   */
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    this.person = null;
  }

  /**
   * Validate the properties posted in this request. If validation errors are found, return an <code>ActionErrors</code>
   * object containing the errors. If no validation errors occur, return <code>null</code> or an empty
   * <code>ActionErrors</code> object.
   * 
   * @param mapping
   *          The current mapping (from struts-config.xml)
   * @param request
   *          The servlet request object
   */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

    ActionErrors errors = new ActionErrors();

    if ((person == null) || (person.length() < 1))
      errors.add("person", new ActionMessage("examples.hello.no.person.error"));

    return errors;
  }
}
