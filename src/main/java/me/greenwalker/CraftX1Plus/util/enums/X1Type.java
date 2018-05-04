package me.floydz69.CraftX1Plus.util.enums;

/**
 * Created by gusta on 08/05/2017.
 */
public enum X1Type {

    NORMAL("Normal"),
    HARD("Hard"),
    INSANO("Insano");

    String type;

    private X1Type(String type) {
        this.type = type;
    }

    public String getStatus() {
        return this.type;
    }

    public static X1Type fromName(String name) {
        for (X1Type x1Type : X1Type.values()) {
            if (x1Type.toString().equalsIgnoreCase(name)) {
                return x1Type;
            }
        }
        return null;
    }

}
