package goodapp.company.kissmyass.Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static goodapp.company.kissmyass.TiujKiss.TiujKissR.serverTimezone;

/**
 * Created by mehme on 22.06.2016.
 */
public class Message {
    private String ChatId;
    private String MessageID;
    private String SenderId;
    private Boolean MessageRead;
    private String Content;
    private String SentDate;
    private String SeenDate;
    private Boolean SeenStatus;
    private String DeliveredDate;
    private Boolean DeliveredStatus;

    public Message() {
    }

    public Message(String ChatId,
                   String MessageID,
                   String SenderId,
                   //Boolean MessageRead,
                   String Content,
                   String SentDate,
                   String SeenDate,
                   Boolean SeenStatus,
                   Boolean DeliveredStatus,
                   String DeliveredDate

    ) {
        this.ChatId = ChatId;
        this.MessageID = MessageID;
        this.SenderId = SenderId;
        //this.MessageRead = MessageRead;
        this.Content = Content;
        this.SentDate = SentDate;
        this.SeenDate = SeenDate;
        this.SeenStatus = SeenStatus;
        this.DeliveredDate = DeliveredDate;
        this.DeliveredStatus = DeliveredStatus;

    }

    public String getChatId() {
        return ChatId;
    }

    public String getMessageID() {
        return MessageID;
    }

    public String getSenderId() {
        return SenderId;
    }

    /*
    public Boolean getMessageRead(){return MessageRead;}
      */
    public Boolean getDeliveredStatus() {
        return DeliveredStatus;
    }

    public Boolean getSeenStatus() {
        return SeenStatus;
    }

    public String getContent() {
        return Content;
    }

    public String getSentDate() {
        return dateToString(stringToDate(SentDate));
    }

    public String getSeenDate() {
        return dateToString(stringToDate(SeenDate));
    }

    public String getDeliveredDate() {
        return dateToString(stringToDate(DeliveredDate));
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
                stringDate = dateDay.format(date) + " ";
                switch (dateMonth.format(date)) {
                    case "01":
                        stringDate = stringDate + "January ";
                        break;
                    case "02":
                        stringDate = stringDate + "February ";
                        break;
                    case "03":
                        stringDate = stringDate + "March ";
                        break;
                    case "04":
                        stringDate = stringDate + "April ";
                        break;
                    case "05":
                        stringDate = stringDate + "May ";
                        break;
                    case "06":
                        stringDate = stringDate + "June ";
                        break;
                    case "07":
                        stringDate = stringDate + "July ";
                        break;
                    case "08":
                        stringDate = stringDate + "August ";
                        break;
                    case "09":
                        stringDate = stringDate + "September ";
                        break;
                    case "10":
                        stringDate = stringDate + "October ";
                        break;
                    case "11":
                        stringDate = stringDate + "November ";
                        break;
                    case "12":
                        stringDate = stringDate + "December ";
                        break;
                    default:
                        stringDate = stringDate + dateMonth.format(date);
                }
                if (!dateYear.format(new Date()).equals(dateYear.format(date))) {
                    stringDate = stringDate + dateYear.format(date);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringDate;
    }

}