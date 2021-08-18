/**
 * @author Nate Novak
 * 7/25/2021
 * KeyValuePair<T, E> class used for returning key value pairs
 */
public class KeyValuePair<T, E> {
    private T key;
    private E value;

    /**
     * Argument constructor for creating a pair
     * @param key
     * @param value
     */
    public KeyValuePair(T key, E value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Get key from the pair
     * @return the key
     */
    public T getKey() {
        return key;
    }

    /**
     * Get the value associated with the key
     * @return value associated with the key
     */
    public E getValue() {
        return value;
    }
}
