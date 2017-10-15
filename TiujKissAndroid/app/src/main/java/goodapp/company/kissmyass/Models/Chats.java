package goodapp.company.kissmyass.Models;

import android.support.annotation.NonNull;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import microsoft.aspnet.signalr.client.Action;

import static goodapp.company.kissmyass.TiujKiss.TiujKissR.HubConnection;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.chats;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentUser;

/**
 * Created by mehme on 5.08.2016.
 */
public class Chats implements Collection<Chat> {
    public List<Chat> _Chats;

    public Chats()
    {
        _Chats = new ArrayList<Chat>();
    }

    public Chat get(int position)
    {
        return _Chats.get(position);
    }

    public Chat getChatByUserId(String UserId){
        for (Chat chat:_Chats) {
            if (chat.getPartnerId().equals(UserId))
                return chat;
        }
        return null;
    }
    public void updateChat(Message message){
        List<Chat> tempChats = new ArrayList<Chat>();
        for (Chat chat: _Chats ) {
            if(chat.getChatId().equals(message.getChatId())){
                chat.LastMessage = message.getContent();
                chat.LastMessageDate = message.getSentDate();
                chat.UnreadMessageCount = chat.getPartnerId() == message.getSenderId() ? chat.UnreadMessageCount +1 : 0;
                tempChats.add(chat);
                _Chats.remove(chat);
            }
        }
        for (Chat chat: _Chats ) {
            tempChats.add(chat);
        }
        _Chats.clear();
        _Chats = tempChats;
    }
    public void addChat(Chat chat){
        List<Chat> tempChats = new ArrayList<Chat>();
        tempChats.add(chat);
        for (Chat c : _Chats) {
            if (c.getChatId().equals(chat.ChatId))
                return;
            else
                tempChats.add(c);
        }
        _Chats.clear();
        _Chats = tempChats;
    }
    @Override
    public int size() {
        return _Chats.size();
    }

    @Override
    public boolean isEmpty() {
        return _Chats.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return _Chats.contains(o);
    }

    @NonNull
    @Override
    public Iterator<Chat> iterator() {
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
        return _Chats.toArray(ts);
    }

    @Override
    public boolean add(Chat chat) {
        return _Chats.add(chat);
    }

    @Override
    public boolean remove(Object o) {
        return _Chats.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return _Chats.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends Chat> collection) {
        return addAll(collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return _Chats.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return _Chats.retainAll(collection);
    }

    @Override
    public void clear() {
        _Chats.clear();
    }
}
