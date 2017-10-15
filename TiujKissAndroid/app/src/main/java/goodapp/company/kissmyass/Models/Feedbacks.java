package goodapp.company.kissmyass.Models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import microsoft.aspnet.signalr.client.Action;

import static goodapp.company.kissmyass.TiujKiss.TiujKissR.HubConnection;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentUser;

/**
 * Created by mehme on 5.08.2016.
 */
public class Feedbacks implements Collection<Feedback> {
    public List<Feedback> _feedbacks;

    public Feedbacks(){ _feedbacks = new ArrayList<Feedback>(); }

    public Feedback get(int position){ return _feedbacks.get(position); }

    public void removeByFeedbackId(String FeedbackId){
        for (Feedback feed : _feedbacks ) {
            if(feed.getFeedbackID().equals(FeedbackId)) {
                _feedbacks.remove(feed);
                return;
            }
        }
    }

    @Override
    public int size() {
        return _feedbacks.size();
    }

    @Override
    public boolean isEmpty() {
        return _feedbacks.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return _feedbacks.contains(o);
    }

    @NonNull
    @Override
    public Iterator<Feedback> iterator() {
        return _feedbacks.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] ts) {
        return _feedbacks.toArray(ts);
    }

    @Override
    public boolean add(Feedback feedback) {
        return _feedbacks.add(feedback);
    }

    @Override
    public boolean remove(Object o) {
        return _feedbacks.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return _feedbacks.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends Feedback> collection) {
        return _feedbacks.addAll(collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return _feedbacks.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return _feedbacks.removeAll(collection);
    }

    @Override
    public void clear() {
        _feedbacks.clear();
    }
}
