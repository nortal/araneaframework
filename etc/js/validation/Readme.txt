------------------------------------------------------------------
 Javascript Client-side Form Validation Utilities

 This directory contains base javascript utilities required for
 performing client-side form validation.

 Created: 18.05.2004
 Author: Konstantin Tretyakov.
------------------------------------------------------------------


 OVERVIEW

 The base form-validation utilities are provided by several Javascript
 classes contained in the file
   clientside-form-validation.js
 Date and Time-related validation rules are contained in a 
 separate file:
   clientside-datetime-form-validation.js
 Some additional convenience utilities required for JSP-UI
 are provided in the file
   jsp-ui-form-validation.js
 Localized strings used by all the rules are stored in
   localization.js
 Interface for JSP-UI is presented in the file 
   jsp-ui.js
 This is also the only file that should be customized on a per-application
 basis.
 
 For information on the functions in each file, please consult the
 file itself.
 
 The files depend on each other, and should be read in the following order:
   localization.js
   clientside-form-validation.js
   clientside-datetime-form-validation.js
   jsp-ui-form-validation.js
   jsp-ui.js
 
 Framework presented there consists of four kinds of classes:
   Rules, Actions, the Validator class and the FormValidator class.

 The idea of the framework is, in short, the following: 
   The FormValidator class manages several instances of Validator class,
   and uses the validate() function of these instances to perform
   form validation. 
   Each Validator is meant to perform a small 'piece' of validation
   (e.g. validate a single form element). It contains a set of Rules 
   that it checks when its  "validate()" method is invoked, and a set 
   Actions that it performs after checking the rules.
   
   Each Rule presents a single "check()" method, that tells whether the 
   condition of a rule holds.
   Each Action presents a single "execute(..)" method, that performs that
   action.


 SAMPLE USAGE

 Here is a simple example of how the utilities in this class could be used.
 First, an instance of the FormValidator class should be constructed:

     formValidator = new FormValidator();
     
 Then, several Validators could be added to it. Each Validator should contain
 some Rules and Actions:
 
     elementValidator = new Validator();
     elementValidator.addRule(new MinMaxLengthRule('formName', 'elementName', 'Some Element', 3, 64));
     elementValidator.addAction(new ChangeCssClassAction('elementSpan', 'class-valid', 'class-invalid'));
     formValidator.addValidator(elementValidator);
             
 Afterwards, form validation can be performed in the following way:
 
     if (!formValidator.validate()) alert(formValidator.errorMessage);


 MORE DETAILED SPECIFICATION

 Javascript is type-free, and therefore does not provide facilities
 to require a certain interface. So the best we can do is provide 
 textual descriptions of these interfaces here. We employ Java-like syntax.

 // Rule is a class used to check some condition.
 interface Rule {
 
    // The function that performs the check. It may not have side-effects.
    boolean check();
    
    // If the check failed, an explanatory message is provided by this property.
    String errorMessage;
 }

 
 // Action is a class encapsulating an action that should be performed after
 // a sequence of rule checks was performed. It is a kind of Listener for the Validator.
 interface Action {
 
   // The function that performs the action.
   // Parameters: 
   //   checkPassed  - indicates whether the rule check was successful
   //                  (i.e. it is true, if the check() method of all 
   //                  the rules in the sequence returned true)
   //   errorMessage - The errorMessage of the first rule in the sequence, that failed.
   void execute(boolean checkPassed, String errorMessage);
 }

 
 // Validator is a class that manages a set of Rules and a set of Actions
 class Validator {
 
   void addRule(Rule r);
   
   void addAction(Action a);
   
   // validate() returns true, if all the rules passed the check.
   boolean validate();
   
   // This is the errorMessage of the first rule in the sequence, that failed.
   String errorMessage;
 }


 // FormValidator is to Validators same as Validator is to Rules.
 class FormValidator {
    
   void addValidator();
   
   void addPreprocessAction(Action a);
   
   void addAction(Action a);
   
   boolean validate();
   
   // This error message is the concatenation of errorMessage-s of the validators
   // that failed.
   String errorMessage;
 }

 The file clientside-form-validation.js provides declaration of the base classes
 Validator and FormValidator, and provides several basic actions and rules.
 
 Please consult the script file for more explanations.  
 It is reasonable to create a custom subclass of FormValidator, that
 would provide shorthand methods for adding Validator-s.

 
 FILES IN THIS DIRECTORY
 
 ---------
  |
  +- localization.js              - Localization utilities and default strings
  |
  +- clientside-form-validation.js      - Script containing the base objects
  |
  +- clientside-datetime-form-validation.js - Date and Time validation additions to the 
  |                                           previous script.
  |
  +- ui-jsp-form-validation  - The definition of the UiFormValidator class, that provides 
  |                                 functionality required for JSP-UI
  |
  +- jsp-ui.js               - File providing interface functions for JSP-UI
  |
  +- validation.html              - Sample usage of validation utilities.
  |
  +- style.css                    - External CSS for validation.html
  |
  +- README.TXT                   - This file.
  
-----------------------------------------------------------------------------------------
EOF
 
