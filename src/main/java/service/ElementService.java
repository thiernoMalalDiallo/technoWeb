package service;

import DAO.DAO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import model.Tag;
import model.ToDo;
import model.TodoList;
import spark.Request;
import spark.Response;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class ElementService {

    //
    private TodoList list_e;

    public StringWriter getList(Configuration configuration) {
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

    public StringWriter rechercheList(Configuration configuration, Request request, DAO model) {
        StringWriter writer = new StringWriter();
        String recherche = request.queryParams("search");
        Map<String, List<ToDo>> params = new HashMap<>();
        List<ToDo> le = DAO.recherche(recherche);
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

    public StringWriter getAddList(Configuration configuration, DAO model) {
        StringWriter writer = new StringWriter();
        try {
            Template template = configuration.getTemplate("templates/ajoutlist.ftl");//render("accueil.ftl", model);
            Map<String, Object> params = new HashMap<>();
            List<ToDo> le = DAO.getAllElement();
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

    public Object addList(DAO model, Request request, Response response, TodoList list_e) {
        String titre = request.queryParams("titre");
        String description = request.queryParams("description");
        String id = request.queryParams("idd");
        ToDo newListe = new TodoList();
        if (titre != null || description != null) {
            newListe.setTitre(titre);
            newListe.setDescription(description);
            Date d = new Date();
            newListe.setId(UUID.randomUUID().hashCode());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long date = new Date().getTime();
            sdf.format(date);
            if (list_e.add(newListe)) {
                DAO.insertTableElement(newListe.getId(), newListe.getId(), sdf.format(date), sdf.format(date), newListe.getTitre(), newListe.getDescription(), 0);//etat 0 par default, les listes n'ont pas d'état

            } else {
                response.redirect("/listes");
            }
            response.redirect("/listes/" + newListe.getId());
        } else {
            response.redirect("/listes/add");
        }
        return "! add !";
    }

    public StringWriter getALl(Configuration configuration, DAO model, TodoList list_e) {
        list_e.setListe(DAO.getAllElement());
        StringWriter writer = new StringWriter();
        Map<String, List<ToDo>> params = new HashMap<>();
        List<ToDo> le = rechercheMere(model, list_e);
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


    public Object deleteList(Response response, Request request, DAO model) {
        int i = Integer.parseInt(replacePasInt(request.params(":name")));
        ToDo ee = DAO.getElement(i);
        DAO.deleteElement(ee.getId());
        response.redirect("/listes/" + i);
        return "SUPPRIMER name: " + request.params(":name") + " inexistante.(en cours)";
    }

    public StringWriter getListUser(Configuration configuration, Request request, DAO model) {
        list_e.setListe(DAO.getAllElement());//update de la liste
        StringWriter writer = new StringWriter();
        String s = replacePasInt(request.params(":name"));
        int i = -3;
        i = Integer.parseInt(replacePasInt(s));//request.params(":name")
        ToDo ee;
        ee = DAO.getElement(i);
        Map<String, List<ToDo>> params = new HashMap<>();
        List<ToDo> le = new ArrayList<>();
        le.add(ee);// future Liste<ToDo>
        List<ToDo> lee = new ArrayList<>();//future Liste element
        lee.addAll(TodoList.rechercheFils(model, list_e.getListe(), ee.getId()));
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


    public Object updateListUser(Configuration configuration, Request request, DAO model) {
        StringWriter writer = new StringWriter();
        int i = Integer.parseInt(replacePasInt(request.params(":name")));
        ToDo ee = DAO.getElement(i);

        Map<String, Object> params = new HashMap<>();
        List<ToDo> le = new ArrayList<>();
        le.add(ee);
        List<String> ls = new ArrayList<>();//ls.add("atest");ls.add("btest");ls.add("ctest");ls.add("dtest");ls.add("etest");
        for (Tag t : DAO.getAllTag(i)) {
            ls.add(t.getTag() + ",");
        }
        params.put("liste_e", le);
        params.put("liste_e_pere", null);

        //pour ne pas afficher le input des tags pour les listes
        list_e.setListe(DAO.getAllElement());//update de la liste
        if (TodoList.rechercheFils(model, list_e.getListe(), ee.getId()).size() > 0) {
            params.put("liste_tag", null);
        } else {
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


    public Object updateEelemntList(Request request, DAO model, Response response) {
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
        if (afaire == null) {
            if (fait != null) {
                etat = 2;
            }
        } else {
            etat = 1;
        }
        if (titre != null || description != null) {
            //modification
            ToDo ee = DAO.getElement(i);
            DAO.updateElement(ee.getId(), ee.getId(), ee.getDateCreation(), d, titre, description, etat);
            DAO.deleteTagsElement(ee.getId()); //spprime tout les tags
            for (String s : ls) {
                DAO.insertTableTag(ee.getId(), s);//ajout des nouveaux tags
            }
            response.redirect("/listes/" + i);
        } else {
            response.redirect("/listes/" + i);
        }
        return "!";
    }

    public Object getEleementList(Configuration configuration, Request request, DAO model) {
        StringWriter writer = new StringWriter();
        int i = -3;
        i = Integer.parseInt(replacePasInt(request.params(":name")));
        ToDo ee;
        ee = DAO.getElement(i);//inutile ?

        Map<String, Object> params = new HashMap<>();
        List<ToDo> le = new ArrayList<>();
        le.add(ee);
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

    public Object addElementList(Request request, Response response, DAO model) {
        String titre = request.queryParams("titre");//request.params("")
        String description = request.queryParams("description");//request.params("")
        String id = replacePasInt(request.queryParams("idd"));
        String tags = request.queryParams("tags");
        String[] ls = tags.split(",");
        ToDo newListe = new TodoList();
        int etat = 0;
        String afaire = request.queryParams("afaire");
        String fait = request.queryParams("fait");
        if (afaire == null) {
            if (fait != null) {
                etat = 2;
            }
        } else {
            etat = 1;
        }
        if (titre != null || description != null) {
            newListe.setTitre(titre);
            newListe.setDescription(description);
            newListe.setId(UUID.randomUUID().hashCode());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long date = new Date().getTime();
            sdf.format(date);
            if (list_e.add(newListe)) {
                DAO.insertTableElement(newListe.getId(), Integer.parseInt(id), sdf.format(date), sdf.format(date), newListe.getTitre(), newListe.getDescription(), etat);//
                DAO.insertTablePossede(newListe.getId(), Integer.parseInt(id));
                for (String s : ls) {
                    DAO.insertTableTag(newListe.getId(), s);
                }
            } else {
                //redirection erreur nouvelle liste
            }
            response.redirect("/listes/" + Integer.parseInt(id));
        } else {
            // faire redirection erreur nouvelle liste
            response.redirect("/listes/add");
        }
        //response.redirect("/listes/"+id);
        return "!";
    }

    public Object getdeleteElementList(Request request, DAO model, Response response) {
        int i = Integer.parseInt(replacePasInt(request.params(":name")));
        ToDo ee = DAO.getElement(i);
        DAO.deleteElement(ee.getId());
        response.redirect("/listes/all");
        return "Liste supp: " + request.params(":name") + " inexistante.(en cours)";
    }

    public Object deleteElementList(Request request, DAO model, Response response) {
        //request.params(":name")
        int i = Integer.parseInt(replacePasInt(request.params(":name")));
        ToDo ee = DAO.getElement(i);
        DAO.deleteElement(ee.getId());
        response.redirect("/listes/all");
        return "Liste supp: " + request.params(":name") + " inexistante.";
    }

    public List<ToDo> rechercheMere(DAO model, TodoList list_e) {
        List<ToDo> liste = new ArrayList<>();
        for (ToDo a : list_e.getListe()) {
            List<ToDo> l = TodoList.recherchePere(model, list_e.getListe(), a.getId());
            if (l.size() <= 0) {
                liste.add(a);
            }
        }
        return liste;
    }

    private String replacePasInt(String s) {
        return s.replaceAll(",", "").replaceAll(" ", "").replaceAll("%", "");
    }


}