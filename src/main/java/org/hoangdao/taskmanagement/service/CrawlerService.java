package org.hoangdao.taskmanagement.service;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.hoangdao.taskmanagement.entity.Magazine;
import org.hoangdao.taskmanagement.entity.MagazineContent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CrawlerService {

    private static final String TARGET_DOMAIN = "https://tapchitoaan.vn";
    @Autowired
    private MagazineService magazineService;
    private OkHttpClient client;

    public CrawlerService() {
        this.client = new OkHttpClient();
    }

    public void crawMagazine() throws IOException {
        Request request = new Request.Builder()
                .url(TARGET_DOMAIN)
                .build();
        Response response = client.newCall(request).execute();
        String homePage = response.body().string();

        Document homePageDocument = Jsoup.parse(homePage);
        Elements categoryElements = homePageDocument.select(".col-md-12 .menu-top nav ul li");
        List<String> categoryPageUrl = new ArrayList<>();
        for (Element element : categoryElements) {
            categoryPageUrl.add(element.selectFirst("a").attr("href"));
        }

        for (int i = 1; i < categoryPageUrl.size(); ++i) {
            crawMagazineOfCategory(categoryPageUrl.get(i), "");
        }
    }

    public void crawMagazineOfCategory(String url, String category) throws IOException {
        System.out.println(url);
        List<String> pageUrls = new ArrayList<>();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        String pageContent = response.body().string();
        Document document = Jsoup.parse(pageContent);
        Elements paginationElements = document.select(
                ".pagination li.page-item"
        );
        int firstPage = Integer.parseInt(paginationElements.get(1).text());
        int lastPage = Integer.parseInt(paginationElements.get(paginationElements.size() - 2).text());
        for (int i = firstPage; i <= lastPage; ++i) {
            String realUrl = url + "?page=" + i;
            pageUrls.add(realUrl.replace("/chuyen-muc", ""));
        }

        Set<String> magazineUrls = new HashSet<>();
        for (int i = 0; i < pageUrls.size(); ++i) {
            System.out.println(pageUrls.get(i));
            magazineUrls.addAll(crawAPage(pageUrls.get(i)));
        }

        for (String magazineUrl : magazineUrls) {
            try {
                crawMagazine(magazineUrl, category, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Set<String> crawAPage(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        String pageContent = response.body().string();
        Document document = Jsoup.parse(pageContent);

        Set<String> magazineUrls = new HashSet<>();
        Elements magazines = document.select(".related-posts.related-posts-cat ul li");
        for (Element element : magazines) {
            magazineUrls.add(
                    element.selectFirst("div.col-md-12 div a")
                            .attr("href")
            );
        }
        return magazineUrls;
    }

    public Magazine crawMagazine(String url, String category, String subCategory) throws IOException {
        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        String pageContent = response.body().string();
        Document document = Jsoup.parse(pageContent);

        Magazine magazine = new Magazine();

        try {
            Element thumbnailElement = document.selectFirst(
                    ".col-md-8.block-single .img-with-no-margin img"
            );
            magazine.setThumbnail(thumbnailElement.attr("src"));
        } catch (NullPointerException nullPointerException) {

        }

        Element titleElement = document.selectFirst(
                ".col-md-8.block-single h3"
        );
        magazine.setTitle(titleElement.text());

        Element timeElement = document.selectFirst(
                ".col-md-8.block-single .clear .bs-comments");
        Element messageElement = timeElement.selectFirst("a");
        magazine.setCreatedAt(
                parseMagazineTime(
                        timeElement.text()
                                .substring(
                                        0,
                                        timeElement.text().length() - messageElement.text().length())
                )
        );
        magazine.setUpdatedAt(magazine.getCreatedAt());

        Elements contentElements = document.select(
                ".col-md-8.block-single .row p"
        );
        List<MagazineContent.AbstractContent> contents = new ArrayList<>();
        for (Element element : contentElements) {
            //  Is image
            if (element.selectFirst("img") != null) {
                MagazineContent.ImageContent imageContent = new MagazineContent.ImageContent();
                imageContent.setUrl(
                        TARGET_DOMAIN + element.selectFirst("img").attr("src")
                );

                if (magazine.getThumbnail() == null) {
                    magazine.setThumbnail(imageContent.getUrl());
                }

                try {
                    Element imageTitleElement = element.nextElementSibling();
                    if (imageTitleElement.selectFirst("em") != null) {
                        imageContent.setTitle(imageTitleElement.text());
                    }
                } catch (NullPointerException e) {

                }

                contents.add(imageContent);
            }

            //  Is normal text
            else if (element.selectFirst("img") == null) {
                MagazineContent.TextContent textContent = new MagazineContent.TextContent();
                textContent.setAsHtml(element.html());
                textContent.setRawText(element.text());
                contents.add(textContent);
            }
        }

        magazine.setContents(contents);
        return magazineService.createMagazine(magazine);
    }

    public LocalDateTime parseMagazineTime(String time) {
        //  Example: 17 tháng 07 năm 2023 10:25 GMT+7
        String[] items = time.split(" ");
        return LocalDateTime.of(
                Integer.parseInt(items[4]),
                Integer.parseInt(items[2]),
                Integer.parseInt(items[0]),
                Integer.parseInt(items[5].split(":")[0]),
                Integer.parseInt(items[5].split(":")[1]),
                0
        );
    }


}
