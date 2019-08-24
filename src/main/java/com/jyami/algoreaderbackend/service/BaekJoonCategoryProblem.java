package com.jyami.algoreaderbackend.service;

import com.jyami.algoreaderbackend.domain.BaekJoon;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.jyami.algoreaderbackend.util.ConfortableJsoup.getConnection;
import static com.jyami.algoreaderbackend.util.ConfortableJsoup.getConnectionWith404;

@Slf4j
@Component
public class BaekJoonCategoryProblem {

    public static List<BaekJoon> getAllCategoryProblem() {
        Document document = getConnection("https://www.acmicpc.net/problem/tags");
        Elements problemTypes = document.select("tr");
        List<Element> elements = problemTypes.subList(1, problemTypes.size());
        List<BaekJoon> baekJoonsResultList = new ArrayList<>();
        for (Element element : elements) {
            String categoryName = element.select("td").get(0).text();
            log.info(categoryName);
            baekJoonsResultList.addAll(getOneCategoryProblem(categoryName));
        }
        return baekJoonsResultList;
    }

    private static Integer getCategoryLength(String category) {
        Document document = getConnection("https://www.acmicpc.net/problem/tag/" + category);
        Elements select = document.select(".pagination li");
        return select.size();
    }

    public static List<BaekJoon> getOneCategoryProblem(String category) {
        Integer categoryLength = getCategoryLength(category);

        List<BaekJoon> baekJoons = onePageCrawling(category, 1);

        for (int i = 2; i <= categoryLength; i++) {
            List<BaekJoon> baekJoonsSub = onePageCrawling(category, i);
            baekJoons.addAll(baekJoonsSub);
        }

        return baekJoons;
    }

    private static List<BaekJoon> onePageCrawling(String category, int page) {
        Document document = getConnectionWith404("https://www.acmicpc.net/problem/tag/" + category + "/" + page);
        if (document != null) {
            Elements rows = document.select("tr");
            List<Element> editRows = rows.subList(1, rows.size());
            return editRows.stream()
                    .map(element -> makeOneBaekJoon(element, category))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private static BaekJoon makeOneBaekJoon(Element elementRow, String categoryName) {
        Elements select = elementRow.select("td");

        Long number = Long.valueOf(select.get(0).text());

        return BaekBuild(select, categoryName, number);
    }

    protected static BaekJoon BaekBuild(Elements element, String categoryName, Long number) {
        String name = element.get(1).text();
        String category = categoryName;
        Float collectRate = Float.valueOf(element.get(5).text().replace("%", ""));
        Long collectCount = Long.valueOf(element.get(3).text());
        Long submitCount = Long.valueOf(element.get(4).text());

        return BaekJoon.builder()
                .number(number)
                .name(name)
                .category(category)
                .collectCount(collectCount)
                .collectRate(collectRate)
                .submitCount(submitCount)
                .build();
    }

}
