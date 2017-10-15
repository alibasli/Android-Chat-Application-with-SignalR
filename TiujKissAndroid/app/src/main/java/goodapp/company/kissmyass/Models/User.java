package goodapp.company.kissmyass.Models;

/**
 * Created by mehme on 3.08.2016.
 */
public class User {
    public String UserID;
    public String Name;
    public String Lastname;
    public String Photo;
    public String Gender;
    public String BirthDate;
    public String Email;
    public String About;

    public User(){}

    public User(
            String UserID,
            String Name,
            String Lastname,
            String Photo,
            String Email,
            String BirthDate,
            String Gender,
            String About
    ) {

        this.UserID = UserID;
        this.Name = Name;
        this.Lastname = Lastname;
        this.Photo = Photo;
        this.Gender = Gender;
        this.BirthDate = BirthDate;
        this.Email = Email;
        this.About = About;

    }

    public String getUserID() {
        return UserID;
    }

    public String getName() {
        return Name;
    }

    public String getLastname() {
        return Lastname;
    }

    public String getPhoto() {
        return Photo;
    }

    public String getGender() {
        return Gender;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public String getEmail() {
        return Email;
    }

    public String getAbout(){return About;}
}
