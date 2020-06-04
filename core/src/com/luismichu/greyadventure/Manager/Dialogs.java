package com.luismichu.greyadventure.Manager;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.utils.Array;

import java.util.Dictionary;
import java.util.HashMap;

public class Dialogs {
    private static Dialogs dialogs;
    private static HashMap<String, Array<String>> dictionary;
    private final Array<String> l1d1;
    private final Array<String> l1d2;
    private final Array<String> l1d3;
    private final Array<String> l1d4;
    private final Array<String> l1d5;
    private final Array<String> l1d6;

    private Dialogs(){
        l1d1 = new Array<>();
        l1d2 = new Array<>();
        l1d3 = new Array<>();
        l1d4 = new Array<>();
        l1d5 = new Array<>();
        l1d6 = new Array<>();
        populateArrays();
        dictionary = new HashMap<>();
        dictionary.put("l1d1", l1d1);
        dictionary.put("l1d2", l1d2);
        dictionary.put("l1d3", l1d3);
        dictionary.put("l1d4", l1d4);
        dictionary.put("l1d5", l1d5);
        dictionary.put("l1d6", l1d6);
    }

    public static Array<String> get(String name){
        if(dialogs == null)
            dialogs = new Dialogs();

        Array<String> dialog = null;
        if(dictionary.containsKey(name)) {
            dialog = dictionary.get(name);
            dictionary.remove(name);
        }
        return dialog;
    }

    private void populateArrays(){
        l1d1.addAll("Hola, viajero. Tu primera vez por estas tierras? No te preocupes, te voy a recordar como moverte.",
                "Para desplazarte a izquierda o derecha pulsa las teclas A y D, o las flechas de direccion.",
                "Si quieres saltar solo tienes que apretar Espacio. Prueba a moverte hasta ese barranco a la derecha.");

        l1d2.addAll("Ahora es el momento de darlo todo y hacer un salto de fe. Intenta llegar al otro lado cogiendo impulso.");
        l1d3.addAll("Ups! Se me olvido decirte que con saltar no es suficiente jeje. Ha llegado la hora de que aprendas a",
                "moverte mas rapido. Pulsa Shift y una direccion para impulsarte rapidamente. Recuerda que puedes",
                "ir hacia arriba o combinar dos direcciones. Ah! Y por ultimo, no puedes usarlo todo el rato.",
                "Tendras que esperar a que dejes de ser azul.");
        l1d4.addAll("Prueba saltando y despues usando Shift para llegar al otro lado. Y echale mas ganas que la ultima vez");
        l1d5.addAll("Cuidado con esos pinchos mas adelante. No me gustaria verte convertido en un pincho moruno.");
        l1d6.addAll("Esta vez te vas a tener que fiar de mi. Solo dejate caer. Mas adelante veras enemigos, intenta",
                "saltar sobre ellos y esquivarlos, o perderas vida");
    }
}
