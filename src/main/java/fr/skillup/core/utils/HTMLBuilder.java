package fr.skillup.core.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.List;

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

                String parentHtml = new String(HTMLBuilder.class.getResourceAsStream("/fr/skillup/views/" + parentPath).readAllBytes());
                Document parentDoc = Jsoup.parse(parentHtml);

                Element parentHead = parentDoc.head();
                Element childHead = mainDoc.head();
                for (Element element : parentHead.children()) {
                    childHead.appendChild(element.clone());
                }

                Element sectionPlaceholder = parentDoc.selectFirst("create-section[title=" + sectionId + "]");
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
            mainDoc.head().prependElement("base").attr("href", basePath.toString());

            System.out.println(mainDoc.html());

            return mainDoc.html();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la construction de la vue", e);
        }
    }


}
