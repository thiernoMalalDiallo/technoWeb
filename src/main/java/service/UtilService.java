package service;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringWriter;

public class UtilService {
    public StringWriter getAcceuil(Configuration configuration){
        StringWriter writer = new StringWriter();
        try {
            Template template = configuration.getTemplate("templates/accueil.ftl");
            template.process(null, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer;
    }
}
