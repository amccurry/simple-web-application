package swa.spi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class CheckBoxFormElement implements FormElement {

  String label;

  String name;

  @Builder.Default
  FormType formType = FormType.CHECKBOX;

  String value;
  
  @Builder.Default
  boolean defaultValue = false;

}
