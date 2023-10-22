package ru.aston.learn.cat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import ru.aston.learn.cat.entity.cat.Cat;
import ru.aston.learn.cat.service.CatService;
import ru.aston.learn.cat.service.ObjectMapperService;

@WebServlet(urlPatterns = "/cat/*")
@Slf4j
public class CatServlet extends HttpServlet {

    private final CatService catService = CatService.getInstance();
    private final ObjectMapper objectMapper = ObjectMapperService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null) {
            getCatImagesPage(req, resp);
        } else {
            notFound(resp);
        }
    }

    private void getCatImagesPage(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Integer page = Optional.ofNullable(req.getParameter("page"))
                    .map(Integer::valueOf)
                    .orElseThrow();

            Integer pageSize = Optional.ofNullable(req.getParameter("pageSize"))
                    .map(Integer::valueOf)
                    .orElseThrow();

            List<Cat> images = catService.getCatImages(page, pageSize);

            String body = objectMapper.writeValueAsString(images);

            resp.setStatus(HttpStatus.SC_OK);
            resp.getWriter().print(body);
        } catch (Exception e) {
            log.error("Can't get cat images page", e);
            resp.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null) {
            notFound(resp);
        } else {
            postMapping(pathInfo, req, resp);
        }
    }

    private void postMapping(String path,
                             HttpServletRequest req,
                             HttpServletResponse resp) {

        if ("/enrich".equals(path)) {
            enrichCatData(req, resp);
        } else {
            notFound(resp);
        }
    }

    private void enrichCatData(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Integer cnt = Optional.ofNullable(req.getParameter("cnt"))
                    .map(Integer::valueOf)
                    .orElse(1);

            catService.enrichCatData(cnt);

            resp.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            resp.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void notFound(HttpServletResponse resp) {
        resp.setStatus(HttpStatus.SC_NOT_FOUND);
    }
}
