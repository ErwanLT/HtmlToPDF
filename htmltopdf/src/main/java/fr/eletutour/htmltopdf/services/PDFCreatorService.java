package fr.eletutour.htmltopdf.services;

import com.google.gson.Gson;
import com.itextpdf.html2pdf.HtmlConverter;
import fr.eletutour.htmltopdf.models.Ingredient;
import fr.eletutour.htmltopdf.models.Recipe;
import fr.eletutour.htmltopdf.models.Step;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class PDFCreatorService {

    public File genererPDF() {

        StringBuilder html = new StringBuilder();

        Recipe recipe = getRecipeFromResource();
        File f = new File(recipe.getName()+".pdf");

        style(html);
        title(html, recipe);
        image(html, recipe);
        body(html, recipe);

        try {
            HtmlConverter.convertToPdf(html.toString(), new FileOutputStream(f));
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;

    }

    private Recipe getRecipeFromResource() {
        String content = "";
        try {
            File file = ResourceUtils.getFile("classpath:static/recipe.json");
            content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        return gson.fromJson(content, Recipe.class);
    }

    private void style(StringBuilder html) {
        html.append("<style>");

        String content = "";
        try {
            File file = ResourceUtils.getFile("classpath:static/bootstrap.min.css");
            content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        html.append(content);

        html.append("</style>");
    }

    private void title(StringBuilder html, Recipe recipe) {
        html.append("<H1 class=\"card-header text-center\">")
                .append(recipe.getName()).append("</H1>")
                .append("<br>");
    }

    private void image(StringBuilder html, Recipe recipe) {
        html.append("<img src='htmltopdf/src/main/resources/static/").append(recipe.getImg()).append("' ")
                .append("style=\"width: 30%; height: 30%; display: block; margin-left: auto; margin-right: auto\" />");
    }

    private void body(StringBuilder html, Recipe recipe) {
        html
                .append("<div><span style=\"font-weight: bold;\">Pour: </span>").append(recipe.getNumber()).append("</div>")
                .append("<div><span style=\"font-weight: bold;\">Temps de préparation: </span>").append(recipe.getPreparation()).append("</div>")
                .append("<div><span style=\"font-weight: bold;\">Temps de repos: </span>").append(recipe.getRest()).append("</div>")
                .append("<div><span style=\"font-weight: bold;\">Temps de cuisson: </span>").append(recipe.getCooking()).append("</div>")
                .append("<br>");

        // ingredients
        html.append("<div class=\"card\">")
                .append("<div class=\"card-header\">")
                .append("Ingredients")
                .append("</div>")
                .append("<div class=\"card-body\">")
                .append("<ul>");
        for (Ingredient i:recipe.getIngredients()) {
            html.append("<li>")
                    .append(i.getQuantities() + " " + i.getLabel())
                    .append("</li>");
        }
        html.append("</ul>")
                .append("</div>")
                .append("</div>")
                .append("<br>");

        // étapes
        html.append("<div class=\"card\">")
                .append("<div class=\"card-header\">")
                .append("Etapes")
                .append("</div>")
                .append("<div class=\"card-body\">")
                .append("<ol>");
        for (Step s:recipe.getSteps()) {
            html.append("<li>")
                    .append(s.getLabel())
                    .append("</li>");
        }
        html.append("</ol>")
                .append("</div>")
                .append("</div>")
                .append("<br>");

        // notes
        if(recipe.getNotes() != null && !recipe.getNotes().isBlank()){
            html.append("<span style=\"font-weight: bold;\">")
                    .append(recipe.getNotes())
                    .append("</span></div>");
        }
    }
}
