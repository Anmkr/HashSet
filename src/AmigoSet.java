import com.sun.org.apache.xalan.internal.xsltc.compiler.util.InternalError;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AmigoSet <E> extends AbstractSet<E> implements Serializable, Cloneable,Set<E> {
    private final static Object PRESENT = new Object();
    private transient HashMap<E, Object> map;

    public AmigoSet() {
        map = new HashMap<>();
    }

    public AmigoSet(Collection<? extends E> collection) {
        int capacity = Math.max(16, (int) Math.floor(collection.size() / .75f) + 1);
        map = new HashMap<>(capacity);

        for (E e : collection) {
            this.add(e);
        }
    }


    public boolean add(E e) {
        return null == map.put(e, PRESENT);
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.keySet().contains(o);
    }

    @Override
    public boolean remove(Object o) {
        return map.keySet().remove(o);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Object clone() {
        AmigoSet amigoSet;
        try {
            amigoSet = (AmigoSet) super.clone();
            amigoSet.map = (HashMap) map.clone();
        } catch (Exception e) {
            throw new InternalError("");
        }
        return amigoSet;

    }
    private void writeObject(ObjectOutputStream objectOutputStream)
    {
        try
        {
            objectOutputStream.defaultWriteObject();

            objectOutputStream.writeObject(map.keySet().size());
            for (E e : map.keySet()) {
                objectOutputStream.writeObject(e);

            }

            objectOutputStream.writeObject(HashMapReflectionHelper.callHiddenMethod(map, "capacity"));
            objectOutputStream.writeObject(HashMapReflectionHelper.callHiddenMethod(map, "loadFactor"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void readObject(ObjectInputStream objectInputStream)
    {
        try
        {
            objectInputStream.defaultReadObject();

            Set<E> set = new HashSet<>();
            int size = (int) objectInputStream.readObject();
            for (int i = 0; i < size; i++)
            {
                set.add((E) objectInputStream.readObject());
            }

            int capacity = (int) objectInputStream.readObject();
            float loadFactor = (float) objectInputStream.readObject();
            map = new HashMap<>(capacity, loadFactor);
            for (E elem : set)
            {
                map.put(elem, PRESENT);
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}

