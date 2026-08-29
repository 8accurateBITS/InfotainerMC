package de.infotainer;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.net.http.*;
import java.net.URI;
import java.util.*;
import java.util.concurrent.*;

public class NewsManager {
    private final List<String> sources = List.of(
        "http://feeds.bbci.co.uk/news/world/rss.xml",
        "https://www.reutersagency.com/feed/?best-topics=world&post_type=best",
        "http://rss.cnn.com/rss/edition.rss"
    );
    
    private final List<NewsItem> news = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private int index = 0;

    public record NewsItem(String title, String source) {}

    public void start() {
        executor.scheduleAtFixedRate(this::fetch, 0, 5, TimeUnit.MINUTES);
    }

    private void fetch() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            List<NewsItem> temp = new ArrayList<>();
            for(String url : sources) {
                try {
                    HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                        .header("User-Agent", "Infotainer-Mod/1.0").build();
                    String xml = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
                    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .parse(new java.io.ByteArrayInputStream(xml.getBytes()));
                    NodeList items = doc.getElementsByTagName("item");
                    for(int i=0; i<Math.min(3, items.getLength()); i++) {
                        Element el = (Element) items.item(i);
                        String title = el.getElementsByTagName("title").item(0).getTextContent();
                        temp.add(new NewsItem(title, new URI(url).getHost()));
                    }
                } catch(Exception ignored) {}
            }
            if(!temp.isEmpty()) {
                news.clear();
                Collections.shuffle(temp);
                news.addAll(temp);
            }
        } catch(Exception e) { e.printStackTrace(); }
    }

    public NewsItem getNext() {
        if(news.isEmpty()) return new NewsItem("Loading world news...", "infotainer");
        NewsItem item = news.get(index % news.size());
        index++;
        return item;
    }
    
    public List<NewsItem> getAll() { return news; }
}
