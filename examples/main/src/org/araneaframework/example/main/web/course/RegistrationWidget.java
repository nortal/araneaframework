package org.araneaframework.example.main.web.course;

import java.util.List;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.UserMO;
import org.araneaframework.example.main.web.LoginWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;

public class RegistrationWidget extends TemplateBaseWidget {
  private static final long serialVersionUID = 1L;
  /* Widget we will create and attach to this widget. */
  private FormWidget registrationForm;

  protected void init() throws Exception {
    setViewSelector("registration");
    setGlobalEventListener(new ProxyEventListener(this));

    /* Create a new FormWidget with two self-described input fields. */
    registrationForm = new FormWidget();
    // Add the input fields. Arguments taken by addElement() :  
    // String elementName, String labelId, Control control, Data data, boolean mandatory
    registrationForm.addElement("username", "#User", new TextControl(), new StringData(), true);
    registrationForm.addElement("password", "#Password", new TextControl(), new StringData(), true);
    registrationForm.addElement("password2", "#Repeat Password", new TextControl(), new StringData(), true);

    // attach created form to our widget. 
    addWidget("registrationForm", registrationForm);
  }
  
  private void handleEventReturn() {
    getFlowCtx().replace(new LoginWidget());
  }
  
  private void handleEventConfirm() throws Exception {
    if (registrationForm.convertAndValidate()) {
      String username = (String)registrationForm.getValueByFullName("username");
      String password = (String) registrationForm.getValueByFullName("password");
      
      System.out.println(username);
      System.out.println(password);
      
      List names = getUserDAO().findByName(username);
      
      if (!names.isEmpty()) {
        getMessageCtx().showErrorMessage("User name already taken, choose another one.");
        return;
      } 
      
      if (!registrationForm.getValueByFullName("password").equals(registrationForm.getValueByFullName("password2"))) {
        getMessageCtx().showErrorMessage("Passwords do not match.");
        return;
      }
      
      UserMO user = new UserMO();

      user.setPassword(password);
      user.setName(username);
      getGeneralDAO().add(user);
      
      getFlowCtx().replace(new LoginWidget("Registration successful, you may now log in"));
    }
  }
}
