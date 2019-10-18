package swa.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import spark.Service;
import swa.SimpleWebApplication;
import swa.spi.CheckBoxFormElement;
import swa.spi.Column;
import swa.spi.Form;
import swa.spi.FormElement;
import swa.spi.Menu;
import swa.spi.Option;
import swa.spi.ReadOnlyFormElement;
import swa.spi.Row;
import swa.spi.SelectFormElement;
import swa.spi.Table;
import swa.spi.TextFormElement;

public class Example {

  public static void main(String[] args) throws IOException {
    Service service = Service.ignite();

    Table actionTable1 = new Table() {

      @Override
      public String getLink() {
        return "test1";
      }

      @Override
      public List<Row> getRows(Map<String, String[]> queryParams) {
        return Arrays.asList(createRow("1"), createRow("2"), createRow("3"));
      }

      private Row createRow(String id) {
        return Row.builder()
                  .id(id)
                  .columns(Arrays.asList(Column.builder()
                                               .value("test1")
                                               .build(),
                      Column.builder()
                            .value("test2")
                            .build()))
                  .build();
      }

      @Override
      public List<String> getActions(Map<String, String[]> queryParams) {
        return Arrays.asList("action1", "action2");
      }

      @Override
      public List<String> getHeaders(Map<String, String[]> queryParams) {
        return Arrays.asList("col1", "col2");
      }
    };

    Table actionTable2 = new Table() {

      @Override
      public String getLink() {
        return "test2";
      }

      @Override
      public List<Row> getRows(Map<String, String[]> queryParams) {
        return Arrays.asList(createRow("1"), createRow("2"), createRow("3"));
      }

      private Row createRow(String id) {
        return Row.builder()
                  .id(id)
                  .columns(Arrays.asList(Column.builder()
                                               .value("test1")
                                               .build(),
                      Column.builder()
                            .value("test2")
                            .build()))
                  .build();
      }

      @Override
      public List<String> getHeaders(Map<String, String[]> queryParams) {
        return Arrays.asList("col1 another line", "col2");
      }
    };

    Form form = new Form() {

      @Override
      public String getLink() throws IOException {
        return "create";
      }

      @Override
      public List<FormElement> getElements(Map<String, String[]> queryParams) {
        FormElement element1 = TextFormElement.builder()
                                              .name("name")
                                              .label("label")
                                              .defaultValue("default")
                                              .build();

        List<Option> options = Arrays.asList(Option.builder()
                                                   .label("olabel")
                                                   .value("ovalue")
                                                   .build());
        SelectFormElement element2 = SelectFormElement.builder()
                                                      .label("label")
                                                      .name("name")
                                                      .options(options)
                                                      .build();

        CheckBoxFormElement element3 = CheckBoxFormElement.builder()
                                                          .value("text")
                                                          .label("l3")
                                                          .name("l3")
                                                          .build();

        CheckBoxFormElement element4 = CheckBoxFormElement.builder()
                                                          .value("text")
                                                          .label("l3")
                                                          .name("l3")
                                                          .defaultValue(true)
                                                          .build();

        ReadOnlyFormElement element5 = ReadOnlyFormElement.builder()
                                                          .value("nice")
                                                          .label("l23432")
                                                          .name("name1231")
                                                          .build();

        return Arrays.asList(element1, element2, element3, element4, element5);
      }

    };

    Form formError1 = new Form() {

      @Override
      public String getLink() throws IOException {
        return "error1";
      }

      @Override
      public List<FormElement> getElements(Map<String, String[]> queryParams) {
        FormElement element1 = TextFormElement.builder()
                                              .name("name")
                                              .label("label")
                                              .defaultValue("default")
                                              .build();

        return Arrays.asList(element1);
      }

      @Override
      public String execute(Map<String, String[]> queryParams) throws Exception {
        throw new RuntimeException("this is the error");
      }

    };

    Form formError2 = new Form() {

      @Override
      public String getLink() throws IOException {
        return "error2";
      }

      @Override
      public List<FormElement> getElements(Map<String, String[]> queryParams) {
        throw new RuntimeException("this is the error");
      }

    };

    List<Table> tables = Arrays.asList(actionTable1, actionTable2);
    List<Form> forms = Arrays.asList(form, formError1, formError2);

    List<Menu> menus = new ArrayList<>();
    menus.add(Menu.create(actionTable1));
    menus.add(Menu.create(actionTable2));
    menus.add(Menu.create(form));
    menus.add(Menu.create(formError1));
    menus.add(Menu.create(formError2));

    SimpleWebApplication.setup(service, "test1", menus, tables, forms);
  }
}
