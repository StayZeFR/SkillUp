package fr.skillup.core.tools;

import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLBuilder {

    public static String buildView(String resourcePath) {
        try {
            String path = "/fr/skillup/views/" + resourcePath;
            String mainHtml = new String(HTMLBuilder.class.getResourceAsStream(path).readAllBytes());
            Document mainDoc = Jsoup.parse(mainHtml);

            Elements extendElements = mainDoc.select("extend");
            for (Element extend : extendElements) {
                String parentPath = extend.attr("parent");
                String sectionId = extend.attr("id");

                // Charger le layout parent
                String parentHtml = new String(HTMLBuilder.class.getResourceAsStream("/fr/skillup/views/" + parentPath).readAllBytes());
                Document parentDoc = Jsoup.parse(parentHtml);

                // Insérer les sections dans le layout parent
                Element sectionPlaceholder = parentDoc.selectFirst("create-section[title=" + sectionId + "]");
                if (sectionPlaceholder != null) {
                    sectionPlaceholder.html(extend.html()); // Remplace le contenu du placeholder par celui de <extend>
                }

                // Fusionner le layout modifié dans le document principal
                extend.replaceWith(parentDoc.body());
            }

            Elements renderSections = mainDoc.select("render-section");
            for (Element renderSection : renderSections) {
                String sectionName = renderSection.attr("name");

                Element layoutSection = mainDoc.selectFirst("create-section[title=" + sectionName + "]");
                if (layoutSection != null) {
                    layoutSection.html(renderSection.html()); // Remplace le contenu du placeholder par celui de <render-section>
                }

                renderSection.remove();
            }

            mainDoc.select("extend").unwrap(); // Supprime les balises <extend> mais conserve leur contenu
            mainDoc.select("create-section").unwrap(); // Supprime les balises <create-section> mais conserve leur contenu

            return mainDoc.html();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la construction de la vue", e);
        }
    }


}
