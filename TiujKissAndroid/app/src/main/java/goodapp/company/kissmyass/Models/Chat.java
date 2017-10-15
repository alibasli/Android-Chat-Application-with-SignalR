package goodapp.company.kissmyass.Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static goodapp.company.kissmyass.TiujKiss.TiujKissR.serverTimezone;

/**
 * Created by mehme on 24.06.2016.
 */
public class Chat {
    public String ChatId;
    public String PartnerId;
    public String PartnerName;
    public String PartnerLastname;
    public String PartnerPhoto;
    public String LastMessage;
    public int UnreadMessageCount;
    public String LastMessageDate;

    public Chat(){}

    public Chat(String ChatId,
                String PartnerId,
                String PartnerName,
                String PartnerLastname,
                String PartnerPhoto,
                String LastMessage,
                int UnreadMessageCount,
                String LastMessageDate) {
        this.ChatId = ChatId;
        this.PartnerId = PartnerId;
        this.PartnerName = PartnerName;
        this.PartnerLastname = PartnerLastname;
        this.PartnerPhoto = PartnerPhoto;
        this.LastMessage = LastMessage;
        this.UnreadMessageCount = UnreadMessageCount;
        this.LastMessageDate = LastMessageDate;
    }

    public String getChatId() {
        return ChatId;
    }

    public String getPartnerId() {
        return PartnerId;
    }

    public String getPartnerName() {
        return PartnerName;
    }

    public String getPartnerLastname() {
        return PartnerLastname;
    }


    public String getPartnerPhoto() {
        return PartnerPhoto;
    }

    public String getLastMessage() {
        return LastMessage;
    }

    public int getUnreadMessageCount() {
        return UnreadMessageCount;
    }

    public String getLastMessageDate() {
        return dateToString(stringToDate(LastMessageDate));
    }

    private Date stringToDate(String dateString) {
        // "2010-10-15T09:27:37Z"  example standart date
        SimpleDateFormat formatServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
        SimpleDateFormat formatLocal = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
        formatServer.setTimeZone(serverTimezone);
        formatLocal.setTimeZone(TimeZone.getDefault());

        Date dateServer = new Date();
        Date dateLocal = new Date();
        try {
            dateServer = formatServer.parse(dateString);
            String dt = formatLocal.format(dateServer);
            dateLocal = formatLocal.parse(dt);//.parse(dateServer);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateLocal;
    }

    private String dateToString(Date date) {
        String stringDate = "";

        SimpleDateFormat dateYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateMonth = new SimpleDateFormat("MM");
        SimpleDateFormat dateDay = new SimpleDateFormat("dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm");

        try {
            if (dateFormat.format(new Date()).equals(dateFormat.format(date))) {
                stringDate = dateFormat2.format(date);
            } else {
                stringDate = dateDay.format(date) +" ";
                switch (dateMonth.format(date))
                {
                    case "01" :
                        stringDate = stringDate + "January ";
                        break;
                    case "02" :
                        stringDate = stringDate + "February ";
                        break;
                    case "03" :
                        stringDate = stringDate + "March ";
                        break;
                    case "04" :
                        stringDate = stringDate + "April ";
                        break;
                    case "05" :
                        stringDate = stringDate + "May ";
                        break;
                    case "06" :
                        stringDate = stringDate + "June ";
                        break;
                    case "07" :
                        stringDate = stringDate + "July ";
                        break;
                    case "08" :
                        stringDate = stringDate + "August ";
                        break;
                    case "09" :
                        stringDate = stringDate + "September ";
                        break;
                    case "10" :
                        stringDate = stringDate + "October ";
                        break;
                    case "11" :
                        stringDate = stringDate + "November ";
                        break;
                    case "12" :
                        stringDate = stringDate + "December ";
                        break;
                    default:
                        stringDate = stringDate + dateMonth.format(date);
                }
                if(!dateYear.format(new Date()).equals(dateYear.format(date))) {
                    stringDate = stringDate +dateYear.format(date);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringDate;
    }

}
