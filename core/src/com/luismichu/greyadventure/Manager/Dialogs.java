package com.luismichu.greyadventure.Manager;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class Dialogs {
    private static Dialogs dialogs;
    private static HashMap<String, Array<String>> dictionary;

    private final Array<String> l0d1;

    private final Array<String> l1d1;
    private final Array<String> l1d2;
    private final Array<String> l1d3;
    private final Array<String> l1d4;
    private final Array<String> l1d5;
    private final Array<String> l1d6;

    private final Array<String> l2d1;

    private final Array<String> l3d1;
    private final Array<String> l3d2;

    private Dialogs(){
        l0d1 = new Array<>();

        l1d1 = new Array<>();
        l1d2 = new Array<>();
        l1d3 = new Array<>();
        l1d4 = new Array<>();
        l1d5 = new Array<>();
        l1d6 = new Array<>();

        l2d1 = new Array<>();

        l3d1 = new Array<>();
        l3d2 = new Array<>();

        populateArrays();

        dictionary = new HashMap<>();
        dictionary.put("l0d1", l0d1);

        dictionary.put("l1d1", l1d1);
        dictionary.put("l1d2", l1d2);
        dictionary.put("l1d3", l1d3);
        dictionary.put("l1d4", l1d4);
        dictionary.put("l1d5", l1d5);
        dictionary.put("l1d6", l1d6);

        dictionary.put("l2d1", l2d1);

        dictionary.put("l3d1", l3d1);
        dictionary.put("l3d2", l3d2);
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
        l0d1.addAll("Esta es la historia de un humilde pueblo que sobrevivia en un valle. Los inviernos eran duros",
                "pero poco a poco salian adelante. Hasta que, de repente, aparecio una horda de demonios de colores",
                "sedientos de sangre. El pueblo necesita un heroe!, exclamo el alcalde frente a la muchedumbre",
                "de la que quedaste tu mas adelantado. La multitud te ovacionaba, sentias recaer sobre tus hombros",
                "sus esperanzas. Pero estabas alucinando por haber sido elegido por todos. Bueno, tecnicamente",
                "se echaron atras mientras que a ti no te dio tiempo, pero la intencion es lo que cuenta. Animo",
                "en tu aventura, yo estare aqui para intentar que no mueras. Demasiado xd.");

        l1d1.addAll("Hola, viajero. Tu primera vez por estas tierras? No te preocupes, te voy a recordar como moverte.",
                "Para desplazarte a izquierda o derecha pulsa las teclas A y D, o las flechas de direccion.",
                "Si quieres saltar solo tienes que apretar Espacio. Prueba a moverte hasta ese barranco a la derecha.");

        l1d2.addAll("Ahora es el momento de darlo todo y hacer un salto de fe. Intenta llegar al otro lado cogiendo impulso.");
        l1d3.addAll("Ups! Se me olvido decirte que con saltar no es suficiente jeje. Ha llegado la hora de que aprendas a",
                "moverte mas rapido. Pulsa Shift y una direccion para impulsarte rapidamente. Recuerda que puedes",
                "ir hacia arriba o combinar dos direcciones. Ah! Y por ultimo, no puedes usarlo todo el rato.",
                "Tendras que esperar a que dejes de ser de otro color.");
        l1d4.addAll("Prueba saltando y despues usando Shift para llegar al otro lado. Y echale mas ganas que la ultima vez");
        l1d5.addAll("Cuidado con esos pinchos mas adelante. No me gustaria verte convertido en un pincho moruno.");
        l1d6.addAll("Esta vez te vas a tener que fiar de mi. Solo dejate caer. Mas adelante veras enemigos, intenta",
                "saltar sobre ellos y esquivarlos, o perderas vida");

        l2d1.addAll("Increible, has conseguido sobrevivir el tiempo suficiente para desarrollar una nueva habilidad.",
                "Ahora al moverte mas rapido no solo llegas mas lejos, sino que ademas eres invulnerable al ataque",
                "de tus enemigos, aunque todavia puedes morir hecho un colador por esos pinchos o si te caes por un ",
                "precipicio. No lo pruebes jeje. Intenta sobrevivir un poco mas y quizas consigas aprender algo util.");

        l3d1.addAll("Estas que lo tiras. Acabas de aprender como matar a esos bichos. Estrellate contra ellos a gran",
                "velocidad y probablemente veas sus tripas por los aires. Disfruta matando a unos cuantos, que te lo has",
                "ganado por salvar a tu pueblo.");
        l3d2.addAll("Enhorabuena, te has cebado con unas pobres criaturas del infierno que solo querian hacer sufrir",
                "a una panda de inocentes. Espero que estes orgulloso. Bueno, me tengo que ir a aconsejar a",
                "otros heroes. De verdad, no me pagan lo suficiente para este trabajo... Los heroes de antes",
                "molaban... Espera, que no se ha colgado? Estooo, no iba por ti jeje. Pi pi pi");
    }

    public static void dispose(){
        dialogs = null;
    }
}