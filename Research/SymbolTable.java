import java.util.HashMap;

/**
 * A symbol table used to keep track of information about symbol within an AutoScript source file.
 */
public class SymbolTable {
    private final HashMap<String, Symbol> symbols;
    // we want to be aware of which scope the symbol table is for.
    // if we are in the global scope, then the symbol table's parent will be null.
    // if we are in a local scope, then the symbol table will always have a parent, which
    // will eventually reach up to the global scope.
    //var oldSym = symbols
    //symbols = oldSym.createScope()
    //var returnObj = visitChildren(ctx)
    //sym = oldSym
    //return returnObj
    private SymbolTable parent = null;

    /**
     * C`tor.
     */
    public SymbolTable() {
        symbols = new HashMap<>();
    }

    /**
     * Inserts a record within the symbol table.
     *
     * @param identifier An identifier to identify this symbol.
     * @param symbol     Symbol information, such as type, name, value etc.
     */
    public void insert(String identifier, Symbol symbol){
        symbols.put(identifier, symbol);
    }

    /**
     * Updates the value associated with an entry in the symbol table.
     *
     * @param identifier The key.
     * @param symbol     The new symbol information.
     */
    public void update(String identifier, Symbol symbol){
        symbols.replace(identifier, symbol);
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

    public void setParent(SymbolTable parent){
        this.parent = parent;
    }

    /**
     * Creates a new scope (symbol table) by setting the current scope to the parent of the newly created scope.
     * @return the new scope.
     */
    public SymbolTable createScope(){
        SymbolTable newScope = new SymbolTable();
        newScope.setParent(this);
        return newScope;
    }
}
