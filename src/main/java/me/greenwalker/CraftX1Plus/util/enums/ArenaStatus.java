package me.floydz69.CraftX1Plus.util.enums;


/**
 * Created by gusta on 02/05/2017.
 */
public enum ArenaStatus {

    ABERTO("Aberta"),
    PENDENTE("Pendente"),
    ENTRANDO("Entrando"),
    EM_JOGO("Em Jogo"),
    DESATIVADA("Fechada");

    String type;
    private ArenaStatus(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }

    public static ArenaStatus fromName(String name){
        for(ArenaStatus arenaStatus : ArenaStatus.values()){
            if(arenaStatus.toString().equalsIgnoreCase(name)){
                return arenaStatus;
            }
        }
        return null;
    }
}
