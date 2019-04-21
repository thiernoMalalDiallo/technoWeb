package service;

import DAO.UnSql2oModel;
import freemarker.template.Template;
import model.AListe;
import model.LaListe;
import model.Tag;
import spark.Request;
import spark.Response;

import freemarker.template.Configuration;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public  class ElementService{

    //
    private LaListe list_e;
    public StringWriter getList(Configuration configuration){
        StringWriter writer = new StringWriter();
        try {
            Template template = configuration.getTemplate("templates/listes.ftl");
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

    public StringWriter getListUser(Configuration configuration, Request request, UnSql2oModel model){
        list_e.setListe(model.getAllElement());//update de la liste
        StringWriter writer = new StringWriter();
        String s = replacePasInt(request.params(":name"));
        int i = -3;i = Integer.parseInt(replacePasInt(s));//request.params(":name")
        AListe ee;ee = model.getElement(i);
        Map<String, List<AListe>> params = new HashMap<>();
        List<AListe> le = new ArrayList<>();le.add(ee);// future Liste<AListe>
        List<AListe> lee = new ArrayList<>();//future Liste element
        lee.addAll(LaListe.rechercheFils(model,list_e.getListe(),ee.getId()));
        params.put("liste_e", le);
        params.put("liste_e_fils", lee);
        try {

            Template template = configuration.getTemplate("templates/listes.ftl");
            template.process(params, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer;
    }


    public Object updateListUser(Configuration configuration, Request request, UnSql2oModel model) {
        StringWriter writer = new StringWriter();
        int i = Integer.parseInt(replacePasInt(request.params(":name")));
        AListe ee = model.getElement(i);

        Map<String, Object> params = new HashMap<>();
        List<AListe> le = new ArrayList<>();le.add(ee);
        List<String> ls = new ArrayList<>();//ls.add("atest");ls.add("btest");ls.add("ctest");ls.add("dtest");ls.add("etest");
        for(Tag t: model.getAllTag(i)){
            ls.add(t.getTag()+",");
        }
        params.put("liste_e", le);
        params.put("liste_e_pere", null);

        //pour ne pas afficher le input des tags pour les listes
        list_e.setListe(model.getAllElement());//update de la liste
        if(LaListe.rechercheFils(model,list_e.getListe(),ee.getId()).size() > 0){
            params.put("liste_tag", null);
        }else{
            params.put("liste_tag", ls);
        }

        try {
            Template template = configuration.getTemplate("templates/ajoutlist.ftl");
            template.process(params, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer;
    }


    public Object updateEelemntList(Request request,UnSql2oModel model,Response response) {
        String titre = request.queryParams("titre");//request.params("")
        String description = request.queryParams("description");//request.params("")
        String id = request.queryParams("idd");//request.params("")
        String tags = request.queryParams("tags");

        String[] ls = tags.split(",");
        int i = Integer.parseInt(replacePasInt(request.params(":name")));
        Date d = new Date();
        d.setTime(System.currentTimeMillis());//inutile
        int etat = 0;
        String afaire = request.queryParams("afaire");
        String fait = request.queryParams("fait");
        if(afaire == null){
            if(fait != null){
                etat = 2;
            }
        }else{
            etat = 1;
        }
        if(titre != null || description != null){
            //modification
            AListe ee = model.getElement(i);
            model.updateElement(ee.getId(),ee.getId(), ee.getDateCreation(),d,titre,description,etat);
            model.deleteTagsElement(ee.getId()); //spprime tout les tags
            for(String s:ls){
                model.insertTableTag(ee.getId(),s);//ajout des nouveaux tags
            }
            response.redirect("/listes/"+i);
        }else{
            response.redirect("/listes/"+i);
        }
        return "!";
    }

    public Object getEleementList(Configuration configuration, Request request, UnSql2oModel model) {
        StringWriter writer = new StringWriter();
        int i = -3;i = Integer.parseInt(replacePasInt(request.params(":name")));
        AListe ee;ee = model.getElement(i);//inutile ?

        Map<String, Object> params = new HashMap<>();
        List<AListe> le = new ArrayList<>();le.add(ee);
        List<String> ls = new ArrayList<>();
        params.put("liste_e", null);
        params.put("liste_e_pere", le); //inutile ?
        params.put("liste_tag", ls);
        try {
            Template template = configuration.getTemplate("templates/ajoutlist.ftl");
            template.process(params, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer;
    }

    public Object addElementList(Request request,Response response,UnSql2oModel model) {
        String titre = request.queryParams("titre");//request.params("")
        String description = request.queryParams("description");//request.params("")
        String id = replacePasInt(request.queryParams("idd"));
        String tags = request.queryParams("tags");
        String[] ls = tags.split(",");
        AListe newListe = new LaListe();
        int etat = 0;
        String afaire = request.queryParams("afaire");
        String fait = request.queryParams("fait");
        if(afaire == null){
            if(fait != null){
                etat = 2;
            }
        }else{
            etat = 1;
        }
        if(titre != null || description != null){
            newListe.setTitre(titre);
            newListe.setDescription(description);
            newListe.setId(UUID.randomUUID().hashCode());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long date = new Date().getTime();
            sdf.format(date);
            if(list_e.add(newListe)){
                model.insertTableElement(newListe.getId(),Integer.parseInt(id), sdf.format(date),sdf.format(date), newListe.getTitre(), newListe.getDescription(),etat);//
                model.insertTablePossede(newListe.getId(), Integer.parseInt(id));
                for(String s:ls){
                    model.insertTableTag(newListe.getId(),s);
                }
            }else{
                //redirection erreur nouvelle liste
            }
            response.redirect("/listes/"+Integer.parseInt(id));
        }else{
            // faire redirection erreur nouvelle liste
            response.redirect("/listes/add");
        }
        //response.redirect("/listes/"+id);
        return "!";
    }
    public Object getdeleteElementList(Request request,UnSql2oModel model,Response response) {
        int i = Integer.parseInt(replacePasInt(request.params(":name")));
        AListe ee = model.getElement(i);
        model.deleteElement(ee.getId());
        response.redirect("/listes/all");
        return "Liste supp: " + request.params(":name") + " inexistante.(en cours)";
    }
    public Object deleteElementList(Request request,UnSql2oModel model,Response response) {
        //request.params(":name")
        int i = Integer.parseInt(replacePasInt(request.params(":name")));
        AListe ee = model.getElement(i);
        model.deleteElement(ee.getId());
        response.redirect("/listes/all");
        return "Liste supp: " + request.params(":name") + " inexistante.";
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