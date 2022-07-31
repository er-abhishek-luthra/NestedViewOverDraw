package com.example.nestedviewoverdraw.overdraw_custom_view;

/**
 * Class for Droid objects. Each Droid has a name and is associated with a color and
 * (optionally) an image.  Droid images generated via http://androidify.com.
 */
public class CardModel {
    /**
     * The Droid's name.
     */
    private String name;

    /**
     * The color associated with a droid.
     */
    private int color;

    /**
     *  The id of a drawable associated with a Droid.
     */
    private int imageId;


    public CardModel(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public CardModel(String name, int color, int imageId) {
        this.name = name;
        this.color = color;
        this.imageId = imageId;
    }

    public String getName() {return name;}
    public int getColor() {return color;}
    public int getAvatarId() {return imageId;}
}