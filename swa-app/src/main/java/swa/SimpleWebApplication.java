package swa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Service;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;
import swa.spi.Form;
import swa.spi.Html;
import swa.spi.Menu;
import swa.spi.MenuTable;
import swa.spi.Table;

public class SimpleWebApplication {

  private static final String ERROR_MESSAGE = "errorMessage";
  private static final String ERROR_HTML = "error.html";
  private static final String TABLE_HTML = "table.html";
  private static final String FORM_HTML = "form.html";
  private static final String SUBMIT_NAME = "submitName";
  private static final String SUBMIT_VALUE = "submitValue";
  private static final String ELEMENTS = "elements";
  private static final String HEADERS = "headers";
  private static final String ROWS = "rows";
  private static final String ACTIONS = "actions";
  private static final String LINK = "link";
  private static final String NAME = "name";
  private static final String MENUS = "menus";
  private static final String CURRENT_MENU_NAME = "currentMenuName";
  private static final String WINDOW_NAME = "windowName";
  private static final String WINDOW_TITLE = "windowTitle";
  private static final String PUBLIC = "/public";
  private static final String ID = "id";
  private static final String ACTION = "action";
  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleWebApplication.class);

  public static void setup(Service service, String defaultTable, List<Menu> menus, List<Table> tables, List<Form> forms)
      throws IOException {
    new SimpleWebApplication(service, defaultTable, menus, tables, forms);
  }

  private final Service _service;
  private final FreeMarkerEngine _engine = new FreeMarkerEngine();
  private final Map<String, Table> _tables = new ConcurrentHashMap<>();
  private final Map<String, Form> _forms = new ConcurrentHashMap<>();
  private final MenuTable _menuTable;

  public SimpleWebApplication(Service service, String defaultTable, List<Menu> menus, List<Table> tables,
      List<Form> forms) throws IOException {
    _service = service;
    _service.staticFileLocation(PUBLIC);
    _menuTable = new MenuTable() {
      @Override
      public List<Menu> getMenus() throws IOException {
        return menus;
      }
    };
    for (Table table : tables) {
      addTable(table);
    }
    for (Form form : forms) {
      addForm(form);
    }
    _service.before((request, response) -> LOGGER.info("request method {} uri {}", request.requestMethod(),
        request.raw()
               .getRequestURI()));
    _service.get("/", (request, response) -> {
      Table table = _tables.get(defaultTable);
      response.redirect("/" + table.getLink());
      return null;
    });
  }

  public void addTable(Table table) throws IOException {
    String name = table.getLink();
    if (_tables.containsKey(name)) {
      throw new RuntimeException("Already contains table " + name);
    }
    _tables.put(name, table);
    _service.get("/" + name, getTable(table), _engine);
    _service.post("/" + name, postTable(table), _engine);
  }

  private void addForm(Form form) throws IOException {
    String name = form.getLink();
    if (_forms.containsKey(name)) {
      throw new RuntimeException("Alread contains form " + name);
    }
    _forms.put(name, form);
    _service.get("/" + name, getForm(form), _engine);
    _service.post("/" + name, postForm(form), _engine);
  }

  private TemplateViewRoute postForm(Form form) {
    return (request, response) -> {
      try {
        QueryParamsMap map = request.queryMap();
        String redirect = form.execute(map.toMap());
        response.redirect("/" + redirect);
        return null;
      } catch (Exception e) {
        Map<String, Object> model = createModel(form);
        model.put(ERROR_MESSAGE, e.getMessage());
        return new ModelAndView(model, ERROR_HTML);
      }
    };
  }

  private TemplateViewRoute getForm(Form form) {
    return (request, response) -> {
      try {
        Map<String, String[]> map = request.queryMap()
                                           .toMap();
        Map<String, Object> model = createModel(form);
        model.put(SUBMIT_NAME, form.getSubmitButtonName());
        model.put(SUBMIT_VALUE, form.getSubmitButtonValue());
        model.put(ELEMENTS, form.getElements(map));
        return new ModelAndView(model, FORM_HTML);
      } catch (Exception e) {
        Map<String, Object> model = createModel(form);
        model.put(ERROR_MESSAGE, e.getMessage());
        return new ModelAndView(model, ERROR_HTML);
      }
    };
  }

  private TemplateViewRoute postTable(Table table) {
    return (request, response) -> {
      try {
        String action = request.queryParams(ACTION);
        String redirect = table.execute(action, request.queryParamsValues(ID));
        response.redirect("/" + redirect);
        return null;
      } catch (Exception e) {
        Map<String, Object> model = createModel(table);
        model.put(ERROR_MESSAGE, e.getMessage());
        return new ModelAndView(model, ERROR_HTML);
      }
    };
  }

  private TemplateViewRoute getTable(Table table) {
    return (request, response) -> {
      try {
        Map<String, String[]> map = request.queryMap()
                                           .toMap();
        Map<String, Object> model = createModel(table);
        model.put(HEADERS, table.getHeaders(map));
        model.put(ROWS, table.getRows(map));
        model.put(ACTIONS, table.getActions(map));
        return new ModelAndView(model, TABLE_HTML);
      } catch (Exception e) {
        Map<String, Object> model = createModel(table);
        model.put(ERROR_MESSAGE, e.getMessage());
        return new ModelAndView(model, ERROR_HTML);
      }
    };
  }

  private Map<String, Object> createModel(Html html) throws IOException {
    Map<String, Object> model = new HashMap<>();
    List<Menu> menus = new ArrayList<>();
    menus.addAll(_menuTable.getMenus());
    model.put(WINDOW_TITLE, html.getWindowTitle());
    model.put(WINDOW_NAME, html.getWindowName());
    model.put(CURRENT_MENU_NAME, html.getName());
    model.put(MENUS, menus);
    model.put(NAME, html.getName());
    model.put(LINK, html.getLink());
    return model;
  }

}
