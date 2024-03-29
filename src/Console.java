import Controller.Interpreter;
import Controller.Lexer;
import HelperClases.PairTuple;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Esta es la clase principal del programa. Esta esta encargada de leer el archivo .txt donde estan las instrucciones que se quieren verificar.
 *
 * El flujo del programa es:
 * 1. Se abre el archivo y se lee todas sus lineas. Se reconocen sub bloques de codigo (definiciones, comandos)
 * 2. En un loop se itera a traves de cada uno de los bloques de codigo del archivo.
 *      2a) Se pasa un bloque de codigo al lexer para ser tokenizados
 *      2b) Se pasa el bloque tokenizado al interpreter para ser validado o no
 * 4. Si todos los bloques de instrucciones son validos en el archivo se determina que la secuencia de instrucciones e valida o de lo contrario no se considerara valido
 *
 *  Esta clase lidia con el paso 1 y 4
 */

public class Console {

    /**
     * Contiene el path al archivo en el que estan las instrucciones para el robot
     */
    public static final String FILEPATH = "./Archivos/instrucciones.txt";


    /**
     * Lee el archivo .txt de instrucciones que esta contenido en la carpeta de Archivos dentro del proyecto.
     * Guarda el contenido del archivo y se lo pasa al lexer y al interpreteer para determinar si las instrucciones son validas o no.
     */
    public static void main(String args[]) {
        //Variable donde se va a guardar el resultado de evaluacion de las instrucciones
        boolean isValid = false;

        //Extrae las instrucciones a un archivo


        String instructions = readFile();

        //System.out.println("el codigo es: " + instructions);


        //Verifica que los parentesis esten blanceados en la totalidad de las instrucciones
        //Si los parentesis no estan balanceados el set de instrucciones es invalido. Si es valido toca hacer mas revisiones
        if(checkForBalancedParenthesis(instructions)){
            //Rompe las instrucciones a bloques de instrucciones individuales
            ArrayList<String> instructionBlocks = getInstructionBlocks(instructions);


            isValid = Interpreter.process(instructionBlocks);
        }
        else{
            isValid = false;
        }

        //Dejar quieto, se va a arreglar cuando el lexer este listo
        String eval =  (isValid) ?  "VALIDO" : "INVALIDO";

        //Printea respuesta en consola
        System.out.println("El set de instrucciones dado es : " + eval);

    }


    /**
     * Lee el archivo .txt que este asociado a la constante FILEPATH que contiene la secuencia de instrucciones que se van a evaluar. Retorna su contenido en el formato de un string.
     * El archivo es limpiado para que su formato sea mas facil de leer y que este mas organizado
     *
     * @return Un String que contiene las instrucciones dentro del archivo .txt de instrucciones
     */
        public static String readFile() {
            Path p = Paths.get("C:\\Users\\User PC\\Documents\\Santiago\\P0_2021\\src\\Archivos\\instrucciones.txt");

            StringBuilder sb = new StringBuilder();

            try (Stream<String> stream = Files.lines(p)) {
                stream.forEach(s -> sb.append(s).append(" "));

            } catch (IOException ex) {
                // Handle exception
            }

            String contents = sb.toString();
            return contents;
        }




    /**
     * Lee el contenido del archivo con tal de determinar si sus parentesis estan balanceados o no.
     * Si lo estan retorna true, de lo contrario false.
     * Si este paso sale mal por default se sabe que las instrucciones son invalidas como un totol.
     *
     * @param str El String que contiene todas las instrucciones del archivo
     * @return True si los parentesiis estan balanceados, False de lo contrario
     */
    public static boolean checkForBalancedParenthesis(String str) {
        char leftParen = '(';
        char rightParen = ')';

        Stack<Character> stack = new Stack<Character>();

        //Esto es responsable de ir anadiendo los parentesis que encuentre en la expresion a un stack
        for (int i = 0; i < str.length(); i++)
        {
            //Anade al stack los (
            if (str.charAt(i) == leftParen) {
                stack.push(leftParen);
            }

            //Cuando se encuentra con un )
            else if (str.charAt(i) == rightParen) {
                // Si el prmer parentesiis que se lee es un ) la expresion esta automaticamente mal
                if (stack.isEmpty()) {
                    return false;
                }

                // Si se hace pop al stack y el parentesis que sale no es un ( eso significa que esta mal
                if (stack.pop() != leftParen) {
                    return false;
                }
            }
        }
        //Si el stack esta vacio
        return stack.isEmpty();
    }


    /**
     * Se lee el string de instrucciones
     * @param str La cadena que contiene el set de instrucciones
     * @pre Se tiene que haber revisado que la totalidad del set de instrucciones tenga los parentesis balanceados
     * @return Un arrayList que contiene cada bloque de codigo en el set de instrucciones del archivo txt
     *
     * Aclaracion: Por bloque de codigo se esta definiendo a una expresion que tiene sus parentesis balanceados
     * Ej Bloques de instrucciones: ( rotat e back ) , ( if (not (blocked?) ) (walk 1) ( NOP ) ), ( define foo (c p) (block (drop c )( free p )(walk one ) ) )
     */

    public static ArrayList<String> getInstructionBlocks(String str){
        //Le hace trim al string para quitarle el trailing y leading space
        str = str.trim().replaceAll("  ", " ");

        //Variables de parentesis
        char leftParen = '(';
        char rightParen = ')';

        //Variables que guardan la pos inicial y final del bloque de instrucciones actual. Se inicializan como el inicio y fin del string
        int initialIndexCurrBlock = 0;
        //int finalIndexCurrBlock = str.lastIndexOf(')');
        int finalIndexCurrBlock = str.length() -1 ;

        //Inicializa las estructuras correspondientes
        ArrayList<String> setOfBlocks = new ArrayList<>();
        Stack<Character> stack = new Stack<Character>();


        for (int i = 0; i < str.length(); i++)
        {
            String currChar = str.charAt(i) + "";
            //Anade al stack los (
            if (str.charAt(i) == leftParen) {
                stack.push(leftParen);
            }

            //Cuando se encuentra con un ) se hace pop al stack
            else if (str.charAt(i) == rightParen) {
                stack.pop();
            }

            //Cuando se haya hecho el ultimo pop al stack y este se encuentra vacio
            if(stack.isEmpty()){
                // Saca el bloque de instrucciones dentro del codigo
                String currBlock = str.substring(initialIndexCurrBlock, (i + 1));

                //Se incrementa el indice inicial para la siguiente lectura
                initialIndexCurrBlock = i + 1;

                //Se anade el bloque de codigo reconocido al arreglo con los bloques de codigo
                setOfBlocks.add(currBlock);
            }
        }

        //Retorna la lista con cada uno de los bloques de codigo reconocidos
        return setOfBlocks;

    }

}
