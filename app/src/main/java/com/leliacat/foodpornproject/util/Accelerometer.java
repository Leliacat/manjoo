package com.leliacat.foodpornproject.util;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;


public class Accelerometer {

    //PROPRIÉTÉS

    //constantes
    private String LOGTAG = "monTag";                       //chaîne pour tagger les logs
    private static final int SHAKE_THRESHOLD = 300;         //seuil de détection de secousse
    private static final int SHAKE_DELAY = 100;             //(unité : ms) seuil d'attente minimum avant d'analyser à nouveau les données de l'accéléromètre
    private final int PAUSE_DELAY = 1000;                   //(unité : ms) délai avant d'accepter de détecter une nouvelle succession de secousses
    private final int MULTIPLE_SHAKES_MAX_DELAY = 800;      //(unité : ms) temps maximum qui peut s'écouler avant que le compteur de secousses consécutives se réinitialise

    //variables
    private Context context;                                //fait référence au contexte qui construit une instance de cette classe (en principe : MainActivity)
    private long curTime;                                   //pour stocker le temps actuel à un instant T
    private long lastUpdate = System.currentTimeMillis();   //pour stocker un état du temps à comparer à curTime
    private float x, y, z;                                  //pour stocker les positions actuelles à un instant T
    private float last_x, last_y, last_z;                   //pour stocker un état de position des axes à comparer aux positions de x, y, z
    private int compteurShake = 0;                          //compteur de secousses à afficher à l'utilisateur
    private int compteurShakeX = 0;                         //ditto, mais pour l'axe X uniquement
    private int compteurShakeY = 0;                         //ditto Y
    private int compteurShakeZ = 0;                         //ditto Z
    private boolean firstShake = false;                     //une première secousse est détectée
    private boolean secondShake = false;                    //une seconde secousse est détectée dans un laps de temps
    private boolean thirdShake = false;                     //une troisième
    private boolean pauseShakeListening = false;            //arrêter temporairement de détecter des secousses successives
    private long timeOfPauseStart = System.currentTimeMillis();
    private long lastUpdateOfFirstShake = System.currentTimeMillis();   //pour tester quand on reçoit la troisième secousse si elle n'est pas trop éloignée de la première dans le temps

    public interface MyCallBackInterface {                         //sert à appeler les actions qu'on veut déclencher à la détection de 3 secousses, en surchargeant onShakesDetected()
        void onShakesDetected();
    }
    MyCallBackInterface callBackInterface;

    //CONSTRUCTEURS

   public  Accelerometer(Context context, MyCallBackInterface callBackInterface) {
        this.context = context;             //pour récupérer le contexte qui instancie cette classe
        this.callBackInterface = callBackInterface;
    }


    //GETTERS & SETTERS



    //MÉTHODES


    /**
     * listener de l'accelerometre
     *
     * @param sensorEvent recupere les donnees de l'accelerometre
     */
    void onSensorChange(SensorEvent sensorEvent) {
        //ici on se contente d'appeler les méthodes désirées, elles ne sont pas prédéfinies, il faut les ajouter en @Override
        boolean troisSecousses = detecterTroisSecoussesConsecutives(sensorEvent);
        if (troisSecousses) {            //si on détecte deux secousses
            Log.w(LOGTAG, "TROIS SECOUSSES DÉTECTÉES !");
            callBackInterface.onShakesDetected();
        }
//        else {
//            Log.v(LOGTAG, "Trois secousses pas encore détectées");
//            compterLesSecoussesDansLesTroisAxes(sensorEvent);
//        }

    }



    /**
     * renvoie true si trois secousses sont detectees dans un laps de temps
     *
     * @param sensorEvent recupere les donnees de l'accelerometre
     * @return true si trois secousses sont detectees dans un laps de temps
     */
    //TODO utiliser une boucle for au lieu d'une série de if en cascade pour pouvoir détecter un nombre de secousses au choix
    private boolean detecterTroisSecoussesConsecutives(SensorEvent sensorEvent) {
        boolean resultat = false;
        curTime = System.currentTimeMillis();                   //on récupère la référence de temps actuelle
        if (!pauseShakeListening) {                             //n'agir que si la pause n'est pas demandée
            if (firstShake) {                                   //si une première secousse a déjà été enregistrée
                if (secondShake) {                              //et qu'on détecte une seconde secousse
                    thirdShake = shakeDetectedAnyAxis(sensorEvent);
                    if (thirdShake) {                           //et une troisième
                        if ((curTime - lastUpdateOfFirstShake) < MULTIPLE_SHAKES_MAX_DELAY) {
                            firstShake = false;                     //on remet à zéro les compteurs de détection
                            secondShake = false;
                            thirdShake = false;
                            pauseShakeListening = true;             //on demande la mise en pause de la détection de secousses successives
                            timeOfPauseStart = curTime;
                            resultat = true;                        //et on renvoie un résultat positif (= on a bien détecté deux secousses consécutives)
                        } else {
                            firstShake = false;                     //on remet à zéro les compteurs de détection
                            secondShake = false;
                            thirdShake = false;
                            Log.w(LOGTAG, "Délai dépassé pour que trois secousses soient acceptées. Réinitialisation du compteur.");
                        }
                    }
                } else {
                    secondShake = shakeDetectedAnyAxis(sensorEvent);
                }
            } else {                                            //si une première secousse n'a pas déjà été enregistrée
                firstShake = shakeDetectedAnyAxis(sensorEvent);   //on teste si on détecte une première secousse
                lastUpdateOfFirstShake = curTime;               //on enregistre un marqueur de temps
            }
        } else {    //si la pause est active
            //on désactive la pause si le délai de pause est dépassé
            if ((curTime - timeOfPauseStart) > PAUSE_DELAY) {   //si le temps écoulé depuis la demande de pause est supérieur au délai de pause réglé en constante
                pauseShakeListening = false;
            }
        }
        return resultat;
    }


    /**
     * renvoie true si une secousse depassant un certain seuil est detectee dans n'importe quel axe (X, Y ou Z). Le seuil est defini par la constante SHAKE_THRESHOLD.
     *
     * @param sensorEvent recupere les donnees de l'accelerometre
     * @return true si une secousse depassant un certain seuil est detectee dans n'importe quel axe (X, Y ou Z)
     */
    private boolean shakeDetectedAnyAxis(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        boolean resultat = false;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {  //double vérification qu'on récupère bien un événement d'accéléromètre
            x = sensorEvent.values[0];                    //on récupère les coordonnées dans les 3 axes
            Log.d(LOGTAG, "x = " + x);
            y = sensorEvent.values[1];
            Log.d(LOGTAG, "y = " + y);
            z = sensorEvent.values[2];
            Log.d(LOGTAG, "z = " + z);
            curTime = System.currentTimeMillis();          //on stocke un repère temporel
            if ((curTime - lastUpdate) > SHAKE_DELAY) {
                //délai entre deux actions, pour ne pas déclencher l'action trop souvent
                //            Log.d(LOGTAG, "délai correctement observé");
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
//                float speedX = Math.abs(x - last_x) / diffTime * 10000;
//                float speedY = Math.abs(y - last_y) / diffTime * 10000;
//                float speedZ = Math.abs(z - last_z) / diffTime * 10000;
                Log.d(LOGTAG, "speed = " + speed);
                if (speed > SHAKE_THRESHOLD) {                  //si la vitesse est suffisante
                    resultat = true;
                    Log.i(LOGTAG, "secousse détectée dans au moins un des 3 axes.");
                }
            }
            last_x = x;
            last_y = y;
            last_z = z;
        }
        return resultat;
    }

    /**
     * renvoie true si une secousse depassant un certain seuil est detectee dans l'axe X. Le seuil est defini par la constante SHAKE_THRESHOLD.
     *
     * @param sensorEvent recupere les donnees de l'accelerometre
     * @return true si une secousse depassant un certain seuil est detectee dans l'axe X
     */
    private boolean shakeDetectedXAxis(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        boolean resultat = false;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {  //double vérification qu'on récupère bien un événement d'accéléromètre
            x = sensorEvent.values[0];                    //on récupère les coordonnées dans l'axe X

            curTime = System.currentTimeMillis();          //on stocke un repère temporel
            if ((curTime - lastUpdate) > SHAKE_DELAY) {
                //délai entre deux actions, pour ne pas déclencher l'action trop souvent
                //            Log.d(LOGTAG, "délai correctement observé");
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                float speedX = Math.abs(x - last_x) / diffTime * 10000;
                Log.d(LOGTAG, "speedX = " + speedX);
                if (speedX > SHAKE_THRESHOLD) {                  //si la vitesse est suffisante
                    resultat = true;
                    Log.i(LOGTAG, "secousse détectée dans l'axe X.");
                }
            }
        }
        last_x = x;
        return resultat;
    }

    /**
     * renvoie true si une secousse depassant un certain seuil est detectee dans l'axe Y. Le seuil est defini par la constante SHAKE_THRESHOLD.
     *
     * @param sensorEvent recupere les donnees de l'accelerometre
     * @return true si une secousse depassant un certain seuil est detectee dans l'axe Y
     */
    private boolean shakeDetectedYAxis(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        boolean resultat = false;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {  //double vérification qu'on récupère bien un événement d'accéléromètre
            y = sensorEvent.values[1];                    //on récupère les coordonnées dans l'axe Y

            curTime = System.currentTimeMillis();          //on stocke un repère temporel
            if ((curTime - lastUpdate) > SHAKE_DELAY) {
                //délai entre deux actions, pour ne pas déclencher l'action trop souvent
                //            Log.d(LOGTAG, "délai correctement observé");
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                float speedY = Math.abs(y - last_y) / diffTime * 10000;
                Log.d(LOGTAG, "speedY = " + speedY);
                if (speedY > SHAKE_THRESHOLD) {                  //si la vitesse est suffisante
                    resultat = true;
                    Log.i(LOGTAG, "secousse détectée dans l'axe Y.");
                }
            }
        }
        last_y = y;
        return resultat;
    }

    /**
     * renvoie true si une secousse depassant un certain seuil est detectee dans l'axe Z. Le seuil est defini par la constante SHAKE_THRESHOLD.
     *
     * @param sensorEvent recupere les donnees de l'accelerometre
     * @return true si une secousse depassant un certain seuil est detectee dans l'axe Z
     */
    private boolean shakeDetectedZAxis(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        boolean resultat = false;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {  //double vérification qu'on récupère bien un événement d'accéléromètre
            z = sensorEvent.values[1];                    //on récupère les coordonnées dans l'axe Y

            curTime = System.currentTimeMillis();          //on stocke un repère temporel
            if ((curTime - lastUpdate) > SHAKE_DELAY) {
                //délai entre deux actions, pour ne pas déclencher l'action trop souvent
                //            Log.d(LOGTAG, "délai correctement observé");
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                float speedZ = Math.abs(z - last_z) / diffTime * 10000;
                Log.d(LOGTAG, "speedZ = " + speedZ);
                if (speedZ > SHAKE_THRESHOLD) {                  //si la vitesse est suffisante
                    resultat = true;
                    Log.i(LOGTAG, "secousse détectée dans l'axe Z.");
                }
            }
        }
        last_z = z;
        return resultat;
    }

}
