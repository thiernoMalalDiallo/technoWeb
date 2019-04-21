package controleur;

import freemarker.template.*;
import model.TodoList;
import DAO.DAO;
import org.apache.log4j.BasicConfigurator;
import service.ElementService;
import service.UserService;
import service.UtilService;

import java.io.File;

import static spark.Spark.*;
import static spark.Spark.internalServerError;

/**
 *
 */
public class Controller {
    //
    Configuration configuration = new Configuration(Configuration.VERSION_2_3_19);
    //
    TodoList list_e;
    //
    DAO model;

    UtilService utilService;

    UserService userService ;

    ElementService elementService;

    public Controller(DAO modelsql, TodoList liste) {
        this.list_e = liste;
        this.model = modelsql;
    }


    public void router(String[] args) throws Exception {
        BasicConfigurator.configure();
        port(8080);
        int maxThreads = 10;
        int minThreads = 1;
        int timeOutMillis = 30000;
        threadPool(maxThreads, minThreads, timeOutMillis);
        staticFiles.expireTime(600); // ten minutes
        configuration = new Configuration(Configuration.VERSION_2_3_19);
        configuration.setDirectoryForTemplateLoading(new File("src/router/ressources"));
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        externalStaticFileLocation("src/router/ressources");

        this.utilService = new UtilService();
        this.userService=new UserService();
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
                return this.utilService.getInfo(configuration);
                //return "info";
            });
            /*--------------------------------------------------------------------------------------------------------------------------------------------*/
            get("/connexion", (request, response) -> {
                return this.userService.getConnexion(configuration);
            });
            post("/connexion", (request, response) -> {
               return this.userService.Connect(configuration,request,response);
            });

            post("/inscription", (request, response) -> {//reunir dans connexion ?
                return this.userService.inscription(configuration,request,response);
            });
            /*LES LISTES--------------------------------------------------------------------------------------------------------------------------------------------*/
            path("/listes", () -> {
                get("", (request, response) -> {
                    return this.elementService.getList(configuration);
                });
                //RECHERCHE................................................................................................................................!!!!!!!!!!!
                get("/recherche", (request, response) -> {

                    return this.elementService.rechercheList(configuration,request,model);
                    //return "Liste Recherche: " + request.params(":name") + " inexistante.(en cours)";
                });
                //ADD................................................................................................................................
                get("/add", (request, response) -> {
                   return this.elementService.getAddList(configuration,model);
                });
                post("/add", (request, response) -> {
                    return this.elementService.addList(model,request,response, list_e);
                });
                //TOUT AFFICHER................................................................................................................................
                get("/all", (request, response) -> {
                    return this.elementService.getALl(configuration, model);
                });

                //SUPPRIMER LISTE................................................................................................................................
                delete("/:name", (request, response) -> {
                    return this.elementService.deleteList(response,request,model);
                });
                //................................................................................................................................
                path("/:name", () -> {
                    //AFFICHAGE LISTE................................................................................................................................!!!!!!!!!!! ELEMENTS
                    get("", (request, response) -> {

                        return this.elementService.getListUser(configuration,request,model);
                    });
                    //MODIFICATION................................................................................................................................
                    path("/modif", () -> {
                        get("", (request, response) -> {
                            //response.type("text/html");

                            return this.elementService.updateListUser(configuration,request,model);
                        });
                        post("", (request, response) -> {
                            return  this.elementService.updateEelemntList(request,model,response);
                        });
                    });

                    //AJOUT ELEMENT................................................................................................................................
                    get("/add", (request, response) -> {
                        //response.type("text/html");
                        return this.elementService.getEleementList(configuration,request,model);
                    });
                    post("/add", (request, response) -> {//...............................................................................................FAIRE AJOUT ELEMENT POAS LALISTE
                       return  this.elementService.addElementList(request,response,model);
                    });
                    //SUPPRESSION ELEMENT................................................................................................................................!!!!!!!!!!!
                    get("/sup", (request, response) -> {
                       return  this.elementService.getdeleteElementList( request, model, response);
                    });
                    delete("/:name", (request, response) -> {
                        return this.elementService.deleteElementList(request, model, response);
                    });
                    //AFFICHAGE ELEMENT................................................................................................................................!!!!!!!!!!!inutile
                    get("/:name", (request, response) -> {
                        return "Liste name: " + request.params(":name") + " inexistante.";
                    });
                });

            });

            get("/:name", (request, response) -> {
                return "Page: " + request.params(":name") + " inexistante.";
            });

        });
        get("/:name", (request, response) -> {
            return "Page: /" + request.params(":name") + " inexistante.";
        });

        //ERREUR ......................................................................!!
        // gerer l'err 404
        notFound((req, res) -> {
            return this.utilService.get404(configuration);
        });

        // gerer l'err  500
        internalServerError((req, res) -> {
            return this.utilService.get500(configuration);
        });
    }

    public String replacePasInt(String s){
        return s.replaceAll(",","").replaceAll(" ","").replaceAll("%","");
    }
}