import java.util.Arrays;
import java.util.Objects;

public class MyArrayList<E> {
    private static final int DEFAULT_CAPACITY = 100;
    private Object[] data;
    private int size;

    public MyArrayList() {
        data = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity(size + 1);
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    public boolean contains(Object element) {
        return indexOf(element) >= 0;
    }
  
    public int indexOf(Object element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(data[i], element)) {
                return i;
            }
        }
        return -1;
    }

    public E get(int index) {
        checkIndex(index);
        return (E) data[index];
    }

    public E remove(int index) {
        checkIndex(index);
        E removed = (E) data[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(data, index + 1, data, index, numMoved);
        }
        data[--size] = null;
        return removed;
    }

  
    public boolean remove(Object element) {
        int index = indexOf(element);
        if (index < 0) {
            return false;
        }
        remove(index);
        return true;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        Arrays.fill(data, 0, size, null);
        size = 0;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

      private void ensureCapacity(int required) {
        if (required <= data.length) {
            return;
        }
        int grown = data.length + (data.length >> 1) + 1;
        data = Arrays.copyOf(data, Math.max(required, grown));
    }

    public void add(E element) {
        ensureCapacity(size + 1);
        data[size++] = element;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) result.append(", ");
            result.append(data[i]);
        }
        return result.append(']').toString();
    }
}
