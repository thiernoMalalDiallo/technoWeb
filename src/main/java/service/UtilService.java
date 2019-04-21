package service;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringWriter;

public class UtilService {
    public StringWriter getAcceuil(Configuration configuration) {
        StringWriter writer = new StringWriter();
        try {
            Template template = configuration.getTemplate("templates/accueil.ftl");
            template.process(null, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer;
    }

    public StringWriter getInfo(Configuration configuration) {
        StringWriter writer = new StringWriter();
        try {
            Template template = configuration.getTemplate("templates/info.ftl");
            template.process(null, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer;
    }

    public Object get404(Configuration configuration) {
        StringWriter writerh = new StringWriter();
        StringWriter writerf = new StringWriter();
        try {
            Template template = configuration.getTemplate("templates/header.ftl");//render("accueil.ftl", model);
            template.process(null, writerh);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        try {
            Template template = configuration.getTemplate("templates/footer.ftl");//render("accueil.ftl", model);
            template.process(null, writerf);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        ///res.type("application/json");
        return writerh + "<center>{\"Erreur\":\"404\" page introuvable}</center>" + writerf;
    }

    public Object get500(Configuration configuration) {
        StringWriter writerh = new StringWriter();
        StringWriter writerf = new StringWriter();
        try {
            Template template = configuration.getTemplate("templates/header.ftl");//render("accueil.ftl", model);
            template.process(null, writerh);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        try {
            Template template = configuration.getTemplate("templates/footer.ftl");//render("accueil.ftl", model);
            template.process(null, writerf);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        //res.type("application/json");
        return writerh + "<center>{\"Erreur\":\"500 Problème(s) serveur\"}</center>" + writerf;
    }
}
