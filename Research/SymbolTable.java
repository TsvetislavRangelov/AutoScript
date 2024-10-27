import java.util.HashMap;

/**
 * A symbol table used to keep track of information about symbol within an AutoScript source file.
 */
public class SymbolTable {
    private final HashMap<String, Symbol> symbols;

    /**
     * C`tor.
     */
    public SymbolTable() {
        symbols = new HashMap<>();
    }

    /**
     * Inserts a record within the symbol table.
     * @param identifier An identifier to identify this symbol.
     * @param symbol Symbol information, such as type, name, value etc.
     * @return The previous value associated with the identifier.
     */
    public Symbol insert(String identifier, Symbol symbol){
        return symbols.put(identifier, symbol);
    }

    /**
     * Updates the value associated with an entry in the symbol table.
     * @param identifier The key.
     * @param symbol The new symbol information.
     * @return The new value.
     */
    public Symbol update(String identifier, Symbol symbol){
        return symbols.replace(identifier, symbol);
    }

    /**
     * Looks up the value of the provided key.
     * @param identifier The key.
     * @return The value associated with the provided key, otherwise null.
     */
    public Symbol lookup(String identifier){
        return symbols.get(identifier);
    }

    /**
     * Looks up whether the provided key exists within the symbol table.
     * @param identifier The key to lookup.
     * @return true if the key exists, false otherwise.
     */
    public boolean containsKey(String identifier){
        return symbols.containsKey(identifier);
    }

    /**
     * Removes all entries and frees storage of the symbol table.
     */
    public void free (){
        symbols.clear();
    }
}
