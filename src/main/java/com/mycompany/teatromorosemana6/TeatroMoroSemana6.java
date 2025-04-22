package com.mycompany.teatromorosemana6;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class TeatroMoroSemana6 {

    public static void main(String[] args) {
        // Scanner para leer entradas del usuario 
        Scanner scanner = new Scanner(System.in);
        // Variable que almacena la opción seleccionada en el menú
        int opcionMenu = -1;
        // Constantes: número de sectores y asientos por sector
        final int cantidadSectores = 3;
        final int asientosPorSector = 10;

        // Precios establecidos para cada tipo de sector
        final int precioVip = 30000;
        final int precioPlatea = 20000;
        final int precioGeneral = 10000;

        // Arreglos que representan cada asiento en los sectores
        boolean[] asientosVip = new boolean[asientosPorSector];
        boolean[] asientosPlatea = new boolean[asientosPorSector];
        boolean[] asientosGeneral = new boolean[asientosPorSector];

        // Ciclo principal que se repite hasta que el usuario elija la opción salir
        do {
            // Mostramos las opciones del menú
            System.out.println("Menú Principal\n");
            System.out.println("1- Disponibilidad y reserva de entradas");
            System.out.println("2- Confirmar compra");
            System.out.println("3- Modificar una venta");
            System.out.println("4- Imprimir boleta");
            System.out.println("5- Salir\n");
            System.out.print("Selecciona una opción (1-5): ");

            // Validamos que el usuario ingrese un número entero
            if (!scanner.hasNextInt()) {
                System.out.println("Entrada inválida. Debes ingresar un número.\n");
                scanner.nextLine(); // Limpiamos scanner
                continue;           // Volvemos al inicio del bucle
            }
            opcionMenu = scanner.nextInt();

            // Switch para el menú
            switch (opcionMenu) {
                case 1:  //Mapa de disponibilidad y reserva de asientos
                    System.out.println("\nUbicaciones del Teatro Moro:");
                    // Mostramos el estado de cada asiento en todos los sectores
                    for (int i = 1; i <= cantidadSectores; i++) {
                        String nombreDelSector = "";
                        boolean[] asientosSector = null;

                        // Asociamos el número de sector con el arreglo correspondiente
                        if (i == 1) {
                            nombreDelSector = "1- Sector Vip     ";
                            asientosSector = asientosVip;
                        } else if (i == 2) {
                            nombreDelSector = "2- Sector Platea  ";
                            asientosSector = asientosPlatea;
                        } else {
                            nombreDelSector = "3- Sector General ";
                            asientosSector = asientosGeneral;
                        }

                        // Se muestra disponibilidad para cada asiento: [O] libre, [X] ocupado
                        System.out.print(nombreDelSector + ": ");
                        for (int a = 0; a < asientosPorSector; a++) {
                            System.out.print(asientosSector[a] ? "[X]" : "[O]");
                        }
                        System.out.println();
                    }

                    // Solicitamos selección de sector al usuario
                    System.out.print("\nSelecciona un sector (1= Vip, 2= Platea, 3= General): ");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Entrada inválida. Debes ingresar un número.\n");
                        scanner.nextLine();
                        break;
                    }
                    int seleccionSector = scanner.nextInt();

                    // [Debug] Punto de depuración: Validar rango de sector
                    if (seleccionSector < 1 || seleccionSector > 3) {
                        System.out.println("Sector no válido, intenta nuevamente.\n");
                        break;
                    }

                    // Solicitamos número de asiento
                    System.out.print("Selecciona un asiento (1-10): ");
                    // [Debug] Punto de depuración: Validar que el número ingresado sea entero
                    if (!scanner.hasNextInt()) {
                        System.out.println("Entrada inválida. Debes ingresar un número.\n");
                        scanner.nextLine();
                        break;
                    }
                    int seleccionAsiento = scanner.nextInt();

                    // [Debug] Punto de depuración: Validar rango de asiento ingresado
                    if (seleccionAsiento < 1 || seleccionAsiento > asientosPorSector) {
                        System.out.println("Asiento no válido, intenta nuevamente.\n");
                        break;
                    }

                    // Calculamos él índice en el arreglo
                    int indice = seleccionAsiento - 1;
                    boolean[] asientosSeleccionados = null;
                    String nombreSector = "";

                    // Determinamos qué arreglo corresponde al sector elegido
                    if (seleccionSector == 1) {
                        asientosSeleccionados = asientosVip;
                        nombreSector = "Sector Vip";
                    } else if (seleccionSector == 2) {
                        asientosSeleccionados = asientosPlatea;
                        nombreSector = "Sector Platea";
                    } else {
                        asientosSeleccionados = asientosGeneral;
                        nombreSector = "Sector General";
                    }

                    // [Debug] Punto de depuración: Verificar si el asiento ya está ocupado
                    if (asientosSeleccionados[indice]) {
                        System.out.println("Ese asiento ya está ocupado. Intenta con otro.\n");
                    } else {
                        // Marcamos el asiento como reservado
                        asientosSeleccionados[indice] = true;
                        System.out.println("\nHas reservado: " + nombreSector + " - Asiento número: " + seleccionAsiento + "\n");
                        System.out.println("Tienes 2 minutos para confirmar tu compra");

                        // Creamos temporizador para cancelar la reserva si no se confirma
                        Timer temporizador = new Timer();
                        final boolean[] asientosFinal = asientosSeleccionados;
                        final int indiceFinal = indice;

                        TimerTask cancelarReserva = new TimerTask() {
                            @Override
                            public void run() {
                                asientosFinal[indiceFinal] = false;  // Liberar asiento
                                reservaPendiente = false;
                                System.out.println("La reserva fue cancelada por inactividad.");
                                temporizador.cancel();
                            }
                        };

                        // Programamos la tarea de cancelar reserva a 2 minutos
                        temporizador.schedule(cancelarReserva, 120000);

                        // Guardamos estado de reserva pendiente
                        reservaPendiente = true;
                        sectorPendienteGlobal = asientosSeleccionados;
                        indiceAsientoPendienteGlobal = indice;
                        temporizadorPendienteGlobal = temporizador;
                    }
                    break;

                case 2:  //Confirmar compra
                    if (!reservaPendiente) {
                        System.out.println("No hay ninguna reserva pendiente para confirmar.\n");
                    } else {
                        // [Debug] Punto de depuración: Cancelar temporizador y limpiar reserva pendiente
                        temporizadorPendienteGlobal.cancel();
                        reservaPendiente = false;

                        // [Debug] Punto de depuración: Verificar sector y calcular precio
                        String sectorCompra = (sectorPendienteGlobal == asientosVip)
                            ? "Sector Vip"
                            : (sectorPendienteGlobal == asientosPlatea)
                              ? "Sector Platea"
                              : "Sector General";
                        int precioCompra = (sectorPendienteGlobal == asientosVip)
                            ? precioVip
                            : (sectorPendienteGlobal == asientosPlatea)
                              ? precioPlatea
                              : precioGeneral;

                        // [Debug] Punto de depuración: Crear objeto Ticket y actualizar estadísticas
                        Ticket nuevaEntrada = new Ticket(sectorCompra,
                                                         indiceAsientoPendienteGlobal + 1,
                                                         precioCompra);
                        entradasCompradas[cantidadEntradas++] = nuevaEntrada;
                        ingresosTotales += precioCompra;
                        entradasVendidasTotales++;

                        System.out.println("\nCompra confirmada: " + nuevaEntrada + "\n");
                    }
                    break;

                case 3: //Mostrar las entradas compradas
                    if (cantidadEntradas == 0) {
                        System.out.println("No hay entradas confirmadas para eliminar.\n");
                        break;
                    }

                    // Mostramos todas las boletas confirmadas
                    System.out.println("Entradas confirmadas:");
                    for (int i = 0; i < cantidadEntradas; i++) {
                        System.out.println((i + 1) + ". " + entradasCompradas[i]);
                    }

                    System.out.print("Ingresa el número de la entrada que deseas eliminar (1 a " + cantidadEntradas + "): ");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Entrada inválida. Debes ingresar un número.\n");
                        scanner.nextLine();
                        break;
                    }
                    int indiceEliminar = scanner.nextInt() - 1;

                    // Validación de índice
                    if (indiceEliminar < 0 || indiceEliminar >= cantidadEntradas) {
                        System.out.println("Número inválido.\n");
                        break;
                    }

                    // Obtenemos la entrada a eliminar y liberamos el asiento correspondiente
                    Ticket entradaEliminar = entradasCompradas[indiceEliminar];
                    if (entradaEliminar.sector.equals("Sector Vip")) {
                        asientosVip[entradaEliminar.asiento - 1] = false;
                    } else if (entradaEliminar.sector.equals("Sector Platea")) {
                        asientosPlatea[entradaEliminar.asiento - 1] = false;
                    } else if (entradaEliminar.sector.equals("Sector General")) {
                        asientosGeneral[entradaEliminar.asiento - 1] = false;
                    }

                    // Desplazamos el resto de entradas en el arreglo para mantener continuidad
                    for (int i = indiceEliminar; i < cantidadEntradas - 1; i++) {
                        entradasCompradas[i] = entradasCompradas[i + 1];
                    }
                    cantidadEntradas--;
                    ingresosTotales -= entradaEliminar.precio;
                    entradasVendidasTotales--;

                    System.out.println("Entrada eliminada correctamente.\n");
                    break;

                case 4: //Mostrar Tickets comprados
                    if (cantidadEntradas == 0) {
                        System.out.println("Aún no has confirmado ninguna compra.\n");
                    } else {
                        // [Debug] Punto de depuración 7: Iniciar recorrido de boletas
                        System.out.println("Boletas confirmadas:");

                        for (int i = 0; i < cantidadEntradas; i++) {
                            Ticket t = entradasCompradas[i];

                            // [Debug] Punto de depuración 8: Mostrar detalles de cada ticket
                            System.out.println("     Ticket #" + (i + 1));
                            System.out.println("     Sector: " + t.sector);
                            System.out.println("     Asiento: " + t.asiento);
                            System.out.println("     Precio: $" + t.precio);

                            System.out.println((i + 1) + ". " + t);
                        }
                    }
                    break;

                case 5:  //Salir 
                    System.out.println("Saliendo del sistema... ¡Vuelve pronto!\n");
                    break;

                default:
                    // Opción no válida 
                    System.out.println("Opción no válida. Intenta nuevamente.\n");
            }

        } while (opcionMenu != 5);

        scanner.close();   //Cerrar scanner
    }

    // Clase interna para representar un ticket de entrada
    static class Ticket {
        private static int siguienteId = 1;  // Generador de IDs único
        int id;
        String sector;
        int asiento;
        int precio;

        public Ticket(String sector, int asiento, int precio) {
            this.id = siguienteId++;
            this.sector = sector;
            this.asiento = asiento;
            this.precio = precio;
        }

        @Override     //Ayud a acomprobar que se sobreescribe correctamente el método 
        public String toString() {
            return "Boleta #" + id + ": " + sector + " – Asiento " + asiento + " – $" + precio;
        }
    }

    // Variables para manejar reserva pendiente y estadísticas globales
    static boolean reservaPendiente = false;
    static boolean[] sectorPendienteGlobal;
    static int indiceAsientoPendienteGlobal;
    static Timer temporizadorPendienteGlobal;

    static Ticket[] entradasCompradas = new Ticket[30]; // Almacenamiento de tickets
    static int cantidadEntradas = 0;                   // Contador de entradas vendidas
    static int ingresosTotales = 0;                    // Total recaudado
    static int entradasVendidasTotales = 0;            // Total de entradas vendidas
}
