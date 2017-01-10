package ravnik.org.meetatsport;

/**
 * Created by Nejc Ravnik on 02-Jan-17.
 */
public class User {

    private String name;
    private String email;
    private String age;
    private String gender;
    private String password;
    private String fbProfileID;


    public User( String name,String email, String age, String gender, String password, String fbProfileID) {

        this.email = email;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.password = password;
        this.fbProfileID = fbProfileID;
    }

    public User(String name, String email, String age, String gender, String fbToken) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.fbProfileID = fbToken;
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFbToken() {
        return fbProfileID;
    }

    public void setFbToken(String fbProfileID) {
        this.fbProfileID = fbProfileID;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", password='" + password + '\'' +
                ", fbProfileID='" + fbProfileID + '\'' +
                '}';
    }
}
