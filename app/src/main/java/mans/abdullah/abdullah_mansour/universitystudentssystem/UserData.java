package mans.abdullah.abdullah_mansour.universitystudentssystem;

public class UserData {
    String email,name,phone,depart,year,section;

    public UserData() {
    }

    public UserData(String email, String name, String phone, String depart, String year, String section) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.depart = depart;
        this.year = year;
        this.section = section;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
