package Tokens;

/**
 * Esta clase describe el token de los parametros de numero que se le pueden introductr a los comandos y funciones
 */
public class Number extends Token {
    /*
     * El valor del numero como INT
     */
    private int value;

    /**
     * Crea un Number token conn un integer como valor
     * @param value
     */
    public Number(int value) {
        super(Tag.NUM);
        this.value = value;
    }

    /**
     * Retorna el valor de un numero
     * @return Un int con el valor de un token de number
     */
    public int getNumValue() {
        return value;
    }
}
