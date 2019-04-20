package service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import spark.Request;
import spark.Response;

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
    public Object Connect(Configuration configuration, Request request, Response response){
        String email = request.queryParams("email");
        String mdp = request.queryParams("mdp");
        String isInscription = request.queryParams("isIncription");
        if(email != null || mdp != null){
                    /*if(Integer.parseInt(isInscription) == 0){
                        //inscription
                        //model.insertTableElement(l.getListElement().size()+1,)
                    }else{
                        //connection
                    }*/
        }else{
            response.redirect("/accueil");
        }
        response.redirect("/accueil");
        return "!";
    }

    public Object inscription(Configuration configuration, Request request, Response response) {
        String email = request.queryParams("email");
        String mdp = request.queryParams("mdp");
        String isInscription = request.queryParams("isIncription");
        if(email != null || mdp != null){

        }else{
            response.redirect("/connexion");
        }
        response.redirect("/connexion");
        return "!";
    }
}
