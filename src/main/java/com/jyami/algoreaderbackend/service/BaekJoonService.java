package com.jyami.algoreaderbackend.service;

import com.jyami.algoreaderbackend.domain.BaekJoon;
import com.jyami.algoreaderbackend.domain.BaekJoonRepositroy;
import com.jyami.algoreaderbackend.dto.BaekJoonResDto;
import com.jyami.algoreaderbackend.exception.CrawlerException;
import com.jyami.algoreaderbackend.util.ConfortableJsoup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.jyami.algoreaderbackend.service.BaekJoonCategoryProblem.*;
import static com.jyami.algoreaderbackend.util.ConfortableJsoup.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BaekJoonService {

    private final BaekJoonRepositroy baekJoonRepositroy;

    public List<BaekJoonResDto> getUser(String userId) {
        Document document = getConnection("https://www.acmicpc.net/user/" + userId);
        Elements userSolvingProblems = document.select(".problem_number a");
        List<Long> collect = userSolvingProblems.stream()
                .map(ConfortableJsoup::getProblemNumFromElement)
                .collect(Collectors.toList());

        return baekJoonRepositroy.findByNumberIn(collect).stream()
                .map(BaekJoon::toDto)
                .collect(Collectors.toList());
    }

    public List<BaekJoonResDto> getProblemNumberAndTime(List<Element> elements) {

        List<Long> numbers = new ArrayList<>();
        List<BaekJoonResDto> baekJoonResDtos = new ArrayList<>();

        for (Element element : elements) {
            try {
                Long number = getNumber(element, numbers);
                String time = getTime(element, numbers, number);
                List<BaekJoon> byNumber = baekJoonRepositroy.findByNumber(number);
                for (BaekJoon b : byNumber) {
                    BaekJoonResDto baekJoonResDto = b.toDto();
                    baekJoonResDto.setTime(time);
                    baekJoonResDtos.add(baekJoonResDto);
                }
            } catch (CrawlerException e) {
                continue;
            }
        }
        return baekJoonResDtos;
    }

    private Long getNumber(Element e, List<Long> numbers) {
        Elements number = e.select(".problem_title");
        if (number.isEmpty()) {
            throw new CrawlerException("없는문제");
        }
        Long convertingNumber = Long.valueOf(number.get(0).text());
        if (numbers.contains(convertingNumber)) {
            throw new CrawlerException("이미 긁어온 문제");
        }
        return convertingNumber;
    }

    private String getTime(Element e, List<Long> numbers, Long convertingNumber) {
        String time = e.select(":nth-child(9) a").get(0).attr("title");
        String convertingTime = convertTime(time);
        numbers.add(convertingNumber);
        return convertingTime;
    }

    public List<BaekJoonResDto> getUserProblemListWithTime(String userId) {
        Document document = getConnection("https://www.acmicpc.net/status?user_id=" + userId + "&result_id=4");
        String nextPage = null;
        List<BaekJoonResDto> baekJoonResDtos = new ArrayList<>();
        while (true) {
            Elements problemRows = document.select("tr");
            List<Element> elements = problemRows.subList(1, problemRows.size());
            List<BaekJoonResDto> problemNumberAndTime = getProblemNumberAndTime(elements);
            baekJoonResDtos.addAll(problemNumberAndTime);
            nextPage = document.select("#next_page").attr("href");
            if (nextPage.isEmpty()) {
                break;
            }
            document = getConnection("https://www.acmicpc.net" + nextPage);
        }
        return baekJoonResDtos;
    }

    public List<BaekJoon> getAllNoneCategoryProblemInsertDB() {
        List<BaekJoon> allNoneCategoryProblem = getAllNoneCategoryProblem();
        return baekJoonRepositroy.saveAll(allNoneCategoryProblem);
    }

    public List<BaekJoon> getAllCategoryProblemInsertDB() {
        List<BaekJoon> allCategoryProblem = getAllCategoryProblem();
        return baekJoonRepositroy.saveAll(allCategoryProblem);
    }

    public List<BaekJoon> getOneCategoryProblemInsertDB(String category) {
        List<BaekJoon> oneCategoryProblem = getOneCategoryProblem(category);
        return baekJoonRepositroy.saveAll(oneCategoryProblem);
    }

    /**
     * 모든 카테고리 list 관련 method
     *
     * @return
     */

    public List<BaekJoon> getAllNoneCategoryProblem() {
        List<BaekJoon> baekJoons = new ArrayList<>();
        for (int i = 1; i <= 165; i++) {
            List<BaekJoon> onePageProblem = getOnePageProblem(i);
//            System.out.println(onePageProblem);
            baekJoons.addAll(onePageProblem);
//            baekJoons.addAll(getOnePageProblem(i));
        }
        return baekJoons;
    }

    private List<BaekJoon> getOnePageProblem(int pageNumber) {
        Document document = getConnection("https://www.acmicpc.net/problemset/" + pageNumber);
        Elements problemTypes = document.select("tr");
        List<Element> elements = problemTypes.subList(1, problemTypes.size());
        return elements.stream()
                .map(this::getOneProblem)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private BaekJoon getOneProblem(Element elementRow) {
        Elements select = elementRow.select("td");

        Long number = Long.valueOf(select.get(0).text());

        if (baekJoonRepositroy.findByNumber(number).size() == 0) {
            return BaekBuild(select, "None", number);
        } else {
            return null;
        }
    }

}
