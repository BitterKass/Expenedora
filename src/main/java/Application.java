import daos.ProducteDAO;
import daos.ProducteDAO_MySQL;
import daos.SlotDAO;
import daos.SlotDAO_MySQL;
import model.Producte;
import model.Slot;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Application {
    static final int PRIMARY_KEY_ALREADY_EXIST = 1062;
    //Passar al DAO -->     //TODO: llegir les propietats de la BD d'un fitxer de configuració (Properties)
    //En general -->        //TODO: Afegir un sistema de Logging per les classes.

    private static ProducteDAO producteDAO = new ProducteDAO_MySQL();            //TODO: passar a una classe DAOFactory
    private static SlotDAO slotDAO = new SlotDAO_MySQL();

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
        Scanner entrada = new Scanner(System.in);
        int opcio = 0;
        do {
            mostrarMenuModificacioMaquina();
            opcio = Integer.parseInt(entrada.nextLine());

            switch (opcio) {
                case 1 -> modificarPosicionsProductes();
                case 2 -> modificarStockProducte();
                case 3 -> afegirRanura();
                case -1 -> System.out.println("Tornant al menú principal...");
                default -> System.out.println("Opció no vàlida");
            }
        } while (opcio != -1);
    }

    private static void mostrarMenuModificacioMaquina() {
        System.out.println("""
                Menú de modificació de la màquina expenedora
                ============================================
                Selecciona l'operació a realitzar introduint el número corresponent:
                            
                [1] Modificar posicions dels productes
                [2] Modificar el stock d'un producte
                [3] Afegir ranures a la màquina
                            
                [-1] Tornar al menú principal
                """);
    }

    private static Slot afegirRanura() {
        Scanner entrada = new Scanner(System.in);
        System.out.println("Introdueix la posicio del nou slot: ");
        int posicio = Integer.parseInt(entrada.nextLine());

        try {
            // Verificar si la posición ya está repetida
            if (SlotDAO.existeixSlotAmbPosicio(posicio)) {
                throw new IllegalArgumentException("La posicio ja esta ocupada per un altre slot.");
            }

            // Verificar si la posición es menor que 0
            if (posicio < 0) {
                throw new IllegalArgumentException("La posicio ha de ser un valor positiu.");
            }

            System.out.println("Introdueix la quantitat del nou slot: ");
            int quantitat = Integer.parseInt(entrada.nextLine());
            System.out.println("Introdueix el codi del nou producte del nou slot: ");
            String codiProd = entrada.nextLine();

            return new Slot(posicio, quantitat, codiProd);
        } catch (NumberFormatException e) {
            System.out.println("Error: S'ha introduit un valor no vàlid.");
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        } finally {
            entrada.close();
        }
    }

    private static void modificarPosicionsProductes() {
        Scanner entrada = new Scanner(System.in);

        System.out.println("Slots disponibles:");
        mostrarMaquina();

        System.out.println("Introdueix el número de slot que vols moure:");
        int numeroSlot = entrada.nextInt();

        // Comprovar si el slot existeix
        Slot slot = null;
        try {
            slot = slotDAO.readSlot(numeroSlot);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (slot == null) {
            System.out.println("El slot no existeix.");
            return;
        }

        // Mostrar la posició actual del slot
        System.out.println("Posició actual del slot: " + slot.getPosicio());


        System.out.println("Introdueix la nova posició del slot:");
        int novaPosicio = Integer.parseInt(entrada.nextLine());

        // Actualitzar la posició del slot
        slot.setPosicio(novaPosicio);
        try {
            slotDAO.updateSlot(slot);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("La posició del slot s'ha modificat correctament.");
    }


    private static void modificarStockProducte() {
        Scanner entrada = new Scanner(System.in);

        System.out.println("Introdueix la posició del producte que vols modificar: ");
        int posicio = Integer.parseInt(entrada.nextLine());
        try {
            slotDAO.modificarStock(posicio);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // TODO: Implementar la lògica per modificar el stock del producte a la posició especificada
    }

    private static void afegirProductes() {

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
        Producte p = null;
        try {
            p = dadesProducte();

            ArrayList<Producte> productes = producteDAO.readProductes();

            producteDAO.createProducte(p);
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getErrorCode() == PRIMARY_KEY_ALREADY_EXIST) {//Error code 1062 equival a camp clau repetit
                System.out.println("Error: Codi de producte duplicat");
                System.out.println("Codi de l'error: " + e.getErrorCode());
                producteRepetit(p);
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Error: Introdueix un valor numeric per el preu del producte.");
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            e.printStackTrace();
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
                        prod = new Producte(p);
                        producteDAO.updateProducte(prod);
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
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private static void comprarProducte() {
        Scanner entrada = new Scanner(System.in);
        /**
         * Mínim: es realitza la compra indicant la posició on es troba el producte que es vol comprar
         * Ampliació (0.5 punts): es permet entrar el NOM del producte per seleccionar-lo (abans cal mostrar els
         * productes disponibles a la màquina)
         *
         * Tingueu en compte que quan s'ha venut un producte HA DE QUEDAR REFLECTIT a la BD que n'hi ha un menys.
         * (stock de la màquina es manté guardat entre reinicis del programa)
         */
        mostrarMaquina();
        System.out.println("Entra la posicio del producte desitjat: ");
        int posicio = Integer.parseInt(entrada.nextLine());
        try {
            slotDAO.restarSlot(posicio);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        mostrarMaquina();
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

        try {
            // Obtener la lista de slots de la máquina
            List<Slot> slots = slotDAO.readSlots();


            // Imprimir la cabecera de la tabla
            System.out.println("Posicio\t\tProducte\t\tQuantitat disponible");
            System.out.println("===================================================");

            // Imprimir cada fila de la tabla con los datos de los slots
            for (Slot slot : slots) {
                System.out.println(slot.getPosicio() + "\t\t\t" + slot.getCodi_producte() + "\t\t\t" + slot.getQuantitat());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private static void mostrarMenu() {
        System.out.println("""
                Menú de la màquina expenedora
                =============================
                Selecciona la operació a realitzar introduïnt el número corresponent:
                                
                [1] Mostrar Posició / Nom producte / Stock de la màquina
                [2] Comprar un producte
                                
                [10] Mostrar llistat productes disponibles (BD)
                [11] Afegir productes disponibles
                [12] Assignar productes / stock a la màquina
                [13] Mostrar benefici
                                
                [-1] Sortir de l'aplicació
                """);
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
