package zadaci;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import model.Avion;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by Vladimir on 1/19/2017.
 */
public class AvionNit extends Thread {

    private Avion avion;

    static Dao<Avion,Integer> avionDao;

    public static Boolean dozvoljenoPoletanje = true;

    public AvionNit(Avion avion) { this.avion = avion; }

    public void run() {
        Random random = new Random();
        System.out.println("Pocinju provere za avion " + avion.getId() + ".");

        try {
            sleep(random.nextInt(2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Avion " + avion.getId() + " je zavrsio proveru i ceka dozvolu za poletanje.");

        do {
            synchronized (dozvoljenoPoletanje) {
                if (dozvoljenoPoletanje) {
                    System.out.println("Avion " + avion.getId() + " izlazi na pistu i polece.");
                    dozvoljenoPoletanje = false;

                    try {
                        sleep(random.nextInt(2000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    synchronized (dozvoljenoPoletanje) {
                        System.out.println("Avion " + avion.getId() + " je poleteo");
                        dozvoljenoPoletanje = true;
                    }
                }
            }
        } while (!dozvoljenoPoletanje);
    }

    public static void main(String[] args) {
        ConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcConnectionSource("jdbc:sqlite:avionRoba.db");
            avionDao = DaoManager.createDao(connectionSource, Avion.class);

            List<Avion> sviAvioni = avionDao.queryForAll();
            AvionNit an1 = new AvionNit(sviAvioni.get(0));
            AvionNit an2 = new AvionNit(sviAvioni.get(1));

            an1.start();
            an2.start();

            an1.join();
            an2.join();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connectionSource != null) {
                try {
                    //Zatvaranje konekcije sa bazom
                    connectionSource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
