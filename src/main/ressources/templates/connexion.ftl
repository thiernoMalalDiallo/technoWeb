<#include "header.ftl"/>
<article>
    <form class="connexion" method="post">
    <h3>Connexion</h3>
    <p>
        <label for="email">E-Mail</label>
        <input type="email" name="email" id="email" size="20" > <br/>
    </p>
    <p>
        <label for="mdp">Mot de passe</label>
        <input type="password" name="mdp" id="mdp" size="20" >
    </p>
    <p>
        <div class="boutonall">
            <button  class="bouton" title="Valider" value="connexion">Valider</button>
        </div>
    </p>
    </form>
</article>
    <script>
        function co() {
            location.replace("/listes/all")
        }
    </script>
<#include "footer.ftl"/>
