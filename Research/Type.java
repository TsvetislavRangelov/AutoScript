public enum Type {
    INTEGER("integer"),
    CHARACTER("character"),
    BOOLEAN("boolean"),
    STRING("string"),
    INTEGER_ARRAY("integer[]"),
    CHARACTER_ARRAY("character[]"),
    BOOLEAN_ARRAY("boolean[]"),
    STRING_ARRAY("boolean[]"),
    FUNCTION("function");

    public final String label;

    Type(String label){
        this.label = label;
    }

    /**
     * Converts a string to an enum typed array value.
     * @param arg The string type.
     * @return One of the array types in Type.
     */
    public static Type stringToTypedArray(String arg){
        for(Type type : Type.values()){
            if(arg.equals(String.valueOf(type.label))){
                return Type.valueOf(type + "_ARRAY");
            }
        }
        return null;
    }
}
