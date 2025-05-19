package fr.skillup.core.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class HTMLBuilder {

    private HTMLBuilder() {
        throw new IllegalStateException("Class utilitaire");
    }

    /**
     * Construit une vue HTML à partir d'un fichier de ressource
     *
     * @param resourcePath : Chemin du fichier de ressource
     * @return String : Vue HTML
     */
    public static String buildView(String resourcePath, Map<String, Object> params) {
        try {
            String path = "/fr/skillup/views/" + resourcePath;
            String mainHtml = new String(HTMLBuilder.class.getResourceAsStream(path).readAllBytes());
            Document mainDoc = Jsoup.parse(mainHtml);

            Elements extendElements = mainDoc.select("extend");
            for (Element extend : extendElements) {
                String parentPath = extend.attr("parent");

                String randomId = UUID.randomUUID().toString();
                extend.attr("id", randomId);

                String parentHtml = new String(HTMLBuilder.class.getResourceAsStream("/fr/skillup/views/" + parentPath).readAllBytes());
                Document parentDoc = Jsoup.parse(parentHtml);

                Element parentHead = parentDoc.head();
                Element childHead = mainDoc.head();
                for (Element element : parentHead.children()) {
                    childHead.appendChild(element.clone());
                }

                Element sectionPlaceholder = parentDoc.selectFirst("create-section[title=" + randomId + "]");
                if (sectionPlaceholder != null) {
                    sectionPlaceholder.html(extend.html());
                }

                Element parentBody = parentDoc.body();
                List<Node> parentBodyNodes = Jsoup.parseBodyFragment(parentBody.html()).body().childNodes();

                for (Node node : parentBodyNodes) {
                    extend.before(node);
                }
                extend.remove();
            }

            Elements renderSections = mainDoc.select("render-section");
            for (Element renderSection : renderSections) {
                String sectionName = renderSection.attr("name");

                Element layoutSection = mainDoc.selectFirst("create-section[title=" + sectionName + "]");
                if (layoutSection != null) {
                    layoutSection.html(renderSection.html());
                }

                renderSection.remove();
            }

            mainDoc.select("extend").unwrap();
            mainDoc.select("create-section").unwrap();

            URL basePath = HTMLBuilder.class.getResource("/fr/skillup/");
            assert basePath != null;
            mainDoc.head().prependElement("base").attr("href", basePath.toString());

            Element titleElement = mainDoc.head().selectFirst("title");
            if (titleElement != null) {
                titleElement.after("<script src=\"assets/js/core/app.js\"></script>");
                titleElement.after("<script src=\"assets/js/core/bridge.js\"></script>");
            }

            return HTMLBuilder.insertParams(mainDoc.html(), params);
        } catch (Exception e) {
            Logger.getLogger(HTMLBuilder.class.getName()).severe(e.getMessage());
        }
        return null;
    }

    /**
     * Insert les paramètres dans le HTML
     *
     * @param html   : HTML à modifier
     * @param params : Paramètres à insérer
     * @return String : HTML modifié
     */
    private static String insertParams(String html, Map<String, Object> params) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            html = html.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
        }
        return html;
    }

}
