/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.ivica.chat.helpers;

/**
 * Helper class for setting and getting values on the EDT
 *
 * @param <T>
 */
public class ItemHolder<T> {

    public void set(T item) {
        this.item = item;
    }

    public T get() {
        return item;
    }
    private T item;
}