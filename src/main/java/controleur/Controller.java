package controleur;

import DAO.DAO;
import autre.log4jConf;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import model.TodoList;
import org.apache.log4j.BasicConfigurator;
import service.ElementService;
import service.UserService;
import service.UtilService;

import java.io.File;

import static spark.Spark.*;

/**
 *
 */
public class Controller {
    private Configuration configuration = new Configuration(Configuration.VERSION_2_3_19);
    private TodoList list_e;
    private DAO model;
    private UtilService utilService = new UtilService();
    private UserService userService = new UserService();
    private ElementService elementService = new ElementService();

    public Controller(DAO modelsql, TodoList liste) {
        this.list_e = liste;
        this.model = modelsql;
    }

    /**
     * @param args
     * @throws Exception
     */
    public void routes(String[] args) throws Exception {

        BasicConfigurator.configure();
        log4jConf.log.info("This is Logger Info");

        port(8080);
        int maxThreads = 10;
        int minThreads = 1;
        int timeOutMillis = 30000;
        threadPool(maxThreads, minThreads, timeOutMillis);

        staticFiles.expireTime(600);

        configuration = new Configuration(Configuration.VERSION_2_3_19);
        configuration.setDirectoryForTemplateLoading(new File("src/main/ressources"));
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);

        externalStaticFileLocation("src/main/ressources");

        get("/", (request, response) -> {
            response.redirect("/connexion");
            return "!!";
        });

        path("/", () -> {
            /*--------------------------------------------------------------------------------------------------------------------------------------------*/
            get("/accueil", (request, response) -> {
                return this.utilService.getAcceuil(this.configuration);
            });
            /*--------------------------------------------------------------------------------------------------------------------------------------------*/
            get("/connexion", (request, response) -> {
                return this.userService.getConnexion(configuration);
            });
            post("/connexion", (request, response) -> {

                return this.userService.Connect(configuration, request, response);
            });
            /*LES LISTES--------------------------------------------------------------------------------------------------------------------------------------------*/
            path("/listes", () -> {
                get("", (request, response) -> {

                    return this.elementService.getList(configuration);
                });
                //RECHERCHE................................................................................................................................!!!!!!!!!!!
                get("/recherche", (request, response) -> {
                    return this.elementService.rechercheList(configuration, request, model);
                });
                //ADD................................................................................................................................
                get("/add", (request, response) -> {
                    return this.elementService.getAddList(configuration, model);
                });
                post("/add", (request, response) -> {
                    return this.elementService.addList(model, request, response, list_e);
                });
                //TOUT AFFICHER................................................................................................................................
                get("/all", (request, response) -> {
                    return this.elementService.getALl(configuration, model, list_e);
                });

                //SUPPRIMER LISTE................................................................................................................................
                delete("/:name", (request, response) -> {
                    return this.elementService.deleteList(response, request, model);
                });
                //................................................................................................................................
                path("/:name", () -> {
                    //AFFICHAGE LISTE................................................................................................................................!!!!!!!!!!! ELEMENTS
                    get("", (request, response) -> {
                        return this.elementService.getListUser(configuration, request, model);
                    });
                    //MODIFICATION................................................................................................................................
                    path("/modif", () -> {
                        get("", (request, response) -> {
                            return this.elementService.updateListUser(configuration, request, model);
                        });
                        post("", (request, response) -> {
                            return this.elementService.updateEelemntList(request, model, response);
                        });
                    });

                    //AJOUT ELEMENT................................................................................................................................
                    get("/add", (request, response) -> {
                        return this.elementService.getEleementList(configuration, request, model);
                    });
                    post("/add", (request, response) -> {//...............................................................................................FAIRE AJOUT ELEMENT POAS LALISTE
                        return this.elementService.addElementList(request, response, model);
                    });
                    //SUPPRESSION ELEMENT................................................................................................................................!!!!!!!!!!!
                    get("/sup", (request, response) -> {
                        return this.elementService.getdeleteElementList(request, model, response);
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

            DAO.getAllElement();
            final String[] vals3 = {""};
            vals3[0] += list_e;
            String finalVals3 = vals3[0];
            get("/all", (req, res) -> finalVals3);


            get("/:name", (request, response) -> {
                return "Page: " + request.params(":name") + " inexistante.";
            });

        });
        get("/:name", (request, response) -> {
            //request.params(":name")
            return "Page: /" + request.params(":name") + " inexistante.";
        });

        // gerer l'err 404
        notFound((req, res) -> {
            return this.utilService.get404(configuration);
        });

        internalServerError((req, res) -> {
            return this.utilService.get500(configuration);
        });
    }


}