<#include "header.ftl"/>
<link  href="/../test/tagsinput.css" rel="stylesheet">
<div class="liste">
    <br>
    <br>
    <#if liste_e??>
        <#if liste_e_pere??>
            <label >Nouvel element : </label>
        <#else>
            <label >Modifier Liste/Element :</label>
        </#if>
    <#else>
        <#if liste_e_pere??>
            <label >Nouvel element : </label>
        <#else>
            <label >Nouvelle Liste : </label>
        </#if>
    </#if>
    <br>
    <br>
    <form id="liste" method="post">
        <#if liste_e?? && !liste_e_pere??>

            <table class="table_addliste" >
            <tr >
            <td> <label for="titre">Titre :</label></td>
            <td><input id="titre" name="titre" required=true value="${liste_e[0].titre}"/></input></td><br>
            </tr>
            <td><label for="description">Description :</label></td>
            <td><textarea id="description" name="description" required=true rows="10" cols="30">${liste_e[0].description}</textarea></td>
            <td><br></td>
            <#if liste_tag??>
                <tr >
                    <td>
                        Tags :
                    </td>
                    <td>
                    <pre>
                        <code data-language="html"><input id="tags" name="tags" value="<#list liste_tag as e>${e}</#list>" data-role="tagsinput" >
                        </code>
                    </pre>
                    </td>
                </tr>
            </#if>
            </table>
        <#else >
            <#if liste_e_pere??>
                <input style="display:none;" id="idd" name="idd" value="${liste_e_pere[0].id}"/></input>
            </#if>
            <table class="table_addliste" >
                    <tr >
                        <td> <label for="titre">Titre :</label></td>
                        <td><input id="titre" name="titre" required=true /></input></td><br>
                    </tr>
                    <td><label for="description">Description :</label></td>
                    <td><textarea id="description" name="description" required=true name="message" rows="10" cols="30"></textarea></td>
                    <td><br></td>
                <tr >
                <#if liste_tag??>
                <tr >
                    <td>
                        Tags :
                    </td>
                    <td>
                            <pre>
                                <code data-language="html"><input id="tags"  name="tags" value="<#list liste_tag as e>${e}</#list>" data-role="tagsinput" >
                            </pre>
                    </td>
                </tr>
                <tr>
                    <td>
                        Etat :
                    </td>
                    <td>
                        <input type="radio" name="afaire" value="1"/> A Faire<br />
                        <input type="radio" name="fait" value="2"/> Fait<br />
                    </td>
                </tr>
                </#if>
            </table>
        </#if>
            <br>
            <br>
            <br>
            <br>
        <#if liste_e?? && !liste_e_pere??>
            <input type="submit" value="Modifier"/>
        <#else>
            <input type="submit" value="Ajouter"/>
        </#if>
    </form>
</div>
<br>
<br>
<br>
<footer class="footer">
        <p>Code licensed under <a href="https://raw.github.com/TimSchlechter/bootstrap-tagsinput/master/LICENSE" target="_blank">MIT License</a></p>
    </footer>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="/../test/tagsinput.min.js"></script>
</body>
</html>

