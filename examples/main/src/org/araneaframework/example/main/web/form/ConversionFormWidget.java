
package org.araneaframework.example.main.web.form;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.form.Converter;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.BaseFieldConstraint;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.converter.BaseConverter;

/**
 * This widget shows how custom data converters and validators can be used to define custom input logic. Here we have
 * two inputs - one for user providing us an integer (1 - 3999) so that it could be converted to a Roman numeral; other
 * input for doing to opposite.
 * <p>
 * Let's start from the beginning. Form elements handle data in two formats. The value from user is first stored in form
 * element control in its built-in format (which needs to be as generic as possible). When form (element) is converted,
 * the value from control is provided to a converter so that it would be returned as defined in form element data.
 * <p>
 * In this example, the first element uses NumberControl for gathering input, which holds its data internally as
 * BigIntger. The second element uses TextControl for gathering input, which uses a String variable to hold data
 * internally. The final data type of both form elements is RomanNumber (a custom data class defined below).
 * <p>
 * Obviously, we need to define converters so that the form could automatically convert data from control to the target
 * data type. Here we have two: RomanNumberToBigIntegerConverter and RomanNumberToStringConverter. We chose to specify
 * these converters directly to form elements through FormElement.setConverter(Converter), but this manual work can be
 * avoided when converters are registered in {@link org.araneaframework.uilib.form.converter.ConverterFactory} (see
 * reference manual for more information).
 * <p>
 * In addition, a validator has been added for text input: RomanNumberValidator, which informs user about incorrect
 * input when control value is not empty but form element value is empty. It works this way because converters are
 * called before validators (remember method FormWidget.convertAndValidate()). Validators are called constraints in
 * Aranea framework and they can be specified only through FormElement.setConstraint(Constraint). Constraints cannot be
 * declared centrally.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 */
public class ConversionFormWidget extends TemplateBaseWidget {

  private static final String[] ROMAN_CODE = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };

  private static final int[] ROMAN_VAL = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };

  // Here is our form used in this demo:
  private FormWidget convForm = new FormWidget();

  @Override
  protected void init() {
    // We need to define two form elements for gathering input.

    // 1. Number input (from 1 to 3999) to convert it into Roman numeral.
    // The control value type is BigInteger and form element data type is RomanNumber.
    NumberControl toRomanControl = new NumberControl(BigInteger.ONE, BigInteger.valueOf(3999));
    FormElement<BigInteger, RomanNumber> toRomanFE = this.convForm.addElement("toRoman", "form.roman.to",
        toRomanControl, new Data<RomanNumber>(RomanNumber.class), true);
    toRomanFE.setConverter(new RomanNumberToBigIntegerConverter()); // Note this important data converter!

    // 2. Text input to convert it from Roman numeral to a decimal.
    // The control value type is String and form element data type is RomanNumber.
    FormElement<String, RomanNumber> fromRomanFE = this.convForm.addElement("fromRoman", "form.roman.from",
        new TextControl(), new Data<RomanNumber>(RomanNumber.class), true);
    fromRomanFE.setConstraint(new RomanNumberValidator()); // A validator for validating input.
    fromRomanFE.setConverter(new RomanNumberToStringConverter()); // Note this important data converter!

    // Now we need to only register the form and the JSP page.
    addWidget("conversionForm", this.convForm);
    setViewSelector("form/conversionForm");
  }

  /**
   * Handles converting a decimal to Roman numeral.
   * <p>
   * <code>this.convForm.getValueByFullName("toRoman")</code> returns a RomanNumber object.
   */
  public void handleEventToRoman() {
    if (this.convForm.getElement("toRoman").convertAndValidate()) {
      getMessageCtx().showInfoMessage("form.msg.toRoman", this.convForm.getValueByFullName("toRoman"));
    }
  }

  /**
   * Handles converting a Roman numeral to decimal.
   */
  public void handleEventFromRoman() {
    if (this.convForm.getElement("fromRoman").convertAndValidate()) {
      RomanNumber value = (RomanNumber) this.convForm.getValueByFullName("fromRoman");
      getMessageCtx().showInfoMessage("form.msg.fromRoman", value.number);
    }
  }

  /**
   * A converter for converting a Roman numeral to BigInteger and vice-versa.
   */
  private static class RomanNumberToBigIntegerConverter extends BaseConverter<BigInteger, RomanNumber> {

    @Override
    protected RomanNumber convertNotNull(BigInteger data) {
      return new RomanNumber(data.intValue());
    }

    @Override
    protected BigInteger reverseConvertNotNull(RomanNumber data) {
      return BigInteger.valueOf(data.number);
    }

    @Override
    public Converter<BigInteger, RomanNumber> newConverter() {
      return new RomanNumberToBigIntegerConverter();
    }
  }

  /**
   * A converter for converting a Roman numeral to String and vice-versa. When a String is not a valid Roman numeral, a
   * null value will be returned.
   */
  private static class RomanNumberToStringConverter extends BaseConverter<String, RomanNumber> {

    @Override
    protected RomanNumber convertNotNull(String data) {
      try {
        return new RomanNumber(data);
      } catch (IllegalArgumentException e) {
        return null;
      }
    }

    @Override
    protected String reverseConvertNotNull(RomanNumber data) {
      return data.toString();
    }

    @Override
    public Converter<String, RomanNumber> newConverter() {
      return new RomanNumberToStringConverter();
    }
  }

  /**
   * A Roman numeral value validator that checks whether a String value was provided to an input and whether it was
   * possible to convert the value to a RomanNumber object. When it was not possible, the user will be informed.
   */
  private static class RomanNumberValidator extends BaseFieldConstraint<String, RomanNumber> {

    @Override
    protected void validateConstraint() {
      if (getField().getControl().getRawValue() != null && getValue() == null) {
        addError("form.msg.invalidRomanNum");
      }
    }
  }

  /**
   * A custom data type used in this demo as the final form element type into which raw value from input will be
   * converted to.
   */
  public static class RomanNumber implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int number;

    public RomanNumber(int num) {
      this.number = num;
    }

    public RomanNumber(String roman) {
      this.number = parse(roman);
    }

    private static int parse(String roman) {
      int num = 0;
      int indexText = 0;
      int prevIndexCode = -1;

      Map<String, Integer> usedLiterals = new HashMap<String, Integer>();

      while (indexText < roman.length()) {
        int indexCode = -1;
        String code = null;

        if (indexText < roman.length() - 1) {
          code = roman.substring(indexText, indexText + 2);
          indexCode = ArrayUtils.indexOf(ROMAN_CODE, code);
        }
        if (indexCode < 0) {
          code = roman.substring(indexText, indexText + 1);
          indexCode = ArrayUtils.indexOf(ROMAN_CODE, code);
        }

        int usedCount = usedLiterals.containsKey(code) ? usedLiterals.get(code) + 1 : 1;
        usedLiterals.put(code, usedCount);

        if (indexCode < 0) {
          throw new IllegalArgumentException("Invalid character at position " + indexText);
        } else if (code == null || code.length() == 1 && usedCount > 3 || code.length() == 2 && usedCount > 1
            || prevIndexCode >= 0 && prevIndexCode > indexCode) {
          throw new IllegalArgumentException("Invalid roman number");
        }

        num += ROMAN_VAL[indexCode];

        prevIndexCode = indexCode;
        indexText += code.length();
      }

      return num;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();

      int num = this.number;

      if (num > 0 && num < 4000) {
        for (int i = 0; i < ROMAN_CODE.length; i++) {
          while (num >= ROMAN_VAL[i]) {
            num -= ROMAN_VAL[i];
            sb.append(ROMAN_CODE[i]);
          }
        }
      }

      return sb.toString();
    }

  }
}
