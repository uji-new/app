package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.controller.generic.BaseController;
import app.manager.QueryManager;

@RestController
@RequestMapping("/query")
public class QueryController extends BaseController {
    @Autowired protected QueryManager queries;

    @GetMapping("/{query}")
    public Object query(@PathVariable String query) {
        return queries.getAllData(query);
    }
}
