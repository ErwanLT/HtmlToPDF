package fr.eletutour.htmltopdf.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recipe {

    String country;
    String name;
    String img;
    String number;
    String preparation;
    String rest;
    String cooking;
    Ingredient[] ingredients;
    Step[] steps;
    String notes;

}
