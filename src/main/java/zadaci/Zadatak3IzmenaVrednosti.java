package zadaci;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import model.Avion;
import model.Roba;

import java.awt.image.RasterOp;
import java.io.IOException;
import java.util.List;

import static zadaci.Zadatak2DodavanjeVrednosti.avionDao;
import static zadaci.Zadatak2DodavanjeVrednosti.robaDao;

/**
 * Created by Vladimir on 1/19/2017.
 */
public class Zadatak3IzmenaVrednosti {
    public static void main(String[] args) {
        ConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcConnectionSource("jdbc:sqlite:avionRoba.db");

            avionDao = DaoManager.createDao(connectionSource, Avion.class);
            robaDao = DaoManager.createDao(connectionSource, Roba.class);

            List<Roba> roba = robaDao.queryForAll();
            for(Roba r: roba)
                System.out.println("Roba = " + r);

            List<Roba> pronadjenaRoba = robaDao.queryForEq(Roba.POLJE_OPIS, "Plasticna stolica");
            Roba robaZaIzmenu = pronadjenaRoba.get(0);
            robaZaIzmenu.setOpis("Drvena stolica");
            robaDao.update(robaZaIzmenu);

            roba = robaDao.queryForAll();
            for(Roba r: roba)
                System.out.println("Roba = " + r);



        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connectionSource != null) {
                try {
                    connectionSource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}