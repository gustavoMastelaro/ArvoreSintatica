package parser;

/**
 * Um token.
 * 
 * @author Prof. Dr. David Buzatto
 */
public record Token( String valor, TipoToken tipo ) {
    
    public static final Token EOF = new Token( "", TipoToken.EOF );
    
    public String toString() {
        return valor;
    }
    
}
