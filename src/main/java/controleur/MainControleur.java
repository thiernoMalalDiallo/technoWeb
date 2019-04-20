package controleur;

import autre.log4jConf;
import freemarker.template.*;
import model.AListe;
import model.LaListe;
import DAO.UnSql2oModel;
import model.Tag;
import org.apache.log4j.BasicConfigurator;
import service.UtilService;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

import java.text.SimpleDateFormat;
import java.util.*;

import static spark.Spark.*;
import static spark.Spark.internalServerError;

/**
 *
 */
public class MainControleur {
    //
    Configuration configuration = new Configuration(Configuration.VERSION_2_3_19);
    //
    LaListe list_e;
    //
    UnSql2oModel model;

    UtilService utilService;

    public MainControleur(UnSql2oModel modelsql, LaListe liste) {
        this.list_e = liste;
        this.model = modelsql;
    }


    public void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        port(8080);
        int maxThreads = 10;
        int minThreads = 1;
        int timeOutMillis = 30000;
        threadPool(maxThreads, minThreads, timeOutMillis);
        staticFiles.expireTime(600); // ten minutes
        configuration = new Configuration(Configuration.VERSION_2_3_19);
        configuration.setDirectoryForTemplateLoading(new File("src/main/ressources"));
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        externalStaticFileLocation("src/main/ressources");

        this.utilService = new UtilService();

        /**
         * routes
         */
        get("/", (request, response) -> {
            response.redirect("/accueil");
            return null;
        });

        path("/", () -> {
           get("/accueil", (request, response) -> {
                return this.utilService.getAcceuil(this.configuration);
            });
            /*--------------------------------------------------------------------------------------------------------------------------------------------*/
            get("/info", (request, response) -> {
                StringWriter writer = new StringWriter();
                try {
                    Template template = configuration.getTemplate("templates/info.ftl");//render("accueil.ftl", model);
                    template.process(null, writer);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                return writer;
                //return "info";
            });
            /*--------------------------------------------------------------------------------------------------------------------------------------------*/
            get("/connexion", (request, response) -> {
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
            });
            post("/connexion", (request, response) -> {
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
            });

            post("/inscription", (request, response) -> {//reunir dans connexion ?
                String email = request.queryParams("email");
                String mdp = request.queryParams("mdp");
                String isInscription = request.queryParams("isIncription");
                if(email != null || mdp != null){

                }else{
                    response.redirect("/connexion");
                }
                response.redirect("/connexion");
                return "!";
            });
            /*LES LISTES--------------------------------------------------------------------------------------------------------------------------------------------*/
            path("/listes", () -> {
                get("", (request, response) -> {
                    StringWriter writer = new StringWriter();
                    try {
                        Template template = configuration.getTemplate("templates/listes.ftl");//render("accueil.ftl", model);
                        //template.dump(writer);
                        template.process(null, writer);
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    return writer;
                    //return "Liste : " + request.params(":name") + " inexistante.";
                });
                //RECHERCHE................................................................................................................................!!!!!!!!!!!
                get("/recherche", (request, response) -> {
                    StringWriter writer = new StringWriter();
                    String recherche = request.queryParams("search");
                    Map<String, List<AListe>> params = new HashMap<>();
                    List<AListe> le = model.recherche(recherche);
                    params.put("liste_e", le);
                    params.put("liste_e_fils", null);
                    try {
                        Template template = configuration.getTemplate("templates/listes.ftl");//render("accueil.ftl", model);
                        template.process(params, writer);
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    return writer;
                    //return "Liste Recherche: " + request.params(":name") + " inexistante.(en cours)";
                });
                //ADD................................................................................................................................
                get("/add", (request, response) -> {
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
                });
                post("/add", (request, response) -> {
                    String titre = request.queryParams("titre");
                    String description = request.queryParams("description");
                    String id = request.queryParams("idd");
                    AListe newListe = new LaListe();
                    if(titre != null || description != null){
                        newListe.setTitre(titre);
                        newListe.setDescription(description);
                        Date d = new Date();
                        d.setTime(System.currentTimeMillis());//inutile
                        newListe.setDateCreation(d);//inutile
                        newListe.setDateCreation(d);//inutile
                        newListe.setId(UUID.randomUUID().hashCode());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        long date = new Date().getTime();
                        sdf.format(date);
                        if(list_e.add(newListe)){
                            model.insertTableElement(newListe.getId(),newListe.getId(), sdf.format(date), sdf.format(date),newListe.getTitre(), newListe.getDescription(),0);//etat 0 par default, les listes n'ont pas d'état
                            //model.insertTablePossede(newListe.getId(), list_e.getId());
                        }else{
                            //redirection erreur nouvelle liste
                        }
                        response.redirect("/listes/"+newListe.getId());
                    }else{
                        // faire redirection erreur nouvelle liste
                        response.redirect("/listes/add");
                    }
                    return "! add !";
                });
                //TOUT AFFICHER................................................................................................................................
                get("/all", (request, response) -> {
                    list_e.setListe(model.getAllElement());//update de la liste
                    StringWriter writer = new StringWriter();
                    Map<String, List<AListe>> params = new HashMap<>();
                    List<AListe> le = rechercheMere();//model.getAllElement();
                    params.put("liste_e", le);
                    params.put("liste_e_fils", null);
                    try {
                        Template template = configuration.getTemplate("templates/listes.ftl");//render("accueil.ftl", model);
                        template.process(params, writer);
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    return writer;
                });

                //SUPPRIMER LISTE................................................................................................................................
                delete("/:name", (request, response) -> {
                    //request.params(":name")
                    int i = Integer.parseInt(replacePasInt(request.params(":name")));
                    AListe ee = model.getElement(i);
                    model.deleteElement(ee.getId());
                    //model.d
                    //modification
                    //model.insertTableElement(l.getListElement().size()+1,)
                    response.redirect("/listes/"+i);
                    return "SUPPRIMER name: " + request.params(":name") + " inexistante.(en cours)";
                });
                //................................................................................................................................
                path("/:name", () -> {
                    //AFFICHAGE LISTE................................................................................................................................!!!!!!!!!!! ELEMENTS
                    get("", (request, response) -> {
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
                            Template template = configuration.getTemplate("templates/listes.ftl");//render("accueil.ftl", model);
                            template.process(params, writer);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return writer;
                    });
                    //MODIFICATION................................................................................................................................
                    path("/modif", () -> {
                        get("", (request, response) -> {
                            //response.type("text/html");
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
                                Template template = configuration.getTemplate("templates/ajoutlist.ftl");//render("accueil.ftl", model);
                                template.process(params, writer);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return writer;
                        });
                        post("", (request, response) -> {
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
                        });
                    });

                    //AJOUT ELEMENT................................................................................................................................
                    get("/add", (request, response) -> {
                        //response.type("text/html");
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
                    });
                    post("/add", (request, response) -> {//...............................................................................................FAIRE AJOUT ELEMENT POAS LALISTE
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
                    });
                    //SUPPRESSION ELEMENT................................................................................................................................!!!!!!!!!!!
                    get("/sup", (request, response) -> {
                        int i = Integer.parseInt(replacePasInt(request.params(":name")));
                        AListe ee = model.getElement(i);
                        model.deleteElement(ee.getId());
                        response.redirect("/listes/all");
                        return "Liste supp: " + request.params(":name") + " inexistante.(en cours)";
                    });
                    delete("/:name", (request, response) -> {
                        //request.params(":name")
                        int i = Integer.parseInt(replacePasInt(request.params(":name")));
                        AListe ee = model.getElement(i);
                        model.deleteElement(ee.getId());
                        response.redirect("/listes/all");
                        return "Liste supp: " + request.params(":name") + " inexistante.";
                    });
                    //AFFICHAGE ELEMENT................................................................................................................................!!!!!!!!!!!inutile
                    get("/:name", (request, response) -> {
                        //request.params(":name")
                        /*list_e.setListe(model.getAllElement());
                        StringWriter writer = new StringWriter();
                        int i = -3;i = Integer.parseInt(request.params(":name"));//request.params(":name")
                        AListe ee;ee = model.getElement(i);
                        Map<String, List<AListe>> params = new HashMap<>();
                        List<AListe> le = new ArrayList<>();le.add(ee);// future Liste<AListe>
                        List<AListe> lee = new ArrayList<>();//future Liste element
                        lee.addAll(LaListe.rechercheFils(model,list_e.getListe(),ee.getId()));
                        params.put("liste_e", le);
                        params.put("liste_e_fils", lee);
                        try {
                            Template template = configuration.getTemplate("templates/listes.ftl");//render("accueil.ftl", model);
                            template.process(params, writer);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return writer;*/
                        return "Liste name: " + request.params(":name") + " inexistante.";
                    });
                });

            });

            model.getAllElement();
            final String[] vals3 = {""};
            vals3[0] += list_e;
            String finalVals3 = vals3[0];
            get("/all", (req, res) -> finalVals3);

            //...........................................................................................utile?
            get("/:name", (request, response) -> {
                return "Page: " + request.params(":name") + " inexistante.";
            });

        });
        get("/:name", (request, response) -> {
            //request.params(":name")
            return "Page: /" + request.params(":name") + " inexistante.";
        });

        //ERREUR ......................................................................!!
        // gerer l'err 404
        notFound((req, res) -> {
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
            return writerh+"<center>{\"Erreur\":\"404\" page introuvable}</center>"+writerf;
        });

        // gerer l'err  500
        internalServerError((req, res) -> {
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
            return writerh+"<center>{\"Erreur\":\"500 Problème(s) serveur\"}</center>"+writerf;
        });
    }

    /**
     * recherche d'element sans parent
     * @return
     */
    public List<AListe> rechercheMere(){
        List<AListe> liste = new ArrayList<>();
        for(AListe a: list_e.getListe()){
            List<AListe> l =LaListe.recherchePere(model,list_e.getListe(),a.getId());
            if(l.size() <= 0){
                liste.add(a);
            }
        }
        return liste;
    }


    public String replacePasInt(String s){
        return s.replaceAll(",","").replaceAll(" ","").replaceAll("%","");
    }

    //pas encore utile
    /**
     *
     * @param templateName
     * @param root
     * @return
     */
    public static Template render(String templateName, Map<String, Object> root) {//root = data model -> faire une classe data modele
        try{
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_19);
            // Specify the source where the template files come from. Here I set a
            // plain directory for it, but non-file-system sources are possible too:
            cfg.setDirectoryForTemplateLoading(new File("src/public"));

            // Set the preferred charset template files are stored in. UTF-8 is
            // a good choice in most applications:
            cfg.setDefaultEncoding("UTF-8");

            // Sets how errors will appear.
            // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            // Don't log exceptions inside FreeMarker that it will thrown at you anyway:
            cfg.setLogTemplateExceptions(false);

            // Wrap unchecked exceptions thrown during template processing into TemplateException-s.
            //cfg.setWrapUncheckedExceptions(true);

            Template temp = cfg.getTemplate(templateName);//"test.ftlh"
            Writer out = new OutputStreamWriter(System.out);
            if(root != null){//si il y a des données a ajouter
                temp.process(root, out);
            }else{
                //temp.dump(out);
            }
            return temp;
        }catch(Exception e){//IOException e,TemplateException te
            return null;
        }
    }

}