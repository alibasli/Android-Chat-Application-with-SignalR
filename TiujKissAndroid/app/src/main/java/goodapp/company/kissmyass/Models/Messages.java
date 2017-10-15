package goodapp.company.kissmyass.Models;

import android.support.annotation.NonNull;

import org.jsoup.select.Collector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mehme on 5.08.2016.
 */
public class Messages implements Collection<Message> {
    public List<Message> _Messages;

    public Messages()
    {
        _Messages = new ArrayList<Message>();
    }

    public Message get(int position)
    {
        return _Messages.get(position);
    }

    public Messages getMessagesbyChatId(String chatId){
        Messages chatMessages = new Messages();
        for (Message msg :_Messages) {
            if(msg.getChatId().equals(chatId)) {
                chatMessages.add(msg);
            }
        }
        return chatMessages;
    }

    public void removeByMessageId(String MessageId){
        for (Message msg :_Messages) {
            if(msg.getMessageID().equals(MessageId)) {
                _Messages.remove(msg);
                return;
            }
        }
    }

    @Override
    public int size() {
        return _Messages.size();
    }

    @Override
    public boolean isEmpty() {
        return _Messages.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return _Messages.contains(o);
    }

    @NonNull
    @Override
    public Iterator<Message> iterator() {
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
        return _Messages.toArray(ts);
    }

    @Override
    public boolean add(Message message) {
        return _Messages.add(message);
    }

    @Override
    public boolean remove(Object o) {
        return _Messages.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return _Messages.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends Message> collection) {
        return _Messages.addAll(collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return _Messages.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return _Messages.retainAll(collection);
    }

    @Override
    public void clear() {
        _Messages.clear();
    }

    @Override
    public boolean equals(Object o) {
        return _Messages.equals(o);
    }

    @Override
    public int hashCode() {
        return _Messages.hashCode();
    }
}
