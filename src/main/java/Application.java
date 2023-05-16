import daos.ProducteDAO;
import daos.ProducteDAO_MySQL;
import model.Producte;

import javax.sound.midi.Soundbank;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class Application {

    //Passar al DAO -->     //TODO: llegir les propietats de la BD d'un fitxer de configuració (Properties)
    //En general -->        //TODO: Afegir un sistema de Logging per les classes.

    private static ProducteDAO producteDAO = new ProducteDAO_MySQL();            //TODO: passar a una classe DAOFactory

    public static void main(String[] args) {

        Scanner lector = new Scanner(System.in);            //TODO: passar Scanner a una classe InputHelper

        int opcio = 0;

        do {
            mostrarMenu();
            opcio = lector.nextInt();

            switch (opcio) {
                case 1 -> mostrarMaquina();
                case 2 -> comprarProducte();
                case 10 -> mostrarInventari();
                case 11 -> afegirProductes();
                case 12 -> modificarMaquina();
                case 13 -> mostrarBenefici();
                case -1 -> System.out.println("Bye...");
                default -> System.out.println("Opció no vàlida");
            }
        } while (opcio != -1);

    }


    private static void modificarMaquina() {

        /**
         * Ha de permetre:
         *      - modificar les posicions on hi ha els productes de la màquina (quin article va a cada lloc)
         *      - modificar stock d'un producte que hi ha a la màquina
         *      - afegir més ranures a la màquina
         */
    }

    private static void afegirProductes() {
        Producte p = null;
        /**
         *      Crear un nou producte amb les dades que ens digui l'operari
         *      Agefir el producte a la BD (tenir en compte les diferents situacions que poden passar)
         *          El producte ja existeix
         *              - Mostrar el producte que té el mateix codiProducte
         *              - Preguntar si es vol actualitzar o descartar l'operació
         *          El producte no existeix
         *              - Afegir el producte a la BD
         *
         *     Podeu fer-ho amb llenguatge SQL o mirant si el producte existeix i després inserir o actualitzar
         */

        try {
            p = dadesProducte();

            ArrayList<Producte> productes = producteDAO.readProductes();

            producteDAO.createProducte(p);
        } catch (SQLException e) {
            System.out.println("Error: Codi de producte duplicat");
            System.out.println("Codi de l'error: " + e.getErrorCode());
            producteRepetit(p);
        } catch (NumberFormatException e) {
            System.out.println("Error: Introdueix un valor numeric per el preu del producte.");
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Error: S'ha introduit un valor incorrecte.");
            System.out.println(e.getMessage());
        }
    }

    private static void producteRepetit(Producte p) {
        Scanner entrada = new Scanner(System.in);
        ArrayList<Producte> productes = null;
        try {
            productes = producteDAO.readProductes();

            for (Producte prod : productes) {
                if (prod.getCodiProducte().equals(p.getCodiProducte())) {
                    System.out.println("Producte Original: " + prod.getDescripcio() + "\nProducte Nou " + p.getDescripcio());
                    System.out.println("Vols reemplaçar el producte original per el nou (1) o canviar el codi del producte nou (2)? ");
                    int opcio = Integer.parseInt(entrada.nextLine());
                    if (opcio == 1) {
                        prod.setNom(p.getNom());
                        prod.setDescripcio(p.getDescripcio());
                        prod.setPreuCompra(p.getPreuCompra());
                        prod.setPreuVenta(p.getPreuVenta());
                        producteDAO.updateProducte(prod, p);
                    } else if (opcio == 2) {
                        System.out.println("Entra el nou codi de producte: ");
                        p.setCodiProducte(entrada.nextLine());
                        producteDAO.createProducte(p);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InputMismatchException e) {
            System.out.println("Error: S'ha introduit un valor incorrecte.");
            System.out.println(e.getMessage());
        }
    }


    private static Producte dadesProducte() {
        Scanner entrada = new Scanner(System.in);
        System.out.println("Introdueix el codi del nou producte: ");
        String codiProd = entrada.nextLine();
        System.out.println("Introdueix el nom del nou producte: ");
        String nomProd = entrada.nextLine();
        System.out.println("Introdueix la descripció del nou producte: ");
        String descProd = entrada.nextLine();
        System.out.println("Introdueix el preu de compra del nou producte(decimals separats per un .): ");
        float preuCompra = Float.parseFloat(entrada.nextLine());
        System.out.println("Introdueix el preu de venta del nou producte(decimals separats per un .): ");
        float preuVenta = Float.parseFloat(entrada.nextLine());
        return new Producte(codiProd, nomProd, descProd, preuCompra, preuVenta);
    }

    private static void mostrarInventari() {

        try {
            //Agafem tots els productes de la BD i els mostrem
            ArrayList<Producte> productes = producteDAO.readProductes();
            for (Producte prod : productes) {
                System.out.println(prod);
            }

        } catch (SQLException e) {          //TODO: tractar les excepcions
            e.printStackTrace();
        }
    }

    private static void comprarProducte() {

        /**
         * Mínim: es realitza la compra indicant la posició on es troba el producte que es vol comprar
         * Ampliació (0.5 punts): es permet entrar el NOM del producte per seleccionar-lo (abans cal mostrar els
         * productes disponibles a la màquina)
         *
         * Tingueu en compte que quan s'ha venut un producte HA DE QUEDAR REFLECTIT a la BD que n'hi ha un menys.
         * (stock de la màquina es manté guardat entre reinicis del programa)
         */

    }

    private static void mostrarMaquina() {

        /** IMPORTANT **
         * S'està demanat NOM DEL PRODUCTE no el codiProducte (la taula Slot conté posició, codiProducte i stock)
         * també s'acceptarà mostrant només el codi producte, però comptarà menys.
         *
         * Posicio      Producte                Quantitat disponible
         * ===========================================================
         * 1            Patates 3D              8
         * 2            Doritos Tex Mex         6
         * 3            Coca-Cola Zero          10
         * 4            Aigua 0.5L              7
         */
    }

    private static void mostrarMenu() {
        System.out.println("\nMenú de la màquina expenedora");
        System.out.println("=============================");
        System.out.println("Selecciona la operació a realitzar introduïnt el número corresponent: \n");


        //Opcions per client / usuari
        System.out.println("[1] Mostrar Posició / Nom producte / Stock de la màquina");
        System.out.println("[2] Comprar un producte");

        //Opcions per administrador / manteniment
        System.out.println();
        System.out.println("[10] Mostrar llistat productes disponibles (BD)");
        System.out.println("[11] Afegir productes disponibles");
        System.out.println("[12] Assignar productes / stock a la màquina");
        System.out.println("[13] Mostrar benefici");

        System.out.println();
        System.out.println("[-1] Sortir de l'aplicació");
    }

    private static void mostrarBenefici() {

        /** Ha de mostrar el benefici de la sessió actual de la màquina, cada producte té un cost de compra
         * i un preu de venda. La suma d'aquesta diferència de tots productes que s'han venut ens donaran el benefici.
         *
         * Simplement s'ha de donar el benefici actual des de l'últim cop que s'ha engegat la màquina. (es pot fer
         * amb un comptador de benefici que s'incrementa per cada venda que es fa)
         */

        /** AMPLIACIÓ **
         * En entrar en aquest menú ha de permetre escollir entre dues opcions: veure el benefici de la sessió actual o
         * tot el registre de la màquina.
         *
         * S'ha de crear una nova taula a la BD on es vagi realitzant un registre de les vendes o els beneficis al
         * llarg de la vida de la màquina.
         */
    }
}
