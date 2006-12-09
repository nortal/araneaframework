package app.struts;

/**
 * <p>
 * This is a Model object which simply contains the name of the person we want to say "Hello!" to.
 * <p>
 * 
 * In a more advanced application, this Model component might update a persistent store with the person name, use it in
 * an argument in a web service call, or send it to a remote system for processing.
 * 
 */
public class HelloModel {

  // --------------------------------------------------- Instance Variables

  /**
   * The new person we want to say "Hello!" to
   */
  private String _person = null;

  // ----------------------------------------------------------- Properties

  /**
   * Return the new person we want to say "Hello!" to
   * 
   * @return String person the person to say "Hello!" to
   */
  public String getPerson() {
    return this._person;
  }

  /**
   * Set the new person we want to say "Hello!" to
   * 
   * @param person
   *          The new person we want to say "Hello!" to
   */
  public void setPerson(String person) {

    this._person = person;

  }

  // --------------------------------------------------------- Public Methods

  /**
   * This is a stub method that would be used for the Model to save the information submitted to a persistent store. In
   * this sample application it is not used.
   */
  public void saveToPersistentStore() {

    /*
     * This is a stub method that might be used to save the person's name to a persistent store if this were a real
     * application.
     * 
     * The actual business operations that would exist within a Model component would depend upon the requirements of
     * the application.
     */
  }
}