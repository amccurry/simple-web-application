package swa.spi;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
public class SelectFormElement implements FormElement {

  String label;

  String name;

  @Builder.Default
  FormType formType = FormType.SELECT;

  @Builder.Default
  List<Option> options = new ArrayList<>();

}
