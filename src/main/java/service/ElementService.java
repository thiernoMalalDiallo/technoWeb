package service;

import DAO.UnSql2oModel;
import freemarker.template.Template;
import model.AListe;
import model.LaListe;
import spark.Request;
import spark.Response;

import freemarker.template.Configuration;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public  class ElementService{

    //
    private LaListe list_e;
    public StringWriter getList(Template template){
        StringWriter writer = new StringWriter();
        try {
            //template.dump(writer);
            template.process(null, writer);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return writer;
    }
    public StringWriter rechercheList(Configuration configuration, Request request,UnSql2oModel model){
        StringWriter writer = new StringWriter();
        String recherche = request.queryParams("search");
        Map<String, List<AListe>> params = new HashMap<>();
        List<AListe> le = model.recherche(recherche);
        params.put("liste_e", le);
        params.put("liste_e_fils", null);
        try {
            Template template = configuration.getTemplate("templates/listes.ftl");
            template.process(params, writer);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return  writer;
    }
    public StringWriter getAddList(Configuration configuration, UnSql2oModel model){
        StringWriter writer = new StringWriter();
        try {
            Template template = configuration.getTemplate("templates/ajoutlist.ftl");//render("accueil.ftl", model);
            Map<String, Object> params = new HashMap<>();
            List<AListe> le = model.getAllElement();
            List<String> ls = new ArrayList<>();
            params.put("liste_e", null);
            params.put("liste_e_pere", null);
            params.put("liste_tag", null);//.........................pas de tag pour les listes ?
            template.process(params, writer);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return writer;
    }

    public Object addList( UnSql2oModel model, Request request, Response response, LaListe list_e) {
        String titre = request.queryParams("titre");
        String description = request.queryParams("description");
        String id = request.queryParams("idd");
        AListe newListe = new LaListe();
        if(titre != null || description != null){
            newListe.setTitre(titre);
            newListe.setDescription(description);
            Date d = new Date();
            newListe.setId(UUID.randomUUID().hashCode());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long date = new Date().getTime();
            sdf.format(date);
            if(this.list_e.add(newListe)){
                model.insertTableElement(newListe.getId(),newListe.getId(), sdf.format(date), sdf.format(date),newListe.getTitre(), newListe.getDescription(),0);//etat 0 par default, les listes n'ont pas d'état

            }else{
                response.redirect("/listes");
            }
            response.redirect("/listes/"+newListe.getId());
        }else{
            response.redirect("/listes/add");
        }
        return "! add !";
    }

    public StringWriter getALl(Configuration configuration, UnSql2oModel model) {
        list_e.setListe(model.getAllElement());//update de la liste
        StringWriter writer = new StringWriter();
        Map<String, List<AListe>> params = new HashMap<>();
        List<AListe> le = rechercheMere(model);
        params.put("liste_e", le);
        params.put("liste_e_fils", null);
        try {
            Template template = configuration.getTemplate("templates/listes.ftl");
            template.process(params, writer);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return writer;
    }


    public Object deleteList(Response response, Request request,UnSql2oModel model ) {
        int i = Integer.parseInt(replacePasInt(request.params(":name")));
        AListe ee = model.getElement(i);
        model.deleteElement(ee.getId());
        response.redirect("/listes/"+i);
        return "SUPPRIMER name: " + request.params(":name") + " inexistante.(en cours)";
    }
    private List<AListe> rechercheMere(UnSql2oModel model){
        List<AListe> liste = new ArrayList<>();
        for(AListe a: list_e.getListe()){
            List<AListe> l =LaListe.recherchePere(model,list_e.getListe(),a.getId());
            if(l.size() <= 0){
                liste.add(a);
            }
        }
        return liste;
    }
    private String replacePasInt(String s){
        return s.replaceAll(",","").replaceAll(" ","").replaceAll("%","");
    }

}