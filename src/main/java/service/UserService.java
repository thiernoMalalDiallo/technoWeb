package service;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    public StringWriter getConnexion(Configuration configuration){
        StringWriter writer = new StringWriter();
        try {
            Map<String, Integer> params = new HashMap<>();
            params.put("isIncription", 0);
            Template template = configuration.getTemplate("templates/connexion.ftl");
            template.process(params, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer;
    }
}
