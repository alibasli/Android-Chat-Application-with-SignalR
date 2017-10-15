package goodapp.company.kissmyass.Models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mehme on 8.08.2016.
 */
public class Users implements Collection<User> {
    public List<User> _Users;

    public Users()
    {
        _Users = new ArrayList<User>();
    }

    public User get(int position)
    {
        return _Users.get(position);
    }
    @Override
    public int size() {
        return _Users.size();
    }

    @Override
    public boolean isEmpty() {
        return _Users.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return _Users.contains(o);
    }

    @NonNull
    @Override
    public Iterator<User> iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] ts) {
        return null;
    }

    @Override
    public boolean add(User user) {
        return _Users.add(user);
    }

    @Override
    public boolean remove(Object o) {
        return _Users.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return _Users.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends User> collection) {
        return _Users.addAll(collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return _Users.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return _Users.removeAll(collection);
    }

    @Override
    public void clear() {
        _Users.clear();
    }
}
