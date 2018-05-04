package me.floydz69.CraftX1Plus.util.enums;

/**
 * Created by gusta on 04/05/2017.
 */
public enum ArenaType {

    PLAYER("PLAYER"),
    CLAN("CLAN"),
    AMBOS("AMBOS");

    String type;
    private ArenaType(String type){
        this.type = type;
    }

    public String getStatus(){
        return this.type;
    }

    public static ArenaType fromName(String name){
        for(ArenaType arenaType : ArenaType.values()){
            if(arenaType.toString().equals(name)){
                return arenaType;
            }
        }
        return null;
    }
}
