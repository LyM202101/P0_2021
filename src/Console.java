import java.util.ArrayList;
import java.util.Stack;

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
    public static final String FILEPATH = "";





    /**
     * Lee el archivo .txt de instrucciones que esta contenido en la carpeta de Archivos dentro del proyecto.
     * Guarda el contenido del archivo y se lo pasa al lexer y al interpreteer para determinar si las instrucciones son validas o no.
     */
    public static void main(String args[]){
        System.out.println("Archivo que esta siendo leido esta en la ruta : " + FILEPATH + "\n");


        //Se llama al metodo que extrae el contenido del archivo y lo ubica en un String
        ArrayList<String> instructions = null;
    }

    /**
     * Lee el archivo .txt que este asociado a la constante FILEPATH que contiene la secuencia de instrucciones que se van a evaluar. Retorna su contenido en el formato de un string.
     * El archivo es limpiado para que su formato sea mas facil de leer y que este mas organizado
     * @return
     */
    public String readFile(){
        String instructions= "";

        //TODO (RAPPY) : Set up el File IO para que lea el archivo de instrucciones que esta en la carpeta de Archivos. Leer cada linea del archivo y pasar lo leido a un string. Si se puede que ignore los "\n" y los bloques de espacio largos



        return instructions;
    }


    /**
     * Lee el contenido del archivo con tal de determinar si sus parentesis estan balanceados o no.
     * Si lo estan retorna true, de lo contrario false.
     * Si este paso sale mal por default se sabe que las instrucciones son invalidas como un totol.
     * @param str El String que contiene todas las instrucciones del archivo
     * @return True si los parentesiis estan balanceados, False de lo contrario
     */
    public boolean checkForBalancedParenthesis(String str){
        char leftParen = '(';
        char rightParen = ')';

        Stack<Character> stack = new Stack<Character>();

        //Esto es responsable de ir anadiendo los parentesis que encuentre en la expresion a un stack
        for(int i = 0; i < str.length(); i++){

            //Anade al stack los (
            if(str.charAt(i) == leftParen){
                stack.push(leftParen);
            }

            //Cuando se encuentra con un )
            else if(str.charAt(i) == rightParen){
                // Si el prmer parentesiis que se lee es un ) la expresion esta automaticamente mal
                if (stack.isEmpty()){
                    return false;
                }

                // Si se hace pop al stack y el parentesis que sale no es un ( eso significa que esta mal
                if (stack.pop() != leftParen){
                    return false;
                }
            }
        }



    }
}
