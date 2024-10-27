public class Symbol {
    private String name;
    private Type type;
    private Object value;

    /**
     * C`tor
     * @param name the symbol's name.
     * @param type the symbol's type.
     * @param value the symbol's value.
     */
    public Symbol(String name, Type type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    /**
     * Getter for the symbol's value.
     * @return the symbol's value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Getter for the symbol's name.
     * @return the symbol's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the symbol's type.
     * @return the symbol's type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Setter for the value. This setter will not work on strings,
     * as they are immutable.
     * @param value
     */
    public void setValue(Object value) {
        // we want strings to be immutable
        if(!this.type.equals(Type.STRING)){
            this.value = value;
        }
    }
}
